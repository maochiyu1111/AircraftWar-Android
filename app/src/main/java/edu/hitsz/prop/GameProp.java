package edu.hitsz.prop;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 道具类。
 * 三个不同的道具
 *
 * @author hitsz
 */
public abstract class GameProp extends AbstractFlyingObject {


    public GameProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        this.locationX += this.speedX;
        this.locationY += this.speedY;

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= MainActivity.screenWidth) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= MainActivity.screenHeight) {
            // 向下飞行出界
            vanish();
        } else if (locationY <= 0) {
            // 向上飞行出界
            vanish();
        }
    }

    public abstract void takeEffect();

}
