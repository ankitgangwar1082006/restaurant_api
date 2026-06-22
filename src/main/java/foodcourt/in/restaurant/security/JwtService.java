package foodcourt.in.restaurant.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private  String SECRET_KEY;

    public String generateToken(UserDetails userDetails)
    {
        Map<String,Object> claims=new HashMap<>();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        claims.put("role",role);
        return createToken(claims,userDetails.getUsername());
    }
    private String createToken(Map<String,Object> extraClaims,String email)
    {
        return Jwts.builder().
                setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSignInKey()
    {
        byte keyBytes[] = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // =========================================================================================
    // THE VENDING MACHINE TOOL (Generics <T> aur Function)
    // 1. <T> T : Ye "Magic Box" hai. Return type abhi fix nahi hai. Jo detail mangoge,
    //            wahi return type ban jayega (Email manga toh String, Date mangi toh Date object).
    // 2. Function<Claims, T> : Ye Vending machine ka "Button" hai. Isme Input 'Claims'
    //                          (saara data) jayega, aur Output 'T' (manga hua data) aayega.
    // =========================================================================================
    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Pehle lifafa khol kar saara data (Claims) nikala
        return claimsResolver.apply(claims); // Jo button dabaya tha, uska output return kar diya
    }

    // =========================================================================================
    // THE SHORTCUT (Method Reference ::)
    // Claims::getSubject : Ye lamba code (claims -> claims.getSubject()) likhne ka shortcut hai.
    // Jaise restaurant mein menu par ungli rakh kar bolte hain "Ye laa do", waise hi hum
    // yahan bol rahe hain "Claims dabe ke andar jao aur Subject (Email) nikal kar de do".
    // =========================================================================================
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Vending machine mein Subject wala button dabaya
    }
    public boolean isTokenValid(String token ,UserDetails userDetails)
    {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    private Date extractExpiryDate(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }
    private boolean isTokenExpired(String token)
    {
        return extractExpiryDate(token).before(new Date());
    }
}
