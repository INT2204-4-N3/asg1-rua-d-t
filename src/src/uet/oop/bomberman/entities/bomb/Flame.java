package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.level.LevelLoader;

public class Flame extends Entity {

	protected Board _board;
	protected int _direction;
	private int _radius;
	protected int xOrigin, yOrigin;
	protected FlameSegment[] _flameSegments = new FlameSegment[0];
	protected boolean last=false; int x1=0,y1=0;

	/**
	 *
	 * @param x hoành độ bắt đầu của Flame
	 * @param y tung độ bắt đầu của Flame
	 * @param direction là hướng của Flame
	 * @param radius độ dài cực đại của Flame
	 */
	public Flame(int x, int y, int direction, int radius, Board board) {
		xOrigin = x;
		yOrigin = y;
		_x = x;
		_y = y;
		_direction = direction;
		_radius = radius;
		_board = board;
		createFlameSegments();
	}

	/**
	 * Tạo các FlameSegment, mỗi segment ứng một đơn vị độ dài
	 */
	private void createFlameSegments() {
		/**
		 * tính toán độ dài Flame, tương ứng với số lượng segment
		 */
		_flameSegments = new FlameSegment[calculatePermitedDistance()];

		/**
		 * biến last dùng để đánh dấu cho segment cuối cùng
		 */

		// TODO: tạo các segment dưới đây
		for(int i=0;i< calculatePermitedDistance();i++) {

			if(i== calculatePermitedDistance()-1) { last=true;}
			if(_direction==1) {x1=xOrigin +i+1; y1= yOrigin;}
			if(_direction==3) {x1=xOrigin - i-1; y1= yOrigin;}
			if(_direction==2) {x1=xOrigin ; y1= yOrigin +i+1;}
			if(_direction==0){x1=xOrigin ; y1= yOrigin-i-1;}
				_flameSegments[i] = new FlameSegment(x1, y1, _direction, last);
				last = false;
		}

		}

	/**
	 * Tính toán độ dài của Flame, nếu gặp vật cản là Brick/Wall, độ dài sẽ bị cắt ngắn
	 * @return
	 */
	public int calculatePermitedDistance() {

		Entity t=null;int radi=0;
		// TODO: thực hiện tính toán độ dài của Flame
		for(int i=0;i< _radius;i++) {
			if (_direction == 1) {
				x1 = xOrigin + i + 1;
				y1 = yOrigin;
			}
			if (_direction == 3) {
				x1 = xOrigin - i - 1;
				y1 = yOrigin;
			}
			if (_direction == 2) {
				x1 = xOrigin;
				y1 = yOrigin + i + 1;
			}
			if (_direction == 0) {
				x1 = xOrigin;
				y1 = yOrigin - i - 1;
			}
			if (x1 >= 0 && y1 >= 0) {
				t = _board.getEntityAt(x1, y1);
				if (t instanceof LayeredEntity) {
					t.collide(new FlameSegment(x1, y1, _direction, last));// tao gia 1 flame de va cham
					if (((LayeredEntity) t).getTopEntity().getSprite() == Sprite.grass) {
						radi++;
					}

				} else if (t instanceof Brick) {
					_board.addCharacter(new Oneal(Coordinates.tileToPixel(x1), Coordinates.tileToPixel(y1) + Game.TILES_SIZE, _board));
					_board.addEntity(x1 + y1 * LevelLoader.getWidth(), new BombItem(x1, y1, Sprite.grass));

				}
				else if (t instanceof Grass) { // đk là cỏ và tạo đó ko có nhân vật
					radi++;
				}

				}
			}
		return radi;
	}
	
	public FlameSegment flameSegmentAt(int x, int y) {
		for (int i = 0; i < _flameSegments.length; i++) {
			if(_flameSegments[i].getX() == x && _flameSegments[i].getY() == y)
				return _flameSegments[i];
		}
		return null;
	}

	@Override
	public void update() {}
	
	@Override
	public void render(Screen screen) {
		for (int i = 0; i < _flameSegments.length; i++) {

			_flameSegments[i].render(screen);
		}
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý va chạm với Bomber, Enemy. Chú ý đối tượng này có vị trí chính là vị trí của Bomb đã nổ
		return true;
	}
}
