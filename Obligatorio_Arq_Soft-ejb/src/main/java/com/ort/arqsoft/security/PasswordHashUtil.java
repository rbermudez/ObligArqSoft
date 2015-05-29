/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.security;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;

public class PasswordHashUtil {

    /**
     * 
     * @param password en claro
     * @return Pair with the HashedPassword and the salt used for it.
     */
    public static Pair<String, String> getHashedPassword(String password) {
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        ByteSource salt = rng.nextBytes();
        String hashedPassword = new Sha256Hash(password, salt, 45782).toHex();
        Pair<String,String> p = new ImmutablePair<>(hashedPassword, salt.toHex());
        return p;
    }
    
    public static String getHashedPassword(String password, String salt){
         ByteSource saltSource = ByteSource.Util.bytes(Hex.decode(salt));
         String hashedPassword = new Sha256Hash(password, saltSource, 45782).toHex();
         return hashedPassword;         
    }
}
