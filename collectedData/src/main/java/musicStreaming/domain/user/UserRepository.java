package musicStreaming.domain.user;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "users",
    path = "users"
)
public interface UserRepository
    extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
}
