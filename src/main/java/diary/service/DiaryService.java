package diary.service;

import diary.api.DiaryRequest;
import diary.api.DiaryResponse;
import diary.repository.*;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    final int MAX_CONTENT_LENGTH = 30;
    final int MAX_TITLE_LENGTH = 10;

    public DiaryService(DiaryRepository diaryRepository, UserRepository userRepository){
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public void createDiary(String title, String content, Long user_id, Category category, boolean isShare) {

        // 30자 제한 체크
        if(content.length() > MAX_CONTENT_LENGTH){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일기는 30자 이하로 작성해주세요.");
        }

        // 제목 10자 제한 체크
        if(title.length() > MAX_TITLE_LENGTH){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일기는 30자 이하로 작성해주세요.");
        }

        // 제목 중복 체크
        Optional<DiaryEntity> tempDiary = diaryRepository.findByTitle(title);
        if (tempDiary.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 제목입니다.");
        }

//        // 가장 최근의 update 시간 확인
//        Optional<DiaryEntity> recentDiary = Optional.ofNullable(diaryRepository.findTopByOrderByUpdatedAtDesc());

        // recentDiary가 존재할 경우 최근 5분 이내에 작성되었는지 확인
//        recentDiary.ifPresent(diary -> {
//            LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(5);
//
//            if (diary.getUpdatedAt().isAfter(timeLimit)) {
//                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "조금 후에 다시 시도해주세요.");
//            }
//        });

        // 문제 없을 경우 일기 생성
        DiaryEntity diaryEntity = new DiaryEntity(title, content, user_id, category, isShare);
        diaryRepository.save(diaryEntity);
    }

    @Transactional(readOnly = true)
    public List<Diary> getList(boolean orderByDate){

        // DB에서 isShare 값이 true인 모든 일기를 가져옴
        List<DiaryEntity> diaryEntityList = diaryRepository.findAll().stream()
                .filter(DiaryEntity::isShare) // isShare가 true인 항목만 필터링
                .collect(Collectors.toList());

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

                // user_id에 따른 nickname 정보 추가
                String nickname = userRepository.findById(diaryEntity.getUser_id())
                        .map(UserEntity::getNickname)
                        .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

                diaryList.add(
                        new Diary(diaryEntity.getId(), diaryEntity.getTitle(), nickname, diaryEntity.getCreatedAt())
                );
            } else {
                break;
            }
        }

        return diaryList;
    }

    @Transactional(readOnly = true)
    public List<Diary> getMyList(boolean orderByDate, Long user_id){

        // DB에서 현재 사용자의 일기를 모두 가져옴
        List<DiaryEntity> diaryEntityList = diaryRepository.findByUserId(user_id);

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
                        new Diary(diaryEntity.getId(), diaryEntity.getTitle(), null, diaryEntity.getCreatedAt())
                );
            } else {
                break;
            }
        }

        return diaryList;
    }


    @Transactional(readOnly = true)
    public List<Diary> getCategoryList(Long user_id, Category category, boolean isHome){

        List<DiaryEntity> diaryEntityList;

        if(isHome){
            // isShare가 true인 일기 중 category에 맞는 것 가져옴
            diaryEntityList = diaryRepository.findByIsShareTrueAndCategory(category);

        } else {
            // user_id, category가 맞는 것을 가져옴
            diaryEntityList = diaryRepository.findByUserIdAndCategory(user_id, category);
        }

        // DiaryEntity를 Diary로 변환
        final List<Diary> diaryList = new ArrayList<>();

        // 기준에 따라 상위 10개의 일기를 추출
        int i = 0;
        for(DiaryEntity diaryEntity : diaryEntityList) {
            if(i < 10){
                i++;

                // user_id에 따른 nickname 정보 추가
                String nickname = userRepository.findById(diaryEntity.getUser_id())
                        .map(UserEntity::getNickname)
                        .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

                diaryList.add(
                        new Diary(diaryEntity.getId(), diaryEntity.getTitle(), nickname, diaryEntity.getCreatedAt())
                );
            } else {
                break;
            }
        }
        return diaryList;
    }

    @Transactional(readOnly = true)
    public DiaryResponse getDiaryById(Long id, Long user_id){
        DiaryEntity diaryEntity = diaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 id에 맞는 일기가 존재하지 않습니다"));

        // 본인 일기가 아닌 일기를 조회시 isShare 확인하여 제공
        if(diaryEntity.getUser_id() != user_id){
            if(!diaryEntity.isShare()){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
            }
        }

        // user_id에 따른 nickname 정보 추가
        String nickname = userRepository.findById(diaryEntity.getUser_id())
                .map(UserEntity::getNickname)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        return new DiaryResponse(diaryEntity.getId(), nickname, diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getCreatedAt());
    }


    @Transactional
    public void updateDiary(Long id, Long user_id, DiaryRequest diaryRequest){

        // 일기 존재 여부 체크
        DiaryEntity diary = diaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 id에 맞는 일기가 존재하지 않습니다"));


        // 일기 작성자가 수정을 시도하는 것인지 체크
        if(diary.getUser_id() != user_id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }


        // 30자 제한 체크
        if(diaryRequest.content().length() > MAX_CONTENT_LENGTH){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일기는 30자 이하로 작성해주세요.");
        }

        // 값 업데이트
        diary.setTitle(diaryRequest.title());
        diary.setContent(diaryRequest.content());
        diary.setCategory(diaryRequest.category());
        diary.setShare(diaryRequest.isShare());

        // @Transactional annotation에 의해 수정사항 자동으로 반영
        // diaryRepository.save(diary);
    }

    @Transactional
    public void deleteDiary(Long id, Long user_id){

        // 일기 존재 여부 체크
        DiaryEntity diary = diaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 id에 맞는 일기가 존재하지 않습니다"));

        // 일기 작성자가 삭제를 시도하는 것인지 체크
        if(diary.getUser_id() != user_id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        diaryRepository.deleteById(id);
    }
}
