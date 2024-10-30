package diary.repository;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private int contentLength;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public DiaryEntity(){

    }

    public DiaryEntity(String name, String title, String content){
        this.name = name;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.contentLength = content.length();
    }

    public Long getId() {
        return id;
    }

    public String getName() { return name; }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public int getContentLength() { return contentLength; }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content){
        this.content = content;
    }
}
