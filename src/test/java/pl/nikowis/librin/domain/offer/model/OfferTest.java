package pl.nikowis.librin.domain.offer.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.nikowis.librin.domain.rating.exception.UnauthorizedCreateRatingException;
import pl.nikowis.librin.domain.user.model.User;

public class OfferTest {

    private static final Long RANDOM_ID = 11222L;
    private static final Long OWNER_ID = 12L;
    private static final Long RATING_AUTHOR_ID = 15L;

    @Test
    void testCreateRating() {
        Offer o = new Offer();
        o.setOwnerId(OWNER_ID);
        o.setStatus(OfferStatus.SOLD);
        User buyer = new User();
        buyer.setId(RATING_AUTHOR_ID);
        o.setBuyer(buyer);

        Assertions.assertDoesNotThrow(() -> o.validateCanCreateRating(OWNER_ID, RATING_AUTHOR_ID));
    }

    @Test
    void testCreateRatingWrongOfferStatus() {
        Offer o = new Offer();
        o.setOwnerId(OWNER_ID);
        o.setStatus(OfferStatus.ACTIVE);
        User buyer = new User();
        buyer.setId(RATING_AUTHOR_ID);
        o.setBuyer(buyer);

        Assertions.assertThrows(UnauthorizedCreateRatingException.class, () -> o.validateCanCreateRating(OWNER_ID, RATING_AUTHOR_ID));
    }

    @Test
    void testCreateRatingWrongOfferOwner() {
        Offer o = new Offer();
        o.setOwnerId(RANDOM_ID);
        o.setStatus(OfferStatus.SOLD);
        User buyer = new User();
        buyer.setId(RATING_AUTHOR_ID);
        o.setBuyer(buyer);

        Assertions.assertThrows(UnauthorizedCreateRatingException.class, () -> o.validateCanCreateRating(OWNER_ID, RATING_AUTHOR_ID));
    }

    @Test
    void testCreateRatingWrongOfferBuyerNull() {
        Offer o = new Offer();
        o.setOwnerId(OWNER_ID);
        o.setStatus(OfferStatus.SOLD);
        o.setBuyer(null);

        Assertions.assertThrows(UnauthorizedCreateRatingException.class, () -> o.validateCanCreateRating(OWNER_ID, RATING_AUTHOR_ID));
    }

    @Test
    void testCreateRatingWrongOfferBuyer() {
        Offer o = new Offer();
        o.setOwnerId(OWNER_ID);
        o.setStatus(OfferStatus.SOLD);
        User buyer = new User();
        buyer.setId(RANDOM_ID);
        o.setBuyer(buyer);

        Assertions.assertThrows(UnauthorizedCreateRatingException.class, () -> o.validateCanCreateRating(OWNER_ID, RATING_AUTHOR_ID));
    }
}
