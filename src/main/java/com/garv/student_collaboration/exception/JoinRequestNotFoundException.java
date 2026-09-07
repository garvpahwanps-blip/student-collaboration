package com.garv.student_collaboration.exception;

public class JoinRequestNotFoundException extends RuntimeException {
    public JoinRequestNotFoundException(String message) {
        super(message);
    }
}
