package ir.fum.ai.csp.magnetpuzzle;

import ir.fum.ai.csp.magnetpuzzle.config.reader.FileConfigParser;
import ir.fum.ai.csp.magnetpuzzle.csp.algorithm.BacktrackAlgorithm;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.solver.CSPSolverAlgorithm;
import ir.fum.ai.csp.magnetpuzzle.game.Board;
import ir.fum.ai.csp.magnetpuzzle.game.BoardConfiguration;
import ir.fum.ai.csp.magnetpuzzle.game.Piece;
import ir.fum.ai.csp.magnetpuzzle.game.PieceContent;
import ir.fum.ai.csp.magnetpuzzle.game.csp.MagnetPuzzleCSP;
import ir.fum.ai.csp.magnetpuzzle.util.Util;
import lombok.extern.log4j.Log4j2;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ali Mojahed on 12/30/2021
 * @project magnet-puzzle
 **/

@Log4j2
public class Test {
    public static void main(String[] args) throws FileNotFoundException {


//        solveGame();
        Map<String, Integer> map = new HashMap<>();
        map.put("hello", 0);
        map.put("a", -1);
        map.put("lkshd", 100);
        map.put("lksh123d", 1234);

        System.out.println(Util.sortByValue(map).values());

        System.out.println(Arrays.asList(100, -1, 0, 9).stream().sorted().collect(Collectors.toList()));
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
