package diary.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import diary.api.DiaryRequest;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드 제외
public class Diary {

    private final long id;
    private String title;
    private String nickname;
    private LocalDateTime createdAt;

    public Diary(Long id, String title, String nickname, LocalDateTime createdAt){
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
