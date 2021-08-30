package com.han.auth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.han.auth.entity.Role;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.*;

import static org.passay.DictionarySubstringRule.ERROR_CODE;

public class JwtTokenUtils {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final Long EXPIRATION = 60 * 1000 * 30L; //过期时间30分钟
    private static final Long LONG_EXPIRATION = 60 * 1000 * 60 * 24 * 7L; //一个星期
    private static final String SECRET = "Fm8oXFUSIrRDDC7VH3ccImCcNzLlQNgy";
    private static final String ISSUER = "com.han.ledger";
    private static final String ROLE = "role";
    private static final String USERNAME = "username";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    //创建token
    public static String createToken(String username, List<String> roles){
        return JWT.create()
                .withClaim(USERNAME,username)
                .withClaim(ROLE,roles)
                .withIssuer(ISSUER)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(ALGORITHM);
    }

    //创建token
    public static String createTokenWithLongTime(String username, List<String> roles){
        return JWT.create()
                .withClaim(USERNAME,username)
                .withClaim(ROLE,roles)
                .withIssuer(ISSUER)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + LONG_EXPIRATION))
                .sign(ALGORITHM);
    }

    //从token中获取用户名(此处的token是指去掉前缀之后的)
    public static String getUserName(String token){
        String username;
        try {
            username = getTokenBody(token).get(USERNAME).asString();
        } catch (Exception e){
            username = null;
        }
        return username;
    }

    public static List<Role> getUserRole(String token){
        List<String> roles = getTokenBody(token).get(ROLE).asList(String.class);
        List<Role> roleList = new ArrayList<>();
        roles.forEach(item -> {
            Role role = new Role();
            role.setName(item);
            roleList.add(role);
        });
        return roleList;

    }


    private static Map<String, Claim> getTokenBody(String token){
        Map<String, Claim> claims = null;
        JWTVerifier jwtVerifier = JWT.require(ALGORITHM).withIssuer(ISSUER).build();
        try{
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            claims = JWT.decode(token).getClaims();
        } catch(Exception e){
            new JWTVerificationException("无法验证令牌");
        }
        return claims;
    }

    public static String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(16, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }


}