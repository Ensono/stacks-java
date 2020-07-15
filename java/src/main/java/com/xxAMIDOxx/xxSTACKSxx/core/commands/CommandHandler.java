package com.xxAMIDOxx.xxSTACKSxx.core.commands;

import java.util.Optional;
import java.util.UUID;

public interface CommandHandler<T extends ApplicationCommand> {
    Optional<UUID> handle(T command);
}
