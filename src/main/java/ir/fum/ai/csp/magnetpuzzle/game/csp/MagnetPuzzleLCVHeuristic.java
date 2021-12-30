package ir.fum.ai.csp.magnetpuzzle.game.csp;

import ir.fum.ai.csp.magnetpuzzle.csp.heuristic.ValuePickerHeuristic;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;
import ir.fum.ai.csp.magnetpuzzle.game.Board;
import ir.fum.ai.csp.magnetpuzzle.game.Piece;
import ir.fum.ai.csp.magnetpuzzle.game.PieceContent;

import java.util.List;

/**
 * @author Ali Mojahed on 12/30/2021
 * @project magnet-puzzle
 **/
public class MagnetPuzzleLCVHeuristic implements ValuePickerHeuristic<Board, Piece, PieceContent> {


    @Override
    public List<PieceContent> orderValues(CSP<Board, Piece, PieceContent> csp,
                                          Variable<Piece, PieceContent> variable) {
        return null;
    }
}
