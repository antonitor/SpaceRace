package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

/**
 * Created by Toni on 29/11/2017.
 */

//TODO EXERCICI 3 b) - Classe Laser que hereta de Scrollable
public class Laser extends Scrollable {

    // Rectangle per controlar colisions amb asteroides
    private Rectangle collisionRect;

    public Laser(float x, float y, int width, int height, int velocity) {

        super(x, y, width, height, velocity);

        AssetManager.laserSound.play(.1f);

        collisionRect = new Rectangle();
    }

    public void act(float delta) {
        super.act(delta);

        // Controla si el laser surt per el costat dret de la pantalla
        if (position.x > Settings.GAME_WIDTH) {
            Gdx.app.log("Right of Screen", ""+position.x );
            rightOfScreen = true;
        }

        collisionRect.set(position.x, position.y, width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetManager.laser, position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }
}
