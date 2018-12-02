package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class BombItem extends Item {
	Board _board;
	public BombItem(int x, int y, Sprite sprite, Board board) {
		super(x, y, sprite);
		_board=board;
	}

	@Override
	public boolean collide(Entity e) {
		if(e instanceof Bomber) {
			Game.addBombRate(1);
			remove();
			return false;
		}
		// TODO: xử lý Bomber ăn Item
		else {return false;}
	}
	


}
