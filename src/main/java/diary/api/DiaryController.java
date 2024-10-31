package diary.api;

import diary.repository.Category;
import diary.repository.DiaryEntity;
import diary.repository.DiaryRepository;
import diary.service.Diary;
import diary.service.DiaryService;
import diary.service.UserService;
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
    ResponseEntity<String> postDiary(@RequestHeader("user_id") Long user_id, @RequestBody DiaryRequest diaryRequest) {

        // Diary 생성
        diaryService.createDiary(diaryRequest.title(), diaryRequest.content(), user_id, diaryRequest.category(), diaryRequest.isShare());

        return ResponseEntity.status(HttpStatus.CREATED).body("새로운 일기가 생성되었습니다.");
    }


    @GetMapping("/diary/home")
    ResponseEntity<DiaryListResponse> getAllDiaries(){

        // 서비스로부터 가져온 diary list
        boolean orderByDate = true; // 최신순으로 가져옴
        List<Diary> diaryList = diaryService.getList(orderByDate);

        // Client와 협의한 interface로 변환
        List<DiaryResponse> diaryResponseList = diaryList.stream()
                .map(diary -> new DiaryResponse(diary.getId(), diary.getNickname(), diary.getTitle(), null, diary.getCreatedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }


    @GetMapping("/diary/home/byLength")
    ResponseEntity<DiaryListResponse> getDiariesByLength(){

        // 서비스로부터 가져온 diary list
        boolean orderByDate = false; // length 기준으로 가져옴
        List<Diary> diaryList = diaryService.getList(orderByDate);

        // Client와 협의한 interface로 변환
        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.getId(), diary.getNickname() ,diary.getTitle(), null, null));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("diary/home/{category}")
    ResponseEntity<DiaryListResponse> getDiaryByCategory(@PathVariable Category category) {

        // 서비스로부터 가져온 diary list
        boolean isShare = true; // isShare가 true인 것들만 반환
        List<Diary> diaryList = diaryService.getCategoryList(null, category, isShare);

        // Client와 협의한 interface로 변환
        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.getId(), diary.getNickname() ,diary.getTitle(), null, null));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }


    @GetMapping("/diary/me")
    ResponseEntity<DiaryListResponse> getMyDiary(@RequestHeader("user_id") Long user_id) {

        // 서비스로부터 가져온 diary list
        boolean orderByDate = true; // 최신순으로 가져옴
        List<Diary> diaryList = diaryService.getMyList(orderByDate, user_id);

        // Client와 협의한 interface로 변환
        List<DiaryResponse> diaryResponseList = diaryList.stream()
                .map(diary -> new DiaryResponse(diary.getId(), null,diary.getTitle(), null, diary.getCreatedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("diary/me/{category}")
    ResponseEntity<DiaryListResponse> getDiaryByCategory(@RequestHeader("user_id") Long user_id, @PathVariable Category category) {

        // 서비스로부터 가져온 diary list
        boolean isShare = false; // isShare가 true인 것들만 반환
        List<Diary> diaryList = diaryService.getCategoryList(user_id, category, isShare);

        // Client와 협의한 interface로 변환
        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.getId(), diary.getNickname() ,diary.getTitle(), null, null));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/diary/{id}")
    ResponseEntity<DiaryResponse> getDiaryDetail(@RequestHeader("user_id") Long user_id, @PathVariable Long id) {

        DiaryResponse diaryResponse = diaryService.getDiaryById(id, user_id);

        return ResponseEntity.ok(diaryResponse);
    }



    @PatchMapping("/diary/{id}")
    ResponseEntity<String> updateDiary(@RequestHeader("user_id") Long user_id, @PathVariable Long id, @RequestBody DiaryRequest diaryRequest){

        diaryService.updateDiary(id, user_id, diaryRequest);
        return ResponseEntity.ok("업데이트가 완료되었습니다.");
    }


    @DeleteMapping("/diary/{id}")
    ResponseEntity<String> deleteDiary(@RequestHeader("user_id") Long user_id, @PathVariable Long id){

        diaryService.deleteDiary(id, user_id);

        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }


}
