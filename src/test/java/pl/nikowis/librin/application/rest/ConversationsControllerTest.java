package pl.nikowis.librin.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.TestConstants;
import pl.nikowis.librin.domain.conversation.dto.CreateConversationDTO;
import pl.nikowis.librin.domain.conversation.dto.SendMessageDTO;
import pl.nikowis.librin.domain.conversation.model.Conversation;
import pl.nikowis.librin.domain.conversation.model.Message;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.infrastructure.config.GlobalExceptionHandler;
import pl.nikowis.librin.infrastructure.config.Profiles;
import pl.nikowis.librin.infrastructure.repository.ConversationRepository;
import pl.nikowis.librin.infrastructure.repository.MessageRepository;
import pl.nikowis.librin.infrastructure.repository.OfferRepository;
import pl.nikowis.librin.infrastructure.repository.UserRepository;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
class ConversationsControllerTest {

    public static final long USER_ID = 1L;
    private static final Long OFFER_ID = 1L;
    private MockMvc mockMvc;

    @Autowired
    private ConversationsController messagesController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    @MockBean
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
        testUser = userRepository.findByEmailEmail(TestConstants.EMAIL);
        testUser2 = userRepository.findByEmailEmail(TestConstants.EMAIL2);
        testUser3 = userRepository.findByEmailEmail(TestConstants.EMAIL3);

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

        mockMvc.perform(post(ConversationsController.CONVERSATIONS_ENDPOINT)
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

        Message message1 = new Message();
        message1.setId("1");
        message1.setContent(messageContent);
        message1.setCreatedBy(testUser2.getId());
        message1.setCreatedAt(new Date());
        message1.setConversationId(saved.getId());

        when(messagesRepository.save(any(Message.class))).thenReturn(message1);
        when(messagesRepository.findByConversationIdOrderByCreatedAt(any(Long.class), any(Pageable.class))).thenReturn(new PageImpl<>(Lists.newArrayList(message1)));

        mockMvc.perform(post(ConversationsController.CONVERSATION_ENDPOINT, saved.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(sendMessageDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(message1.getId())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())));
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL2)
    public void getConversation() throws Exception {
        Conversation conversation = new Conversation();
        conversation.setCustomer(testUser2);
        conversation.setOffer(o);
        Conversation saved = conversationRepository.save(conversation);

        Message message1 = new Message();
        message1.setId("1");
        String messageContent1 = "Hello, how much?";
        message1.setContent(messageContent1);
        message1.setCreatedBy(testUser.getId());
        message1.setCreatedAt(new Date());
        message1.setConversationId(saved.getId());

        Message message2 = new Message();
        message2.setId("2");
        String messageContent2 = "12 dollars?";
        message2.setContent(messageContent2);
        message2.setCreatedBy(testUser2.getId());
        message2.setCreatedAt(new Date());
        message2.setConversationId(saved.getId());

        when(messagesRepository.findByConversationIdOrderByCreatedAt(any(Long.class), any(Pageable.class))).thenReturn(new PageImpl<>(Lists.newArrayList(message1, message2)));


        messagesRepository.save(message1);
        messagesRepository.save(message2);

        mockMvc.perform(get(ConversationsController.CONVERSATION_ENDPOINT, saved.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.offer.id", is(o.getId().intValue())))
                .andExpect(jsonPath("$.offer.price", is(o.getPrice())));
    }

    @Test
    @WithUserDetails(TestConstants.EMAIL2)
    public void getConversationMessages() throws Exception {
        Conversation conversation = new Conversation();
        conversation.setCustomer(testUser2);
        conversation.setOffer(o);
        Conversation saved = conversationRepository.save(conversation);

        Message message1 = new Message();
        message1.setId("1");
        String messageContent1 = "Hello, how much?";
        message1.setContent(messageContent1);
        message1.setCreatedBy(testUser.getId());
        message1.setCreatedAt(new Date());
        message1.setConversationId(saved.getId());

        Message message2 = new Message();
        message2.setId("2");
        String messageContent2 = "12 dollars?";
        message2.setContent(messageContent2);
        message2.setCreatedBy(testUser2.getId());
        message2.setCreatedAt(new Date());
        message2.setConversationId(saved.getId());

        when(messagesRepository.findByConversationIdOrderByCreatedAt(any(Long.class), any(Pageable.class))).thenReturn(new PageImpl<>(Lists.newArrayList(message1, message2)));

        messagesRepository.save(message1);
        messagesRepository.save(message2);

        mockMvc.perform(get(ConversationsController.MESSAGES_ENDPOINT, saved.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(notNullValue())))
                .andExpect(jsonPath("$.content[0].content", is(messageContent1)))
                .andExpect(jsonPath("$.content[0].createdBy", is(notNullValue())))
                .andExpect(jsonPath("$.content[1].id", is(notNullValue())))
                .andExpect(jsonPath("$.content[1].content", is(messageContent2)))
                .andExpect(jsonPath("$.content[1].createdBy", is(notNullValue())))
                .andExpect(jsonPath("$.numberOfElements", is(2)))
                .andExpect(jsonPath("$.totalPages", is(1)));
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

        Conversation saved = conversationRepository.save(conversation);

        mockMvc.perform(get(ConversationsController.CONVERSATION_ENDPOINT, saved.getId()))
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

        mockMvc.perform(get(ConversationsController.CONVERSATIONS_ENDPOINT))
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

        mockMvc.perform(post(ConversationsController.CONVERSATIONS_ENDPOINT)
                .contentType(APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(notNullValue())));
    }

}