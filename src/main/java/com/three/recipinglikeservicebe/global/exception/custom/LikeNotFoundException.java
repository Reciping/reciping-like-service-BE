package com.three.recipinglikeservicebe.global.exception.custom;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException(String message) {
        super(message);
    }
}