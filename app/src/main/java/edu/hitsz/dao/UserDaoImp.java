package edu.hitsz.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/7 21:36
 */
public class UserDaoImp implements UserDao, Serializable{

    private final List<User> users;
    private int maxId;
    public UserDaoImp(){
        users = new ArrayList<>();
        maxId=0;
    }

    @Override
    public void add(String name, int score, Date date) {
        users.add(new User(++maxId,name,score,date));
    }

    /**
     * 获取在List中序号（从0开始）为index的记录
     * @param index 待取记录的序号
     * @return 取得的记录，其序号是index
     */
    @Override
    public User getByIndex(int index) {
        return users.get(index);
    }

    @Override
    public void deleteById(int recordId) {
        users.removeIf(r -> r.getId()==recordId);
    }

    @Override
    public UserDao getSorted() {
        users.sort(Comparator.comparing(User::getScore).reversed());
        return this;
    }

    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public List<DataGridRow> toRowList() {
        List<DataGridRow> result= new LinkedList<>();
        int i=1;
        for (User user:users){
            result.add(new DataGridRow(i++,
                    user.getName(),
                    user.getScore(),
                    sdf.format(user.getDate())));
        }
        return result;
    }

    /**
     * 从文件读取RecordDaoImpl
     * @param name 待读取的文件名
     * @return 读取到的RecordDaoImpl
     */
    public static UserDaoImp readFromFile(AppCompatActivity activity, String name){
        UserDaoImp result = null;
        try(FileInputStream fis=activity.openFileInput(name);
            ObjectInputStream ois=new ObjectInputStream(fis)) {
            result=(UserDaoImp) ois.readObject();
        }catch (FileNotFoundException|InvalidClassException e){
            result=new UserDaoImp();
        }catch (Exception e){
            Log.e("outtag", "readFromFile: fuuuuuuuuuuuuuuuuuuuck");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向文件写入RecordDaoImpl
     * @param name 待写入的文件路径
     */
    @Override
    public void writeToFile(AppCompatActivity activity, String name){
        try(FileOutputStream fos=activity.openFileOutput(name, Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);) {
            oos.writeObject(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
