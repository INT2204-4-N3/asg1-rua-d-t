package uet.oop.bomberman.entities.bomb;

import javazoom.jl.player.Player;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.level.Coordinates;

import java.io.FileInputStream;
import java.io.IOException;


public class Bomb extends AnimatedEntitiy {

	protected double _timeToExplode = 120; //2 seconds
	public int _timeAfter = 20;
	
	protected Board _board;
	protected Flame[] _flames;
	protected boolean _exploded=false;
	protected boolean _allowedToPassThru = true;
	protected Keyboard _put;
	public Bomb(int x, int y, Board board) {
		_x = x;
		_y = y;
		_board = board;
		_sprite = Sprite.bomb;
		_put = _board.getInput();

	}
	
	@Override
	public void update() {
		if(_timeToExplode > 0) {
			_timeToExplode--;
		}
		else {
			if(!_exploded) {
				explode();
				Entity e= _board.getEntityAt(0,0);
				collide(e); // bom no lien hoan
			}
			else
				updateFlames();
			
			if(_timeAfter > 0) 
				_timeAfter--;
			else
				remove();
		}
			
		animate();
	}
	
	@Override
	public void render(Screen screen) {
		if(_exploded) {
			_sprite =  Sprite.bomb_exploded1;
			renderFlames(screen);
		} else
			_sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, _animate, 120);
		
		int xt = (int)_x << 4;
		int yt = (int)_y << 4;
		
		screen.renderEntity(xt, yt , this);
	}
	
	public void renderFlames(Screen screen) {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].render(screen);
		}
	}
	
	public void updateFlames() {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].update();
		}
	}

    /**
     * Xử lý Bomb nổ
     */
	public void explode() {
		Thread music = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Player music1= new Player(new FileInputStream("res\\audio\\explosion (online-audio-converter.com).mp3"));
							music1.play();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			});
			music.start();
			_exploded = true;
			// TODO: xử lý khi Character đứng tại vị trí Bomb
			Entity val = _board.getEntityAt(this._x, this._y);

			// TODO: tạo các Flame
			_flames = new Flame[4];
			_flames[0] = new Flame((int) this._x, (int) this._y, 0, Game.getBombRadius(), _board);
			_flames[1] = new Flame((int) this._x, (int) this._y, 1, Game.getBombRadius(), _board);
			_flames[2] = new Flame((int) this._x, (int) this._y, 2, Game.getBombRadius(), _board);
			_flames[3] = new Flame((int) this._x, (int) this._y, 3, Game.getBombRadius(), _board);
	}
	
	public FlameSegment flameAt(int x, int y) {
		if(!_exploded) return null;
		
		for (int i = 0; i < _flames.length; i++) {
			if(_flames[i] == null) return null;
			FlameSegment e = _flames[i].flameSegmentAt(x, y);
			if(e != null) return e;
		}
		
		return null;
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý khi Bomber đi ra sau khi vừa đặt bom (_allowedToPassThru)
		// TODO: xử lý va chạm với Flame của Bomb khác

		Bomb b=null;
		for(int j=0;j<4;j++) {
			for (int i = 0; i < _flames[j].calculatePermitedDistance(); i++) {
				if(j==0) { b= _board.getBombAt(_x , _y-i-1);} // duoi
				if(j==1) { b = _board.getBombAt(_x + i+1, _y);} // phai
				if(j==2) { b = _board.getBombAt(_x , _y+i+1);}
				if(j==3) { b = _board.getBombAt(_x - i-1, _y);}


				if (b != null) {
					if (this._exploded) {
						b.explode();
					}
				}
			}
		}
			return false;
		}
}
