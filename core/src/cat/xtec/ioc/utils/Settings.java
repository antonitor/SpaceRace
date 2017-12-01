package cat.xtec.ioc.utils;

public class Settings {

    // Mida del joc, s'escalarà segons la necessitat
    public static final int GAME_WIDTH = 240;
    public static final int GAME_HEIGHT = 135;

    // Propietats de la nau
    public static final float SPACECRAFT_VELOCITY = 50;
    public static final int SPACECRAFT_WIDTH = 36;
    public static final int SPACECRAFT_HEIGHT = 15;
    public static final float SPACECRAFT_STARTX = 20;
    public static final float SPACECRAFT_STARTY = GAME_HEIGHT/2 - SPACECRAFT_HEIGHT/2;

    // Rang de valors per canviar la mida de l'asteroide.
    public static final float MAX_ASTEROID = 1.5f;
    public static final float MIN_ASTEROID = 0.5f;

    // Configuració Scrollable
    public static final int ASTEROID_SPEED = -150;
    public static final int ASTEROID_GAP = 75;
    public static final int BG_SPEED = -100;

    //TODO EXERCICI 2 - Propietats del botó pause
    public static final int PAUSE_BUTTON_WIDTH = 50;
    public static final int PAUSE_BUTTON_HEIGHT = 50;
    public static final float PAUSE_BUTTON_X = GAME_WIDTH - PAUSE_BUTTON_WIDTH;
    public static final float PAUSE_BUTTON_Y = 0;

    //TODO EXERCICI 3 b) - Propietats del botó fire
    public static final int FIRE_BUTTON_WIDTH = 50;
    public static final int FIRE_BUTTON_HEIGHT = 50;
    public static final float FIRE_BUTTON_X = GAME_WIDTH - FIRE_BUTTON_WIDTH;
    public static final float FIRE_BUTTON_Y = GAME_HEIGHT - FIRE_BUTTON_HEIGHT;



    //TODO EXERCICI 3 a) - Nombre màxim d'axteroides
    public static int MAX_ASTEROID_NUMBER = 4;
    public static final float MAX_ASTEROID_INTERVAL = 2f;
    public static final float MIN_ASTEROID_INTERVAL = 1f;

    //TODO EXERCICI 3 b) - Propietats del laser
    public static int LASER_SPEED = 200;
    public static int LASER_WIDTH = 6;
    public static int LASER_HEIGHT = 2;

    //Explosió
    public static float EXPLOSION_FRAME_DURATION = .05f;
    public static float EXPLOSION_FRAMES = 16;


    //TODO EXERCICI 2 i 3 b) - Propietats dels botons
    public static enum Status {
        SHOWN, HIDDEN
    }
}
