package cat.xtec.ioc.utils;

import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.Random;

public class Methods {

    // Mètode que torna un float aleatòri entre un mínim i un màxim
    public static float randomFloat(float min, float max) {
        Random r = new Random();
        return r.nextFloat() * (max - min) + min;
    }

    //TODO EXERCICI 2 - Acció de parpalleig
    public static RepeatAction getParpalleig() {
        // Alpha a 0.5
        AlphaAction alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(0.5f);
        alphaAction1.setDuration(0.2f);

        // Alpha a 1.0
        AlphaAction alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(1f);
        alphaAction2.setDuration(0.2f);

        // Sequencia de alpha 0.5 a alpha 1.0
        SequenceAction sequencia = new SequenceAction();
        sequencia.addAction(alphaAction1);
        sequencia.addAction(alphaAction2);

        // Repetició de la seqüencia
        RepeatAction parpalleig = new RepeatAction();
        parpalleig.setCount(RepeatAction.FOREVER);
        parpalleig.setAction(sequencia);
        return parpalleig;
    }
}