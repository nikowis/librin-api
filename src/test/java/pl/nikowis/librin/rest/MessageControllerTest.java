package pl.nikowis.librin.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.TestConstants;
import pl.nikowis.librin.config.GlobalExceptionHandler;
import pl.nikowis.librin.config.Profiles;
import pl.nikowis.librin.dto.CreateConversationDTO;
import pl.nikowis.librin.dto.SendMessageDTO;
import pl.nikowis.librin.model.Conversation;
import pl.nikowis.librin.model.Message;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.model.User;
import pl.nikowis.librin.repository.ConversationRepository;
import pl.nikowis.librin.repository.MessageRepository;
import pl.nikowis.librin.repository.OfferRepository;
import pl.nikowis.librin.repository.UserRepository;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = Profiles.TEST)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/testdata.sql")
class MessageControllerTest {

    private static final Long OFFER_ID = 1L;
    public static final long USER_ID = 1L;

    private MockMvc mockMvc;

    @Autowired
    private MessagesController messagesController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private MessageRepository messagesRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private User testUser;
    private User testUser2;
    private User testUser3;
    private Offer o;
    private Offer o2;
    private Offer o3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(messagesController)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        testUser = userRepository.findByEmail(TestConstants.EMAIL);
        testUser2 = userRepository.findByEmail(TestConstants.EMAIL2);
        testUser3 = userRepository.findByEmail(TestConstants.EMAIL3);

        o = new Offer();
        o.setTitle("Title");
        o.setAuthor("Author");
        o.setOwner(testUser);
        o.setOwnerId(testUser.getId());
        o.setStatus(OfferStatus.ACTIVE);
        o = offerRepository.save(o);

        o2 = new Offer();
        o2.setTitle("Title2");
        o2.setAuthor("Author2");
        o2.setOwner(testUser2);
        o2.setOwnerId(testUser2.getId());
        o2.setStatus(OfferStatus.ACTIVE);
        o2 = offerRepository.save(o2);

        o3 = new Offer();
        o3.setTitle("Title3");
        o3.setAuthor("Author3");
        o3.setOwner(testUser3);
        o3.setOwnerId(testUser3.getId());
        o3.setStatus(OfferStatus.ACTIVE);
        o3 = offerRepository.save(o3);
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL2)
    public void createConversation() throws Exception {

        CreateConversationDTO createDto = new CreateConversationDTO();
        createDto.setOfferId(o.getId());

        mockMvc.perform(post(MessagesController.CONVERSATIONS_ENDPOINT)
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())));
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL2)
    public void sendMessage() throws Exception {
        Conversation conversation = new Conversation();
        conversation.setCustomer(testUser2);
        conversation.setOffer(o);
        Conversation saved = conversationRepository.save(conversation);

        SendMessageDTO sendMessageDTO = new SendMessageDTO();
        String messageContent = "New message";
        sendMessageDTO.setContent(messageContent);

        mockMvc.perform(post(MessagesController.CONVERSATION_ENDPOINT, saved.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(sendMessageDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.messages[0].content", is(messageContent)));
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL2)
    public void getConversation() throws Exception {
        Conversation conversation = new Conversation();
        conversation.setCustomer(testUser2);
        conversation.setOffer(o);
        Conversation saved = conversationRepository.save(conversation);

        Message message1 = new Message();
        String messageContent1 = "Hello, how much?";
        message1.setContent(messageContent1);
        message1.setCreatedBy(testUser.getId());
        message1.setCreatedAt(new Date());
        message1.setConversationId(saved.getId());

        Message message2 = new Message();
        String messageContent2 = "12 dollars?";
        message2.setContent(messageContent2);
        message2.setCreatedBy(testUser2.getId());
        message2.setCreatedAt(new Date());
        message2.setConversationId(saved.getId());

        messagesRepository.save(message1);
        messagesRepository.save(message2);

        mockMvc.perform(get(MessagesController.CONVERSATION_ENDPOINT, saved.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.messages[0].id", is(notNullValue())))
                .andExpect(jsonPath("$.messages[0].content", is(messageContent1)))
                .andExpect(jsonPath("$.messages[0].createdBy", is(notNullValue())))
                .andExpect(jsonPath("$.messages[0].read", is(true)))
                .andExpect(jsonPath("$.messages[1].id", is(notNullValue())))
                .andExpect(jsonPath("$.messages[1].content", is(messageContent2)))
                .andExpect(jsonPath("$.messages[1].createdBy", is(notNullValue())))
                .andExpect(jsonPath("$.messages[1].read", is(false)))
                .andExpect(jsonPath("$.offer.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.offer.price", is(o.getPrice())));
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL3)
    public void getConversationByUnauthorizedUser() throws Exception {
        Conversation conversation = new Conversation();
        conversation.setCustomer(testUser2);
        conversation.setOffer(o);

        Message message1 = new Message();
        String messageContent1 = "Hello, how much?";
        message1.setContent(messageContent1);
        message1.setCreatedBy(testUser.getId());

        Message message2 = new Message();
        String messageContent2 = "12 dollars?";
        message2.setContent(messageContent2);
        message2.setCreatedBy(testUser2.getId());

        conversation.getMessages().add(message1);
        conversation.getMessages().add(message2);
        Conversation saved = conversationRepository.save(conversation);

        mockMvc.perform(get(MessagesController.CONVERSATION_ENDPOINT, saved.getId()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithUserDetails(TestConstants.EMAIL2)
    public void getConversations() throws Exception {
        Conversation conversation = new Conversation();
        conversation.setCustomer(testUser2);
        conversation.setOffer(o);
        Conversation saved = conversationRepository.save(conversation);

        Conversation conversation2 = new Conversation();
        conversation2.setCustomer(testUser);
        conversation2.setOffer(o2);
        Conversation saved2 = conversationRepository.save(conversation2);

        Conversation conversation3 = new Conversation();
        conversation3.setCustomer(testUser3);
        conversation3.setOffer(o3);
        Conversation saved3 = conversationRepository.save(conversation3);

        mockMvc.perform(get(MessagesController.CONVERSATIONS_ENDPOINT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.content[0].offer.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.content[0].offer.price", is(o.getPrice())))
                .andExpect(jsonPath("$.content[1].offer.id", is(o2.getId().intValue())))
                .andExpect(jsonPath("$.content[1].offer.price", is(o2.getPrice())));
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL2)
    public void createConversationReturnsExisting() throws Exception {
        Conversation conversation = new Conversation();
        conversation.setCustomer(testUser2);
        conversation.setOffer(o);
        Conversation saved = conversationRepository.save(conversation);

        CreateConversationDTO createDto = new CreateConversationDTO();
        createDto.setOfferId(o.getId());

        mockMvc.perform(post(MessagesController.CONVERSATIONS_ENDPOINT)
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())));
    }

}