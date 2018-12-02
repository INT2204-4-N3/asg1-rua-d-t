package uet.oop.bomberman.entities.character.enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.ai.AI;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

import java.awt.*;

public class Kondoria extends Enemy {

    public Kondoria(int x, int y, Board board) {
        super(x, y, board, Sprite.kondoria_dead, Game.getBomberSpeed() / 2, 100);

        _sprite = Sprite.kondoria_left1;

        _ai = new AILow();
    }
    @Override
    public void move(double xa, double ya) {
        if(!_alive) return;
        _y += ya;
        _x += xa;
    }

    @Override
    public boolean canMove(double x, double y) {
        double _x2 =0,_y2=0;
        for (int c = 0; c < 4; c++) {
            _x2=((_x + x) + c % 2 * 11);
            _y2 = ((_y + y) + c / 2 * 12-13);
            Entity e= _board.getEntity(Coordinates.pixelToTile(_x2),Coordinates.pixelToTile(_y2),this);
            collide(e);
            if (Coordinates.pixelToTile(_x2)<1 || Coordinates.pixelToTile(_y2)<1 || Coordinates.pixelToTile(_x2)>29 || Coordinates.pixelToTile(_y2)>11 ) {

                return false;
            }
        }
        return  true;
    }
    @Override
    protected void chooseSprite() {
        switch (this._direction) {
            case 0:
                _sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, _animate, 120);
                break;
            case 1:
                _sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, _animate, 120);

                break;
            case 2:
                _sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, _animate, 120);

                break;
            case 3:
                _sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, _animate, 120);
                break;


        }

    }

    @Override
    public boolean collide(Entity e) {
        if(e instanceof FlameSegment) {
           kill();
            return false;
        }
        return true;
    }
}
