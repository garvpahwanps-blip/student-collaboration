package com.garv.student_collaboration.exception;

public class AlreadyAcceptedAsTeamMemberException extends RuntimeException {
    public AlreadyAcceptedAsTeamMemberException(String message) {
        super(message);
    }
}
