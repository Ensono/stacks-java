package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/v1/menu/{id}/category")
public interface CreateCategoryController {

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(
            tags = "Category",
            summary = "Create a category in the menu",
            description = "Adds a category to menu",
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
    ResponseEntity<ResourceCreatedResponse> addMenuCategory(
            @Parameter(description = "Menu id",required=true) @PathVariable("id") UUID menuId,
            @Valid @RequestBody CreateCategoryRequest body,
            @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId
    );
}
