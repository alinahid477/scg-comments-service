package scg.example.comment;

import org.springframework.data.repository.ListCrudRepository;

public interface CommentRepository extends ListCrudRepository<Comment,Integer> {
}
