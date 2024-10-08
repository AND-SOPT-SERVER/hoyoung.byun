package org.sopt.seminar1;

import java.util.List;

public class DiaryController {
    private Status status = Status.READY;
    private final DiaryService diaryService = new DiaryService();

    Status getStatus() {
        return status;
    }

    void boot() {
        this.status = Status.RUNNING;
    }

    void finish() {
        this.status = Status.FINISHED;
    }

//    // APIS
    final List<Diary> getList() {
        return diaryService.getDiaryList();
    }

    final void post(final String body) {

        try {
            if(body.length() > 30){
                throw new IllegalArgumentException();
            }
            diaryService.writeDiary(body);

        } catch (IllegalArgumentException e) {

            System.out.println("일기는 30자 이내로 작성해주세요.");

        }
    }

    final void delete(final String id) {

        Long DiaryId = null;

        try{
            DiaryId = Long.parseLong(id);
            diaryService.deleteDiary(DiaryId);

        } catch (NumberFormatException e) {

            System.out.println("올바른 id 형식이 아닙니다.");

        }
    }

    final void patch(final String id, final String body) {
        Long DiaryId = null;

        try{
            DiaryId = Long.parseLong(id);
            if(body.length() > 30){
                throw new IllegalArgumentException();
            }
            diaryService.updateDiary(DiaryId, body);

        } catch (NumberFormatException e) {

            System.out.println("올바른 id 형식이 아닙니다.");

        } catch (IllegalArgumentException e) {

            System.out.println("일기는 30자 이내로 작성해주세요.");

        }
    }

    enum Status {
        READY,
        RUNNING,
        FINISHED,
        ERROR,
    }
}