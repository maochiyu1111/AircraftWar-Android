package edu.hitsz.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/7 21:36
 */
public class User implements Serializable {

    private final int id;
    private String name;
    private int score;
    private Date date;
    public User(int id,String name,int score,Date date){
        this.id=id;
        this.name=name;
        this.score=score;
        this.date=date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}


