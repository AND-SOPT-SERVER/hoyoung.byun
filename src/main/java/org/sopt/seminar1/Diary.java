package org.sopt.seminar1;

import java.time.LocalDate;

public class Diary {

    private Long id;
    private String body;
    private int updateCount;
    private LocalDate lastUpdateDate;

    public Diary(Long id, String body){
        this.id = id;
        this.body = body;
        this.updateCount = 0;
        this.lastUpdateDate = LocalDate.now();
    }

    public Long getId(){
        return id;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
