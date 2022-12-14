package com.mygdx.dino.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class MakeAnimation {
    private Array<TextureRegion> frames;
    private float currentFrameTime;
    private float maxFrameTime;
    private int frameCount;
    private int frame;

    public MakeAnimation(TextureRegion region, int frameCount, float cycleTime) {
        frames = new Array<TextureRegion>();
        int frameWidth = region.getRegionWidth() /  frameCount;
        for(int i = 0 ; i < frameCount ; i++) {
            frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
        }
        this.frameCount = frameCount;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
    }

    public void update(float delta) {
        currentFrameTime += delta;
        if(currentFrameTime > maxFrameTime) {
            frame++;
            currentFrameTime = 0;
        }
        if(frame >= frameCount)
            frame = 0;
    }

    public TextureRegion getFrame() {
        return frames.get(frame);
    }
    public void restart() {
        frame = 0;
    }
    public boolean frameEnd() {
        return frame == frameCount - 1;
    }
}
