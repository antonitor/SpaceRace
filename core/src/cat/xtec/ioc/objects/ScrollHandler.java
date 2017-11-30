package cat.xtec.ioc.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class ScrollHandler extends Group {

    // Fons de pantalla
    Background bg, bg_back;

    //private ArrayList<Asteroid> asteroids;
    private DelayedRemovalArray<Asteroid> asteroids;

    //TODO EXERCICI 3 b) - Lista de lasers
    private DelayedRemovalArray<Laser> llistaLaser;

    // Objecte Random
    Random r;

    //TODO EXERCICI 3 a) Temps i interval entre asteroids
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

        //TODO EXERCICI 3 a) i b) - Inicialitzem les llistes de laser i asteroids
        asteroids = new DelayedRemovalArray<Asteroid>();
        llistaLaser = new DelayedRemovalArray<Laser>();

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

        // TODO EXERICIC 3 a) - Segons entre el moment actual i el començament del últim asteroid
        float elapsedTime = MathUtils.nanoToSec * (TimeUtils.nanoTime() - startTime);

        for (Asteroid asteroid : asteroids) {
            //Si un asteroid surt de la pantalla l'esborrem del array i del stage
            if (asteroid.isLeftOfScreen()) {
                removeActor(asteroid);
                asteroids.removeValue(asteroid, true);
            }
        }

        // TODO EXERICIC 3 a) - Quan ha transcurregut l'interval de temps afegim un nou asteroid
        if (elapsedTime >= randomInterval) {
            // Generam un nou interval de temps aleatori entre el mínim i el màxim
            this.randomInterval = MathUtils.random(Settings.MIN_ASTEROID_INTERVAL, Settings.MAX_ASTEROID_INTERVAL);
            // Tan sols si hi ha 4 o menys asteroids en generem un de nou
            if (asteroids.size < Settings.MAX_ASTEROID_NUMBER) {
                // Quan es genera un nou asteroide es reseteja el startTime
                startTime = TimeUtils.nanoTime();
                // Afegim un nou asteroide de mida i posició y aleatoria al array i a l'stage
                float newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;
                Asteroid a = new Asteroid(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
                asteroids.add(a);
                addActor(a);
            }
        }

        // TODO EXERCICI 3 b) - Eliminem els lasers que surten de la pantalla
        for (Laser laser : llistaLaser) {
            if(laser.isRightOfScreen()) {
                removeActor(laser);
                llistaLaser.removeValue(laser, true);
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


    //TODO EXERCICI 3 a) - Quan comença una nova partida liminem tots els asteroids del array i del stage
    public void reset() {
        for (Asteroid asteroid : asteroids) {
            removeActor(asteroid);
        }
        asteroids.clear();
    }

    public DelayedRemovalArray<Asteroid> getAsteroids() {
        return asteroids;
    }

    // TODO EXERCICI 3 b) - Quan disparem un laser l'afegim al stage i al array.
    public void fireLaser(Laser laser) {
        addActor(laser);
        llistaLaser.add(laser);
    }
}