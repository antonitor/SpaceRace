package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

/**
 * Created by Toni on 29/11/2017.
 */

//TODO EXERCICI 3 b) - Classe Laser que hereta de Scrollable i que a més controla si surt per el costat dret de la pantalla
public class Laser extends Scrollable {

    public Laser(float x, float y, int width, int height, int velocity) {

        super(x, y, width, height, velocity);

    }

    public void act(float delta) {
        super.act(delta);

        if (position.x > Settings.GAME_WIDTH) {
            Gdx.app.log("Right of Screen", ""+position.x );
            rightOfScreen = true;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetManager.laser, position.x, position.y, width, height);
    }
}
