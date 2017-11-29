package cat.xtec.ioc.objects;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

import java.util.ArrayList;
import java.util.Random;

import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class ScrollHandler extends Group {

    // Fons de pantalla
    Background bg, bg_back;

    // Asteroides
    int numAsteroids;
    private ArrayList<Asteroid> asteroids;

    // Objecte Random
    Random r;

    //TODO EXERCICI 2 - Parpalleig pels asteroids
    private RepeatAction parpalleig;
    private boolean pause = false;


    public ScrollHandler() {

        // Creem els dos fons
        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);

        // Afegim els fons al grup
        addActor(bg);
        addActor(bg_back);

        // Creem l'objecte random
        r = new Random();

        // Comencem amb 3 asteroids
        numAsteroids = 3;

        // Creem l'ArrayList
        asteroids = new ArrayList<Asteroid>();

        // Definim una mida aleatòria entre el mínim i el màxim
        float newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;

        // Afegim el primer Asteroid a l'Array i al grup
        Asteroid asteroid = new Asteroid(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
        asteroids.add(asteroid);
        addActor(asteroid);

        // Des del segon fins l'últim asteroide
        for (int i = 1; i < numAsteroids; i++) {
            // Creem la mida al·leatòria
            newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;
            // Afegim l'asteroid.
            asteroid = new Asteroid(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
            // Afegim l'asteroide a l'ArrayList
            asteroids.add(asteroid);
            // Afegim l'asteroide al grup d'actors
            addActor(asteroid);
        }

        parpalleig = Methods.getParpalleig();

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!pause) {
            // Si algun element està fora de la pantalla, fem un reset de l'element.
            if (bg.isLeftOfScreen()) {
                bg.reset(bg_back.getTailX());

            } else if (bg_back.isLeftOfScreen()) {
                bg_back.reset(bg.getTailX());

            }

            for (int i = 0; i < asteroids.size(); i++) {

                Asteroid asteroid = asteroids.get(i);
                if (asteroid.isLeftOfScreen()) {
                    if (i == 0) {
                        asteroid.reset(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP);
                    } else {
                        asteroid.reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
                    }
                }
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

    public void reset() {

        // Posem el primer asteroid fora de la pantalla per la dreta
        asteroids.get(0).reset(Settings.GAME_WIDTH);
        // Calculem les noves posicions de la resta d'asteroids.
        for (int i = 1; i < asteroids.size(); i++) {

            asteroids.get(i).reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);

        }
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    //TODO EXERCICI 2 - Afegim acció parpalleig als asteroids durant l'estat de pausa
    public void pause() {
        pause = true;
        for (Asteroid asteroid : getAsteroids()) {
            asteroid.addAction(parpalleig);
        }
    }

    //TODO EXERCICI 2 - Eliminem l'acció de parpalleig
    public void resume(){
        pause = false;
        // Tornam alpha a 1
        this.addAction(Actions.alpha(1f));
        // Eliminem la seqüencia
        for (Asteroid asteroid : getAsteroids()) {
            asteroid.removeAction(parpalleig);
        }
    }
}