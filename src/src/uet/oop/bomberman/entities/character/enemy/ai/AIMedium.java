package uet.oop.bomberman.entities.character.enemy.ai;

import com.sun.xml.internal.ws.policy.jaxws.SafePolicyReader;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Random;

public class AIMedium extends AI {
	Bomber _bomber;
	Enemy _e;
	Board _board;
	Entity e=null;
	
	public AIMedium(Bomber bomber, Enemy e, Board board) {
		_bomber = bomber;
		_e = e;
		_board=board;
	}
	@Override
	public int calculateDirection() {

		// TODO: cài đặt thuật toán tìm đường đi
		if(_bomber== null) {
			return random.nextInt(4);
		}
		int _maindir= random.nextInt(2);
		if(_maindir==1) {
			int _di= row();
			if(_di!= -1) {
				return _di;
			}
			else {
				return col();
			}
		}
		else {
			int _dir= col();
			if(_dir!=1) {
				return _dir;
			}
			else {
				return row();
			}

		}
	}
	public int row() {

		if(_bomber.getXTile()> _e.getXTile()) { // bomber been phair enemy thif enemy dc ve phai
			 e= _board.getEntityAt(_e.getXTile()+1, _e.getYTile());
			if(e instanceof LayeredEntity) { return choose();}
			return 1;
		}
		if(_bomber.getXTile()< _e.getXTile()) {
			 e= _board.getEntityAt(_e.getXTile()-1, _e.getYTile());
			if(e instanceof LayeredEntity) { return choose();}// bomber ben trai
			return 3;
		}
		return -1;

	}
	public int col() {
		if(_bomber.getYTile()> _e.getYTile()) { // bomber ben duoi
			 e= _board.getEntityAt(_e.getXTile(), _e.getYTile()+1);
			if(e instanceof LayeredEntity) { return choose();}
			return 2;
		}
		if(_bomber.getYTile()< _e.getYTile()) { // bomb ben tren
			e= _board.getEntityAt(_e.getXTile(), _e.getYTile()-1);
			if(e instanceof LayeredEntity) {return choose();}
			return 0;
		}
		return -1;

	}
	public int choose() {
		Entity e1= _board.getEntityAt(_e.getXTile()+1, _e.getYTile());
		Entity e2= _board.getEntityAt(_e.getXTile(), _e.getYTile()-1);
		Entity e3= _board.getEntityAt(_e.getXTile(), _e.getYTile()+1);
		Entity e4= _board.getEntityAt(_e.getXTile()-1, _e.getYTile());
		if(!(e1 instanceof LayeredEntity)) {return 1;}
		else if(!(e2 instanceof LayeredEntity)) {return 0;}
		else if(!(e3 instanceof LayeredEntity)) {return 2;}
		else {return 3;}

	}


}
