package diary.api;

import com.fasterxml.jackson.annotation.JsonInclude; // null인 type을 response에 포함시키지 않음

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드 제외
public class DiaryResponse {

    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createdAt;


    public DiaryResponse(Long id, String nickname ,String title, String content, LocalDateTime createdAt){
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }


    public long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
