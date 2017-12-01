package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.Asteroid;
import cat.xtec.ioc.objects.FireButton;
import cat.xtec.ioc.objects.PauseButton;
import cat.xtec.ioc.objects.ScrollHandler;
import cat.xtec.ioc.objects.Spacecraft;
import cat.xtec.ioc.utils.Settings;

import static cat.xtec.ioc.utils.Settings.EXPLOSION_FRAMES;
import static cat.xtec.ioc.utils.Settings.EXPLOSION_FRAME_DURATION;

public class GameScreen implements Screen {

    // Els estats del joc
    public enum GameState {

        READY, RUNNING, GAMEOVER, PAUSE

    }

    private GameState currentState;

    // Objectes necessaris
    private Stage stage;
    private Spacecraft spacecraft;
    private ScrollHandler scrollHandler;

    //TODO EXERCICI 2 - Variable que referencia l'actor PauseButton
    private PauseButton pauseButton;

    //TODO EXERCICI 3 b) -  Variable que referencia l'actor FireButton
    private FireButton fireButton;

    //TODO EXERCICI 3 b) - Variables per reproduir les explosions simultanies d'asteroides
    private DelayedRemovalArray<Asteroid> explosiveAsteroid;
    private DelayedRemovalArray<Float> explosionsTimes;

    // Encarregats de dibuixar elements per pantalla
    private ShapeRenderer shapeRenderer;
    private Batch batch;

    // Per controlar l'animació de l'explosió
    private float explosionTime = 0;

    // Preparem el textLayout per escriure text
    private GlyphLayout textLayout;

    //TODO EXERCICI 2 : GlyphLayout que ens ajudarà a centrar el texte "Pausa"
    private GlyphLayout pauseLayout;

