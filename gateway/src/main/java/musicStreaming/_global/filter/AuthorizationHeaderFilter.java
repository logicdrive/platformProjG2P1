package musicStreaming._global.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import musicStreaming._global.logger.CustomLogger;
import musicStreaming._global.logger.CustomLoggerType;
import musicStreaming._global.security.JwtDto;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    @Autowired
    private final JwtDecoder jwtDecoder;

    public AuthorizationHeaderFilter(Environment environment, JwtDecoder jwtDecoder) {
        super(Config.class);
        this.jwtDecoder = jwtDecoder;
    }
    
    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        return (exchange, chain) -> {
            try {

                ServerHttpRequest request = exchange.getRequest();
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                    return responseWithError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
                

                String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String base64Token = authorizationHeader.replace("Bearer ", "");
                JwtDto jwtDto = new JwtDto(jwtDecoder.decode(base64Token));
                CustomLogger.debug(CustomLoggerType.ENTER_EXIT, "Jwt token was decoded", String.format("{jwtDto: %s}", jwtDto.toString()));


                ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("User-Id", jwtDto.getUserId())
                    .header("User-Role", jwtDto.getRole())
                    .build();

                ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

                return chain.filter(mutatedExchange);

            } catch(Exception e) {
                CustomLogger.error(e);
                return responseWithError(exchange, "AuthorizationHeaderFilter Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        };
    }

    private Mono<Void> responseWithError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {}
}