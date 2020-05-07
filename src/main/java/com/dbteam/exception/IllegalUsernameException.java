package com.dbteam.exception;

public class IllegalUsernameException extends Exception {
    public IllegalUsernameException() {
        super();
    }

    public IllegalUsernameException(String message) {
        super(message);
    }
}
