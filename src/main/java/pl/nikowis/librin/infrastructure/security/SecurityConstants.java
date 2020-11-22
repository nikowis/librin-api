package pl.nikowis.librin.infrastructure.security;

public class SecurityConstants {

    public final static String LETTERS_GROUP_WITH_POLISH_CHARS = "[a-zA-Z\u0104\u0106\u0118\u0141\u0143\u015A\u0179\u017B\u0105\u0107\u0119\u0142\u0144\u015B\u017A\u017C\u00F3\u00D3-]";
    public static final String NAME_REGEX = "^[" + LETTERS_GROUP_WITH_POLISH_CHARS + "]+$";
    /*
        Password that contains 8 characters, no white spaces, at least one digit or special character
     */
    public final static String PSWD_REGEX = "^.{8,}$";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MOD = "MOD";
}
