package musicStreaming.domain.comment;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "comments",
    path = "comments"
)
public interface CommentRepository
    extends PagingAndSortingRepository<Comment, Long> {
}
