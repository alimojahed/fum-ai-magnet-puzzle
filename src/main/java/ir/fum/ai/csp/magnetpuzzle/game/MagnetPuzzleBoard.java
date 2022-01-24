package ir.fum.ai.csp.magnetpuzzle.game;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/


@Getter
@ToString
public class MagnetPuzzleBoard implements Serializable {
    @ToString.Exclude
    private MagnetPuzzleConfiguration magnetPuzzleConfiguration;
    private Pole[][] poles;

    @ToString.Exclude
    private Map<Integer, Magnet> tiles = new HashMap<>();


    public MagnetPuzzleBoard(MagnetPuzzleConfiguration magnetPuzzleConfiguration) {
        this.magnetPuzzleConfiguration = magnetPuzzleConfiguration;
        poles = new Pole[magnetPuzzleConfiguration.getROW_NUM()][magnetPuzzleConfiguration.getCOL_NUM()];

        createPiecesAndTiles(magnetPuzzleConfiguration.getTileIdsForPieces());

    }

    private void createPiecesAndTiles(int[][] tileIdsForPieces) {
        for (int i = 0; i < magnetPuzzleConfiguration.getROW_NUM(); i++) {
            for (int j = 0; j < magnetPuzzleConfiguration.getCOL_NUM(); j++) {
                poles[i][j] = new Pole(new Position(i, j));

                int tileId = tileIdsForPieces[i][j];
                Magnet magnet = tiles.getOrDefault(tileId, null);
                if (magnet == null) {
                    magnet = new Magnet(tileId);
                    magnet.setFirst(poles[i][j]);
                } else {
                    magnet.setSecond(poles[i][j]);
                    if (magnet.getFirst().getPosition().getX() == i) {
                        magnet.setMagnetStatus(MagnetStatus.HORIZONTAL);
                    } else {
                        magnet.setMagnetStatus(MagnetStatus.VERTICAL);
                    }
                }

                tiles.put(tileId, magnet);
            }
        }
    }

    public int countMagnetPolesInCol(int col, PoleContent pole) {
        int count = 0;

        for (int i = 0; i < magnetPuzzleConfiguration.getROW_NUM(); i++) {
            if (poles[i][col].getContent() == pole) {
                count++;
            }
        }

        return count;
    }


    public int countMagnetPolesInRow(int row, PoleContent pole) {
        int count = 0;
        for (int j = 0; j < magnetPuzzleConfiguration.getCOL_NUM(); j++) {
            if (poles[row][j].getContent() == pole) {
                count++;
            }
        }

        return count;
    }

    public boolean isSolved() {
        return rowColConstraintsIsMet() && tilesHasValidPieces() && neighborsHasOppositePole();
    }


    public boolean canSetPoleOn(int x, int y, PoleContent poleContent) {
        Pole pole = poles[x][y];
        PoleContent previousContent = pole.getContent();

        pole.setContent(poleContent);
        boolean canSet = true;

        if (!tilePiecesHasValidPoles(pole.getMagnet())) {
            canSet = false;
        }

        if (!neighborsOfPieceHasOppositePole(x, y)) {
            canSet = false;
        }

        if (!(countMagnetPolesInCol(y, PoleContent.NEGATIVE) <= magnetPuzzleConfiguration.getColNegativeConstraints()[y] &&
                countMagnetPolesInCol(y, PoleContent.POSITIVE) <= magnetPuzzleConfiguration.getColPositiveConstraints()[y] &&
                countMagnetPolesInRow(x, PoleContent.NEGATIVE) <= magnetPuzzleConfiguration.getRowNegativeConstraints()[x] &&
                countMagnetPolesInRow(x, PoleContent.POSITIVE) <= magnetPuzzleConfiguration.getRowPositiveConstraint()[x])) {
            canSet = false;
        }

        pole.setContent(previousContent);

        return canSet;
    }

    public boolean tilePiecesHasValidPoles(Magnet magnet) {
        if (magnet.getFirst().getContent() == PoleContent.None || magnet.getSecond().getContent() == PoleContent.None) {
            return true;
        }

        return magnet.getFirst().getContent().getMagneticPole() == -magnet.getSecond().getContent().getMagneticPole();

    }

    public void setPoleOn(int x, int y, PoleContent pole) {
        poles[x][y].setContent(pole);
    }

