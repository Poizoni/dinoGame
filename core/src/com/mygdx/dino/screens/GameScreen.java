package com.mygdx.dino.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dino.DinoGame;
import com.mygdx.dino.GameContactListener;
import com.mygdx.dino.objects.Cactus;
import com.mygdx.dino.objects.Dino;

public class GameScreen extends ScreenAdapter {
    DinoGame dinoGame;

    private static final int FLOOR_Y_OFFSET = 5;
    private static final int CACTUS_SPACING = 800;
    private static final int CACTUS_COUNT = 2;

    private GameContactListener contactListener;
    private Box2DDebugRenderer debugRenderer;
    private Vector2 floorPos1, floorPos2;
    private OrthographicCamera camera;
    private World world;

    private TextureRegion gameoverRegion;
    private TextureRegion floor;
    private Array<Cactus> cacti;
    private Cactus cactus;
    public Dino dino;

    public boolean gameover;

    GameScreen(DinoGame dinoGame) {
        this.dinoGame = dinoGame;

        world = new World(new Vector2(0, 0), false);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, DinoGame.WIDTH, DinoGame.HEIGHT);
        contactListener = new GameContactListener(this);
        world.setContactListener(contactListener);
        debugRenderer = new Box2DDebugRenderer();

        gameoverRegion = new TextureRegion(DinoGame.spriteSheet, 1293, 28, 382, 22);
        floor = new TextureRegion(DinoGame.spriteSheet, 0, 100, 2402, 28);
        cactus = new Cactus(-100, this);
        dino = new Dino(0, 0, this);

        cacti = new Array<Cactus>();
        for(int i = 1; i <= CACTUS_COUNT ; i++) {
            cacti.add(new Cactus(i * (CACTUS_SPACING + cactus.getCactusWidth()), this));
        }

        floorPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, FLOOR_Y_OFFSET);
        floorPos2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + floor.getRegionWidth(), FLOOR_Y_OFFSET);

        gameover = false;
    }

    protected void handleInput() {
        if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            dino.jump();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    public void update(float delta) {
        if(gameover) {
            dinoGame.setScreen(new EndScreen(dinoGame));
        }
        world.step(1 / 60f, 6, 2);

        handleInput();
        updateFloor();

        dino.update(delta);
        camera.position.x = dino.getPosition().x + 600;

        for(int i = 0 ; i < cacti.size ; i++) {
            Cactus cactus = cacti.get(i);
            if(camera.position.x - (camera.viewportWidth / 2) > cactus.getCactusPos().x + cactus.getCactus().getRegionWidth())
                cactus.reposition(cactus.getCactusPos().x + ((cactus.getCactusWidth() + CACTUS_SPACING) * CACTUS_COUNT), this);
        }

        DinoGame.batch.setProjectionMatrix(camera.combined);
        camera.update();
    }

    @Override
    public void render(float delta) {

        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        DinoGame.batch.begin();

        DinoGame.batch.draw(dino.getDinoTexture(), dino.getPosition().x, dino.getPosition().y);
        for(Cactus cactus : cacti) {
            DinoGame.batch.draw(cactus.getCactus(), cactus.getCactusPos().x, cactus.getCactusPos().y);
        }
        DinoGame.batch.draw(floor, floorPos1.x, floorPos1.y);
        DinoGame.batch.draw(floor, floorPos2.x, floorPos2.y);
        if(gameover)
            DinoGame.batch.draw(gameoverRegion, camera.position.x - (gameoverRegion.getRegionWidth() / 2) , camera.position.y);

        DinoGame.batch.end();

        // debugRenderer.render(world, camera.combined);
    }
    public void updateFloor() {
        if(camera.position.x - (camera.viewportWidth / 2) > floorPos1.x + floor.getRegionWidth())
            floorPos1.add(floor.getRegionWidth() * 2, 0);
        if(camera.position.x - (camera.viewportWidth / 2) > floorPos2.x + floor.getRegionWidth())
            floorPos2.add(floor.getRegionWidth() * 2, 0);
    }
    @Override
    public void hide() {

    }
    public World getWorld() {
        return world;
    }
}
