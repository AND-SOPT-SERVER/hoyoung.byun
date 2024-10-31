package diary.repository;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hyb_diary")
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column
//    private String name;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private int content_length;

    @Column(updatable = false)
    private LocalDateTime createdAt;

//    @Column
//    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING) // DB에는 문자열로 저장되도록 함
    @Column(nullable = false)
    private Category category;  // 카테고리

    @Column
    private long userId;

    @Column
    private boolean isShare;

    public DiaryEntity(){

    }

    public DiaryEntity(String title, String content, Long user_id, Category category, boolean isShare){
//        this.name = name;
        this.title = title;
        this.content = content;
        this.userId = user_id;
        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
        this.content_length = content.length();
        this.category = category;
        this.isShare = isShare;
    }

    public Long getId() {
        return id;
    }

//    public String getName() {
//      return name;
//    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public int getContentLength() {
        return content_length;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public long getUser_id() {
        return userId;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isShare() {
        return isShare;
    }

//    public LocalDateTime getUpdatedAt(){
//        return updatedAt;
//    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content){
        this.content = content;
        this.content_length = content.length();
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setShare(boolean isShare){
        this.isShare = isShare;
    }
}
