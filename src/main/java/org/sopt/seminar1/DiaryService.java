package org.sopt.seminar1;

//import org.sopt.seminar1.DiaryRepository;
import java.util.List;

public class DiaryService {

    private final DiaryRepository diaryRepository = new DiaryRepository();

    void writeDiary(final String body){
        Diary diary = new Diary(null, body);
        diaryRepository.save(diary);
    }

    List<Diary> getDiaryList(){
        return diaryRepository.findAll();
    }

    void deleteDiary(long id){
        diaryRepository.delete(id);
    }

    void updateDiary(final long id, final String newBody){
        Diary diary = new Diary(id, newBody);
        diaryRepository.update(diary);
        return ;
   }

}
