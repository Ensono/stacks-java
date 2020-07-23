package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.DeleteMenuController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.DeleteMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.DeleteMenuHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.menu.mappers.RequestToCommandMapper.map;
import static org.springframework.http.HttpStatus.OK;

/**
 * DeleteMenuController implementation.
 *
 * @author ArathyKrishna
 */
@RestController
public class DeleteMenuControllerImpl implements DeleteMenuController {

    private DeleteMenuHandler handler;

    public DeleteMenuControllerImpl(DeleteMenuHandler handler) {
        this.handler = handler;
    }

    @Override
    public ResponseEntity<Void> deleteMenu(UUID menuId, String correlationId) {

        DeleteMenuCommand command = map(correlationId, menuId);
        handler.handle(command);
        return new ResponseEntity<>(OK);
    }
}
