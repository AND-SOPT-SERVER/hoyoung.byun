package diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {

    // updatedAt이 가장 최신인 DiaryEntity 하나를 가져오는 메서드
    DiaryEntity findTopByOrderByUpdatedAtDesc();

}