    private boolean rowColConstraintsIsMet() {
        for (int i = 0; i < magnetPuzzleConfiguration.getROW_NUM(); i++) {
            if (countMagnetPolesInRow(i, PoleContent.NEGATIVE) != magnetPuzzleConfiguration.getRowNegativeConstraints()[i]) {
                return false;
            }

            if (countMagnetPolesInRow(i, PoleContent.POSITIVE) != magnetPuzzleConfiguration.getRowPositiveConstraint()[i]) {
                return false;
            }
        }

        for (int j = 0; j < magnetPuzzleConfiguration.getCOL_NUM(); j++) {
            if (countMagnetPolesInCol(j, PoleContent.NEGATIVE) != magnetPuzzleConfiguration.getColNegativeConstraints()[j]) {
                return false;
            }

            if (countMagnetPolesInCol(j, PoleContent.POSITIVE) != magnetPuzzleConfiguration.getColPositiveConstraints()[j]) {
                return false;
            }
        }

        return true;
    }

    private boolean tilesHasValidPieces() {
        for (Magnet magnet : tiles.values()) {
            if (magnet.getFirst().getContent().getMagneticPole() != -magnet.getSecond().getContent().getMagneticPole()) {
                return false;
            }
        }

        return true;
    }

    private boolean neighborsHasOppositePole() {
        for (int i = 0; i < magnetPuzzleConfiguration.getROW_NUM(); i++) {
            for (int j = 0; j < magnetPuzzleConfiguration.getCOL_NUM(); j++) {
                if (!neighborsOfPieceHasOppositePole(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean neighborsOfPieceHasOppositePole(int i, int j) {
        if (i - 1 >= 0 && !piecesHasOppositePole(poles[i][j], poles[i - 1][j])) {
            return false;
        }

        if (i + 1 < magnetPuzzleConfiguration.getROW_NUM() && !piecesHasOppositePole(poles[i][j], poles[i + 1][j])) {
            return false;
        }

        if (j + 1 < magnetPuzzleConfiguration.getCOL_NUM() && !piecesHasOppositePole(poles[i][j], poles[i][j + 1])) {
            return false;
        }

        if (j - 1 >= 0 && !piecesHasOppositePole(poles[i][j], poles[i][j - 1])) {
            return false;
        }

        return true;
    }

    public boolean piecesHasOppositePole(Pole p1, Pole p2) {
        if (pieceHasPole(p1) && pieceHasPole(p2)) {
            return p1.getContent().getMagneticPole() == -p2.getContent().getMagneticPole();
        }

        return true;
    }

    private boolean pieceHasPole(Pole p) {
        return p.getContent() != PoleContent.None;
    }

    public MagnetPuzzleConfiguration getMagnetPuzzleConfiguration() {
        return magnetPuzzleConfiguration;
    }

    public Set<Pole> getNeighborsOf(Pole p) {
        Set<Pole> neighbors = new HashSet<>();
        int i = p.getPosition().getX();
        int j = p.getPosition().getY();

        if (i - 1 >= 0 && !piecesHasOppositePole(poles[i][j], poles[i - 1][j])) {
            neighbors.add(poles[i - 1][j]);
        }

        if (i + 1 < magnetPuzzleConfiguration.getROW_NUM() && !piecesHasOppositePole(poles[i][j], poles[i + 1][j])) {
            neighbors.add(poles[i + 1][j]);
        }

        if (j + 1 < magnetPuzzleConfiguration.getCOL_NUM() && !piecesHasOppositePole(poles[i][j], poles[i][j + 1])) {
            neighbors.add(poles[i][j + 1]);
        }

        if (j - 1 >= 0 && !piecesHasOppositePole(poles[i][j], poles[i][j - 1])) {
            neighbors.add(poles[i][j - 1]);
        }

        return neighbors;
    }

    public Set<Pole> getPiecesInRow(int row) {
        Set<Pole> poles = new HashSet<>();

        for (int j = 0; j < magnetPuzzleConfiguration.getCOL_NUM(); j++) {
            poles.add(this.poles[row][j]);
        }

        return poles;
    }

    public Set<Pole> getPiecesInCol(int col) {
        Set<Pole> poles = new HashSet<>();

        for (int i = 0; i < magnetPuzzleConfiguration.getROW_NUM(); i++) {
            poles.add(this.poles[i][col]);
        }

        return poles;
    }

    public Pole getPieceByPos(int x, int y) {
        if (x >= magnetPuzzleConfiguration.getROW_NUM() || y >= magnetPuzzleConfiguration.getCOL_NUM() || x < 0 || y < 0) {
            return null;
        }

        return poles[x][y];
    }

}
