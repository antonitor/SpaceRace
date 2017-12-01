package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import cat.xtec.ioc.objects.Spacecraft;
import cat.xtec.ioc.screens.GameScreen;

public class InputHandler implements InputProcessor {

    // Enter per a la gesitó del moviment d'arrastrar
    int previousY = 0;
    // Objectes necessaris
    private Spacecraft spacecraft;
    private GameScreen screen;
    private Vector2 stageCoord;

    private Stage stage;

    // TODO EXERCICI 3 b) - Enter que enregistra el punter que mou la nau. Necessari per tal de poder
    // disparar a l'hora que movem la nau sense que el clic al botó de dispar ens influeixi en el
    // moviment de la nau.
    private int movementPointer;

    public InputHandler(GameScreen screen) {

        // Obtenim tots els elements necessaris
        this.screen = screen;
        spacecraft = screen.getSpacecraft();
        stage = screen.getStage();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        switch (screen.getCurrentState()) {

            case READY:
                // Si fem clic comencem el joc
                screen.setCurrentState(GameScreen.GameState.RUNNING);
                break;
            case RUNNING:

                stageCoord = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
                Actor actorHit = stage.hit(stageCoord.x, stageCoord.y, true);
                if (actorHit != null) {
                    Gdx.app.log("HIT", actorHit.getName() + ", pointer: " + pointer);
                    //TODO EXERCICI 2 - Si es fa clic sobre el botó pause pausem el joc
                    if (actorHit.getName().equals("pause")) {
                        screen.pauseGame();
                    //TODO EXERCICI 3 b) - Si fem clic sobre el botó fire disparem un laser
                    } else if (actorHit.getName().equals("fire")) {
                        screen.getSpacecraft().fire();
                        // Si es toca el botó fire no volem que es tracti com a movementPointer
                        if (movementPointer == pointer) {
                            movementPointer = -1;
                        }
                    }
                } else {
                    previousY = screenY;
                    Gdx.app.log("HIT ", "null, pointer: " + pointer);
                    movementPointer = pointer;
                }
                break;
            // Si l'estat és GameOver tornem a iniciar el joc
            case GAMEOVER:
                screen.reset();
                break;
            //TODO EXERCICI 2 - Si fem clic sobre la pantalla durant l'estat pausat reprenem el joc
            case PAUSE:
                screen.resumeGame();
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        // TODO EXERCICI 3 b) Tan sols  posem la nau en estat normal quan deixem anar el dit, si es tracta del pointer de moviment
        if (movementPointer == pointer) {
            spacecraft.goStraight();
        }
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //TODO EXERCICI 3 b) - Tan sols  mourem la nau si es tracta del pointer de moviment
        if (movementPointer == pointer) {
            // Posem un umbral per evitar gestionar events quan el dit està quiet
            if (Math.abs(previousY - screenY) > 2)

                // Si la Y és major que la que tenim
                // guardada és que va cap avall
                if (previousY < screenY) {
                    spacecraft.goDown();
                } else {
                    // En cas contrari cap a dalt
                    spacecraft.goUp();
                }
            // Guardem la posició de la Y
            previousY = screenY;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
