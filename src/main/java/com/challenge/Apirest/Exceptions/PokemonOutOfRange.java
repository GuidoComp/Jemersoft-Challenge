package com.challenge.Apirest.Exceptions;

import lombok.Data;
@Data
public class PokemonOutOfRange extends Exception {
    public PokemonOutOfRange(String message) {
        super(message);
    }

    public PokemonOutOfRange(String message, Throwable cause) {
        super(message, cause);
    }

    public PokemonOutOfRange(Throwable cause) {
        super(cause);
    }

    protected PokemonOutOfRange(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
