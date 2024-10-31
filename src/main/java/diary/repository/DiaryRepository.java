package diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {

    // updatedAt이 가장 최신인 DiaryEntity 하나를 가져오는 메서드
//    DiaryEntity findTopByOrderByUpdatedAtDesc();

    // user_id로 일기 찾아오는 함수
    List<DiaryEntity> findByUserId(Long user_id);

    // title로 일기 찾아서 반환하는 함수
    Optional<DiaryEntity> findByTitle(String title);

    // id로 일기 찾아서 반환하는 함수
    Optional<DiaryEntity> findById(Long user_id);

    // isShare가 True이고 category 별로 일기 반환해주는 함수
    List<DiaryEntity> findByIsShareTrueAndCategory(Category category);

    // user_id, category 값에 따라 일기 반환해주는 함수
    List<DiaryEntity> findByUserIdAndCategory(Long userId, Category category);
}
