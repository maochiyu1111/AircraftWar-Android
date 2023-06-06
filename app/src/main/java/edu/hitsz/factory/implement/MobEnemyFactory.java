package edu.hitsz.factory.implement;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.game.Game;
import edu.hitsz.factory.base.EnemyFactory;

public class MobEnemyFactory implements EnemyFactory {
    public AbstractAircraft creatEnemy() {
        return new MobEnemy( (int) (ImageManager.MOB_ENEMY_IMAGE.getWidth() + Math.random() * (MainActivity.screenWidth - 2 * ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * MainActivity.screenHeight * 0.05),
                0,
                (int) (20 * Game.speedMultiplier),
                (int) (30* Game.HPMultiplier));
    }
}
