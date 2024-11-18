package com.delivery_project.controller.api;

import com.delivery_project.dto.MenuDetails;
import com.delivery_project.dto.request.OrderItemRequestDto;
import com.delivery_project.dto.request.OrderRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.dto.response.OrderResponseDto;
import com.delivery_project.entity.Order;
import com.delivery_project.entity.Restaurant;
import com.delivery_project.entity.User;
import com.delivery_project.enums.OrderStatus;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.security.UserDetailsImpl;
import com.delivery_project.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.delivery_project.util.ApiDocumentUtils.getDocumentRequest;
import static com.delivery_project.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@AutoConfigureMockMvc(addFilters = false)
public class OrderRestControllerTest {

    User mockUser;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // Mock User 객체 생성
        mockUser = User.builder()
                .id(UUID.randomUUID())
                .username("김성호")
                .address("서울특별시 노원구")
                .role(UserRoleEnum.CUSTOMER)
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(mockUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("주문 생성 테스트")
    public void createOrderTest() throws Exception {
        // given: 주문 항목 생성
        OrderItemRequestDto.Create orderItem1 = OrderItemRequestDto.Create.builder()
                .menuId(UUID.randomUUID())
                .quantity(3)
                .build();
        OrderItemRequestDto.Create orderItem2 = OrderItemRequestDto.Create.builder()
                .menuId(UUID.randomUUID())
                .quantity(2)
                .build();
        List<OrderItemRequestDto.Create> orderItems = List.of(orderItem1, orderItem2);

        // 주문 요청 DTO 생성
        OrderRequestDto.Create orderRequestDto = OrderRequestDto.Create.builder()
                .orderItemRequestDtos(orderItems)
                .orderType("online")
                .deliveryAddress("노원구 섬밭로52")
                .deliveryRequest("문 앞에 놓아주세요")
                .restaurantId(UUID.randomUUID())
                .build();

        // 응답 메시지 DTO
        MessageResponseDto responseDto = new MessageResponseDto("Order" + SuccessMessage.CREATE.getMessage());

        // Mocking
        doNothing().when(orderService).createOrder(Mockito.any(OrderRequestDto.Create.class), Mockito.eq(mockUser));

        // when / then: MockMvc 요청
        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(responseDto.getMessage())) // 응답 메시지 검증
                .andDo(document("create-order",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("orderItemRequestDtos[].menuId").description("주문한 메뉴의 고유 ID"),
                                fieldWithPath("orderItemRequestDtos[].quantity").description("주문한 메뉴의 수량"),
                                fieldWithPath("orderType").description("주문의 유형 (online 또는 offline)"),
                                fieldWithPath("deliveryAddress").description("배달 주소"),
                                fieldWithPath("deliveryRequest").description("배달 요청 사항"),
                                fieldWithPath("restaurantId").description("주문한 음식점의 고유 ID")
                        ),
                        responseFields(
                                fieldWithPath("message").description("주문 생성 성공 메시지")
                        )
                ));

        // verify: Mock Service 호출 여부 확인
        Mockito.verify(orderService).createOrder(Mockito.any(OrderRequestDto.Create.class), Mockito.any());
    }

    @Test
    @DisplayName("주문 상세 조회")
    public void findOrderTest() throws Exception {
        // given
        UUID orderId = UUID.fromString("54661171-6767-433e-bb0f-d5295056872c");

        Map<String, MenuDetails> orderItems = new HashMap<>();
        orderItems.put("피자", new MenuDetails(2, 15000));
        orderItems.put("파스타", new MenuDetails(1, 12000));

        OrderResponseDto orderResponseDto = new OrderResponseDto(
                orderItems,
                "피자리아",
                "김원재",
                42000,
                Timestamp.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                "온라인",
                "의정부시 흥선동",
                "양파빼고 주세요",
                OrderStatus.FINISHED.getStatus()
        );

        // `findOrderDetails` 메서드가 호출되면 `orderResponseDto` 반환
        when(orderService.findOrderDetails(orderId)).thenReturn(orderResponseDto);

        // when / then
        mockMvc.perform(get("/api/order/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurantName").value("피자리아"))
                .andExpect(jsonPath("$.userName").value("김원재"))
                .andExpect(jsonPath("$.totalPrice").value(42000))
                .andExpect(jsonPath("$.orderItems.피자.quantity").value(2))
                .andExpect(jsonPath("$.orderItems.피자.price").value(15000))
                .andExpect(jsonPath("$.orderItems.파스타.quantity").value(1))
                .andExpect(jsonPath("$.orderItems.파스타.price").value(12000))
                .andDo(document("find-order",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("orderId").description("조회할 주문의 ID")
                        ),
                        responseFields(
                                fieldWithPath("restaurantName").description("가게 이름"),
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("totalPrice").description("주문 총 금액"),
                                fieldWithPath("orderDate").description("주문 생성 시간"),
                                fieldWithPath("orderType").description("주문 타입"),
                                fieldWithPath("deliveryAddress").description("배달 주소"),
                                fieldWithPath("deliveryRequest").description("배달 요청 사항"),
                                fieldWithPath("status").description("주문 상태"),
                                fieldWithPath("orderItems.피자.quantity").description("메뉴 '피자'의 수량"),
                                fieldWithPath("orderItems.피자.price").description("메뉴 '피자'의 가격"),
                                fieldWithPath("orderItems.파스타.quantity").description("메뉴 '파스타'의 수량"),
                                fieldWithPath("orderItems.파스타.price").description("메뉴 '파스타'의 가격")
                        )
                ));
    }

    @Test
    @DisplayName("주문 삭제 기능")
    void deleteOrderTest() throws Exception {
        // given: Mock User 설정
        OrderRequestDto.Create mockOrderRequestDto = Mockito.mock(OrderRequestDto.Create.class);
        Restaurant mockRestaurant = Mockito.mock(Restaurant.class);

        // Order 객체 생성
        Order mockOrder = Order.builder()
                .orderRequestDto(mockOrderRequestDto) // Mock OrderRequestDto
                .totalPrice(15000)                   // 예시: 총 가격
                .restaurant(mockRestaurant)          // Mock Restaurant
                .user(mockUser)                      // Mock User
                .build();

        // Mocking 서비스 호출
        Mockito.when(orderService.getOrder(mockOrder.getId())).thenReturn(mockOrder);
        doNothing().when(orderService).deleteOrder(Mockito.eq(mockOrder), Mockito.eq(mockUser));

        // when / then: MockMvc 요청 및 검증
        mockMvc.perform(patch("/api/order/{orderId}", mockOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order" + SuccessMessage.DELETE.getMessage()))
                .andDo(document("delete-order",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("orderId").description("삭제할 주문의 고유 ID")
                        ),
                        responseFields(
                                fieldWithPath("message").description("주문 삭제 성공 메시지")
                        )
                ));

        // Verify: 서비스 메서드 호출 검증
        Mockito.verify(orderService).getOrder(mockOrder.getId());
        Mockito.verify(orderService).deleteOrder(Mockito.eq(mockOrder), Mockito.eq(mockUser));
    }
}
