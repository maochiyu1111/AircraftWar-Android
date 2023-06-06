package edu.hitsz.factory.implement;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.factory.base.EnemyFactory;

public class BossEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft creatEnemy() {
        return new BossEnemy(
                (int) (ImageManager.BOSS_ENEMY_IMAGE.getWidth() + Math.random() * (MainActivity.screenWidth - 2 * ImageManager.BOSS_ENEMY_IMAGE.getWidth())),
                (int) (MainActivity.screenHeight * 0.05),
                5,
                0,
                500);
    }
}
