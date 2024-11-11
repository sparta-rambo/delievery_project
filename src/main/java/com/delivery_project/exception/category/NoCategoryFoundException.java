package com.delivery_project.exception.category;

public class NoCategoryFoundException extends RuntimeException {

    public NoCategoryFoundException(String message) {
        super(message);
    }
}
