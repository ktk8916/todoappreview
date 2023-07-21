package com.study.todoappreview.global.auth;

import com.study.todoappreview.global.auth.exception.NoTokenException;
import com.study.todoappreview.global.login.domain.dto.MemberDto;
import com.study.todoappreview.member.domain.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;

@Service
public class AuthService {

//    @Value("${jwt.secret}")
    private final String SECRET_KEY = "adasdadsKJHJKH%^^%^&675674565VBGJHGHJghjghj";
    private final SignatureAlgorithm HS256 = SignatureAlgorithm.HS256;

    public String makeToken(Member member){

        final SecretKeySpec KEY = new SecretKeySpec(SECRET_KEY.getBytes(), HS256.getJcaName());

        return Jwts.builder()
                .claim("memberId", member.getId())
                .claim("name", member.getName())
                .claim("age", member.getAge())
                .setExpiration(new Date(System.currentTimeMillis()+1_200_000))
                .signWith(KEY)
                .compact();
    }

    public Map<String, Object> getClaims(String token){

        if(token.isBlank()){
            throw new NoTokenException();
        }

        return (Claims) Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parse(token)
                .getBody();
    }

    public MemberDto tokenToMemberDto(String token){

        Map<String, Object> claims = getClaims(token);
        return new MemberDto(
                ((Integer) claims.get("memberId")).longValue(),
                (String) claims.get("name"),
                (Integer) claims.get("age")
        );
    }
}
