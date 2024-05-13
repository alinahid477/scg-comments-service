package dev.scg.example.comment;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class CommentJsonTest {

    @Autowired
    JacksonTester<Comment> jacksonTester;

    @Test
    void shouldSerializeComment() throws IOException, JSONException {
        var comment = new Comment(1,1,"TEST_NAME","test@gmail.com","TEST_BODY",null);
        JSONObject obj = new JSONObject();
        obj.put("id", comment.id());
        obj.put("postId", comment.postId());
        obj.put("name", comment.name());
        obj.put("email", comment.email());
        obj.put("body", comment.body());
        obj.put("version", null);
        String expected = obj.toString();
        

        assertThat(jacksonTester.write(comment)).isEqualToJson(expected);
    }

    @Test
    void shouldDeserializeComment() throws Exception {
        var comment = new Comment(1,1,"TEST_NAME","test@gmail.com","TEST_BODY",null);
        JSONObject obj = new JSONObject();
        obj.put("id", comment.id());
        obj.put("postId", comment.postId());
        obj.put("name", comment.name());
        obj.put("email", comment.email());
        obj.put("body", comment.body());
        obj.put("version", null);
        String response = obj.toString();
        
        assertThat(jacksonTester.parseObject(response).id()).isEqualTo(1);
        assertThat(jacksonTester.parseObject(response).postId()).isEqualTo(1);
        assertThat(jacksonTester.parseObject(response).name()).isEqualTo("TEST_NAME");
        assertThat(jacksonTester.parseObject(response).email()).isEqualTo("test@gmail.com");
        assertThat(jacksonTester.parseObject(response).body()).isEqualTo("TEST_BODY");
        assertThat(jacksonTester.parseObject(response).version()).isEqualTo(null);
    }

}