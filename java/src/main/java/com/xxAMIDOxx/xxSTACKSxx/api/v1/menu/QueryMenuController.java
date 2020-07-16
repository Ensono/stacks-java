package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.SearchMenuResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;


@RequestMapping("/v1/menu")
public interface QueryMenuController {

    @GetMapping(value = "", produces = "application/json")
    @Operation(
            tags = "MenuDTO",
            summary = "Get or search a list of menus",
            description =
                    "By passing in the appropriate options, you can search for available menus in the system",
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
            tags = "MenuDTO",
            summary = "Get a menu",
            description = "By passing the menu id, you can get access to available categories and items in the menu",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "MenuDTO",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MenuDTO.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    ResponseEntity<MenuDTO> getMenu(@PathVariable(name = "id") UUID id);
}

