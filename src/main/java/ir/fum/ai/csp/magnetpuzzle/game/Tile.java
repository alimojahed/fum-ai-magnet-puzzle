package ir.fum.ai.csp.magnetpuzzle.game;

import lombok.*;

import java.io.Serializable;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tile implements Serializable {
    @EqualsAndHashCode.Include
    private int tileId;

    @EqualsAndHashCode.Include
    private Piece first;

    @EqualsAndHashCode.Include
    private Piece second;

    @EqualsAndHashCode.Include
    private TileStatus tileStatus;

    public Tile(int tileId) {
        this.tileId = tileId;
    }

    public void setFirst(Piece first) {
        this.first = first;
        first.setTile(this);
    }

    public void setSecond(Piece second) {
        this.second = second;
        second.setTile(this);
    }
}
