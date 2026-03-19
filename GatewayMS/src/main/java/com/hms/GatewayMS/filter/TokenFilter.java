//package com.hms.GatewayMS.filter;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class TokenFilter extends AbstractGatewayFilterFactory<TokenFilter.Config> {
//
//    private static final String SECRET = "1ca9f8e9d3255e1459f9facb888026b265ba7d5d72a5982e97b55bc5e902d573739274551653873ba44dd65f27d20e8e23144f2a113329f96c84aeb24ed90997";
//
//    public TokenFilter() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            String path = exchange.getRequest().getPath().toString();
//            if (path.equals("/user/login") || path.equals("/user/register")) {
//                return chain.filter(exchange.mutate().request(r -> r.header("X-Secret-Key", "SECRET")).build());
//            }
//            HttpHeaders header = exchange.getRequest().getHeaders();
//            if (!header.containsKey(HttpHeaders.AUTHORIZATION)) {
//                throw new RuntimeException("Authorization header is missing");
//            }
//            String authHeader = header.getFirst(HttpHeaders.AUTHORIZATION);
//            if (authHeader == null || !authHeader.startsWith("Bearer")) {
//                throw new RuntimeException("Authorization header is invalid");
//            }
//            String token = authHeader.substring(7);
//            try {
//                Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
//                exchange = exchange.mutate().request(r -> r.header("X-Secret-Key", "SECRET")).build();
//            } catch (Exception e) {
//                throw new RuntimeException("Token is invalid");
//            }
//            return chain.filter(exchange);
//        };
//    }
//
//    public static class Config {
//
//    }
//
//}
package com.hms.GatewayMS.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class TokenFilter extends AbstractGatewayFilterFactory<TokenFilter.Config> {

    private static final String SECRET = "1ca9f8e9d3255e1459f9facb888026b265ba7d5d72a5982e97b55bc5e902d573739274551653873ba44dd65f27d20e8e23144f2a113329f96c84aeb24ed90997";

    public TokenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            HttpMethod method = exchange.getRequest().getMethod();

            if (HttpMethod.OPTIONS.equals(method)
                    || path.equals("/user/login")
                    || path.equals("/user/register")) {
                exchange = exchange.mutate()
                        .request(r -> r.header("X-Secret-Key", "SECRET"))
                        .build();
                return chain.filter(exchange);
            }

            HttpHeaders headers = exchange.getRequest().getHeaders();

            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            try {
                SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                exchange = exchange.mutate()
                        .request(r -> r.header("X-Secret-Key", "SECRET"))
                        .build();

            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}