package com.mygdx.dino.screens;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dino.objects.MakeAnimation;
import com.mygdx.dino.GameContactListener;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.dino.objects.Cactus;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dino.objects.Dino;
import com.mygdx.dino.DinoGame;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import java.util.Random;

public class GameScreen extends ScreenAdapter {
    DinoGame dinoGame;

    private static final int FLOOR_Y_OFFSET = 5;
    private static final int CACTUS_SPACING = 800;
    private static final int CACTUS_COUNT = 2;

    private GameContactListener contactListener;
    private Box2DDebugRenderer debugRenderer;
    private Vector2 floorPos1, floorPos2;
    private Vector2 cloudPos;
    private OrthographicCamera camera;
    private Random random;
    private World world;

    private TextureRegion restartAnimationRegion;
    private MakeAnimation restartAnimation;
    private TextureRegion gameoverRegion;
    private TextureRegion restartButton;
    private TextureRegion[] scoreRegion;
    private String finalScoreFormatted;
    private TextureRegion restart;
    private TextureRegion floor;
    private TextureRegion cloud;
    private Array<Cactus> cacti;
    private Cactus cactus;
    public Dino dino;

    private int timerIterations;
    private int animationCount;
    private float timeSeconds;
    private int restartCount;
    private int finalScore;
    private float period;
    private int score;

    private boolean startedScore = false;
    public boolean gameover;

    GameScreen(DinoGame dinoGame) {
        this.dinoGame = dinoGame;

        world = new World(new Vector2(0, 0), false);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, DinoGame.WIDTH, DinoGame.HEIGHT);
        contactListener = new GameContactListener(this);
        world.setContactListener(contactListener);
        debugRenderer = new Box2DDebugRenderer();
        random = new Random();

        restartAnimationRegion = new TextureRegion(DinoGame.spriteSheet, 0, 129, 578, 65);
        gameoverRegion = new TextureRegion(DinoGame.spriteSheet, 1293, 28, 382, 22);
        restartButton = new TextureRegion(DinoGame.spriteSheet, 505, 129, 73, 65);
        scoreRegion = new TextureRegion[10];
            scoreRegion[0] = new TextureRegion(DinoGame.spriteSheet, 1292, 0, 20, 23);
            scoreRegion[1] = new TextureRegion(DinoGame.spriteSheet, 1312, 0, 20, 23);
            scoreRegion[2] = new TextureRegion(DinoGame.spriteSheet, 1332, 0, 20, 23);
            scoreRegion[3] = new TextureRegion(DinoGame.spriteSheet, 1352, 0, 20, 23);
            scoreRegion[4] = new TextureRegion(DinoGame.spriteSheet, 1372, 0, 20, 23);
            scoreRegion[5] = new TextureRegion(DinoGame.spriteSheet, 1392, 0, 20, 23);
            scoreRegion[6] = new TextureRegion(DinoGame.spriteSheet, 1412, 0, 20, 23);
            scoreRegion[7] = new TextureRegion(DinoGame.spriteSheet, 1432, 0, 20, 23);
            scoreRegion[8] = new TextureRegion(DinoGame.spriteSheet, 1452, 0, 20, 23);
            scoreRegion[9] = new TextureRegion(DinoGame.spriteSheet, 1472, 0, 20, 23);
        restartAnimation = new MakeAnimation(restartAnimationRegion, 8, 2f);
        restart = new TextureRegion(DinoGame.spriteSheet, 9, 133, 56, 56);
        floor = new TextureRegion(DinoGame.spriteSheet, 0, 100, 2402, 28);
        cloud = new TextureRegion(DinoGame.spriteSheet, 165, 0, 95, 30);
        cactus = new Cactus(-100, this);
        dino = new Dino(0, 0, this);

        cacti = new Array<Cactus>();
        for(int i = 1; i <= CACTUS_COUNT ; i++) {
            cacti.add(new Cactus(i * (CACTUS_SPACING + cactus.getCactusWidth()), this));
        }

        floorPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, FLOOR_Y_OFFSET);
        floorPos2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + floor.getRegionWidth(), FLOOR_Y_OFFSET);
        cloudPos = new Vector2((camera.position.x - camera.viewportWidth / 2) + cloud.getRegionWidth(), 100);

        timerIterations = 0;
        animationCount = 0;
        restartCount = 0;
        timeSeconds = 0f;
        finalScore = 0;
        period = 1.2f;
        score = 0;

        gameover = false;
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        handleInput();
        updateFloor();
        updateCloud();

        if(gameover) {
            if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                dispose();
                dinoGame.setScreen(new GameScreen(dinoGame));
            }
        }

        if(dino.getPosition().x == 0 && timerIterations == 0) {
            timeSeconds += Gdx.graphics.getDeltaTime();
            if(timeSeconds > period && timerIterations == 0) {
                timeSeconds -= period;
                timerIterations++;
                startScore();
            }
        }

        dino.update(delta);
        restartAnimation.update(delta);
        camera.position.x = dino.getPosition().x + 600;

        for(int i = 0 ; i < cacti.size ; i++) {
            Cactus cactus = cacti.get(i);
            if(camera.position.x - (camera.viewportWidth / 2) > cactus.getCactusPos().x + cactus.getCactus().getRegionWidth())
                cactus.reposition(cactus.getCactusPos().x + ((cactus.getCactusWidth() + CACTUS_SPACING) * CACTUS_COUNT), this);
        }

        DinoGame.batch.setProjectionMatrix(camera.combined);
        camera.update();
    }
    public void updateFloor() {
        if(camera.position.x - (camera.viewportWidth / 2) > floorPos1.x + floor.getRegionWidth())
            floorPos1.add(floor.getRegionWidth() * 2, 0);
        if(camera.position.x - (camera.viewportWidth / 2) > floorPos2.x + floor.getRegionWidth())
            floorPos2.add(floor.getRegionWidth() * 2, 0);
    }
    public void updateCloud() {
        if(camera.position.x - (camera.viewportWidth / 2) > cloudPos.x + cloud.getRegionWidth())
            cloudPos.add(camera.viewportWidth * (randomCloud()*1.4f), cloudPos.y * randomCloud());
        if(cloudPos.y > camera.viewportHeight) {
            cloudPos.y = camera.viewportHeight / 2;
        }
    }

    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        DinoGame.batch.begin();
        DinoGame.batch.draw(cloud, cloudPos.x, cloudPos.y);
        DinoGame.batch.draw(dino.getDinoTexture(), dino.getPosition().x, dino.getPosition().y);
        for(Cactus cactus : cacti) {
            DinoGame.batch.draw(cactus.getCactus(), cactus.getCactusPos().x, cactus.getCactusPos().y);
        }
        DinoGame.batch.draw(floor, floorPos1.x, floorPos1.y);
        DinoGame.batch.draw(floor, floorPos2.x, floorPos2.y);
        if(gameover) {
            DinoGame.batch.draw(gameoverRegion, camera.position.x - (gameoverRegion.getRegionWidth() / 2), camera.position.y + 20);
            if(restartAnimation.frameEnd()) {
                restartCount++;
            }
            if(restartCount == 0) {
                if(animationCount == 0)
                    restartAnimation.restart();
                animationCount++;
                DinoGame.batch.draw(restartAnimation.getFrame(), camera.position.x - (restart.getRegionWidth() / 2), camera.position.y / 2);
            }
            if(restartCount > 0)
                DinoGame.batch.draw(restartButton, camera.position.x - (restart.getRegionWidth() / 2) + 1, camera.position.y / 2);
        }
        drawNumbers();
        DinoGame.batch.end();

        // debugRenderer.render(world, camera.combined);
    }

    protected void handleInput() {
        if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            dino.jump();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
    public float randomCloud() {
        return random.nextFloat()+0.5f;
    }

    private void drawNumbers() {
        if(!gameover) {
            if(startedScore)
                score++;
            String formatScore = String.format("%07d", score);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(formatScore.charAt(0))], camera.position.x + 400, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(formatScore.charAt(1))], camera.position.x + 420, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(formatScore.charAt(2))], camera.position.x + 440, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(formatScore.charAt(3))], camera.position.x + 460, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(formatScore.charAt(4))], camera.position.x + 480, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(formatScore.charAt(5))], camera.position.x + 500, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(formatScore.charAt(6))], camera.position.x + 520, camera.position.y + 100);
        }
        if(gameover) {
            finalScore = score;
            finalScoreFormatted = String.format("%07d", finalScore);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(finalScoreFormatted.charAt(0))], camera.position.x + 400, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(finalScoreFormatted.charAt(1))], camera.position.x + 420, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(finalScoreFormatted.charAt(2))], camera.position.x + 440, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(finalScoreFormatted.charAt(3))], camera.position.x + 460, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(finalScoreFormatted.charAt(4))], camera.position.x + 480, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(finalScoreFormatted.charAt(5))], camera.position.x + 500, camera.position.y + 100);
                DinoGame.batch.draw(scoreRegion[Character.getNumericValue(finalScoreFormatted.charAt(6))], camera.position.x + 520, camera.position.y + 100);
        }
    }
    public void startScore() {
        startedScore = true;
    }

    public World getWorld() {
        return world;
    }

    public void dispose() {
        world.dispose();
        dino.dispose();
    }
}
