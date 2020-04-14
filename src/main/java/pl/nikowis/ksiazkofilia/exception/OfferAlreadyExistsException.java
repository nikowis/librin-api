package pl.nikowis.ksiazkofilia.exception;

public class OfferAlreadyExistsException extends BusinessException {

    @Override
    public String getFieldName() {
        return "title";
    }
}
