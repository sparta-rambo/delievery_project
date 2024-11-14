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
import com.delivery_project.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 생성 테스트")
    public void createOrderTest() throws Exception {
        // given
        OrderItemRequestDto.Create orderItem1 = OrderItemRequestDto.Create.builder()
                .menuId(UUID.randomUUID())
                .quantity(3)
                .build();
        OrderItemRequestDto.Create orderItem2 = OrderItemRequestDto.Create.builder()
                .menuId(UUID.randomUUID())
                .quantity(2)
                .build();

        List<OrderItemRequestDto.Create> orderItems = List.of(orderItem1, orderItem2);

        OrderRequestDto.Create orderRequestDto = OrderRequestDto.Create.builder()
                .orderItemRequestDtos(orderItems)
                .orderType("online")
                .deliveryAddress("노원구 섬밭로52")
                .deliveryRequest("문 앞에 놓아주세요")
                .restaurantId(UUID.randomUUID())
                .build();
        Order order = Order.builder()
                .orderRequestDto(orderRequestDto)
                .totalPrice(7000)
                .user(Mockito.mock(User.class))
                .restaurant(Mockito.mock(Restaurant.class))
                .build();
        MessageResponseDto responseDto = new MessageResponseDto("Order" + SuccessMessage.CREATE.getMessage());

        doNothing().when(orderService).createOrder(Mockito.any(OrderRequestDto.Create.class), Mockito.any());

        // when / then
        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(responseDto.getMessage())) // 응답 메시지 검증
                .andDo(document("create-order",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("orderItemRequestDtos[].menuId").description("주문한 메뉴의 id 입니다."),
                                fieldWithPath("orderItemRequestDtos[].quantity").description("주문한 메뉴의 수량 입니다."),
                                fieldWithPath("orderType").description("주문의 타입입니다. 'online' or 'offline'"),
                                fieldWithPath("deliveryAddress").description("배달 배송지입니다."),
                                fieldWithPath("deliveryRequest").description("배달 요청사항입니다."),
                                fieldWithPath("restaurantId").description("가게 id 입니다")
                        ),
                        responseFields(
                                fieldWithPath("message").description("주문이 성공적으로 생성 시 메시지입니다.")
                        )
                ));
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
}
