package com.garv.student_collaboration.exception;

public class RequestRejectedException extends RuntimeException {
    public RequestRejectedException(String message) {
        super(message);
    }
}
