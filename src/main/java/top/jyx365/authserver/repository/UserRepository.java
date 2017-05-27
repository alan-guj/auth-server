package top.jyx365.authserver.repository;

import top.jyx365.authserver.domain.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

/**
 * Spring Data JPA repository for the User entity.
 */
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    //@EntityGraph(attributePaths = {"userCompanies"})
    Optional<User> findOneByActivationKey(String activationKey);

    //@EntityGraph(attributePaths = {"userCompanies"})
    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    //@EntityGraph(attributePaths = {"userCompanies"})
    Optional<User> findOneByResetKey(String resetKey);

    //@EntityGraph(attributePaths = {"userCompanies"})
    Optional<User> findOneByEmail(String email);

    //@EntityGraph(attributePaths = {"userCompanies"})
    Optional<User> findOneByLogin(String login);

    @Override
    @EntityGraph(value="User.authority", type=EntityGraphType.LOAD)
    <S extends User> List<S> findAll(Example<S> example);

    @EntityGraph(value="User.authority", type=EntityGraphType.LOAD)
    User findOneWithAuthoritiesById(Long id);

    @EntityGraph(value="User.authority", type=EntityGraphType.LOAD)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(value="User.authority", type=EntityGraphType.LOAD)
    Optional<User> findOneWithAuthoritiesByLoginOrOpenidOrMobileOrEmail(String login, String openid, String mobile, String email);

    @EntityGraph(value="User.authority", type=EntityGraphType.LOAD)
    Optional<User> findOneWithAuthoritiesByOpenid(String openid);

    @EntityGraph(value="User.authority", type=EntityGraphType.LOAD)
    Page<User> findAllByLoginNot(Pageable pageable, String login);
}
