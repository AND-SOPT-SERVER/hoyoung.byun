package diary.api;

import java.time.LocalDate;

public class DiaryResponse {

    private long id;
    private String name;
    private String title;
    private String content;
    private LocalDate createdAt;

    public DiaryResponse(long id, String name, String title, String content, LocalDate createdAt){
        this.id = id;
        this.name = name;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
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

    public String getContent() {
        return content;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
