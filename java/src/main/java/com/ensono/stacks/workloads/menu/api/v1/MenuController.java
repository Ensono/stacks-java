package com.ensono.stacks.workloads.menu.api.v1;

import static org.springframework.http.HttpStatus.OK;

import com.ensono.stacks.core.api.annotations.CreateAPIResponses;
import com.ensono.stacks.core.api.annotations.DeleteAPIResponses;
import com.ensono.stacks.core.api.annotations.ReadAPIResponses;
import com.ensono.stacks.core.api.annotations.SearchAPIResponses;
import com.ensono.stacks.core.api.annotations.UpdateAPIResponses;
import com.ensono.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.ensono.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.ensono.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.ensono.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.ensono.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.ensono.stacks.workloads.menu.api.v1.dto.response.SearchMenuResult;
import com.ensono.stacks.workloads.menu.service.v1.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;

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

    return new ResponseEntity<>(menuService.create(body, correlationId), HttpStatus.CREATED);
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

    return ResponseEntity.ok(menuService.search(searchTerm, restaurantId, pageSize, pageNumber));
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

    return ResponseEntity.ok(menuService.get(id, correlationId));
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

    return new ResponseEntity<>(menuService.update(body, correlationId), HttpStatus.OK);
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
