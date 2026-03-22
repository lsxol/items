package items.items.application.usecase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import items.items.config.security.AuthHelper;
import items.items.config.security.dto.AuthDto;
import items.items.domain.exception.ItemException;
import items.items.domain.exception.ItemException.ErrorCode;
import items.items.domain.model.Item;
import items.items.domain.model.RoleEnum;
import items.items.domain.model.UserId;
import items.items.domain.ports.in.ShareItemUseCase;
import items.items.domain.ports.out.ItemRepository;
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
public class ShareItemUseCaseTest {

  @Mock
  private ItemRepository itemRepository;
  @InjectMocks
  private ShareItemUseCaseImpl useCase;


  private final UserId ownerId = new UserId(UUID.randomUUID());

  @Test
  @DisplayName("Powinien rzucić błąd przy próbie udostępnienia notatki samemu sobie")
  public void shouldThrowExceptionWhenSharingToSelf() {
    // given
    Item item = Item.createNew(ownerId, "Title", "Content");
    when(itemRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(item));

    var command = new ShareItemUseCase.ShareItemCommand(UUID.randomUUID(), ownerId.value(), RoleEnum.EDITOR);

    try (MockedStatic<AuthHelper> authMock = mockStatic(AuthHelper.class)) {
      authMock.when(AuthHelper::getAuth).thenReturn(new AuthDto(ownerId.value(), "owner"));

      // when & then
      assertThatThrownBy(() -> useCase.shareItem(command))
          .isInstanceOf(ItemException.class)
          .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ITEM_INVALID);
    }
  }

  @Test
  @DisplayName("Powinien nadpisać rolę, jeśli użytkownik już posiada uprawnienia")
  public void shouldOverwriteRoleIfAlreadyShared() {
    // given
    UserId targetUserId = new UserId(UUID.randomUUID());
    Item item = Item.createNew(ownerId, "Title", "Content");
    item.shareWith(targetUserId, RoleEnum.VIEWER, ownerId);

    when(itemRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(item));

    var command = new ShareItemUseCase.ShareItemCommand(UUID.randomUUID(), targetUserId.value(), RoleEnum.EDITOR);

    try (MockedStatic<AuthHelper> authMock = mockStatic(AuthHelper.class)) {
      authMock.when(AuthHelper::getAuth).thenReturn(new AuthDto(ownerId.value(), "owner"));

      // when
      useCase.shareItem(command);

      // then
      verify(itemRepository).save(argThat(savedItem ->
          savedItem.getPermissions().getFirst().getRole() == RoleEnum.EDITOR
      ));
    }
  }
}
