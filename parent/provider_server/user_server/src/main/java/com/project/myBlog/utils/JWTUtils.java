package com.project.myBlog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    private static final String jwtToken = "@1560789myBlog!@#$";

    public static String createToken(Long userId){
        // content
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);

        // encrypt process
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) // use token to sign
                .setClaims(claims) // unique content body
                .setIssuedAt(new Date()) // set sign time
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));// one day to expire
        String token = jwtBuilder.compact();
        return token;
    }

    // decrypt
    public static Map<String, Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody(); // get useId I set before
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
