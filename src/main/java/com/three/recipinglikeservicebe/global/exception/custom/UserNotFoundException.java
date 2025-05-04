package com.three.recipinglikeservicebe.global.exception.custom;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
