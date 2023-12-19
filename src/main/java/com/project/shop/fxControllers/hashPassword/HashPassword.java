package com.project.shop.fxControllers.hashPassword;


import org.mindrot.jbcrypt.BCrypt;

public class HashPassword {
    private static final String FIXED_SALT = "$2a$12$abcdefghijklmnopqrstuu";

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, FIXED_SALT);
    }
}
