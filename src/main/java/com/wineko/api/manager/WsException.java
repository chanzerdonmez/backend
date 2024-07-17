package com.wineko.api.manager;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WsException extends ResponseStatusException {
    public WsException(HttpStatus code, String message) {
        super(code, message);
    }
}

