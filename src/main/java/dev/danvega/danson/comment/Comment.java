package scg.example.comment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

public record Comment(
        @Id
        Integer id,
        Integer postId,
        @NotEmpty
        String name,
        @Email
        String email,
        @NotEmpty
        String body,
        @Version
        Integer version
) {
}
