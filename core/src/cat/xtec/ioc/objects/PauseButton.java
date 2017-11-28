package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

/**
 * Created by Toni on 27/11/2017.
 */

//TODO Exercici 2 - Actor que defineix el botó pause. Te dos estats: SHOWN i HIDDEN

public class PauseButton extends Actor {

    // Paràmetres de la spacecraft
    private Vector2 position;
    private int width, height;
    private Status status;

    public PauseButton(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
        status = Status.SHOWN;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        switch (status) {
            case SHOWN:
                batch.draw(AssetManager.pauseButton, position.x, position.y, width, height);
                break;
            case HIDDEN:
                //No dibuixem res
                break;
        }
    }

    public enum Status {
        SHOWN, HIDDEN
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
