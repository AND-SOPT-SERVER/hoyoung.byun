package diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByNickname(String nickname); // 닉네임으로 사용자 조회
}