package com.delivery_project.controller.api;


import com.delivery_project.dto.request.TestRequestDto;
import com.delivery_project.entity.TestEntity;
import com.delivery_project.service.TestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestRestController.class)
public class TestRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestService testService;

    @Test
    public void createUserTest() throws Exception {

        // given
        TestRequestDto.Create testRequestDto = new TestRequestDto.Create("seongho","password","ROLE_USER");
        TestEntity testEntity = new TestEntity(testRequestDto);
//        given(testService.save(Mockito.any())).willReturn(testEntity);

        //when / then
        mockMvc.perform(post("/api/test")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(testRequestDto)))
                .andExpect(status().isCreated())
                .andDo(document("create-test-entity",
                        requestFields(
                                fieldWithPath("username").description("The username of the user"),
                                fieldWithPath("password").description("The password of the user"),
                                fieldWithPath("role").description("The role of the user")
                        ),
                        responseFields(
                                fieldWithPath("id").description("The ID of the created user"),
                                fieldWithPath("username").description("The username of the created user"),
                                fieldWithPath("password").description("The password of the created user"),
                                fieldWithPath("role").description("The role of the created user"),
                                fieldWithPath("createdAt").description("The time the user was created"),
                                fieldWithPath("updatedAt").description("The last time the user was updated"),
                                fieldWithPath("createdBy").optional().description("The user who created this record"),
                                fieldWithPath("updatedBy").optional().description("The last user who updated this record"),
                                fieldWithPath("deletedAt").optional().description("The time the user was deleted"),
                                fieldWithPath("deletedBy").optional().description("The user who deleted this record")
                        )
                ));
    }

}