    public GameScreen(Batch prevBatch, Viewport prevViewport) {

        // Iniciem la música
        AssetManager.music.play();

        // Creem el ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);

        batch = stage.getBatch();

        // Creem la nau i la resta d'objectes
        spacecraft = new Spacecraft(Settings.SPACECRAFT_STARTX, Settings.SPACECRAFT_STARTY, Settings.SPACECRAFT_WIDTH, Settings.SPACECRAFT_HEIGHT, this);
        scrollHandler = new ScrollHandler();
        //TODO EXERCICI 2 - Creem botó pause
        pauseButton = new PauseButton(Settings.PAUSE_BUTTON_X, Settings.PAUSE_BUTTON_Y, Settings.PAUSE_BUTTON_WIDTH, Settings.PAUSE_BUTTON_HEIGHT);

        //TODO EXERCICI 3 b) - Creem el botó fire
        fireButton = new FireButton(Settings.FIRE_BUTTON_X, Settings.FIRE_BUTTON_Y, Settings.FIRE_BUTTON_WIDTH, Settings.FIRE_BUTTON_HEIGHT);

        // Afegim els actors a l'stage
        stage.addActor(spacecraft);
        stage.addActor(scrollHandler);

        // Donem nom a l'Actor
        spacecraft.setName("spacecraft");

        //TODO EXERCICI 2 - Afegim el boto pause a l'stage i li donem nom
        stage.addActor(pauseButton);
        pauseButton.setName("pause");

        //TODO EXERCICI 3 b) - Afegim el boto fire a l'stage i li donem nom
        stage.addActor(fireButton);
        fireButton.setName("fire");

        //TODO EXERCICI 3 b) - Iniciem els arrays necessaris per reproduir anmacións d'explosió
        // simultanies quan es destrueix un asteroide.
        explosiveAsteroid = new DelayedRemovalArray<Asteroid>();
        explosionsTimes = new DelayedRemovalArray<Float>();

        // Iniciem el GlyphLayout
        textLayout = new GlyphLayout();
        textLayout.setText(AssetManager.font, "Are you\nready?");

        //TODO EXERCICI 2 - Afegim la font i el texte al GlyphLayout
        pauseLayout = new GlyphLayout();
        pauseLayout.setText(AssetManager.font, "Pause");

        currentState = GameState.READY;

        // Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));
        stage.addActor(spacecraft);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Dibuixem tots els actors de l'stage
        stage.draw();

        // Depenent de l'estat del joc farem unes accions o unes altres
        switch (currentState) {
            case GAMEOVER:
                updateGameOver(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case READY:
                updateReady();
                break;
            case PAUSE:
                updatePause(delta);
                break;
        }

        //drawElements();

    }

    private void updateReady() {

        // Dibuixem el text al centre de la pantalla
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        //stage.addActor(textLbl);
        batch.end();

    }

    private void updateRunning(float delta) {
        currentState = GameState.RUNNING;
        stage.act(delta);

        if (scrollHandler.collides(spacecraft)) {
            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("spacecraft").remove();
            textLayout.setText(AssetManager.font, "Game Over :'(");
            currentState = GameState.GAMEOVER;
        }


        //TODO EXERCICI 3 b) - Quan es destrueix un asteroide, es guarda aquest a la llista per
        //tal de reproduir-ne l'explosió a la posició on s'ha destruït.
        //Afegim també un zero a la llista explosionsTimes per tal de controlar el temps d'animació
        //d'aquesta explosió.
        Asteroid asteroid = scrollHandler.asteroidDestroyed();
        if (asteroid != null) {
            explosiveAsteroid.add(asteroid);
            explosionsTimes.add(0f);
            asteroid = null;
        }

        //TODO EXERCICI 3 b) - Quan el temps d'explosió excedeix el necessari per reproduïr l'explosió,
        //esborrem l'asteroide i el temporitzador de les seves corresponents llistes.
        for (int i = 0; i < explosionsTimes.size; i++) {
            if (explosionsTimes.get(i) > EXPLOSION_FRAME_DURATION * EXPLOSION_FRAMES) {
                explosiveAsteroid.removeIndex(i);
                explosionsTimes.removeIndex(i);
            }
        }


        //TODO EXERCICI 3 b) Per cada asteroide enregistrat a la llista explosiveAsteroid, reproduím un cop
        //l'anmimació d'explosió en la posició d'aquest.
        for(int i = 0; i <  explosiveAsteroid.size; i++) {
            Asteroid explosive = explosiveAsteroid.get(i);
            batch.begin();
            batch.draw(AssetManager.explosionAnim.getKeyFrame(explosionsTimes.get(i), false), (explosive.getX() + explosive.getWidth() / 2) - 32, explosive.getY() + explosive.getHeight() / 2 - 32, 64, 64);
            batch.end();
            explosionsTimes.set(i, explosionsTimes.get(i) + delta);
            if (explosionsTimes.get(i) > 0f) {
                Gdx.app.log("Explosion Time nº" + i + ": ", "" + explosionsTimes.get(i));
            }
        }
    }

    //TODO EXERCICI 2 - Durant l'estat de Pausa dibuixem el texte i actualitzem els actors que parpallegen
    private void updatePause(float delta) {
        this.spacecraft.act(delta);
        batch.begin();
        AssetManager.font.draw(batch, pauseLayout, (Settings.GAME_WIDTH / 2) - pauseLayout.width / 2, (Settings.GAME_HEIGHT / 2) - pauseLayout.height / 2);
        batch.end();
    }

    private void updateGameOver(float delta) {
        stage.act(delta);
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH - textLayout.width) / 2, (Settings.GAME_HEIGHT - textLayout.height) / 2);
        // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
        batch.draw(AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (spacecraft.getX() + spacecraft.getWidth() / 2) - 32, spacecraft.getY() + spacecraft.getHeight() / 2 - 32, 64, 64);
        batch.end();
        explosionTime += delta;
    }

    public void reset() {

        // Posem el text d'inici
        textLayout.setText(AssetManager.font, "Are you\nready?");
        // Cridem als restart dels elements.
        spacecraft.reset();
        scrollHandler.reset();

        // Posem l'estat a 'Ready'
        currentState = GameState.READY;

        // Afegim la nau a l'stage
        stage.addActor(spacecraft);

        // Posem a 0 les variables per controlar el temps jugat i l'animació de l'explosió
        explosionTime = 0.0f;

    }

    //TODO EXERCICI 2 : Quan es prem el botó pausa s'executa aquest mètode
    public void pauseGame() {
        //Es posa l'estat d'aquesta classe a PAUSE
        this.setCurrentState(GameScreen.GameState.PAUSE);
        //S'amaguen el botó Pause i Fire
        this.getPauseButton().setStatus(Settings.Status.HIDDEN);
        this.fireButton.setStatus(Settings.Status.HIDDEN);
        //Es truquen els mètodes pause dels actors que parpallejaràn
        this.getSpacecraft().pause();
        //Disminuim el volum de la música
        AssetManager.music.setVolume(.05f);
    }

    //TODO EXERCICI 2 : Quan es surt de l'estat de pausa
    public void resumeGame() {
        //Es truquen els mètodes resume dels actors per que tornin a actuar
        this.spacecraft.resume();
        //Es posa l'estat d'aquesta classe a RUNNING
        this.setCurrentState(GameScreen.GameState.RUNNING);
        //Tornem a mostrem el botó pause mentre corre el joc
        this.getPauseButton().setStatus(Settings.Status.SHOWN);
        this.fireButton.setStatus(Settings.Status.SHOWN);
        //Tornem deixar el volum de la música com estaba
        AssetManager.music.setVolume(.2f);
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public Spacecraft getSpacecraft() {
        return spacecraft;
    }

    public Stage getStage() {
        return stage;
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public PauseButton getPauseButton() {
        return pauseButton;
    }
}
