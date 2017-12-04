package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cat.xtec.ioc.helpers.Assets;
import cat.xtec.ioc.utils.Settings.Status;

/**
 * Created by Toni on 29/11/2017.
 */

public class FireButton extends Image {
    // Paràmetres del botó fire
    private Vector2 position;
    private int width, height;
    private Status status;

    public FireButton(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);

        // D'entrada es mostará aquest botó
        status = Status.SHOWN;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        switch (status) {
            case SHOWN:
                batch.draw(Assets.fireButton, position.x, position.y, width, height);
                break;
            case HIDDEN:
                //No dibuixem res
                break;
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
