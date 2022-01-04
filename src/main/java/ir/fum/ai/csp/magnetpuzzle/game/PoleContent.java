package ir.fum.ai.csp.magnetpuzzle.game;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/

@Getter
public enum PoleContent implements Serializable {
    None("0", 0),
    POSITIVE("+", 1),
    NEGATIVE("-", -1);

    private final String pieceContentCharacter;
    private final int magneticPole;

    PoleContent(String pieceContentCharacter, int magneticPole) {
        this.pieceContentCharacter = pieceContentCharacter;
        this.magneticPole = magneticPole;
    }

    public static PoleContent getOppositePole(PoleContent pole) {
        switch (pole) {
            case NEGATIVE:
                return POSITIVE;
            case POSITIVE:
                return NEGATIVE;
            default:
                return None;
        }
    }
}
