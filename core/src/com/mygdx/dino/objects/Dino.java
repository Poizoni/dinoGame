package com.mygdx.dino.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.dino.ContactType;
import com.mygdx.dino.DinoGame;
import com.mygdx.dino.screens.GameScreen;

public class Dino {
    private static final int GRAVITY = -20;
    private static final int MOVEMENT = 600;

    private TextureRegion runningDinoRegion;
    private TextureRegion dinoRegion;
    private DinoAnimation dinoAnimation;
    private Vector2 position;
    private Vector2 velocity;
    private Sound jumpSound;
    private Body body;

    private boolean animated;

    public boolean colliding;

    public Dino(int x, int y, GameScreen gameScreen) {
        dinoRegion = new TextureRegion(DinoGame.spriteSheet, 1677, 0, 89, 96);
        runningDinoRegion = new TextureRegion(DinoGame.spriteSheet, 1677, 0, 264, 96);
        dinoAnimation = new DinoAnimation(runningDinoRegion, 3, 0.1f);
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jumpSound.ogg"));
        velocity = new Vector2(0, 0);
        position = new Vector2(x, y);
        colliding = false;
        body = BodyHelper.createBody(   position.x + dinoRegion.getRegionWidth() / 2,
                                        position.y + dinoRegion.getRegionHeight() / 2,
                                        dinoRegion.getRegionWidth() / 2,
                                        (dinoRegion.getRegionHeight() / 2) - 5,
                                        0, gameScreen.getWorld(), ContactType.PLAYER);
        animated = true;
    }

    public void update(float delta) {
        dinoAnimation.update(delta);
        if(position.y == 0)
            animated = true;
        if(colliding) {
            animated = false;
            velocity.y = 0;
            dinoRegion = new TextureRegion(DinoGame.spriteSheet, 2029, 0, 89, 96);
        }
        if(position.y > 0)
            animated = false;
            velocity.add(0, GRAVITY);
        velocity.scl(delta);
        if(!colliding)
            position.add(MOVEMENT * delta, velocity.y);
        if(position.y < 0)
            position.y = 0;
        velocity.scl(1/delta);

        body.setTransform(  position.x + dinoRegion.getRegionWidth() / 2,
                            position.y + dinoRegion.getRegionHeight() / 2,
                            0);
    }
    public void jump() {
        if(position.y == 0)
            velocity.y = 600;
        jumpSound.play();
    }

    public TextureRegion getDinoTexture() {
        if(animated)
            return dinoAnimation.getFrame();
        else
            return dinoRegion;
    }
    public Vector2 getPosition() {
        return position;
    }
    public void dispose() {
        jumpSound.dispose();
    }
}
