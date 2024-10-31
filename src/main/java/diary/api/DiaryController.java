package diary.api;

import diary.repository.DiaryEntity;
import diary.service.Diary;
import diary.service.DiaryService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService){
        this.diaryService = diaryService;
    }


    @PostMapping("/diary")
    ResponseEntity<String> postDiary(@RequestBody DiaryRequest diaryRequest) {

        // Diary 생성
        diaryService.createDiary(diaryRequest.name(), diaryRequest.title(), diaryRequest.content());

        return ResponseEntity.status(HttpStatus.CREATED).body("새로운 일기가 생성되었습니다.");
    }


    @GetMapping("/diary")
    ResponseEntity<DiaryListResponse> getAllDiaries(){

        // 서비스로부터 가져온 diary list
        boolean orderByDate = true; // 최신순으로 가져옴
        List<Diary> diaryList = diaryService.getList(orderByDate);

        // Client와 협의한 interface로 변환
        List<DiaryResponse> diaryResponseList = diaryList.stream()
                .map(diary -> new DiaryResponse(diary.getId(), null, diary.getTitle(), null, null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/diary/{id}")
    ResponseEntity<DiaryResponse> getDiaryDetail(@PathVariable Long id) {

        DiaryResponse diaryResponse = diaryService.getDiaryById(id);

        return ResponseEntity.ok(diaryResponse);

    }

    @GetMapping("/diary/length")
    ResponseEntity<DiaryListResponse> getDiariesByLength(){

        // 서비스로부터 가져온 diary list
        boolean orderByDate = false; // length 기준으로 가져옴
        List<Diary> diaryList = diaryService.getList(orderByDate);

        // Client와 협의한 interface로 변환
        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.getId(), null, diary.getTitle(), null, null));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }


    @PatchMapping("/diary/{id}")
    ResponseEntity<String> updateDiary(@PathVariable Long id, @RequestBody DiaryRequest diaryRequest){

        diaryService.updateDiary(id, diaryRequest.title(), diaryRequest.content());
        return ResponseEntity.ok("업데이트가 완료되었습니다.");
    }


    @DeleteMapping("/diary/{id}")
    ResponseEntity<String> deleteDiary(@PathVariable Long id){

        diaryService.deleteDiary(id);

        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }


}
