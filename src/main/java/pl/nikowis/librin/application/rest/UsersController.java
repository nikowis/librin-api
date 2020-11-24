package pl.nikowis.librin.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.domain.rating.dto.CreateRatingDTO;
import pl.nikowis.librin.domain.rating.dto.RatingDTO;
import pl.nikowis.librin.domain.rating.service.RatingService;
import pl.nikowis.librin.domain.user.dto.PublicUserDTO;
import pl.nikowis.librin.domain.user.service.UserService;


@RestController
@RequestMapping(path = UsersController.USERS_ENDPOINT)
public class UsersController {

    public static final String USERS_ENDPOINT = "/users";
    public static final String USERS_ID_VARIABLE = "userId";
    public static final String USER_PATH = "/{" + USERS_ID_VARIABLE + "}";
    public static final String RATINGS_ENDPOINT = "/ratings";
    public static final String RATINGS_PATH = "/{" + USERS_ID_VARIABLE + "}" + RATINGS_ENDPOINT;
    public static final String FULL_USERS_ENDPOINT = USERS_ENDPOINT + USER_PATH;
    public static final String FULL_RATINGS_ENDPOINT = USERS_ENDPOINT + RATINGS_PATH;

    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @GetMapping(path = USER_PATH)
    public PublicUserDTO getUser(@PathVariable(USERS_ID_VARIABLE) Long userId) {
        return userService.getPublicUserInfo(userId);
    }

    @GetMapping(path = RATINGS_PATH)
    public Page<RatingDTO> getUserRatings(@PathVariable(USERS_ID_VARIABLE) Long userId, Pageable pageable) {
        return ratingService.getUserRatings(userId, pageable);
    }

    @PostMapping(path = RATINGS_PATH)
    public RatingDTO createUserRating(@PathVariable(USERS_ID_VARIABLE) Long userId, @RequestBody @Validated CreateRatingDTO dto) {
        return ratingService.createRating(userId, dto);
    }

}
