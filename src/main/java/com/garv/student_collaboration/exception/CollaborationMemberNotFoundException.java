package com.garv.student_collaboration.exception;

public class CollaborationMemberNotFoundException extends RuntimeException {
    public CollaborationMemberNotFoundException(String message) {
        super(message);
    }
}
