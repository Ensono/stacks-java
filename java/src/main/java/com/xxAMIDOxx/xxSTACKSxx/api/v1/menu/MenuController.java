package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto.CreateCategoryRequestDto;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto.MenuCreateRequestDto;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responseDto.ResourceCreatedResponseDto;
import com.xxAMIDOxx.xxSTACKSxx.model.Category;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RequestMapping("/v1/menu")
public interface MenuController {

  @GetMapping(value = "", produces = "application/json")
  @Operation(
          tags = "Menu",
          summary = "Get or search a list of menus",
          description =
                  "By passing in the appropriate options, you can search for " +
                          "available menus in the system",
          responses = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Search results matching criteria",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = SearchMenuResult.class))),
                  @ApiResponse(
                          responseCode = "400",
                          description = "Bad Request",
                          content = @Content(schema = @Schema(hidden = true)))
          })
  ResponseEntity<SearchMenuResult> searchMenu(
          @RequestParam(value = "searchTerm", required = false) String searchTerm,
          @RequestParam(value = "restaurantId", required = false) UUID restaurantId,
          @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
          @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber) throws IOException;

  @GetMapping(value = "/{id}", produces = "application/json")
  @Operation(
          tags = "Menu",
          summary = "Get a menu",
          description = "By passing the menu id, you can get access to available " +
                  "categories and items in the menu",
          responses = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Menu",
                          content = @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = Menu.class))),
                  @ApiResponse(
                          responseCode = "404",
                          description = "Not Found",
                          content = @Content(schema = @Schema(hidden = true))),
                  @ApiResponse(
                          responseCode = "400",
                          description = "Bad Request",
                          content = @Content(schema = @Schema(hidden = true)))
          })
  ResponseEntity<Menu> getMenu(@PathVariable(name = "id") UUID id);

  @PostMapping(value = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @Operation(
          tags = "Menu",
          summary = "Create a Menu",
          description = "Adds a menu",
          responses = {
                  @ApiResponse(
                          responseCode = "201",
                          description = "Resource created",
                          content = @Content(
                                  mediaType = APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = Menu.class))),
                  @ApiResponse(
                          responseCode = "400",
                          description = "Bad Request",
                          content = @Content(schema = @Schema(hidden = true))),
                  @ApiResponse(
                          responseCode = "401",
                          description = "Unauthorized, Access token is missing or invalid",
                          content = @Content(schema = @Schema(hidden = true))),
                  @ApiResponse(
                          responseCode = "403",
                          description = "Bad RequestForbidden, the user does not" +
                                  " have permission to execute this operation",
                          content = @Content(schema = @Schema(hidden = true))),
                  @ApiResponse(
                          responseCode = "409",
                          description = "Conflict, an item already exists",
                          content = @Content(schema = @Schema(hidden = true)))
          })
  ResponseEntity<ResourceCreatedResponseDto> createMenu(
          @Valid @RequestBody MenuCreateRequestDto menuCreateRequestDto);

  @Operation(
          tags = "AddMenuCategory",
          summary = "Create a category in the menu",
          description = "Adds a category to a menu",
          responses = {
                  @ApiResponse(
                          responseCode = "20",
                          description = "Resource created",
                          content = @Content(
                                  mediaType = APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = Category.class))),
                  @ApiResponse(
                          responseCode = "400",
                          description = "Bad Request",
                          content = @Content(schema = @Schema(hidden = true))),
                  @ApiResponse(
                          responseCode = "401",
                          description = "Unauthorized, Access token is missing or invalid",
                          content = @Content(schema = @Schema(hidden = true))),
                  @ApiResponse(
                          responseCode = "403",
                          description = "Bad RequestForbidden, the user does not" +
                                  " have permission to execute this operation",
                          content = @Content(schema = @Schema(hidden = true))),
                  @ApiResponse(
                          responseCode = "403",
                          description = "Bad RequestForbidden, the user does not" +
                                  " have permission to execute this operation",
                          content = @Content(schema = @Schema(hidden = true))),
                  @ApiResponse(
                          responseCode = "404",
                          description = "Resource not found",
                          content = @Content(schema = @Schema(hidden = true)))
          })
  @PostMapping(value = "/{id}/category", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  ResponseEntity<ResourceCreatedResponseDto> createCategory(
          @PathVariable(name = "id") UUID id,
          @Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto);

}

