package ir.fum.ai.csp.magnetpuzzle.game.visualizer;

import ir.fum.ai.csp.magnetpuzzle.game.MagnetPuzzleBoard;
import ir.fum.ai.csp.magnetpuzzle.game.MagnetPuzzleConfiguration;
import ir.fum.ai.csp.magnetpuzzle.game.MagnetStatus;
import ir.fum.ai.csp.magnetpuzzle.game.Pole;

/**
 * @author Mahya Ehsanimehr on 12/24/2021
 * @project magnet-puzzle
 **/
public class CommandLineVisualizer implements Visualizer {
    @Override
    public void visualizeBoard(MagnetPuzzleBoard magnetPuzzleBoard) {

        int rows = magnetPuzzleBoard.getMagnetPuzzleConfiguration().getROW_NUM();
        int cols = magnetPuzzleBoard.getMagnetPuzzleConfiguration().getCOL_NUM();

        String VERTICAL_WALL = "\u2503";
        String HORIZONTAL_WALL = "\u2501";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n");
        stringBuilder.append(VERTICAL_WALL);
        for (int i=0; i < rows-1; i++) {
            stringBuilder.append(HORIZONTAL_WALL).append(" ");

            if (i != rows -2) {
                stringBuilder.append(HORIZONTAL_WALL);
            }

        }
        stringBuilder
                .append(HORIZONTAL_WALL)
                .append(VERTICAL_WALL);
        stringBuilder.append("\n");

        stringBuilder
                .append(VERTICAL_WALL)
                .append("+")
                .append(" ")
                .append(" ")
                .append(VERTICAL_WALL);


        for (int i=0; i < rows; i++) {
            stringBuilder.append(magnetPuzzleBoard.getMagnetPuzzleConfiguration().getColNegativeConstraints()[i])
                    .append(VERTICAL_WALL);
        }


        stringBuilder.append("\n");

        stringBuilder.append(VERTICAL_WALL);
        for (int i=0; i < rows-1; i++) {
            stringBuilder.append(HORIZONTAL_WALL).append(" ");

            if (i != rows -2) {
                stringBuilder.append(HORIZONTAL_WALL);
            }

        }
        stringBuilder.append(HORIZONTAL_WALL)
                .append(VERTICAL_WALL);
        stringBuilder.append("\n");

        stringBuilder
                .append(VERTICAL_WALL)
                .append(" ")
                .append(VERTICAL_WALL)
                .append("-")
                .append(VERTICAL_WALL);

        for (int i=0; i < rows; i++) {
            stringBuilder.append(magnetPuzzleBoard.getMagnetPuzzleConfiguration().getColNegativeConstraints()[i])
                    .append(VERTICAL_WALL);
        }
        stringBuilder.append("\n");
        stringBuilder.append(VERTICAL_WALL);
        for (int i=0; i < rows-1; i++) {
            stringBuilder.append(HORIZONTAL_WALL).append(" ");

            if (i != rows -2) {
                stringBuilder.append(HORIZONTAL_WALL);
            }

        }
        stringBuilder
                .append(HORIZONTAL_WALL)
                .append(VERTICAL_WALL);
        stringBuilder.append("\n");

        for (int i = 0; i < rows; i++) {
            stringBuilder
                    .append(VERTICAL_WALL)
                    .append(magnetPuzzleBoard.getMagnetPuzzleConfiguration().getRowPositiveConstraint()[i])
                    .append(VERTICAL_WALL)
                    .append(magnetPuzzleBoard.getMagnetPuzzleConfiguration().getRowNegativeConstraints()[i])
                    .append(VERTICAL_WALL);

            for (int j = 0; j < cols; j++) {
                Pole pole = magnetPuzzleBoard.getPieceByPos(i, j);
                stringBuilder.append(pole.getContent().getPieceContentCharacter());
                if (pole.getMagnet().getMagnetStatus() == MagnetStatus.HORIZONTAL && pole.getMagnet().getSecond().equals(pole)) {
                    stringBuilder.append(VERTICAL_WALL);
                } else if (pole.getMagnet().getMagnetStatus() == MagnetStatus.VERTICAL) {
                    stringBuilder.append(VERTICAL_WALL);
                }else {
                    stringBuilder.append(" ");
                }

            }
            stringBuilder.append("\n");
            stringBuilder.append(VERTICAL_WALL);
            stringBuilder.append(HORIZONTAL_WALL).append(" ");
            stringBuilder.append(HORIZONTAL_WALL).append(" ");
            for (int j=0; j <cols;j++) {
                Pole pole = magnetPuzzleBoard.getPieceByPos(i, j);
                if (pole.getMagnet().getMagnetStatus() == MagnetStatus.VERTICAL && pole.getMagnet().getSecond().equals(pole)) {
                    stringBuilder.append(HORIZONTAL_WALL);

                } else if (pole.getMagnet().getMagnetStatus() == MagnetStatus.HORIZONTAL) {
                    stringBuilder.append(HORIZONTAL_WALL);

                }else {
                    stringBuilder.append(" ");
                }

                if (j != cols-1) {
                    stringBuilder.append(" ");
                }

            }
            stringBuilder.append(VERTICAL_WALL);
            stringBuilder.append("\n");
//            System.out.println("\n--------------------------");
        }
        System.out.println(stringBuilder.toString());
    }

    private String[][] makeBoardArray(MagnetPuzzleBoard magnetPuzzleBoard) {
        MagnetPuzzleConfiguration config = magnetPuzzleBoard.getMagnetPuzzleConfiguration();
        String[][] boardArr = new String[config.getROW_NUM() + 2][config.getCOL_NUM() + 2];
        boardArr[0][0] = "+";
        boardArr[0][1] = " ";
        boardArr[1][1] = "-";
        boardArr[1][0] = " ";

        // adding column constraints
        for (int i = 0; i < 2; i++) {
            for (int j = 2; j < config.getCOL_NUM() + 2; j++) {
                if (i == 0)
                    boardArr[i][j] = String.valueOf(config.getColPositiveConstraints()[j - 2]);

                if (i == 1)
                    boardArr[i][j] = String.valueOf(config.getColNegativeConstraints()[j - 2]);
            }
        }

        // adding row constraints
        for (int i = 2; i < config.getROW_NUM() + 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (j == 0)
                    boardArr[i][j] = String.valueOf(config.getRowPositiveConstraint()[i - 2]);

                if (j == 1)
                    boardArr[i][j] = String.valueOf(config.getRowNegativeConstraints()[i - 2]);
            }
        }

        // adding tile ids
        for (int i = 2; i < config.getROW_NUM() + 2; i++) {
            for (int j = 2; j < config.getCOL_NUM() + 2; j++) {
                boardArr[i][j] = String.valueOf(config.getTileIdsForPieces()[i][j]);
            }
        }

        return boardArr;
    }
}
