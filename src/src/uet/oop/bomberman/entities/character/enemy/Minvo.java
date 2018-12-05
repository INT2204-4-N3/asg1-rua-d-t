package uet.oop.bomberman.entities.character.enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.graphics.Sprite;

public class Minvo extends Enemy {
    public Minvo(int x, int y, Board board) {
        super(x, y, board, Sprite.minvo_dead, Game.getBomberSpeed() / 2, 100);

        _sprite = Sprite.minvo_left1;

        _ai = new AILow();
    }

    @Override
    protected void chooseSprite() {

    }
}
