package uet.oop.bomberman.entities.character.enemy;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.ai.AIMedium;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;
import java.util.Random;

public class Oneal extends Enemy {
	protected Random random = new Random();
	public Oneal(int x, int y, Board board) {
		super(x, y, board, Sprite.oneal_dead, Game.getBomberSpeed(), 200);

		_sprite = Sprite.oneal_left1;

		_ai = new AIMedium(_board.getBomber(), this,_board);
		_direction  = _ai.calculateDirection();

	}
	@Override
	protected void chooseSprite() {
		//System.out.println(_direction);
		switch(_direction) {
			case 0:
				_sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, _animate, 120);
				break;
			case 1:

				_sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, _animate, 120);
				break;
			case 2:
				_sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, _animate, 120);

				break;
			case 3:
				_sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, _animate, 120);
				break;

		}
	}

	@Override
	public boolean collide(Entity e) {
		if(e instanceof Bomber) { ((Bomber) e).kill(); return false;}
		if (e instanceof FlameSegment) {
			kill();
			return false;
		}
		if (e.getSprite() == Sprite.grass || e instanceof BombItem) {
			return true;
		} else {
			return false;
		}
	}
}
