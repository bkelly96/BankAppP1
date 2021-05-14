package com.app.dbutil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashing {

    public static String hash (String password) {
        try {

            //reference video https://www.youtube.com/watch?v=qSTZVlo2lr0
            //instantiate MessageDigest
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            //passing password into Message Digest Object
            md.update(password.getBytes());

            //create byteArray of password and hashing process
            byte[] resultByteArray = md.digest();

            StringBuilder sb = new StringBuilder();

            for (byte b : resultByteArray){
                //for each loops converts the byte into string
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
}
