package uet.oop.bomberman.entities.character;

import javazoom.jl.player.Player;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.enemy.Balloon;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteSheet;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.level.FileLevelLoader;
import uet.oop.bomberman.level.LevelLoader;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Bomber extends Character {
    private List<Bomb> _bombs;
    protected Keyboard _input;
    public static boolean _movin2 =true;

    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong mỗi lần update()
     */
    protected int _timeBetweenPutBombs = 0;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        _bombs = _board.getBombs();
        _input = _board.getInput();
        _sprite = Sprite.player_right;
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();

            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện tại của Bomber
     */
    private void detectPlaceBomb() {
        if(_input.space && _timeBetweenPutBombs <0 && Game.getBombRate()>0) {
            Thread music = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        Player music1= new Player(new FileInputStream("res\\audio\\bombPutted (online-audio-converter.com).mp3"));
                        music1.play();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            });
            music.start();
                placeBomb(Coordinates.pixelToTile(_x+ _sprite.getSize() / 2),Coordinates.pixelToTile((_y+ _sprite.getSize() / 2) - _sprite.getSize()));

                Game.addBombRate(-1);
                _timeBetweenPutBombs=30;
        }


    }

    protected void placeBomb(int x, int y) {

        Bomb a = new Bomb(x, y, _board);
        _board.addBomb(a);

    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }

    }

    @Override
    public void kill() {
        if (!_alive) return;
        _alive = false;
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) --_timeAfter;
        else {
            _board.endGame();
        }
    }

    @Override
    protected void calculateMove() {
        int _x1=0, _y1=0;
        if(_input.up) { _direction =5;_y1--; }
        else if(_input.down) { _direction =7;_y1++;}
        else if(_input.right) { _direction =6;_x1++;}
        else if (_input.left){_direction =8;_x1--;}
        else if(_input.space) { _direction=9;}
        move(_x1*Game.getBomberSpeed(), _y1* Game.getBomberSpeed());



    }

    @Override
    public boolean canMove(double x, double y) {
        for (int c = 0; c < 4; c++) {
            double _x2 = ((_x + x) + c % 2 * 11);
            double _y2 = ((_y + y) + c / 2 * 12-13);
            Entity val = _board.getEntity(Coordinates.pixelToTile(_x2), Coordinates.pixelToTile(_y2), this);
            if (!collide(val)) {
                return false;
            }
        }
        return  true;
    }

    @Override
    public void move(double xa, double ya) {
        if(canMove(0, ya)) {
            _y += ya;
        }

        if(canMove(xa, 0)) {
            _x += xa;
        }

    }


    @Override
    public boolean collide(Entity e) {
      if(e instanceof Enemy || e instanceof FlameSegment || e.getSprite()== Sprite.bomb_exploded1) { kill(); return false;}
      if(e.getSprite()== Sprite.grass || e instanceof Bomb ) { return true;}
      if(e instanceof LayeredEntity) {  return e.collide(this);}
        return false;
    }

    private void chooseSprite() {
            switch (this._direction) {
                case 5:
                    _sprite = Sprite.player_up;
                    if (_movin2) {
                        _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 30);
                    }
                    break;
                case 6:
                    _sprite = Sprite.player_right;
                    if (_movin2) {
                        _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 30);
                    }
                    break;
                case 7:
                    _sprite = Sprite.player_down;
                    if (_movin2 ) {
                        _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 30);
                    }
                    break;
                case 8:

                    _sprite = Sprite.player_left;
                    if (_movin2) {
                        _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 30);
                    }
                    break;

            }
        }
    }

