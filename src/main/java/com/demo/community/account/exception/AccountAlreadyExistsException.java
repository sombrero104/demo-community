package com.demo.community.account.exception;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String email) {
        super("Email is already in use: " + email);
    }

}
