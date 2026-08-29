package com.garv.student_collaboration.exception;

public class RequiredSkillNotFoundException extends RuntimeException {
    public RequiredSkillNotFoundException(String message) {
        super(message);
    }
}
