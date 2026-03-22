package items.items.infrastructure.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import items.items.domain.model.ItemId;
import items.items.domain.model.RoleEnum;
import items.items.domain.model.UserId;
import items.items.domain.ports.in.CreateItemUseCase;
import items.items.domain.ports.in.CreateItemUseCase.CreateItemCommand;
import items.items.domain.ports.in.DeleteItemUseCase;
import items.items.domain.ports.in.DeletePermissionUseCase;
import items.items.domain.ports.in.DeletePermissionUseCase.DeletePermissionCommand;
import items.items.domain.ports.in.EditItemUseCase;
import items.items.domain.ports.in.EditItemUseCase.EditItemCommand;
import items.items.domain.ports.in.ShareItemUseCase;
import items.items.domain.ports.in.ShareItemUseCase.ShareItemCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Items (Write)", description = "Zarządzanie cyklem życia notatek i uprawnieniami (tworzenie, edycja, usuwanie, udostępnianie)")
public class ItemCommandController {

  private final CreateItemUseCase createItemUseCase;
  private final EditItemUseCase editItemUseCase;
  private final DeleteItemUseCase deleteItemUseCase;
  private final ShareItemUseCase shareItemUseCase;
  private final DeletePermissionUseCase deletePermissionUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Utwórz nową notatkę", description = "Tworzy notatkę przypisaną do aktualnie zalogowanego użytkownika (właściciela).")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Notatka utworzona pomyślnie"),
      @ApiResponse(responseCode = "400", description = "ITEM_INVALID - Dane notatki są nieprawidłowe (np. brak tytułu)")
  })
  public CreateItemUseCase.CreateItemResponse createItem(@RequestBody CreateItemCommand command) {
    return createItemUseCase.createItem(command);
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Edytuj notatkę", description = "Aktualizuje tytuł i treść. Dostępne dla właściciela i edytorów.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Pomyślnie zaktualizowano notatkę"),
      @ApiResponse(responseCode = "400", description = "ITEM_INVALID - Dane notatki są nieprawidłowe"),
      @ApiResponse(responseCode = "403", description = "USER_NOT_AUTHORIZED - Brak uprawnień do wykonania tej operacji (nie jesteś właścicielem ani edytorem)"),
      @ApiResponse(responseCode = "404", description = "ITEM_NOT_EXISTS - Notatka nie istnieje lub została usunięta"),
      @ApiResponse(responseCode = "409", description = "WRONG_VERSION - Konflikt wersji - pobierz notatkę ponownie (zwraca actualVersion w ciele)")
  })
  public EditItemUseCase.EditItemResponse editItem(
      @PathVariable("id") UUID id,
      @RequestBody EditItemRequest request) {

    EditItemCommand command = new EditItemCommand(id, request.title(), request.content(), request.version());
    return editItemUseCase.editItem(command);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Usuń notatkę", description = "Wykonuje miękkie usunięcie (soft delete) notatki. Operacja dostępna wyłącznie dla właściciela.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Notatka usunięta pomyślnie"),
      @ApiResponse(responseCode = "403", description = "USER_NOT_AUTHORIZED - Brak uprawnień do wykonania tej operacji"),
      @ApiResponse(responseCode = "404", description = "ITEM_NOT_EXISTS - Notatka nie istnieje lub została już usunięta")
  })
  public void deleteItem(@PathVariable("id") UUID id) {
    deleteItemUseCase.deleteItem(id);
  }

  @PostMapping("/{id}/share")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Udostępnij notatkę", description = "Nadaje uprawnienia (VIEWER, EDITOR) innemu użytkownikowi. Dostępne tylko dla właściciela.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Pomyślnie nadano uprawnienia"),
      @ApiResponse(responseCode = "400", description = "INVALID_USER - Nieprawidłowy użytkownik (wskazany cel nie istnieje)"),
      @ApiResponse(responseCode = "403", description = "USER_NOT_AUTHORIZED - Brak uprawnień do wykonania tej operacji (tylko właściciel może udostępniać)"),
      @ApiResponse(responseCode = "404", description = "ITEM_NOT_EXISTS - Notatka nie istnieje lub została usunięta")
  })
  public ShareItemUseCase.ShareItemResponse shareItem(
      @PathVariable("id") UUID id,
      @RequestBody ShareItemRequest request) {

    ShareItemCommand command = new ShareItemCommand(id, request.targetUserId(), request.role());
    return shareItemUseCase.shareItem(command);
  }

  @DeleteMapping("/{id}/share/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Odbierz uprawnienia", description = "Usuwa użytkownika z listy uprawnionych. Dostępne tylko dla właściciela.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Uprawnienia zostały pomyślnie odebrane"),
      @ApiResponse(responseCode = "400", description = "INVALID_USER - Nieprawidłowy użytkownik (np. próba odebrania uprawnień właścicielowi lub nieistniejącemu userowi)"),
      @ApiResponse(responseCode = "403", description = "USER_NOT_AUTHORIZED - Brak uprawnień do wykonania tej operacji"),
      @ApiResponse(responseCode = "404", description = "ITEM_NOT_EXISTS - Notatka nie istnieje lub została usunięta")
  })
  public void deletePermission(
      @PathVariable("id") UUID itemId,
      @PathVariable("userId") UUID userId) {

    DeletePermissionCommand command = new DeletePermissionCommand(new ItemId(itemId), new UserId(userId));
    deletePermissionUseCase.deletePermission(command);
  }

  record EditItemRequest(String title, String content, Integer version) {

  }

  record ShareItemRequest(UUID targetUserId, RoleEnum role) {

  }

}