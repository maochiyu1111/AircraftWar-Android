package edu.hitsz.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.hitsz.activity.ActivityManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.activity.OnlineActivity;
import edu.hitsz.aircraft.*;
import edu.hitsz.application.*;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;


import edu.hitsz.factory.base.EnemyFactory;
import edu.hitsz.factory.implement.EliteEnemyFactory;
import edu.hitsz.factory.implement.MobEnemyFactory;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.GameProp;

import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    public static final String TAG = "Game";
    private int screenWidth,screenHeight;
    private final SurfaceHolder surfaceHolder;
    private Handler handler;
    private Canvas canvas;  //绘图的画布
    private final Paint paint;
    private int backgroundTop;
    protected Bitmap backgroundImage;
    private static Boolean playMusic;
    public static boolean isPlayMusic() {
        return playMusic;
    }
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = 40;
    protected final HeroAircraft heroAircraft = HeroAircraft.getInstance();
    protected AbstractAircraft boss;
    protected EnemyFactory enemyFactory;
    protected final List<AbstractAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<GameProp> gameProps;
    protected boolean enemyShootFlag = true;
    protected boolean heroShootFlag = true;


    protected double elitePosibility = 0.2;
    public static double speedMultiplier = 1.0;
    public static double HPMultiplier = 1.0;
    protected int enemyMaxNumber = 5;
    protected static int score = 0;
    public static void changeScore(int num){
        score += num;
    }

    //初始分数阈值
    protected int threshold = 200;
    protected int time = 0;
    protected int timeThreshold = 10000;
    protected int cycleDuration = 600;
    protected int cycleTime = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;
    protected boolean bossAppearance = false;
    private boolean isPaintBossBloodBar = false;
    private int BOSS_MAX_HP;

    private final Activity parentActivity;

    public Game(Context context, Handler handler) {
        super(context);
        this.handler = handler;

        this.parentActivity = (Activity) context;
        ActivityManager.getIns().add(parentActivity);
        heroAircraft.initial();
        score = 0;

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        gameProps = new LinkedList<>();

        //Scheduled 线程池，用于定时任务调度
        ThreadFactory gameThread = r -> {
            Thread t =new Thread(r);
            t.setName("game thread");
            return t;
        };
        executorService = new ScheduledThreadPoolExecutor(1, gameThread);

        paint=new Paint();
        surfaceHolder =this.getHolder();
        surfaceHolder.addCallback(this);
        this.setFocusable(true);
        backgroundTop=0;

        //注册触摸监听器
        this.setOnTouchListener(new HeroController(this,heroAircraft));
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
        Log.i("out", "surfaceChanged: "+screenWidth+screenHeight);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    @Override
    public void run() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            try {
                time += timeInterval;

                // 周期性执行（控制频率）
                if (timeCountAndNewCycleJudge()) {
                    // 新敌机产生
                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        AbstractAircraft enemyAircraft = produceEnemy();
                        enemyAircrafts.add(enemyAircraft);
                    }

                    // 飞机射出子弹
                    shootAction();
                }

                // 子弹移动
                bulletsMoveAction();

                // 飞机移动
                aircraftsMoveAction();

                // 道具移动
                propMoveAction();

                // 撞击检测
                crashCheckAction();

                // 分数检测
                scoreCheckAction();

                //时间检测
                timeCheckAction();

                //音乐检测
                musicCheckAction();


                // 后处理
                postProcessAction();

                //每个时刻重绘界面
                repaint();

                // 游戏结束检查
                checkGameOver();


            }
            catch (Exception e){
                System.out.println(e);
            }


        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************


    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    public AbstractAircraft produceEnemy(){
        double i = Math.random();

        if(i < elitePosibility) {
            enemyFactory = new EliteEnemyFactory();
        }
        else{
            enemyFactory = new MobEnemyFactory();
        }
        return enemyFactory.creatEnemy();
    }



    public void shootAction(){
        convertShootFlag();
        if(enemyShootFlag){
            for(AbstractAircraft enemy : enemyAircrafts  ){
                enemyBullets.addAll(enemy.shoot());
            }
        }

        if(heroShootFlag){
            // 英雄射击
            heroBullets.addAll(heroAircraft.shoot());
        }
    };


    protected abstract void convertShootFlag();

    protected abstract void timeCheckAction();

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    public void propMoveAction(){
        for (GameProp prop : gameProps) {
            prop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {

        for (BaseBullet bullet : enemyBullets){
            if(bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }

        }


        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    if(playMusic){
                        MainActivity.myBinder.playBulletHit();
                    }

                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        //获得分数，产生道具补给
                        if(enemyAircraft instanceof EliteEnemy){
                            //分数+道具
                            score += 20;
                            GameProp prop = (((EliteEnemy) enemyAircraft).creatProp());
                            if (!Objects.isNull(prop)) {
                                gameProps.add(prop);
                            }
                        }
                        else if (enemyAircraft instanceof BossEnemy){
                            score += 50;
                            gameProps.addAll(((BossEnemy) enemyAircraft).creatProps());
                        }

                        score += 10;
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for (GameProp prop :gameProps ){
            if (prop.crash(heroAircraft) || heroAircraft.crash(prop)){
                if(prop instanceof BombProp) {
                    for (BaseBullet enemyBullet : enemyBullets) {
                        ((BombProp) prop).subscribe(enemyBullet);
                    }
                    for (AbstractAircraft enemyAircraft : enemyAircrafts){
                        ((BombProp) prop).subscribe(enemyAircraft);
                    }
                }
                prop.takeEffect();
                prop.vanish();
            }
        }

    }

    /**
     * 分数检测，后续可能会添加多个方法到分数检测中
     * */
    protected abstract void scoreCheckAction();

    private void musicCheckAction() {
        //boss音乐
        if(playMusic){
            if (Objects.isNull(boss) || boss.notValid()) {
                MainActivity.myBinder.stopBgmBoss();
                MainActivity.myBinder.playBgm();
            }

            else {
                MainActivity.myBinder.playBgmBoss();
                MainActivity.myBinder.stopBgm();
            }
        }

    }

    private void checkGameOver(){
        if (heroAircraft.getHp() <= 0) {
            // 游戏结束
            executorService.shutdown();
            gameOverFlag = true;

            if(playMusic){
                MainActivity.myBinder.stopBgm();
                MainActivity.myBinder.playGameOver();
            }


            if(MainActivity.isOnline){
                Message message = Message.obtain();
                message.what = 2 ;
                message.obj = score;
                handler.sendMessage(message);

                System.out.println(score);
            }
            else{
                // 返回分数
                parentActivity.setResult(1,new Intent().putExtra("score",score));
                parentActivity.finish();
            }
        }
    }

    public void setPlayMusic(Boolean playMusic) {
        this.playMusic = playMusic;
    }
    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        gameProps.removeIf(AbstractFlyingObject::notValid);
    }

    public boolean isGameOverFlag(){
        return gameOverFlag;
    }

    public int getScore() {
        return score;
    }

    //***********************
    //      Paint 各部分
    //***********************

    private void repaint() {
        synchronized (surfaceHolder){
            draw();
        }
    }

    private void draw() {
        canvas= surfaceHolder.lockCanvas();
        if (canvas==null) return;

        // 循环绘制背景图片
        canvas.drawBitmap(backgroundImage,0,
                backgroundTop-backgroundImage.getHeight(),paint);
        canvas.drawBitmap(backgroundImage,0,backgroundTop,paint);
        backgroundTop+=4;
        if (backgroundTop==backgroundImage.getHeight()) backgroundTop=0;

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(enemyBullets);
        paintImageWithPositionRevised(heroBullets);
        paintImageWithPositionRevised(gameProps);
        paintImageWithPositionRevised(enemyAircrafts);

        canvas.drawBitmap(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2,
                paint);

        //绘制得分和生命值
        paintScoreAndLife();

        // 提交canvas内容
        surfaceHolder.unlockCanvasAndPost(canvas);
    }


    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            Bitmap image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image,
                    object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2,
                    paint);
        }
    }

    private void paintScoreAndLife() {
        int x = 30;
        int y = 75;
        Paint paint1 = new Paint();
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);

        paint1.setColor(Color.RED);
        paint1.setTypeface(font);
        paint1.setTextSize(50);

        canvas.drawText("SCORE:" + this.score, x, y, paint1);
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y+60, paint1);

        int x2 = 800;
        int y2 = 75;
        Paint paint2 = new Paint();
        Typeface font2 = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);

        paint2.setColor(Color.RED);
        paint2.setTypeface(font2);
        paint2.setTextSize(50);

        canvas.drawText("OPPONENT SCORE:" + 60, x2, y2, paint2);
        if(MainActivity.isOnline){
            //canvas.drawText("OPPONENT_SCORE:" + OnlineActivity.getOpponentScore(), x, y+120, paint1);
        }
    }


//    private void paintBossBloodBar(Graphics g) {
//        boolean canPaint = false;
//        try {
//            if(boss.isValid()){
//                canPaint = true;
//            }
//        } catch (Exception ex){
//            canPaint = false;
//        }
//
//        if(canPaint){
//            if(!isPaintBossBloodBar){
//                BOSS_MAX_HP = boss.getHp();
//                isPaintBossBloodBar = true;
//            }
//            g.setColor(Color.white);
//            g.fillRect(boss.getLocationX() - 50, boss.getLocationY() + 100, 100, 10);
//            int percentage = (int) (boss.getHp() * 100.0 / BOSS_MAX_HP) ;
//            g.setColor(Color.red);
//            g.fillRect(boss.getLocationX() - 50, boss.getLocationY() + 100, percentage, 10);
//        } else {
//            isPaintBossBloodBar = false;
//        }
//
//
//    }


}
