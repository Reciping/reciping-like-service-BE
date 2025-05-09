package com.three.recipinglikeservicebe.global.exception.custom;

public class LikeAlreadyExistsException extends RuntimeException {
    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}