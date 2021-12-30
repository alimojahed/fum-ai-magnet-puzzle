package ir.fum.ai.csp.magnetpuzzle;

import ir.fum.ai.csp.magnetpuzzle.config.reader.FileConfigParser;
import ir.fum.ai.csp.magnetpuzzle.csp.algorithm.BacktrackAlgorithm;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;
import ir.fum.ai.csp.magnetpuzzle.csp.solver.CSPSolverAlgorithm;
import ir.fum.ai.csp.magnetpuzzle.game.*;
import ir.fum.ai.csp.magnetpuzzle.game.csp.MagnetPuzzleCSP;
import lombok.extern.log4j.Log4j2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ali Mojahed on 12/30/2021
 * @project magnet-puzzle
 **/

@Log4j2
public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        Variable<Piece, PieceContent> variable = new Variable<>(new Piece(new Position(1, 1)), null);
        Variable<Piece, PieceContent> variable2 = new Variable<>(new Piece(new Position(1, 1)), null);

        List<Variable<Piece, PieceContent>> variables = new ArrayList<>();
        variables.add(variable);

        System.out.println(variables.contains(variable2));

        log.info("hello");
        System.out.println("hello");
        solveGame();
    }


    private static void solveGame() throws FileNotFoundException {
        System.out.println("hello");
        BoardConfiguration boardConfiguration = new FileConfigParser("test_case1.txt").parseConfig();
        System.out.println("hello");
        System.out.println(boardConfiguration);
        Board board = new Board(boardConfiguration);
        System.out.println("hello");
        System.out.println(board);
        CSP<Board, Piece, PieceContent> problem = new MagnetPuzzleCSP(board);
        System.out.println("hello");
        CSPSolverAlgorithm<Board, Piece, PieceContent> solver = new BacktrackAlgorithm<>(problem);
        System.out.println("hello");
        solver.solve();
        System.out.println("hello");

        for (int i = 0; i < boardConfiguration.getROW_NUM(); i++) {
            for (int j = 0; j < boardConfiguration.getCOL_NUM(); j++) {
                System.out.println(board.getPieceByPos(i, j) + " = " + board.getPieceByPos(i, j).getContent());
            }
        }

    }

}
