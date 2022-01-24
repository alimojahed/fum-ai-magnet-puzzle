package ir.fum.ai.csp.magnetpuzzle;

import ir.fum.ai.csp.magnetpuzzle.config.reader.FileConfigParser;
import ir.fum.ai.csp.magnetpuzzle.csp.algorithm.HeuristicBacktrackAlgorithm;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.solver.CSPSolverAlgorithm;
import ir.fum.ai.csp.magnetpuzzle.game.MagnetPuzzleBoard;
import ir.fum.ai.csp.magnetpuzzle.game.MagnetPuzzleConfiguration;
import ir.fum.ai.csp.magnetpuzzle.game.Pole;
import ir.fum.ai.csp.magnetpuzzle.game.PoleContent;
import ir.fum.ai.csp.magnetpuzzle.game.csp.MagnetPuzzleCSP;
import ir.fum.ai.csp.magnetpuzzle.game.csp.MagnetPuzzleLCVHeuristic;
import ir.fum.ai.csp.magnetpuzzle.game.visualizer.CommandLineVisualizer;
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

        MagnetPuzzleConfiguration magnetPuzzleConfiguration = new FileConfigParser("input1_method2.txt").parseConfig();

        System.out.println(magnetPuzzleConfiguration);
        MagnetPuzzleBoard magnetPuzzleBoard = new MagnetPuzzleBoard(magnetPuzzleConfiguration);

        System.out.println(magnetPuzzleBoard);
        CSP<MagnetPuzzleBoard, Pole, PoleContent> problem = new MagnetPuzzleCSP(magnetPuzzleBoard);

        CSPSolverAlgorithm<MagnetPuzzleBoard, Pole, PoleContent> solver =
                new HeuristicBacktrackAlgorithm<>(problem, new MagnetPuzzleLCVHeuristic());

        solver.solve();

        long endTime = System.nanoTime();

        for (int i = 0; i < magnetPuzzleConfiguration.getROW_NUM(); i++) {
            for (int j = 0; j < magnetPuzzleConfiguration.getCOL_NUM(); j++) {
                System.out.println(magnetPuzzleBoard.getPieceByPos(i, j) + " = " + magnetPuzzleBoard.getPieceByPos(i, j).getContent());
            }
        }

        System.out.println("executed in " + (endTime - startTime) / 1e9);
        CommandLineVisualizer visualizer = new CommandLineVisualizer();
        visualizer.visualizeBoard(magnetPuzzleBoard);
    }

}
