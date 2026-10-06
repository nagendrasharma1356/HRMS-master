package com.papayaCoders.Utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    // make hashPassword
    public static String hashPassword(String planTextPassword){
        // here generate secure hash password using BCrypt
        return BCrypt.hashpw(planTextPassword,BCrypt.gensalt());
    }

    // check password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
