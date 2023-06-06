package edu.hitsz.game;

import android.content.Context;
import android.os.Handler;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.ImageManager;


/**
 * @author qingzhengyu1111@outlook.com
 * @date 2023/4/14 22:28
 */
public class EasyGame extends Game{
    public EasyGame(Context context, Handler handler){
        super(context, handler);
        backgroundImage= ImageManager.BACKGROUND_IMAGE_EASY;
        cycleDuration = 480;
        threshold = 300;
        timeInterval = 40;
        enemyMaxNumber = 4;
        elitePosibility = 0.2;

    }
    // 敌机射击
    @Override
    protected void convertShootFlag(){
        enemyShootFlag = !enemyShootFlag;
    }

    @Override
    protected void timeCheckAction() {

    }

    @Override
    protected void scoreCheckAction() {}



}
