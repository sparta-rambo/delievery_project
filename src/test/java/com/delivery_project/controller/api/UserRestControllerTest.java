package com.delivery_project.controller.api;

import com.delivery_project.dto.request.SignupRequestDto;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.repository.jpa.UserRepository;
import com.delivery_project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import org.springframework.test.web.servlet.MockMvc;

import static com.delivery_project.util.ApiDocumentUtils.getDocumentRequest;
import static com.delivery_project.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserRestController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@AutoConfigureMockMvc(addFilters = false) // Security 필터 비활성화
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("MANAGER 회원가입 성공 테스트")
    void signupManagerSuccessTest() throws Exception {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .username("managertest")
                .password("Manager123!")
                .address("managerAddress")
                .role(UserRoleEnum.MANAGER)
                .managerToken("AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC")
                .build();

        // UserService의 signup 메서드가 정상적으로 동작하도록 설정
        doNothing().when(userService).signup(any(SignupRequestDto.class));

        // 요청을 JSON 형식으로 변환
        String requestBody = objectMapper.writeValueAsString(signupRequestDto);

        // API 요청 테스트
        mockMvc.perform(post("/api/user/signup")
                        .contentType("application/json")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registration Complete"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("user-signup-success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("username").description("사용자 아이디"),
                                fieldWithPath("password").description("사용자 비밀번호"),
                                fieldWithPath("address").description("사용자 주소"),
                                fieldWithPath("role").description("사용자 역할"),
                                fieldWithPath("managerToken").optional().description("MANAGER 토큰 (필요시)"),
                                fieldWithPath("masterToken").optional().description("MASTER 토큰 (필요시)")
                        ),
                        responseFields(
                                fieldWithPath("message").description("회원가입 성공 메시지")
                        )
                ));
    }
}
