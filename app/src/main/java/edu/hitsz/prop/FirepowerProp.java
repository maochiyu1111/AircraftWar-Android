package edu.hitsz.prop;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.game.Game;
import edu.hitsz.strategy.concrete.ScatterShootingStrategy;
import edu.hitsz.strategy.concrete.StraightShootingStrategy;

import java.util.Timer;


public class FirepowerProp extends GameProp{

    private HeroAircraft heroAircraft = HeroAircraft.getInstance();

    private Timer timer = null;

    private boolean isEffective = false;

    PowerUp powerUp = new PowerUp();


    public FirepowerProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void takeEffect(){
        heroAircraft.changeShootNum(1);
        //若上一个线程还在生效，则不创建新线程，火力道具无效
        if(isEffective == false){
            Thread powerUpThread = new Thread(powerUp);
            powerUpThread.start();
        }else return;
    }


    class PowerUp implements Runnable{
        @Override
        public void run() {
            isEffective = true;
            heroAircraft.setStrategy(new ScatterShootingStrategy());
            if (Game.isPlayMusic()){
                MainActivity.myBinder.playGetSupply();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 火力道具效果结束，主角攻击力恢复原来值
            heroAircraft.setStrategy(new StraightShootingStrategy());
            isEffective = false;
        }
    }
}
