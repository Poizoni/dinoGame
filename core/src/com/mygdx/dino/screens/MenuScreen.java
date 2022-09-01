package com.mygdx.dino.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.dino.DinoGame;
import com.badlogic.gdx.*;

public class MenuScreen extends ScreenAdapter {
    DinoGame dinoGame;

    private TextureRegion startDino;

    public MenuScreen(DinoGame dinoGame) {
        this.dinoGame = dinoGame;

        startDino = new TextureRegion(DinoGame.spriteSheet, 75, 5, 90, 90);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if(keyCode == Input.Keys.SPACE) {
                    dinoGame.setScreen(new GameScreen(dinoGame));
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        DinoGame.batch.begin();
        DinoGame.batch.draw(startDino, 0, 0);
        DinoGame.batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
