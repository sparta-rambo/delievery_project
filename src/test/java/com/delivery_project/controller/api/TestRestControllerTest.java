package com.delivery_project.controller.api;


import com.delivery_project.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestRestController.class)
public class TestRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestService testService;

    @Test
    public void createUserTest() throws Exception {
        String userJson = "{\"username\":\"john_doe\"," +
                "\"password\":\"securepassword\"," +
                "\"role\":\"USER\"}";

        mockMvc.perform(post("/api/test")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isCreated())
                .andDo(document("create-test-entity",
                        requestFields(
                                fieldWithPath("id").description("The ID of the user"),
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

    @Test
    public void getUserTest() throws Exception {
        mockMvc.perform(get("/" +
                        "/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(document("get-user",
                        responseFields(
                                fieldWithPath("id").description("The ID of the user"),
                                fieldWithPath("username").description("The username of the user"),
                                fieldWithPath("password").description("The password of the user"),
                                fieldWithPath("role").description("The role of the user"),
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
