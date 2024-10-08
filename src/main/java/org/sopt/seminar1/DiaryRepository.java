package org.sopt.seminar1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DiaryRepository {
    private final Map<Long, String> storage = new ConcurrentHashMap<>();
    private final AtomicLong numbering = new AtomicLong();

    void save(final Diary diary){

        final long id = numbering.addAndGet(1); // numbering에 1추가하여 반환

        // diary 저장
        storage.put(id, diary.getBody());

    }

    void update(final Diary diary){

        long id = diary.getId();
        String body = diary.getBody();

        if (storage.containsKey(id)) { // id에 해당하는 데이터 존재하는지 확인
            storage.put(id, body);
        } else { // id에 해당하는 일기가 없는 경우
            System.out.println("해당 id에 맞는 일기가 존재하지 않습니다.");
        }

    }


    List<Diary> findAll(){

        // 1. diary 담을 자료구조
        final List<Diary> diaryList = new ArrayList<>();

        // 2. diary 가져오기
        for(long index = 1; index <= numbering.longValue(); index++){
            final String body = storage.get(index);

            if(body != null){
                diaryList.add(new Diary(index, body));
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