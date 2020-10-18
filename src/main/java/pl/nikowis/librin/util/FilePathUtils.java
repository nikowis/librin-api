package pl.nikowis.librin.util;

public class FilePathUtils {

    private static final String OFFERS_DIR = "offers";
    private static final String URL_SEPARATOR = "/";

    public static String getOfferPhotoPath(Long ownerId, Long offerId, String uuid, String name) {
        return URL_SEPARATOR +
                getUserIdSubstr(ownerId) +
                URL_SEPARATOR +
                ownerId +
                URL_SEPARATOR +
                OFFERS_DIR +
                URL_SEPARATOR +
                offerId +
                URL_SEPARATOR +
                uuid +
                getFileExtension(name);
    }

    private static String getUserIdSubstr(Long userId) {
        String userIdStr = String.valueOf(userId);
        if (userIdStr.length() > 2) {
            userIdStr = userIdStr.substring(0, 2);
        }
        return userIdStr;
    }

    private static String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

}
