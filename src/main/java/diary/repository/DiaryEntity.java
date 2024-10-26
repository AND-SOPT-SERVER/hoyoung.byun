package diary.repository;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public String name;
    public String title;
    public String content;
    final private LocalDate createdAt;

    public DiaryEntity(){
        this.name = "";
        this.title = "";
        this.content = "";
        this.createdAt = LocalDate.now();
    }

    public DiaryEntity(String name, String title, String content){
        this.name = name;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public LocalDate getCreatedAt(){
        return createdAt;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content){
        this.content = content;
    }
}
