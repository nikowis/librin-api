package pl.nikowis.librin.model;

public enum OfferCondition {
    DESTROYED(1),
    USED(2),
    GOOD(3),
    PERFECT(4),
    NEW(5);

    private final int condition;

    OfferCondition(int condition) {
        this.condition = condition;
    }

    public int condition() {
        return condition;
    }
}
