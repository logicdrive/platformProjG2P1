package musicStreaming._global.security;

import lombok.Getter;
import lombok.ToString;

import org.springframework.security.oauth2.jwt.Jwt;

// JWT에 담긴 내용들을 간편하게 접근하기 위한 DTO
@Getter
@ToString
public class JwtDto {
    private String userId;
    private String email;
    private String name;
    private String role;

    public JwtDto(Jwt jwt) {
        this.userId = jwt.getSubject();
        this.email = jwt.getClaim("email");
        this.name = jwt.getClaim("name");
        this.role = jwt.getClaim("role");
    }
}
