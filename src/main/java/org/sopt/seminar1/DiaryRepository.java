package org.sopt.seminar1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DiaryRepository {
    private final Map<Long, Diary> storage = new ConcurrentHashMap<>();
    private final AtomicLong numbering = new AtomicLong();

    void save(final Diary diary){

        final long id = numbering.addAndGet(1); // numbering에 1추가하여 반환

        // diary 저장
        storage.put(id, diary);

    }

    void update(final Diary newDiary){

        long id = newDiary.getId();
        String newBody = newDiary.getBody();

        if (storage.containsKey(id)) {
            Diary diary = storage.get(id);

            LocalDate today = LocalDate.now();

            // 날짜가 변경되었으면 업데이트 카운트 리셋
            if (!diary.getLastUpdateDate().isEqual(today)) {
                diary.setUpdateCount(0);
                diary.setLastUpdateDate(today);
            }

            if (diary.getUpdateCount() < 2) {
                diary.setBody(newBody);
                diary.setUpdateCount(diary.getUpdateCount() + 1);
                System.out.println("일기가 업데이트되었습니다.");
            } else {
                System.out.println("하루에 최대 2번만 일기 수정이 가능합니다.");
            }
        } else { // id에 해당하는 일기가 없는 경우
            System.out.println("해당 id에 맞는 일기가 존재하지 않습니다.");
        }

    }


    List<Diary> findAll(){

        // 1. diary 담을 자료구조
        final List<Diary> diaryList = new ArrayList<>();

        // 2. diary 가져오기
        for(long index = 1; index <= numbering.longValue(); index++){
            final Diary diary = storage.get(index);

            if(diary.getBody() != null){
                diaryList.add(new Diary(index, diary.getBody()));
            }
        }

        return  diaryList;
    }


    void delete(long id){

        if (storage.containsKey(id)) { // id에 해당하는 데이터 존재하는지 확인
            storage.remove(id);
        } else { // id에 해당하는 일기가 없는 경우
            System.out.println("해당 id에 맞는 일기가 존재하지 않습니다.");
        }
    }
}