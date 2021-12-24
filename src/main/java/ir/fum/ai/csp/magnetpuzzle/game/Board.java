package ir.fum.ai.csp.magnetpuzzle.game;

import lombok.Getter;

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
public class Board implements Serializable {
    private BoardConfiguration boardConfiguration;
    private Piece[][] pieces;
    private Map<Integer, Tile> tiles = new HashMap<>();


    public Board(BoardConfiguration boardConfiguration) {
        this.boardConfiguration = boardConfiguration;
        pieces = new Piece[boardConfiguration.getROW_NUM()][boardConfiguration.getCOL_NUM()];

        createPiecesAndTiles(boardConfiguration.getTileIdsForPieces());

    }

    private void createPiecesAndTiles(int[][] tileIdsForPieces) {
        for (int i = 0; i < boardConfiguration.getROW_NUM(); i++) {
            for (int j = 0; j < boardConfiguration.getCOL_NUM(); j++) {
                pieces[i][j] = new Piece(new Position(i, j));

                int tileId = tileIdsForPieces[i][j];
                Tile tile = tiles.getOrDefault(tileId, null);
                if (tile == null) {
                    tile = new Tile(tileId);
                    tile.setFirst(pieces[i][j]);
                } else {
                    tile.setSecond(pieces[i][j]);
                    if (tile.getFirst().getPosition().getX() == i) {
                        tile.setTileStatus(TileStatus.HORIZONTAL);
                    }
                }

                tiles.put(tileId, tile);
            }
        }
    }

    public int countMagnetPolesInCol(int col, PieceContent pole) {
        int count = 0;

        for (int i = 0; i < boardConfiguration.getROW_NUM(); i++) {
            if (pieces[i][col].getContent() == pole) {
                count++;
            }
        }

        return count;
    }


    public int countMagnetPolesInRow(int row, PieceContent pole) {
        int count = 0;
        for (int j = 0; j < boardConfiguration.getROW_NUM(); j++) {
            if (pieces[row][j].getContent() == pole) {
                count++;
            }
        }

        return count;
    }

    public boolean isSolved() {
        return rowColConstraintsIsMet() && tilesHasValidPieces() && neighborsHasOppositePole();
    }


    public boolean canSetPoleOn(int x, int y, PieceContent pieceContent) {
        Piece piece = pieces[x][y];
        PieceContent previousContent = piece.getContent();

        piece.setContent(pieceContent);
        boolean canSet = true;

        if (!tilePiecesHasValidPoles(piece.getTile())) {
            canSet = false;
        }

        if (!neighborsOfPieceHasOppositePole(x, y)) {
            canSet = false;
        }

        if (!(countMagnetPolesInCol(y, PieceContent.NEGATIVE) <= boardConfiguration.getColNegativeConstraints()[y] &&
                countMagnetPolesInCol(y, PieceContent.POSITIVE) <= boardConfiguration.getColPositiveConstraints()[y] &&
                countMagnetPolesInRow(x, PieceContent.NEGATIVE) <= boardConfiguration.getRowNegativeConstraints()[x] &&
                countMagnetPolesInRow(x, PieceContent.POSITIVE) <= boardConfiguration.getRowPositiveConstraint()[x])) {
            canSet = false;
        }

        piece.setContent(previousContent);

        return canSet;
    }

    public boolean tilePiecesHasValidPoles(Tile tile) {
        if (tile.getFirst().getContent() == PieceContent.None || tile.getSecond().getContent() == PieceContent.None) {
            return true;
        }

        return tile.getFirst().getContent().getMagneticPole() == -tile.getSecond().getContent().getMagneticPole();

    }

    public void setPoleOn(int x, int y, PieceContent pole) {
        pieces[x][y].setContent(pole);
    }

