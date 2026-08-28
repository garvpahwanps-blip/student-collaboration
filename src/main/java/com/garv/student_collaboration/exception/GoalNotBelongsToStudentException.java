package com.garv.student_collaboration.exception;

public class GoalNotBelongsToStudentException extends RuntimeException {
    public GoalNotBelongsToStudentException(String message) {
        super(message);
    }
}
