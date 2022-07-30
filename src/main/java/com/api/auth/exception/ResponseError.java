package com.api.auth.exception;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResponseError implements Serializable {

	private static final long serialVersionUID = 1L;
    
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "america/sao_paulo")
    private LocalDateTime timeStamp = LocalDateTime.now();
    private String status;

    public ResponseError(String message, HttpStatus status) {
        this.message = message;
        this.status = status.getReasonPhrase()
        .concat(" " + (Integer.valueOf(status.value()).toString()));
    }
}
