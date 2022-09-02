package com.mygdx.dino.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.dino.helper.ContactType;
import com.mygdx.dino.screens.GameScreen;
import com.mygdx.dino.helper.BodyHelper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.dino.DinoGame;
import com.badlogic.gdx.Gdx;

public class Dino {
    private static final int GRAVITY = -20;
    private static int movementSpeed = 600;

    private TextureRegion runningDinoRegion;
    private TextureRegion dinoRegion;
    private MakeAnimation dinoAnimation;
    private Vector2 position;
    private Vector2 velocity;
    private Sound jumpSound;
    private Sound deathSound;
    private Body body;

    private int timerIterations;
    private int deathSoundCount;
    private float timeSeconds;
    private boolean animated;
    private float period;

    public boolean colliding;

    public Dino(int x, int y, GameScreen gameScreen) {
        dinoRegion = new TextureRegion(DinoGame.spriteSheet, 1677, 0, 89, 96);
        runningDinoRegion = new TextureRegion(DinoGame.spriteSheet, 1853, 0, 177, 96);
        dinoAnimation = new MakeAnimation(runningDinoRegion, 2, 0.1f);
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jumpSound.ogg"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("deathSound.ogg"));
        velocity = new Vector2(0, 0);
        position = new Vector2(x, y);
        colliding = false;
        body = BodyHelper.createBody(   position.x + dinoRegion.getRegionWidth() / 2,
                                        position.y + dinoRegion.getRegionHeight() / 2,
                                        dinoRegion.getRegionWidth() / 2,
                                        (dinoRegion.getRegionHeight() / 2) - 5,
                                        0, gameScreen.getWorld(), ContactType.PLAYER);
        timerIterations = 0;
        timeSeconds = 0f;
        period = 1.2f;

        animated = true;
    }

    public void update(float delta) {
        dinoAnimation.update(delta);
        if(position.y == 0)
            animated = true;
        if(colliding) {
            animated = false;
            if(deathSoundCount == 0)
                deathSound.play();
            deathSoundCount++;
            resetSpeed();
            stop();
            dinoRegion = new TextureRegion(DinoGame.spriteSheet, 2029, 0, 89, 96);
        }
        if(position.y > 0) {
            animated = false;
            velocity.add(0, GRAVITY);
        }
        velocity.scl(delta);
        if(!colliding)
            position.add(movementSpeed * delta, velocity.y);

        // wait until first jump ends before moving
        if(position.x != 0 && timerIterations == 0) {
            position.x = 0;
            timeSeconds += Gdx.graphics.getDeltaTime();
            if(timeSeconds > period && timerIterations == 0) {
                timeSeconds -= period;
                timerIterations++;
            }
        }
        if(position.y < 0)
            position.y = 0;
        velocity.scl(1/delta);

        body.setTransform(  position.x + dinoRegion.getRegionWidth() / 2,
                            position.y + dinoRegion.getRegionHeight() / 2,
                            0);
    }

    public void jump() {
        if(position.y == 0) {
            velocity.y = 600;
            jumpSound.play();
            incSpeed();
        }
    }
    public void stop() {
        velocity.y = 0;
    }

    public void incSpeed() {
        movementSpeed *= 1.01f;
    }
    public void resetSpeed() {
        movementSpeed = 600;
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
        deathSound.dispose();
    }
}
