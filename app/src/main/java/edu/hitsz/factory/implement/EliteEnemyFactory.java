package edu.hitsz.factory.implement;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.game.Game;
import edu.hitsz.factory.base.EnemyFactory;

public class EliteEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft creatEnemy() {
        return new EliteEnemy(
                (int) (ImageManager.ELITE_ENEMY_IMAGE.getWidth() + Math.random() * (MainActivity.screenWidth - 2 * ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * MainActivity.screenHeight * 0.05),
                0,
                (int) (24 * Game.speedMultiplier),
                (int) (60 * Game.HPMultiplier));
    }
}
