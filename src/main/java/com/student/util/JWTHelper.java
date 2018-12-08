package com.student.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 生成与校验
 * @author niuzhifeng
 * @date 2018-08-08 21:00:00
 */
public class JWTHelper {

    private static final String SECRET = "session_secret" ;
    private static final String ISSUER = "issuser_nzf" ;

    /**
     * 生成token
     * @param claims
     * @return
     */
    public static String genToken(Map<String,String> claims){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builder = JWT.create().withIssuer(ISSUER)
                    .withExpiresAt(DateUtil.strToDate(DateUtil.getPreTime(DateUtil.dateToStr(new Date()),"30")));
            claims.forEach((k,v) -> builder.withClaim(k, v));
            return builder.sign(algorithm).toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String,String> verifyToken(String token){
        Algorithm algorithm = null;
        try {
             algorithm = Algorithm.HMAC256(SECRET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String,Claim> jwtMap = jwt.getClaims();
        Map<String,String> retMap = new HashMap<>();
        jwtMap.forEach((k,v) -> retMap.put(k,v.asString()));
        return retMap;
    }
}
