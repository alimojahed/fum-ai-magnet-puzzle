package ir.fum.ai.csp.magnetpuzzle.game;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/

@Getter
public enum PieceContent implements Serializable {
    None("0", 0),
    POSITIVE("+", 1),
    NEGATIVE("-", -1);

    private final String pieceContentCharacter;
    private final int magneticPole;

    PieceContent(String pieceContentCharacter, int magneticPole) {
        this.pieceContentCharacter = pieceContentCharacter;
        this.magneticPole = magneticPole;
    }

    public static PieceContent getOppositePole(PieceContent pole) {
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
