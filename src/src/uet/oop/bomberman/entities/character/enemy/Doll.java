package uet.oop.bomberman.entities.character.enemy;

import javazoom.jl.player.Player;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

import java.io.FileInputStream;
import java.util.Random;

public class Doll extends Enemy {
    protected Flame[] _flames;
    boolean _exploded = false;
    Random t= new Random();
    int _timeToExplode =120+t.nextInt(500) ;
        int _timeAfter1=20;
    public Doll(int x, int y, Board board) {
        super(x, y, board, Sprite.doll_dead, Game.getBomberSpeed() / 2, 100);
        _sprite = Sprite.doll_left1;
    }

    @Override
    public void calculateMove() {
        Random d = new Random();
        int xa = 0, ya = 0;
        if (_steps <= 0) {
            _direction = d.nextInt(2);
            _steps = MAX_STEPS;
        }
        if (_direction == 0) xa-=1;
        if (_direction == 1) xa+=1;
        if (canMove(xa, ya)) {
            _steps -= 1;
            move(xa, ya);
            _moving = true;
        } else {
            _steps = 0;
            _moving = false;
        }
    }

    @Override
    protected void chooseSprite() {
        switch (this._direction) {
            case 0:
                _sprite = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, _animate, 120);
                break;
            case 1:
                _sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, _animate, 120);

                break;

        }
    }
@Override
    public void update() {
    animate();
        if(_timeToExplode > 0) {
            calculateMove();
            _timeToExplode--;
        }
    else {
            if (!_exploded) {
                explode();
            } else {
                updateFlames();
                if (_timeAfter1 > 0)
                    _timeAfter1--;
                else {
                    remove();
                }
            }
        }
    if(!_alive) {
        afterKill();
        return;
    }

    if(_alive)
        calculateMove();


    }
    @Override
    public void render(Screen screen) {
        if(_exploded) {
            _alive=false;
            renderFlames(screen);
            _sprite = Sprite.bomb_exploded1;

            }
        if(_alive) { chooseSprite();}
        else if(_timeAfter > 0 ) {
                _sprite = _deadSprite;
                _animate = 0;
            }
            else {
                _sprite = Sprite.movingSprite(Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, _animate, 60);
            }

        int xt = Coordinates.pixelToTile(_x)<< 4;
       int yt= Coordinates.pixelToTile(_y) << 4;
        screen.renderEntity(xt, yt - _sprite.SIZE, this);
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
  //      Entity val = _board.getEntityAt(this._x, this._y);

        // TODO: tạo các Flame
        _flames = new Flame[4];
        _flames[0] = new Flame(Coordinates.pixelToTile( this._x),Coordinates.pixelToTile(  this._y)-1, 0, 1, _board);
        _flames[1] = new Flame(Coordinates.pixelToTile( this._x),  Coordinates.pixelToTile( this._y)-1, 1, 1, _board);
        _flames[2] = new Flame(Coordinates.pixelToTile( this._x),  Coordinates.pixelToTile( this._y)-1, 2, 1, _board);
        _flames[3] = new Flame(Coordinates.pixelToTile( this._x),  Coordinates.pixelToTile( this._y)-1, 3, 1, _board);
    }

}
