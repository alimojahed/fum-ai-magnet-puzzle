package ir.fum.ai.csp.magnetpuzzle.game;

import lombok.*;

import java.io.Serializable;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Piece implements Serializable {
    @EqualsAndHashCode.Include
    private Position position;

    @EqualsAndHashCode.Exclude
    private PieceContent content = PieceContent.None;

    @EqualsAndHashCode.Exclude
    private Tile tile;

    public Piece(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "{ (" +
                position.getX() +
                ", " +
                position.getY() +
                ")}";
    }
}
