package edu.hitsz.strategy.concrete;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.ShootingStrategy;

import java.util.LinkedList;
import java.util.List;

public class StraightShootingStrategy implements ShootingStrategy {

    @Override
    public List<BaseBullet> shootBullet(AbstractAircraft aircraft, int direction, int power, int shootNum) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction*5;
        BaseBullet bullet;
        if (direction == -1){
            for(int i=0; i<shootNum; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                bullet = new HeroBullet(x + (i*2 - shootNum + 1)*40, y, speedX, -20, power);
                res.add(bullet);
            }
        }
        else {
            for(int i=0; i<shootNum; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                bullet = new EnemyBullet(x + (i*2 - shootNum + 1)*40, y, speedX, speedY, power);
                res.add(bullet);
            }
        }

        return res;
    }
}
