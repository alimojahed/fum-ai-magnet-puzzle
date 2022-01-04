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
public class Magnet implements Serializable {
    @EqualsAndHashCode.Include
    private int tileId;

    @EqualsAndHashCode.Include
    private Pole first;

    @EqualsAndHashCode.Include
    private Pole second;

    @EqualsAndHashCode.Include
    private MagnetStatus magnetStatus;

    public Magnet(int tileId) {
        this.tileId = tileId;
    }

    public void setFirst(Pole first) {
        this.first = first;
        first.setMagnet(this);
    }

    public void setSecond(Pole second) {
        this.second = second;
        second.setMagnet(this);
    }
}
