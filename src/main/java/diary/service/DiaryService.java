package diary.service;

import diary.api.DiaryResponse;
import diary.repository.DiaryEntity;
import diary.repository.DiaryRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository){
        this.diaryRepository = diaryRepository;
    }

    public void createDiary(String name, String title, String content){

        DiaryEntity diaryEntity = new DiaryEntity(name, title, content);
        diaryRepository.save(diaryEntity);
    }

    public List<Diary> getList(){

        // repository로부터 DiaryEntity(DB에서 가져온 것)를 가져옴
        final List<DiaryEntity> diaryEntityList = diaryRepository.findAll();

        // createdAt 기준으로 내림차순 정렬
        diaryEntityList.sort(Comparator.comparing(DiaryEntity::getCreatedAt).reversed());

        // DiaryEntity를 Diary로 변환
        final List<Diary> diaryList = new ArrayList<>();


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
                .orElseThrow(() -> new IllegalArgumentException("해당 id에 맞는 일기가 존재하지 않습니다."));
        return new DiaryResponse(diaryEntity.getId(), diaryEntity.getName(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getCreatedAt());
    }

    public void updateDiary(Long id, String title, String content){

        DiaryEntity diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id에 맞는 일기가 존재하지 않습니다."));

        // 값 업데이트
        diary.setTitle(title);
        diary.setContent(content);

        // 저장
        diaryRepository.save(diary);
    }

    public void deleteDiary(Long id){

        if(!diaryRepository.existsById(id)){
            throw new IllegalArgumentException("해당 id에 맞는 일기가 존재하지 않습니다");
        }

        diaryRepository.deleteById(id);
    }
}
