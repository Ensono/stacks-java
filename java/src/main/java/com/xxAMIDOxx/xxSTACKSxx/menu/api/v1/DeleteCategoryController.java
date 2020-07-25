package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1;

import com.xxAMIDOxx.xxSTACKSxx.core.api.dto.ErrorResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * @author ArathyKrishna
 */
@RequestMapping("/v1/menu/{id}/category/{categoryId}")
public interface DeleteCategoryController {

    @DeleteMapping(consumes = "application/json", produces = "application/json")
    @Operation(
            tags = "Category",
            summary = "Update a category in the menu",
            description = "Update a category to menu",
            operationId = "UpdateMenuCategory",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resource created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResourceCreatedResponse.class))),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized, Access token is missing or invalid",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden, the user does not have permission to execute this operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict, an item already exists",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
            })
    ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Menu id", required = true) @PathVariable("id") UUID menuId,
            @Parameter(description = "Category id", required = true) @PathVariable("categoryId") UUID categoryId,
            @Parameter(hidden = true) @RequestAttribute("CorrelationId") String correlationId);
}
