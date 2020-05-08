package pl.nikowis.ksiazkofilia.exception;

public class PolicyNotFoundException extends BusinessException {
    @Override
    public String getFieldName() {
        return "fileName";
    }
}
