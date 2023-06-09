package edu.hitsz.aircraft;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.game.Game;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.observer.Observer;
import edu.hitsz.strategy.ShootingStrategy;
import edu.hitsz.strategy.concrete.StraightShootingStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft implements Observer {
    private int power = 10;

    private int direction = 1;

    private ShootingStrategy strategy = new StraightShootingStrategy();

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.screenHeight ) {
            vanish();
        }
    }


    @Override
    public void update() {
        this.notValid();
        this.vanish();
        Game.changeScore(10);
    }
}
