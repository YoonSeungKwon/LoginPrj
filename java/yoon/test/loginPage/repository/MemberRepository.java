package yoon.test.loginPage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.test.loginPage.entity.Members;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    Optional<Members> findByEmail(String email);
    Members findMembersByEmail(String email);
    Members findMembersByRefreshToken(String token);
    boolean existsByEmail(String email);
}
