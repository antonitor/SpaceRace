package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.assets.AssetManager;

import cat.xtec.ioc.utils.Settings;

import static cat.xtec.ioc.utils.Settings.ATLAS;
import static cat.xtec.ioc.utils.Settings.EXPLOSION_FRAME_DURATION;

public class Assets {

    public static AssetManager assetManager;
    // Sprite Sheet
    //public static Texture sheet;

    // Nau i fons
    public static TextureRegion spacecraft, spacecraftDown, spacecraftUp, background;

    // Asteroid
    public static TextureRegion[] asteroid;
    public static Animation asteroidAnim;

    // Explosió
    public static TextureRegion[] explosion;
    public static Animation explosionAnim;

    // Sons
    public static Sound explosionSound;
    public static Music music;

    // TODO EXERCICI 3 b) Laser sound
    public static Sound laserSound;

    //Pause
    public static TextureRegion pauseButton;

    //Fire
    public static TextureRegion fireButton;

    //Laser
    public static TextureRegion laser;

    // Font
    public static BitmapFont font;
    public static BitmapFont font2;

    public static void load() {

        //TODO EXTRA - Carreguem les textures del Atlas creat mitjançant TexturePacker
        assetManager = new AssetManager();
        assetManager.load(Settings.ATLAS, TextureAtlas.class);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get(ATLAS);


        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        //sheet = new Texture(Gdx.files.internal("sheet.png"));
        //sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);


        // Sprites de la nau
        spacecraft = atlas.findRegion(Settings.SPACECRAFT);
        spacecraft.flip(false, true);

        spacecraftUp = atlas.findRegion(Settings.SPACECRAFT_UP);
        spacecraftUp.flip(false, true);

        spacecraftDown = atlas.findRegion(Settings.SPACECRAFT_DOWN);
        spacecraftDown.flip(false, true);

        // Carreguem els 16 estats de l'asteroid
        asteroid = new TextureRegion[16];
        for (int i = 0; i < asteroid.length; i++) {
            String t = "";
            if (i < 9) {
                t = "0" + (i + 1);
            } else {
                t = "" + (i + 1);
            }
            asteroid[i] = atlas.findRegion(Settings.ASTEROID + t);
            asteroid[i].flip(false, true);
        }

        // Creem l'animació de l'asteroid i fem que s'executi contínuament en sentit anti-horari
        asteroidAnim = new Animation(EXPLOSION_FRAME_DURATION, asteroid);
        asteroidAnim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        // Creem els 16 estats de l'explosió
        explosion = new TextureRegion[15];

        // Carreguem els 16 estats de l'explosió
        for (int i = 0; i < explosion.length; i++) {
            String t = "";
            if (i < 9) {
                t = "0" + (i + 1);
            } else {
                t = "" + (i + 1);
            }
            explosion[i] = atlas.findRegion(Settings.EXPLOSION + t);
            explosion[i].flip(false, true);

        }

        // Finalment creem l'animació
        explosionAnim = new Animation(0.04f, explosion);

        // Fons de pantalla
        background = atlas.findRegion(Settings.BACKGROUND);
        background.flip(false, true);

        /******************************* Sounds *************************************/
        // Explosió
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        // Música del joc
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Afterburner.ogg"));
        music.setVolume(0.2f);
        music.setLooping(true);

        /******************************* Text *************************************/
        // Font space
        FileHandle fontFile = Gdx.files.internal("fonts/space.fnt");
        font = new BitmapFont(fontFile, true);
        font.getData().setScale(0.4f);
        //TODO EXERCICI 1 - b) Afegim una nova font per al segón títol del SplashScreen
        font2 = new BitmapFont(fontFile, true);
        font2.getData().setScale(0.2f);

        //TODO EXERCICI 2 - Sprite del botó pause
        /*******************************Pause***************************************/
        pauseButton = atlas.findRegion(Settings.PAUSE_BUTTON);
        pauseButton.flip(false, true);

        //TODO EXERCICI 3 b) - Sprite del botó fire
        /*******************************FIRE***************************************/
        fireButton = atlas.findRegion(Settings.FIRE_BUTTON);
        fireButton.flip(false, true);

        // TODO EXERCICI 3 b) - Sprite del laser i so
        laser = atlas.findRegion(Settings.LASER);
        laser.flip(false, true);
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser-sileced.wav"));
    }

    public static void dispose() {

        // Descrtem els recursos
        assetManager.dispose();
        explosionSound.dispose();
        music.dispose();

    }
}
