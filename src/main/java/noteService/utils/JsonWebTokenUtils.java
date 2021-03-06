package noteService.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JsonWebTokenUtils {

    public String generateJwt(String email, String name, Date date) throws java.io.UnsupportedEncodingException {

        String jwt = Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .claim("name", name)
                .signWith(
                        SignatureAlgorithm.HS256,
                        "password123".getBytes("UTF-8")
                )
                .compact();

        return jwt;
    }

    public Map<String, Object> jwt2Map(String jwt) throws java.io.UnsupportedEncodingException, ExpiredJwtException {

        Jws<Claims> claim = Jwts.parser()
                .setSigningKey("password123".getBytes("UTF-8"))
                .parseClaimsJws(jwt);

        String name = claim.getBody().get("name", String.class);

        Date expDate = claim.getBody().getExpiration();
        String email = claim.getBody().getSubject();

        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("name", name);
        userData.put("exp_date", expDate);

        Date now = new Date();
        if (now.after(expDate)) {
            throw new ExpiredJwtException(null, null, "Session expired!");
        }

        return userData;
    }

    public String getJwtFromHttpRequest(HttpServletRequest request) {
        String jwt = null;
        if (request.getHeader("jwt") != null) {
            jwt = request.getHeader("jwt");     
        } else if (request.getCookies() != null) {
            Cookie[] cookies = request.getCookies();   
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    jwt = cookie.getValue();
                }
            }
        }
        return jwt;
    }

}
