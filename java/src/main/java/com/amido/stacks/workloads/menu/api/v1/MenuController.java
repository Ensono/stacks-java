package com.amido.stacks.workloads.menu.api.v1;

import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.core.api.annotations.CreateAPIResponses;
import com.amido.stacks.core.api.annotations.DeleteAPIResponses;
import com.amido.stacks.core.api.annotations.ReadAPIResponses;
import com.amido.stacks.core.api.annotations.SearchAPIResponses;
import com.amido.stacks.core.api.annotations.UpdateAPIResponses;
import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Item;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.mappers.SearchMenuResultItemMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/v1/menu", produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
@RestController
public class MenuController {

  Logger logger = LoggerFactory.getLogger(MenuController.class);

  @Autowired private MenuMapper menuMapper;

  @Autowired private SearchMenuResultItemMapper searchMenuResultItemMapper;

  @PostMapping
  @Operation(
      tags = "Menu",
      summary = "Create a menu",
      description = "Adds a menu",
      operationId = "CreateMenu")
  @CreateAPIResponses
  ResponseEntity<ResourceCreatedResponse> createMenu(
      @Valid @RequestBody CreateMenuRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId) {

    return new ResponseEntity<>(new ResourceCreatedResponse(UUID.randomUUID()), HttpStatus.CREATED);
  }

  @GetMapping
  @Operation(
      tags = "Menu",
      summary = "Get or search a list of menus",
      description =
          "By passing in the appropriate options, you can search for available menus in the system")
  @ApiResponse(
      responseCode = "200",
      description = "Search results matching criteria",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = SearchMenuResult.class)))
  @SearchAPIResponses
  ResponseEntity<SearchMenuResult> searchMenu(
      @RequestParam(value = "searchTerm", required = false) String searchTerm,
      @RequestParam(value = "restaurantId", required = false) UUID restaurantId,
      @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
      @RequestParam(value = "pageNumber", required = false, defaultValue = "1")
          Integer pageNumber) {

    List<Menu> menuList = new ArrayList<>();

    final String menuId = "d5785e28-306b-4e23-add0-3f9092d395f8";

    Menu mockMenu;
    if (restaurantId == null) {
      mockMenu =
          new Menu(
              menuId,
              "58a1df85-6bdc-412a-a118-0f0e394c1342",
              "name",
              "description",
              new ArrayList<>(),
              true);
    } else {
      mockMenu =
          new Menu(menuId, restaurantId.toString(), "name", "description", new ArrayList<>(), true);
    }

    menuList.add(mockMenu);

    return ResponseEntity.ok(
        new SearchMenuResult(
            pageSize,
            pageNumber,
            menuList.stream()
                .map(m -> searchMenuResultItemMapper.toDto(m))
                .collect(Collectors.toList())));
  }

  @GetMapping(value = "/{id}")
  @Operation(
      tags = "Menu",
      summary = "Get a menu",
      description =
          "By passing the menu id, you can get access to available categories and items in the menu")
  @ApiResponse(
      responseCode = "200",
      description = "Menu",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = MenuDTO.class)))
  @ReadAPIResponses
  ResponseEntity<MenuDTO> getMenu(
      @PathVariable(name = "id") UUID id,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId) {

    final String restaurantId = "58a1df85-6bdc-412a-a118-0f0e394c1342";
    final String categoryId = "2c43dbda-7d4d-46fb-b246-bec2bd348ca1";
    final String itemId = "7e46a698-080b-45e6-a529-2c196d00791c";

    Menu menu =
        new Menu(id.toString(), restaurantId, "name", "description", new ArrayList<>(), true);
    Item item = new Item(itemId, "item name", "item description", 5.99d, true);
    Category category = new Category(categoryId, "cat name", "cat description", List.of(item));
    menu.addOrUpdateCategory(category);

    return ResponseEntity.ok(menuMapper.toDto(menu));
  }

  @PutMapping(value = "/{id}")
  @Operation(
      tags = "Menu",
      summary = "Update a menu",
      description = "Update a menu with new information")
  @UpdateAPIResponses
  ResponseEntity<ResourceUpdatedResponse> updateMenu(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Valid @RequestBody UpdateMenuRequest body,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId) {

    return new ResponseEntity<>(new ResourceUpdatedResponse(UUID.randomUUID()), HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  @Operation(
      tags = "Menu",
      summary = "Removes a Menu with all it's Categories and Items",
      description = "Remove a menu from a restaurant",
      operationId = "DeleteMenu")
  @DeleteAPIResponses
  ResponseEntity<Void> deleteMenu(
      @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
      @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId) {

    return new ResponseEntity<>(OK);
  }
}
