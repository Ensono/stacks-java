package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu;

import java.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RequestMapping("/v1/menu")
public interface MenuController {

    @GetMapping("/")
    @Operation(
            tags = "Menu", summary = "Get or search a list of menus",
            description = "By passing in the appropriate options, you can search for available menus in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Search results matching criteria", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchMenuResult.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true)))
            })
    ResponseEntity<SearchMenuResult> searchMenu(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "restaurantId", required = false) UUID restaurantId,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber) throws IOException;
}
