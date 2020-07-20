package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.ResourceCreatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/v1/menu/{id}/category/{categoryId}/items")
public interface CreateMenuItemController {

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(
            tags = "Item",
            summary = "Add an item to an existing category in a menu",
            description = "Adds a menu item",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Resource created",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResourceCreatedResponse.class))),
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
                            description = "Forbidden, the user does not have permission to execute this operation",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict, an item already exists",
                            content = @Content(schema = @Schema(hidden = true))),
            })
    ResponseEntity<ResourceCreatedResponse> addMenuItem(
            @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
            @Parameter(description = "Category id", required = true) @PathVariable("categoryId") UUID categoryId,
            @Valid @RequestBody CreateItemRequest body,
            @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId
    );
}
