package edu.hitsz.application;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.hitsz.R;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.FirepowerProp;
import edu.hitsz.prop.HpAddProp;


import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    private static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();
    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    public static Bitmap BACKGROUND_IMAGE_EASY;
    public static Bitmap BACKGROUND_IMAGE_MEDIUM;
    public static Bitmap BACKGROUND_IMAGE_HARD;

    public static Bitmap HERO_IMAGE;
    public static Bitmap HERO_BULLET_IMAGE;

    public static Bitmap ENEMY_BULLET_IMAGE;
    public static Bitmap MOB_ENEMY_IMAGE;
    public static Bitmap ELITE_ENEMY_IMAGE;
    public static Bitmap BOSS_ENEMY_IMAGE;

    public static Bitmap PROP_HP_IMAGE;
    public static Bitmap PROP_BOMB_IMAGE;
    public static Bitmap PROP_FIRE_IMAGE;

    public static void initImage(Resources resources){

        BitmapFactory.Options options = new BitmapFactory.Options();
        try {

            BACKGROUND_IMAGE_EASY = BitmapFactory.decodeResource(resources, R.drawable.bg, options);
            BACKGROUND_IMAGE_MEDIUM = BitmapFactory.decodeResource(resources, R.drawable.bg2, options);
            BACKGROUND_IMAGE_HARD = BitmapFactory.decodeResource(resources, R.drawable.bg5, options);

            HERO_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.hero, options);
            MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.mob, options);
            ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.elite, options);
            BOSS_ENEMY_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.boss, options);

            HERO_BULLET_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.bullet_hero, options);
            ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.bullet_enemy, options);

            PROP_HP_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.prop_blood, options);
            PROP_FIRE_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.prop_bullet, options);
            PROP_BOMB_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.prop_bomb, options);

            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE);

            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);

            CLASSNAME_IMAGE_MAP.put(HpAddProp.class.getName(), PROP_HP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(FirepowerProp.class.getName(), PROP_FIRE_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(), PROP_BOMB_IMAGE);

        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    public static Bitmap get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Bitmap get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }
}






