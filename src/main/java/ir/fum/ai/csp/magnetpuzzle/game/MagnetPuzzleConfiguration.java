package ir.fum.ai.csp.magnetpuzzle.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class MagnetPuzzleConfiguration implements Serializable {
    private int[] rowPositiveConstraint;
    private int[] rowNegativeConstraints;
    private int[] colPositiveConstraints;
    private int[] colNegativeConstraints;
    private int[][] tileIdsForPieces;
    private int ROW_NUM;
    private int COL_NUM;
}
