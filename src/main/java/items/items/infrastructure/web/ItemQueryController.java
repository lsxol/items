package items.items.infrastructure.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import items.items.config.security.AuthHelper;
import items.items.domain.ports.in.GetItemHistoryQuery;
import items.items.domain.ports.in.GetItemHistoryQuery.ItemHistoryDto;
import items.items.domain.ports.in.GetItemsQuery;
import items.items.domain.ports.in.GetItemsQuery.PaginatedItemsResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Items (Read)", description = "Pobieranie notatek i historii zmian - zapytania odczytujące")
public class ItemQueryController {

  private final GetItemsQuery getItemsQuery;
  private final GetItemHistoryQuery getItemHistoryQuery;

  @GetMapping
  @Operation(
      summary = "Pobierz listę notatek",
      description = "Zwraca stronicowaną listę notatek, do których zalogowany użytkownik ma dostęp (jest właścicielem lub ma udostępnione uprawnienia VIEWER/EDITOR)."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Zwraca stronę z wynikami oraz metadanymi paginacji")
  })
  public PaginatedItemsResponse getItems(
      @Parameter(description = "Numer strony (zaczynając od 0)")
      @RequestParam(name = "page", defaultValue = "0") int page,

      @Parameter(description = "Ilość elementów na stronie")
      @RequestParam(name = "size", defaultValue = "10") int size) {

    UUID loggedInUserId = AuthHelper.getAuth().userId();
    return getItemsQuery.getItems(loggedInUserId, page, size);
  }

  @GetMapping("/{id}/history")
  @Operation(
      summary = "Pobierz historię zmian notatki",
      description = "Zwraca pełen audyt modyfikacji (kto, co i kiedy zmienił). Dostępne dla osób z uprawnieniami odczytu lub wyższymi."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Zwraca listę historycznych wersji notatki"),
      @ApiResponse(responseCode = "403", description = "USER_NOT_AUTHORIZED - Brak uprawnień do przeglądania historii tej notatki"),
      @ApiResponse(responseCode = "404", description = "ITEM_NOT_EXISTS - Notatka nie istnieje")
  })
  public List<ItemHistoryDto> getItemHistory(
      @Parameter(description = "ID notatki")
      @PathVariable("id") UUID itemId) {

    UUID loggedInUserId = AuthHelper.getAuth().userId();
    return getItemHistoryQuery.getItemHistory(itemId, loggedInUserId);
  }
}