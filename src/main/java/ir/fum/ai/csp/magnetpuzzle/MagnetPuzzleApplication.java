package ir.fum.ai.csp.magnetpuzzle;
import ir.fum.ai.csp.magnetpuzzle.game.Board;
import ir.fum.ai.csp.magnetpuzzle.game.BoardConfiguration;
import ir.fum.ai.csp.magnetpuzzle.game.visualizer.CommandLineVisualizer;

import java.util.Scanner;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/


public class MagnetPuzzleApplication
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int row_num = sc.nextInt();
        int col_num = sc.nextInt();
        int[] positiveRows = new int[row_num];
        int[] negativeRows = new int[row_num];
        int[] positiveCols = new int[row_num];
        int[] negativeCols = new int[row_num];
        int[][] tileIds = new int[row_num][col_num];
        BoardConfiguration boardConfig = new BoardConfiguration();
        for (int i = 0; i < row_num; i++)
            positiveRows[i] = sc.nextInt();

        for (int i = 0; i < row_num; i++)
            negativeRows[i] = sc.nextInt();

        for (int i = 0; i < col_num; i++)
            positiveCols[i] = sc.nextInt();

        for (int i = 0; i < col_num; i++)
            negativeCols[i] = sc.nextInt();

        for (int i = 0; i < row_num; i++)
        {
            for (int j = 0; j < col_num; j++)
            {
                tileIds[i][j] = sc.nextInt();
            }
        }
        boardConfig.setROW_NUM(row_num);
        boardConfig.setCOL_NUM(col_num);
        boardConfig.setRowPositiveConstraint(positiveRows);
        boardConfig.setRowNegativeConstraints(negativeRows);
        boardConfig.setColPositiveConstraints(positiveCols);
        boardConfig.setColNegativeConstraints(negativeCols);
        boardConfig.setTileIdsForPieces(tileIds);

        Board board = new Board(boardConfig);

    }
}
