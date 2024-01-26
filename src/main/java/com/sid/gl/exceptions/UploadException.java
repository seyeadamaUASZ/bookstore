package com.sid.gl.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UploadException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UploadException() {
        super();

    }

    public UploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public UploadException(String message) {
        super(message);

    }

    public UploadException(Throwable cause) {
        super(cause);

    }



}
