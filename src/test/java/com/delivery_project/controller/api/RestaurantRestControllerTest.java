package com.delivery_project.controller.api;

import static com.delivery_project.util.ApiDocumentUtils.getDocumentRequest;
import static com.delivery_project.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.delivery_project.dto.request.RestaurantRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.entity.Category;
import com.delivery_project.entity.User;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = RestaurantController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class RestaurantRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("가게 생성 테스트")
    public void createRestaurantTest() throws Exception {
        // Mock 임시 User와 Category 객체 생성
        User mockUser = mock(User.class);
        Category mockCategory = mock(Category.class);

        RestaurantRequestDto restaurantRequestDto = RestaurantRequestDto.builder()
            .name("짜짜루")
            .address("서울시")
            .isHidden(false)
            .categoryId(UUID.randomUUID())  // 테스트용 Category ID
            .ownerId(UUID.randomUUID())     // 테스트용 Owner ID
            .build();

        MessageResponseDto responseDto = new MessageResponseDto("Restaurant" + SuccessMessage.CREATE.getMessage());

        // RestaurantService의 createRestaurant 메서드 호출 시 아무 동작도 하지 않도록 설정
        doNothing().when(restaurantService).createRestaurant(restaurantRequestDto, mockUser);

        // 요청을 JSON 형식으로 변환
        String requestBody = objectMapper.writeValueAsString(restaurantRequestDto);

        // API 요청 테스트
        mockMvc.perform(post("/api/restaurants")
                .contentType("application/json")
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value(responseDto.getMessage())) // 응답 메시지 검증
            .andDo(print())
            .andDo(document("create-restaurant",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                    fieldWithPath("name").description("가게의 이름입니다."),
                    fieldWithPath("address").description("가게의 주소입니다."),
                    fieldWithPath("categoryId").description("카테고리 ID입니다."),
                    fieldWithPath("ownerId").description("가게 소유자의 ID입니다."),
                    fieldWithPath("isHidden").description("가게의 공개 여부입니다.")
                ),
                responseFields(
                    fieldWithPath("message").description("가게가 성공적으로 생성되었을 때의 메시지입니다.")
                )
            ));
    }

}
