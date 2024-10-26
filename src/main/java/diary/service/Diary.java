package diary.service;

import java.time.LocalDate;

public class Diary {

    private final long id;
    private final String name;
    public String title;
    public String content;
    public LocalDate createdAt;

    public Diary(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getContent(){
        return content;
    }

    public LocalDate getCreatedAt(){
        return createdAt;
    }
}
