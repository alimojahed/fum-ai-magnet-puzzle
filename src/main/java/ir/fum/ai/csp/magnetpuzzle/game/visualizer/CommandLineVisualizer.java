package ir.fum.ai.csp.magnetpuzzle.game.visualizer;

import ir.fum.ai.csp.magnetpuzzle.game.Board;
import ir.fum.ai.csp.magnetpuzzle.game.BoardConfiguration;

/**
 * @author Mahya Ehsanimehr on 12/24/2021
 * @project magnet-puzzle
 **/
public class CommandLineVisualizer implements Visualizer {
    @Override
    public void visualizeBoard(Board board) {
        int rows = board.getBoardConfiguration().getROW_NUM();
        int cols = board.getBoardConfiguration().getCOL_NUM();
        String[][] boardArray = makeBoardArray(board);
        for (int i = 0; i < rows + 2; i++) {
            for (int j = 0; j < cols + 2; j++) {
                // incomplete
                System.out.print(boardArray[i][j] + " | ");
            }
            System.out.println("\n--------------------------");
        }

    }

    private String[][] makeBoardArray(Board board) {
        BoardConfiguration config = board.getBoardConfiguration();
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
