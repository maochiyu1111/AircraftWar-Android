package edu.hitsz.prop;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.game.Game;

public class HpAddProp extends GameProp{

    private HeroAircraft heroAircraft = HeroAircraft.getInstance();

    public HpAddProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void takeEffect(){
        if(Game.isPlayMusic()){
            MainActivity.myBinder.playGetSupply();
        }
        heroAircraft.hpAdding(50);
    }

}
