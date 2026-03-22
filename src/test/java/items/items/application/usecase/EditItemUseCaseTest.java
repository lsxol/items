package items.items.application.usecase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import items.items.config.security.AuthHelper;
import items.items.config.security.dto.AuthDto;
import items.items.domain.exception.ItemException;
import items.items.domain.exception.ItemException.ErrorCode;
import items.items.domain.exception.OptimisticLockException;
import items.items.domain.model.Item;
import items.items.domain.model.ItemId;
import items.items.domain.model.RoleEnum;
import items.items.domain.model.UserId;
import items.items.domain.ports.in.EditItemUseCase;
import items.items.domain.ports.out.ItemRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EditItemUseCaseTest {

  private final UUID itemId = UUID.randomUUID();
  private final UserId ownerId = new UserId(UUID.randomUUID());
  private final UserId editorId = new UserId(UUID.randomUUID());
  private final UserId strangerId = new UserId(UUID.randomUUID());
  @Mock
  private ItemRepository itemRepository;
  @InjectMocks
  private EditItemUseCaseImpl editItemUseCase;

  @Test
  @DisplayName("Powinien rzucić 403, gdy obcy użytkownik próbuje edytować notatkę")
  void shouldThrowForbiddenWhenStrangerEdits() {
    // given
    Item item = Item.createNew(ownerId, "Title", "Content");
    when(itemRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(item));

    var command = new EditItemUseCase.EditItemCommand(itemId, "New Title", "New Content", 0);

    try (MockedStatic<AuthHelper> authMock = mockStatic(AuthHelper.class)) {
      authMock.when(AuthHelper::getAuth).thenReturn(new AuthDto(strangerId.value(), "stranger"));

      // when & then
      assertThatThrownBy(() -> editItemUseCase.editItem(command))
          .isInstanceOf(ItemException.class)
          .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_AUTHORIZED);
    }
  }

  @Test
  @DisplayName("Powinien rzucić 409 (Conflict), gdy wersja w bazie jest inna niż przesłana")
  void shouldThrowConflictWhenVersionsDoNotMatch() {
    Item item = Item.createNew(ownerId, "Title", "Content");
    when(itemRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(item));

    when(itemRepository.save(any(), eq(1))).thenThrow(new OptimisticLockException(2));

    var command = new EditItemUseCase.EditItemCommand(itemId, "New Title", "New Content", 1);

    try (MockedStatic<AuthHelper> authMock = mockStatic(AuthHelper.class)) {
      authMock.when(AuthHelper::getAuth).thenReturn(new AuthDto(ownerId.value(), "owner"));

      assertThatThrownBy(() -> editItemUseCase.editItem(command))
          .isInstanceOf(OptimisticLockException.class);
    }
  }

  @Test
  @DisplayName("Powinien edytować notatkę, jeśli zalogowany użytkownik jest edytorem")
  void shouldEditItemWhenEditorEdits() {
    UUID sharedId = UUID.randomUUID();
    ItemId itemId = new ItemId(sharedId);
    Item item = Item.createNew(ownerId, "Title", "Content");
    item.shareWith(editorId, RoleEnum.EDITOR, ownerId);

    var mockAuditData = new ItemRepository.SavedItemAuditData(
        1,
        LocalDateTime.now(),
        LocalDateTime.now()
    );

    when(itemRepository.findByIdAndDeletedFalse(itemId)).thenReturn(Optional.of(item));

    when(itemRepository.save(any(Item.class), anyInt())).thenReturn(mockAuditData);

    var command = new EditItemUseCase.EditItemCommand(sharedId, "New Title", "New Content", 0);

    try (MockedStatic<AuthHelper> authMock = mockStatic(AuthHelper.class)) {
      authMock.when(AuthHelper::getAuth).thenReturn(new AuthDto(editorId.value(), "editor"));

      // when
      var response = editItemUseCase.editItem(command);

      // then
      assertThat(response.version()).isEqualTo(1);
      verify(itemRepository).save(any(Item.class), eq(0));
    }
  }

}