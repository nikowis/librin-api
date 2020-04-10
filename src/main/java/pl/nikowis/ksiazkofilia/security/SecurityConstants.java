package pl.nikowis.ksiazkofilia.security;

public class SecurityConstants {
    public static final String TOKEN_ROLE_KEY = "rol";
    public static final String TOKEN_ID_KEY = "id";
    public static final String TOKEN_ACTIVE_KEY = "act";
    public static final int JWT_TOKEN_VALIDITY = 24 * 60 * 60;
    public static final String JWT_TOKEN_COOKIE = "jwtToken";

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MOD = "MOD";
}
