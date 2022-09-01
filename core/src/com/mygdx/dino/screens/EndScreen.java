package com.mygdx.dino.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.dino.DinoGame;

public class EndScreen extends ScreenAdapter {
    DinoGame dinoGame;

    private TextureRegion gameover;


    public EndScreen(DinoGame dinoGame) {
        this.dinoGame = dinoGame;

        gameover = new TextureRegion(DinoGame.spriteSheet, 1293, 28, 382, 22);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    dinoGame.setScreen(new GameScreen(dinoGame));
                }
                return true;
            }
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                dinoGame.setScreen(new GameScreen(dinoGame));
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        DinoGame.batch.begin();
        DinoGame.batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
