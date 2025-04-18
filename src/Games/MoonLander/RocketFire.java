package Games.MoonLander;

import com.javarush.engine.cell.*;

import java.util.List;

public class RocketFire extends GameObject {
    private final List<int[][]> FRAMES;
    private int frameIndex;
    private boolean isVisible;

    public RocketFire(List<int[][]> frameList) {
        super(0, 0, frameList.get(0));
        FRAMES = frameList;
        frameIndex = 0;
        isVisible = false;
    }

    private void nextFrame() {
        frameIndex++;

        if (frameIndex >= FRAMES.size()) frameIndex = 0;
        matrix = FRAMES.get(frameIndex);
    }

    public void draw(Game game) {
        if (!isVisible) return;

        nextFrame();
        super.draw(game);
    }

    public void show() {
        isVisible = true;
    }

    public void hide() {
        isVisible = false;
    }
}
