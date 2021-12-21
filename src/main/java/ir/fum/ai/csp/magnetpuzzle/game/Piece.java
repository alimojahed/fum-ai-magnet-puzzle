package ir.fum.ai.csp.magnetpuzzle.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Piece implements Serializable {
    private Position position;
    private PieceContent content = PieceContent.None;
    private Tile tile;

    public Piece(Position position) {
        this.position = position;
    }
}
