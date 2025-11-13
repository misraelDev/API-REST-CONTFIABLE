package com.contfiable.exception;

public class ApiError {

    private String message;
    private String field;

    public ApiError(String message) {
        this.message = message;
    }

    public ApiError(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}

