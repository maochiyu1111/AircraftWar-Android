package edu.hitsz.dao;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/7 21:28
 */
public interface UserDao {

    void add(String name, int score, Date date);

    User getByIndex(int index);

    void deleteById(int recordId);

    UserDao getSorted();


    List<DataGridRow> toRowList();

    void writeToFile(AppCompatActivity activity, String name);


}
