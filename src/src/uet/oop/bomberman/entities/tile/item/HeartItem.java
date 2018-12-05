package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class HeartItem extends Item{
    public HeartItem(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        if(e instanceof Bomber) {
           Bomber._heart+=1;
            remove();
            return false;
        }
        // TODO: xử lý Bomber ăn Item
        else {return false;}
    }
}
