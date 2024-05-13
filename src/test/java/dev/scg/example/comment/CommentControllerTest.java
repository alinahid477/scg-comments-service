package dev.scg.example.comment;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentRepository repository;

    List<Comment> comments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        comments = List.of(
                new Comment(1,1,"FIRST_NAME","test1@gmail.com","FIRST_TEST_BODY",null),
                new Comment(2,1,"SECOND_NAME","test2@gmail.com","SECOND_TEST_BODY",null)
        );
    }

    @Test
    void shouldFindAllComments() throws Exception {
        String jsonResponse = """
                [
                    {
                        "id": 1,
                        "postId": 1,
                        "name": "FIRST_NAME",
                        "email": "test1@gmail.com",
                        "body": "FIRST_TEST_BODY",
                        "version": null
                    },
                    {
                        "id": 2,
                        "postId": 1,
                        "name": "SECOND_NAME",
                        "email": "test2@gmail.com",
                        "body": "SECOND_TEST_BODY",
                        "version": null
                    }
                ]
                """;

        when(repository.findAll()).thenReturn(comments);

        ResultActions resultActions = mockMvc.perform(get("/api/comments"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        JSONAssert.assertEquals(jsonResponse, resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    void shouldFindCommentWhenGivenValidId() throws Exception {
        when(repository.findById(1)).thenReturn(Optional.ofNullable(comments.get(0)));
        String json = """
                    {
                        "id": 1,
                        "postId": 1,
                        "name": "FIRST_NAME",
                        "email": "test1@gmail.com",
                        "body": "FIRST_TEST_BODY",
                        "version": null
                    }
                """;

        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void shouldNotFindCommentWithInvalidIdAndThrowNotFound() throws Exception {
        when(repository.findById(999)).thenThrow(CommentNotFoundException.class);

        mockMvc.perform(get("/api/comments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewCommentWhenGivenValidComment() throws Exception {
        var comment = new Comment(3,1,"Test", "test@gmail.com","Great Post!",null);
        when(repository.save(comment)).thenReturn(comment);
        JSONObject obj = new JSONObject();
        obj.put("id", comment.id());
        obj.put("postId", comment.postId());
        obj.put("name", comment.name());
        obj.put("email", comment.email());
        obj.put("body", comment.body());
        obj.put("version", comment.version());
        String json = obj.toString();
        
        mockMvc.perform(post("/api/comments")
                            .contentType("application/json")
                            .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json(json));
    }

    @Test
    void shouldUpdateCommentWhenGivenValidComment() throws Exception {
        Comment updated = new Comment(1,1,"John Doe","johndoe@example.com","This is my updated comment",1);
        when(repository.findById(1)).thenReturn(Optional.of(comments.get(0)));
        when(repository.save(updated)).thenReturn(updated);
        JSONObject obj = new JSONObject();
        obj.put("id", updated.id());
        obj.put("postId", updated.postId());
        obj.put("name", updated.name());
        obj.put("email", updated.email());
        obj.put("body", updated.body());
        obj.put("version", updated.version());
        String requestBody = obj.toString();
        
        mockMvc.perform(put("/api/comments/1")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotUpdateAndThrowNotFoundWhenGivenAnInvalidCommentID() throws Exception {
        Comment updated = new Comment(1,1,"","john","",1);
        when(repository.save(updated)).thenReturn(updated);
        JSONObject obj = new JSONObject();
        obj.put("id", updated.id());
        obj.put("postId", updated.postId());
        obj.put("name", updated.name());
        obj.put("email", updated.email());
        obj.put("body", updated.body());
        obj.put("version", updated.version());
        String json = obj.toString();
        
        mockMvc.perform(put("/api/comments/1")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteCommentWhenGivenValidID() throws Exception {
        doNothing().when(repository).deleteById(1);

        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isNoContent());

        verify(repository, times(1)).deleteById(1);
    }

}