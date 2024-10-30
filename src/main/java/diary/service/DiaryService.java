package diary.service;

import diary.api.DiaryResponse;
import diary.repository.DiaryEntity;
import diary.repository.DiaryRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository){
        this.diaryRepository = diaryRepository;
    }

    public void createDiary(String name, String title, String content) {

        final int MAX_CONTENT_LENGTH = 30;

        // 30자 제한 체크
        if(content.length() > MAX_CONTENT_LENGTH){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일기는 30자 이하로 작성해주세요.");
        }

        // 가장 최근의 update 시간 확인
        Optional<DiaryEntity> recentDiary = Optional.ofNullable(diaryRepository.findTopByOrderByUpdatedAtDesc());

        // recentDiary가 존재할 경우 최근 5분 이내에 작성되었는지 확인
        recentDiary.ifPresent(diary -> {
            LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(5);

            if (diary.getUpdatedAt().isAfter(timeLimit)) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "조금 후에 다시 시도해주세요.");
            }
        });

        // 문제 없을 경우 일기 생성
        DiaryEntity diaryEntity = new DiaryEntity(name, title, content);
        diaryRepository.save(diaryEntity);
    }

    public List<Diary> getList(boolean orderByDate){

        // repository로부터 DiaryEntity(DB에서 가져온 것)를 가져옴
        final List<DiaryEntity> diaryEntityList = diaryRepository.findAll();

        if(orderByDate){
            // createdAt 기준으로 내림차순 정렬
            diaryEntityList.sort(Comparator.comparing(DiaryEntity::getCreatedAt).reversed());
        } else {
            // contentLength 기준으로 내림차순 정렬
            diaryEntityList.sort(Comparator.comparing(DiaryEntity::getContentLength).reversed());
        }

        // DiaryEntity를 Diary로 변환
        final List<Diary> diaryList = new ArrayList<>();

        // 기준에 따라 상위 10개의 일기를 추출
        int i = 0;
        for(DiaryEntity diaryEntity : diaryEntityList) {
            if(i < 10){
                i++;
                diaryList.add(
                        new Diary(diaryEntity.getId(), diaryEntity.getTitle())
                );
            } else {
                break;
            }
        }

        return diaryList;
    }

    public DiaryResponse getDiaryById(Long id){
        DiaryEntity diaryEntity = diaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 id에 맞는 일기가 존재하지 않습니다"));
        return new DiaryResponse(diaryEntity.getId(), diaryEntity.getName(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getCreatedAt());
    }

    public void updateDiary(Long id, String title, String content){

        DiaryEntity diary = diaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 id에 맞는 일기가 존재하지 않습니다"));

        // 값 업데이트
        diary.setTitle(title);
        diary.setContent(content);

        // 저장
        diaryRepository.save(diary);
    }

    public void deleteDiary(Long id){

        if(!diaryRepository.existsById(id)){

            // 따로 Controller에서 예외처리 필요 없음!
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 id에 맞는 일기가 존재하지 않습니다");
        }

        diaryRepository.deleteById(id);
    }
}
