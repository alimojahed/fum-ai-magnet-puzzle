package ir.fum.ai.csp.magnetpuzzle;

import ir.fum.ai.csp.magnetpuzzle.config.reader.FileConfigParser;
import ir.fum.ai.csp.magnetpuzzle.csp.algorithm.HeuristicBacktrackAlgorithm;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.solver.CSPSolverAlgorithm;
import ir.fum.ai.csp.magnetpuzzle.game.Board;
import ir.fum.ai.csp.magnetpuzzle.game.BoardConfiguration;
import ir.fum.ai.csp.magnetpuzzle.game.Piece;
import ir.fum.ai.csp.magnetpuzzle.game.PieceContent;
import ir.fum.ai.csp.magnetpuzzle.game.csp.MagnetPuzzleCSP;
import ir.fum.ai.csp.magnetpuzzle.game.csp.MagnetPuzzleLCVHeuristic;
import lombok.extern.log4j.Log4j2;

import java.io.FileNotFoundException;

/**
 * @author Ali Mojahed on 12/30/2021
 * @project magnet-puzzle
 **/

@Log4j2
public class Test {
    public static void main(String[] args) throws FileNotFoundException {

        solveGame();
    }


    private static void solveGame() throws FileNotFoundException {
        long startTime = System.nanoTime();

        BoardConfiguration boardConfiguration = new FileConfigParser("input2_method2.txt").parseConfig();

        System.out.println(boardConfiguration);
        Board board = new Board(boardConfiguration);

        System.out.println(board);
        CSP<Board, Piece, PieceContent> problem = new MagnetPuzzleCSP(board);

        CSPSolverAlgorithm<Board, Piece, PieceContent> solver =
                new HeuristicBacktrackAlgorithm<>(problem, new MagnetPuzzleLCVHeuristic());

        solver.solve();

        long endTime = System.nanoTime();

        for (int i = 0; i < boardConfiguration.getROW_NUM(); i++) {
            for (int j = 0; j < boardConfiguration.getCOL_NUM(); j++) {
                System.out.println(board.getPieceByPos(i, j) + " = " + board.getPieceByPos(i, j).getContent());
            }
        }

        System.out.println("executed in " + (endTime - startTime) / 1e9);

    }

}
