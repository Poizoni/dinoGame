package com.mygdx.dino;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.dino.screens.GameScreen;

public class GameContactListener implements ContactListener {

    private GameScreen gameScreen;

    public GameContactListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if(a == null || b == null) return;
        if(a.getUserData() == null || b.getUserData() == null) return;
        if(a.getUserData() == ContactType.PLAYER || b.getUserData() == ContactType.PLAYER) {
            if(a.getUserData() == ContactType.CACTUS || b.getUserData() == ContactType.CACTUS) {
                gameScreen.dino.colliding = true;
                gameScreen.gameover = true;
            }
        }
    }
    @Override
    public void endContact(Contact contact) {

    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
