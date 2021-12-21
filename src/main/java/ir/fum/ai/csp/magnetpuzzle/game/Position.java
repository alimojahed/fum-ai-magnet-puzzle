package ir.fum.ai.csp.magnetpuzzle.game;

import lombok.*;

import java.io.Serializable;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Position implements Serializable {
    @EqualsAndHashCode.Include
    private int x;

    @EqualsAndHashCode.Include
    private int y;

    public static Position from(int x, int y) {
        return new Position(x, y);
    }

}
