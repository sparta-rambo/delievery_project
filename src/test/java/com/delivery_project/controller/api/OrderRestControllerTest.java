package com.delivery_project.controller.api;

import com.delivery_project.dto.request.OrderItemRequestDto;
import com.delivery_project.dto.request.OrderRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.entity.Order;
import com.delivery_project.entity.Restaurant;
import com.delivery_project.entity.User;
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

import java.util.List;
import java.util.UUID;

import static com.delivery_project.util.ApiDocumentUtils.getDocumentRequest;
import static com.delivery_project.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
}
