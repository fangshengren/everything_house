package com.house.everything_house_backend.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class TokenUtils {
    public static String genToken(String userId,String sign){
        return JWT.create().withAudience(userId) // 将 user id 保存到 token 里面,作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(),2)) //2小时后token过期
                .sign(Algorithm.HMAC256(sign));
    }

    public static String genEmailLoginToken(String userId) {
        // 使用一个固定的密钥
        String secret = "sDk$83Jf!2nQ@lP#7aR_5gT^8mW*1xE&9cV%4bY(0hZ)6uI";
        return JWT.create().withAudience(userId) // 将 user id 保存到 token 里面,作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2小时后token过期
                .sign(Algorithm.HMAC256(secret)); // 以固定的密钥作为 token 的密钥
    }

}
