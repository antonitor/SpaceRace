package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.screens.GameScreen;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class Spacecraft extends Actor {

    // Distintes posicions de la spacecraft, recta, pujant i baixant
    public static final int SPACECRAFT_STRAIGHT = 0;
    public static final int SPACECRAFT_UP = 1;
    public static final int SPACECRAFT_DOWN = 2;

    // Paràmetres de la spacecraft
    private Vector2 position;
    private int width, height;
    private int direction;
    private GameScreen gameScreen;

    //TODO EXERCICI 2 - Variables membre
    private RepeatAction parpalleig;
    private boolean paused = false;
    private int lastDirection = SPACECRAFT_STRAIGHT;

    private Rectangle collisionRect;

    public Spacecraft(float x, float y, int width, int height, GameScreen gameScreen) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        this.gameScreen = gameScreen;

        // Inicialitzem la spacecraft a l'estat normal
        direction = SPACECRAFT_STRAIGHT;

        // Creem el rectangle de col·lisions
        collisionRect = new Rectangle();

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);

        parpalleig = Methods.getParpalleig();
    }

    public void act(float delta) {
        super.act(delta);
        // Movem la spacecraft depenent de la direcció controlant que no surti de la pantalla
        if (!paused) {
            switch (direction) {
                case SPACECRAFT_UP:
                    if (this.position.y - Settings.SPACECRAFT_VELOCITY * delta >= 0) {
                        this.position.y -= Settings.SPACECRAFT_VELOCITY * delta;
                    }
                    break;
                case SPACECRAFT_DOWN:
                    if (this.position.y + height + Settings.SPACECRAFT_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                        this.position.y += Settings.SPACECRAFT_VELOCITY * delta;
                    }
                    break;
                case SPACECRAFT_STRAIGHT:
                    break;
            }
        }


        collisionRect.set(position.x, position.y + 3, width, 10);
        setBounds(position.x, position.y, width, height);

    }

    // Getters dels atributs principals
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    // Canviem la direcció de la spacecraft: Puja
    public void goUp() {
        direction = SPACECRAFT_UP;
    }

    // Canviem la direcció de la spacecraft: Baixa
    public void goDown() {
        direction = SPACECRAFT_DOWN;
    }

    // Posem la spacecraft al seu estat original
    public void goStraight() {
        direction = SPACECRAFT_STRAIGHT;
    }

    // Obtenim el TextureRegion depenent de la posició de la spacecraft
    public TextureRegion getSpacecraftTexture() {

        //TODO EXERCICI 2 - Si estem pausats no s'ha de canviar la possició de la nau
        if (paused) {
            direction = lastDirection;
        }

        switch (direction) {
            case SPACECRAFT_STRAIGHT:
                this.lastDirection = SPACECRAFT_STRAIGHT;
                return AssetManager.spacecraft;
            case SPACECRAFT_UP:
                this.lastDirection = SPACECRAFT_UP;
                return AssetManager.spacecraftUp;
            case SPACECRAFT_DOWN:
                this.lastDirection = SPACECRAFT_DOWN;
                return AssetManager.spacecraftDown;
            default:
                return AssetManager.spacecraft;
        }
    }

    public void reset() {
        // La posem a la posició inicial i a l'estat normal
        position.x = Settings.SPACECRAFT_STARTX;
        position.y = Settings.SPACECRAFT_STARTY;
        direction = SPACECRAFT_STRAIGHT;
        collisionRect = new Rectangle();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        //Per que no interfereixi els possibles canvis als colors del batch
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(getSpacecraftTexture(), position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }


    //TODO EXERCICI 2 - Afegim acció parpalleig a la nau durant l'estat de pausa
    public void pause() {
        this.paused = true;
        this.addAction(parpalleig);
    }

    //TODO EXERCICI 2 - Eliminem l'acció de parpalleig
    public void resume(){
        this.paused = false;
        // Tornam alpha a 1
        this.addAction(Actions.alpha(1f));
        // Eliminem la seqüencia
        this.removeAction(parpalleig);
    }

    // TODO EXERCICI 3 b) - Per disparar un laser tan sols hem d'afegir un nou objecte Laser al ScrollHandler amb la posició del frontal de la nau
    public void fire() {
        gameScreen.getScrollHandler().fireLaser(new Laser(position.x + width, position.y + height * 2/3, Settings.LASER_WIDTH, Settings.LASER_HEIGHT, Settings.LASER_SPEED));
    }

}
