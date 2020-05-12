package pl.nikowis.ksiazkofilia.security;

public class SecurityConstants {

    public final static String LETTERS_GROUP_WITH_POLISH_CHARS = "[a-zA-ZzżźćńółęąśŻŹĆĄŚĘŁÓŃ]";
    public static final String NAME_REGEX = "^[" + LETTERS_GROUP_WITH_POLISH_CHARS + "]+$";
    /*
        Password that contains 8 characters, no white spaces, at least one digit or special character
     */
    public final static String PSWD_REGEX = "^(?=.*[" + LETTERS_GROUP_WITH_POLISH_CHARS + "])(?=.*[0-9])(?=\\S+$).{8,}$";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MOD = "MOD";
}
