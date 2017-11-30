package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cat.xtec.ioc.utils.Settings;

public class AssetManager {

    // Sprite Sheet
    public static Texture sheet;

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
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        sheet = new Texture(Gdx.files.internal("sheet3.png"));
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Sprites de la nau
        spacecraft = new TextureRegion(sheet, 0, 0, 36, 15);
        spacecraft.flip(false, true);

        spacecraftUp = new TextureRegion(sheet, 36, 0, 36, 15);
        spacecraftUp.flip(false, true);

        spacecraftDown = new TextureRegion(sheet, 72, 0, 36, 15);
        spacecraftDown.flip(false, true);

        // Carreguem els 16 estats de l'asteroid
        asteroid = new TextureRegion[16];
        for (int i = 0; i < asteroid.length; i++) {

            asteroid[i] = new TextureRegion(sheet, i * 34, 15, 34, 34);
            asteroid[i].flip(false, true);

        }

        // Creem l'animació de l'asteroid i fem que s'executi contínuament en sentit anti-horari
        asteroidAnim = new Animation(0.05f, asteroid);
        asteroidAnim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        // Creem els 16 estats de l'explosió
        explosion = new TextureRegion[16];

        // Carreguem els 16 estats de l'explosió
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                explosion[index++] = new TextureRegion(sheet, j * 64,  i * 64 + 49, 64, 64);
                explosion[index-1].flip(false, true);
            }
        }

        // Finalment creem l'animació
        explosionAnim = new Animation(0.04f, explosion);

        // Fons de pantalla
        background = new TextureRegion(sheet, 0, 177, 480, 135);
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
        pauseButton = new TextureRegion(sheet, 480, 177, 50, 50);
        pauseButton.flip(false, true);

        //TODO EXERCICI 3 b) - Sprite del botó fire
        /*******************************FIRE***************************************/
        fireButton = new TextureRegion(sheet, 480, 227, 50, 50);
        fireButton.flip(false, true);

        laser = new TextureRegion(sheet, 480, 310, Settings.LASER_WIDTH, Settings.LASER_HEIGHT);
        laser.flip(false,true);



    }

    public static void dispose() {

        // Descrtem els recursos
        sheet.dispose();
        explosionSound.dispose();
        music.dispose();

    }
}
