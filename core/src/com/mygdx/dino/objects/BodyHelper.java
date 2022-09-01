package com.mygdx.dino.objects;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.dino.ContactType;

public class BodyHelper {
    public static Body createBody(float x, float y, float width, float height, float density, World world, ContactType type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        body.createFixture(fixtureDef).setUserData(type);

        shape.dispose();

        return body;
    }
}