    private boolean rowColConstraintsIsMet() {
        for (int i = 0; i < boardConfiguration.getROW_NUM(); i++) {
            if (countMagnetPolesInRow(i, PieceContent.NEGATIVE) != boardConfiguration.getRowNegativeConstraints()[i]) {
                return false;
            }

            if (countMagnetPolesInRow(i, PieceContent.POSITIVE) != boardConfiguration.getRowPositiveConstraint()[i]) {
                return false;
            }
        }

        for (int j = 0; j < boardConfiguration.getCOL_NUM(); j++) {
            if (countMagnetPolesInCol(j, PieceContent.NEGATIVE) != boardConfiguration.getColNegativeConstraints()[j]) {
                return false;
            }

            if (countMagnetPolesInCol(j, PieceContent.POSITIVE) != boardConfiguration.getColPositiveConstraints()[j]) {
                return false;
            }
        }

        return true;
    }

    private boolean tilesHasValidPieces() {
        for (Tile tile : tiles.values()) {
            if (tile.getFirst().getContent().getMagneticPole() != -tile.getSecond().getContent().getMagneticPole()) {
                return false;
            }
        }

        return true;
    }

    private boolean neighborsHasOppositePole() {
        for (int i = 0; i < boardConfiguration.getROW_NUM(); i++) {
            for (int j = 0; j < boardConfiguration.getCOL_NUM(); j++) {
                if (!neighborsOfPieceHasOppositePole(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean neighborsOfPieceHasOppositePole(int i, int j) {
        if (i - 1 >= 0 && !piecesHasOppositePole(pieces[i][j], pieces[i - 1][j])) {
            return false;
        }

        if (i + 1 < boardConfiguration.getROW_NUM() && !piecesHasOppositePole(pieces[i][j], pieces[i + 1][j])) {
            return false;
        }

        if (j + 1 < boardConfiguration.getCOL_NUM() && !piecesHasOppositePole(pieces[i][j], pieces[i][j + 1])) {
            return false;
        }

        if (j - 1 >= 0 && !piecesHasOppositePole(pieces[i][j], pieces[i][j - 1])) {
            return false;
        }

        return true;
    }

    private boolean piecesHasOppositePole(Piece p1, Piece p2) {
        if (pieceHasPole(p1) && pieceHasPole(p2)) {
            return p1.getContent().getMagneticPole() == -p2.getContent().getMagneticPole();
        }

        return true;
    }

    private boolean pieceHasPole(Piece p) {
        return p.getContent() != PieceContent.None;
    }

    public Set<Piece> getNeighborsOf(Piece p) {
        Set<Piece> neighbors = new HashSet<>();
        int i = p.getPosition().getX();
        int j = p.getPosition().getY();

        if (i - 1 >= 0 && !piecesHasOppositePole(pieces[i][j], pieces[i - 1][j])) {
            neighbors.add(pieces[i - 1][j]);
        }

        if (i + 1 < boardConfiguration.getROW_NUM() && !piecesHasOppositePole(pieces[i][j], pieces[i + 1][j])) {
            neighbors.add(pieces[i + 1][j]);
        }

        if (j + 1 < boardConfiguration.getCOL_NUM() && !piecesHasOppositePole(pieces[i][j], pieces[i][j + 1])) {
            neighbors.add(pieces[i][j + 1]);
        }

        if (j - 1 >= 0 && !piecesHasOppositePole(pieces[i][j], pieces[i][j - 1])) {
            neighbors.add(pieces[i][j - 1]);
        }

        return neighbors;
    }

    public Set<Piece> getPiecesInRow(int row) {
        Set<Piece> pieces = new HashSet<>();

        for (int j = 0; j < boardConfiguration.getCOL_NUM(); j++) {
            pieces.add(this.pieces[row][j]);
        }

        return pieces;
    }

    public Set<Piece> getPiecesInCol(int col) {
        Set<Piece> pieces = new HashSet<>();

        for (int i = 0; i < boardConfiguration.getROW_NUM(); i++) {
            pieces.add(this.pieces[i][col]);
        }

        return pieces;
    }

}
