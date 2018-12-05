package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class BombItem extends Item {

	public BombItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}

	@Override
	public boolean collide(Entity e) {
	//	System.out.println("quy");
		if(e instanceof Bomber) {

			Game.addBombRate(10);
			remove();
			return true;
		}
		// TODO: xử lý Bomber ăn Item
		else {return false;}
	}
	


}
