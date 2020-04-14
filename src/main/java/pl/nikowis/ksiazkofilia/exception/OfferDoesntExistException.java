package pl.nikowis.ksiazkofilia.exception;

public class OfferDoesntExistException extends BusinessException {

    public static final String ID_FIELD = "id";

    @Override
    public String getFieldName() {
        return ID_FIELD;
    }
}
