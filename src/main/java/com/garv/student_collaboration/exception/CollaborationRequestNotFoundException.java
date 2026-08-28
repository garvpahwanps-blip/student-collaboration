package com.garv.student_collaboration.exception;

public class CollaborationRequestNotFoundException extends RuntimeException {
    public CollaborationRequestNotFoundException(String message) {
        super(message);
    }
}
