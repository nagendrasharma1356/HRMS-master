package com.papayaCoder.Service;

import org.mindrot.jbcrypt.BCrypt;

public class UserService
{

    // Method to check if the provided password matches the stored hash
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
