package pt.upskill.projeto1.gui;

import pt.upskill.projeto1.game.Engine;
import pt.upskill.projeto1.game.FireBallThread;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.List;

/**
 * @author Jorge Rafael Santos
 *
 *          FireTile is the interface required to use "images" as FireBallThread on
 *          ImageMatrixGUI.
 *
 */
public interface FireTile extends ImageTile {

    /**
     * Validate impact of FireTile. This function is called by {{@link FireBallThread}} after
     * each movement. If this function returns false, is job will finish after 500 ms and
     * the image on {{@link ImageMatrixGUI}} is removed.
     * @return true if any impact was detected, false otherwise.
     */
    boolean validateImpact();

    /**
     * Define new position
     * @param position
     */
    void setPosition(Position position);

}
