package com.congvo.be_myapp.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {

    private static final int MIN_PASSWORD_LENGTH = 8;

    public boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

}
