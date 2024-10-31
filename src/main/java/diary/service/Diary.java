package diary.service;

import diary.api.DiaryRequest;

public class Diary {

    private final long id;
    public String title;

    public Diary(Long id, String title){
        this.id = id;
        this.title = title;
    }


    public long getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }
}
