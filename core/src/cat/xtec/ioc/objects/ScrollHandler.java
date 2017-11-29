package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class ScrollHandler extends Group {

    // Fons de pantalla
    Background bg, bg_back;

    //private ArrayList<Asteroid> asteroids;
    private DelayedRemovalArray<Asteroid> asteroids;

    // Objecte Random
    Random r;

    //TODO EXERCICI 3 a)
    long startTime = TimeUtils.nanoTime();
    float randomInterval;

    public ScrollHandler() {

        // Creem els dos fons
        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);

        // Afegim els fons al grup
        addActor(bg);
        addActor(bg_back);

        // Creem l'objecte random
        r = new Random();

        //TODO EXERCICI 3 a)
        asteroids = new DelayedRemovalArray<Asteroid>();

        this.randomInterval = MathUtils.random(Settings.MIN_ASTEROID_INTERVAL, Settings.MAX_ASTEROID_INTERVAL);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Si algun element està fora de la pantalla, fem un reset de l'element.
        if (bg.isLeftOfScreen()) {
            bg.reset(bg_back.getTailX());

        } else if (bg_back.isLeftOfScreen()) {
            bg_back.reset(bg.getTailX());
        }

        // TODO EXERICIC 3 a)
        float elapsedTime = MathUtils.nanoToSec * (TimeUtils.nanoTime() - startTime);

        for (Asteroid asteroid : asteroids) {
            if (asteroid.isLeftOfScreen()) {
                removeActor(asteroid);
                asteroids.removeValue(asteroid, true);
            }
        }

        // TODO EXERICIC 3 a)
        if (elapsedTime >= randomInterval) {
            this.randomInterval = MathUtils.random(Settings.MIN_ASTEROID_INTERVAL, Settings.MAX_ASTEROID_INTERVAL);
            if (asteroids.size < Settings.MAX_ASTEROID_NUMBER) {
                startTime = TimeUtils.nanoTime();
                float newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;
                Asteroid a = new Asteroid(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
                asteroids.add(a);
                addActor(a);
            }
        }
    }

    public boolean collides(Spacecraft nau) {

        // Comprovem les col·lisions entre cada asteroid i la nau
        for (Asteroid asteroid : asteroids) {
            if (asteroid.collides(nau)) {
                return true;
            }
        }
        return false;
    }


    //TODO EXERCICI 3 a)
    public void reset() {
        for (Asteroid asteroid : asteroids) {
            removeActor(asteroid);
        }
        asteroids.clear();
    }

    public DelayedRemovalArray<Asteroid> getAsteroids() {
        return asteroids;
    }
}