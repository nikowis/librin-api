package pl.nikowis.ksiazkofilia.security;

import lombok.Data;

@Data
public class JwtLoginResponse {

    private Long id;
    private String login;

    public JwtLoginResponse() {
    }

    public JwtLoginResponse(Long id, String login) {
        this.id = id;
        this.login = login;
    }

}
