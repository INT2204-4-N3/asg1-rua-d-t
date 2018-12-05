package uet.oop.bomberman.level;

import sun.security.provider.ConfigFile;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Balloon;
import uet.oop.bomberman.entities.character.enemy.Doll;
import uet.oop.bomberman.entities.character.enemy.Kondoria;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.HeartItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class FileLevelLoader extends LevelLoader {

	/**
	 * Ma trận chứa thông tin bản đồ, mỗi phần tử lưu giá trị kí tự đọc được
	 * từ ma trận bản đồ trong tệp cấu hình
	 */
	public static char[][] _map ;
	private InputStream s;
	private int i;
	private int count=0, count1=0, count2=0, a1=0,num=0;
	private Class c = null;
	public FileLevelLoader(Board board, int level) throws LoadLevelException {
		super(board, level);
	}

	@Override
	public void loadLevel(int level) {
		// TODO: đọc dữ liệu từ tệp cấu hình /levels/Level{level}.txt
		// TODO: cập nhật các giá trị đọc được vào _width, _height, _level, _map
		String namelevel = "/levels/Level"+level+".txt";

		try {
			s = this.getClass().getResourceAsStream(namelevel);
			while ((i = s.read()) != -1) {
				if(i==10) { count++; count2=0;
					System.out.println("\n");
				}
				if(count==0){
					if(i==32 && count1==0) { a1=num; num=0; count1++;  }
					else if(i==32 && count1==1) { _height=num; num=0; count1++; }
					else if(i==13 && count1==2) { _width=num;_map = new char[_width][_height]; }
					else {
						num *= 10;
						num += i-48;
					}

				}

				else {
					if(i!=10 && i!=13) {
						_map[count2][count-1] =  (char) i;
						count2++;
					}

				}
			}
			_level=level;
		}
		catch (Exception e) {
			System.out.println("Can't read file map");
		}

	}

	@Override
	public void createEntities() {
		// TODO: tạo các Entity của màn chơi
		// TODO: sau khi tạo xong, gọi _board.addEntity() để thêm Entity vào game

		// TODO: phần code mẫu ở dưới để hướng dẫn cách thêm các loại Entity vào game
		// TODO: hãy xóa nó khi hoàn thành chức năng load màn chơi từ tệp cấu hình
		// thêm Wall
		for (int y = 0; y < _height; y++) {
			for (int x = 0; x < _width; x++) {
				int pos = x + y * _width;
				if (_map[x][y] == 35) {
					_board.addEntity(pos, new LayeredEntity(x, y,
							new Grass(x, y, Sprite.grass),
							new Wall(x, y, Sprite.wall)));
				}
				else if(_map[x][y]==42) {
					//System.out.println(x+" "+y);
					_board.addEntity(pos,
							new LayeredEntity(x, y,
									new Grass(x,y,Sprite.grass), // item dưới brick
									new Brick(x, y, Sprite.brick,_board)
							)
					);

				}
				else if(_map[x][y]==98) { // bom item
					_board.addEntity(pos,
							new LayeredEntity(x, y,
									new Grass(x,y,Sprite.grass),
									new BombItem(x, y, Sprite.powerup_bombs),
									new Brick(x, y, Sprite.brick,_board)
							)
					);
				}
				else if(_map[x][y]==102) {
					_board.addEntity(pos,
							new LayeredEntity(x, y,
									new Grass(x,y,Sprite.grass),
									new FlameItem(x, y, Sprite.powerup_flames),
									new Brick(x, y, Sprite.brick,_board)
							)
					);
				}
				//heart
				else if(_map[x][y]==104) {
					_board.addEntity(pos,
							new LayeredEntity(x, y,
									new Grass(x,y,Sprite.grass),
									new HeartItem(x, y, Sprite.powerup_detonator),
									new Brick(x, y, Sprite.brick,_board)
							)
					);
				}
				else if(_map[x][y]==111) {
					_board.addEntity(pos, new Brick(x, y, Sprite.brick,_board));


				}
				else if(_map[x][y]==115) {
					_board.addEntity(pos,
							new LayeredEntity(x, y,
									new Grass(x,y,Sprite.grass),
									new SpeedItem(x, y, Sprite.powerup_speed),
									new Brick(x, y, Sprite.brick,_board)
							)
					);
				}
				else if(_map[x][y]==120) {
					_board.addEntity(pos,
							new LayeredEntity(x, y,
									new Portal(x,y, Sprite.portal,_board), // item dưới brick
									new Brick(x, y, Sprite.brick,_board)
							)
					);
				}
				else if(_map[x][y]==107) {
					_board.addCharacter(new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
					_board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
				}
				else if(_map[x][y]==100) {
					_board.addCharacter(new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
					_board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
				}
				else if(_map[x][y]==112) {
					_board.addCharacter( new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board) );
					Screen.setOffset(0, 0);
					_board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass)); // chèn thêm glass dưới chân bomber
				}
				else {
					_board.addEntity(pos, new Grass(x, y, Sprite.grass));
				}
				System.out.println("\n");
			}
		}

		// thêm Enemy
		Random rd= new Random();
		Entity e = null;int xE1=0, yE1=0;
		for(int i=0;i< Game.getBallonSize();i++) {
			do {
				xE1 = rd.nextInt(_width);
				yE1 = rd.nextInt(_height);
				e = _board.getEntityAt(xE1, yE1);
			}
			while (e.getSprite() != Sprite.grass);
			_board.addCharacter(new Balloon(Coordinates.tileToPixel(xE1), Coordinates.tileToPixel(yE1) + Game.TILES_SIZE, _board));


			_board.addEntity(xE1 + yE1 * _width, new Grass(xE1, yE1, Sprite.grass));
		}

	}

}
