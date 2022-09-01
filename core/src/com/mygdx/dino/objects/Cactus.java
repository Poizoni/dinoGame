package com.mygdx.dino.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dino.ContactType;
import com.mygdx.dino.DinoGame;
import com.mygdx.dino.screens.GameScreen;

import java.util.Random;

public class Cactus {

    private TextureRegion cactus;
    private Vector2 cactusPos;
    private Body body;

    private Array<Integer> coords;
    private int cactusWidth;
    private Random random;

    public Cactus(float x, GameScreen gameScreen) {
        cactus = new TextureRegion(DinoGame.spriteSheet, 445, 0, 35, 72);
        random = new Random();

        cactusPos = new Vector2(x, 0);
        coords = new Array<Integer>();

        // set default sprite sheet coords as small cactus
        coords.add(445);
        coords.add(0);
        coords.add(35);
        coords.add(72);

        cactusWidth = 35;

        body = BodyHelper.createBody(   (cactusPos.x + cactus.getRegionWidth() / 2) + 10,
                                            cactusPos.y,
                                        cactus.getRegionWidth() / 2, cactus.getRegionHeight(),
                                        10000, gameScreen.getWorld(), ContactType.CACTUS);
    }

    public void reposition(float x, GameScreen gameScreen) {
        getRandomSize();

        cactusWidth = coords.get(2);
        cactus = new TextureRegion(DinoGame.spriteSheet, coords.get(0), coords.get(1), coords.get(2), coords.get(3));
        cactusPos.set(x, 0);

        body = BodyHelper.createBody(   (cactusPos.x + cactus.getRegionWidth() / 2) + 10,
                                            cactusPos.y,
                                        cactus.getRegionWidth() / 2, cactus.getRegionHeight(),
                                        10000, gameScreen.getWorld(), ContactType.CACTUS);
    }

    public void getRandomSize() {
        int result = random.nextInt(10);
        if(result == 0 || result == 1 || result == 2) {
            // single small cactus
            coords.set(0, 445);
            coords.set(2, 35);
            coords.set(3, 72);
        }
        if(result == 3 || result == 4 || result == 5) {
            // double small cactus
            coords.set(0, 479);
            coords.set(2, 69);
            coords.set(3, 72);
        }
        if(result == 6) {
            // triple small cactus
            coords.set(0, 547);
            coords.set(2, 103);
            coords.set(3, 72);
        }
        if(result == 7) {
            // single big cactus
            coords.set(0, 651);
            coords.set(2, 51);
            coords.set(3, 102);
        }
        if(result == 8) {
            // double big cactus
            coords.set(0, 701);
            coords.set(2, 101);
            coords.set(3, 102);
        }
        if(result == 9) {
            // triple big cactus
            coords.set(0, 801);
            coords.set(2, 151);
            coords.set(3, 102);
        }
    }

    public int getCactusWidth() {
        return cactusWidth;
    }
    public TextureRegion getCactus() {
        return cactus;
    }
    public Vector2 getCactusPos() {
        return cactusPos;
    }
}
