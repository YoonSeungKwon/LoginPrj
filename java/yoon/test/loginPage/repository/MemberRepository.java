package yoon.test.loginPage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.test.loginPage.entity.Members;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    Members findMembersByEmail(String email);
}
