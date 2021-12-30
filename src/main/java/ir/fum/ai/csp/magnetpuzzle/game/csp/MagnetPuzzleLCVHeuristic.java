package ir.fum.ai.csp.magnetpuzzle.game.csp;

import ir.fum.ai.csp.magnetpuzzle.csp.heuristic.ValuePickerHeuristic;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Constraint;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;
import ir.fum.ai.csp.magnetpuzzle.game.Board;
import ir.fum.ai.csp.magnetpuzzle.game.Piece;
import ir.fum.ai.csp.magnetpuzzle.game.PieceContent;
import ir.fum.ai.csp.magnetpuzzle.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ali Mojahed on 12/30/2021
 * @project magnet-puzzle
 **/
public class MagnetPuzzleLCVHeuristic implements ValuePickerHeuristic<Board, Piece, PieceContent> {


    @Override
    public List<PieceContent> orderValues(CSP<Board, Piece, PieceContent> csp,
                                          Variable<Piece, PieceContent> variable) {
        Map<PieceContent, Integer> valueMark = new HashMap<>();
        for (PieceContent value : variable.getDomain().getLegalValues()) {
            if (csp.canAssignValueToVariable(value, variable.getName())) {
                if (value == PieceContent.None) {
                    int row = variable.getName().getPosition().getX();
                    int col = variable.getName().getPosition().getY();
                    int countNonePoleVariablesInRow = countVariablesWithPoleInRow(csp, row, PieceContent.None);

                    int countNonePoleVariableInCol = countVariablesWithPoleInCol(csp, col, PieceContent.None);

                    int sumOfRowConstraint = csp.getProblem().getBoardConfiguration().getRowPositiveConstraint()[row] +
                            csp.getProblem().getBoardConfiguration().getRowNegativeConstraints()[row];

                    int sumOfColConstraint = csp.getProblem().getBoardConfiguration().getColPositiveConstraints()[col] +
                            csp.getProblem().getBoardConfiguration().getColNegativeConstraints()[col];

                    if (countNonePoleVariableInCol < sumOfColConstraint && countNonePoleVariablesInRow < sumOfRowConstraint) {
                        valueMark.put(PieceContent.None, -1);
                    } else {
                        valueMark.put(PieceContent.None, Integer.MAX_VALUE);
                    }

                } else {
                    int inConsistencyCounter = 0;
                    for (Constraint<Piece, PieceContent, Board> constraint : csp.getConstraintsOfVariable(variable)) {
                        for (Variable<Piece, PieceContent> neighbor : constraint.getVariables()) {
                            if (!variable.isAssigned()) {
                                csp.assignValueToVariable(value, variable.getName());

                                inConsistencyCounter += getNumberOfInConsistentValues(neighbor, csp);

                                csp.undoLastAction();
                            }
                        }
                    }

                    valueMark.put(value, inConsistencyCounter);
                }
            }
        }

        return new ArrayList<>(Util.sortByValue(valueMark).keySet());
    }


    private int countVariablesWithPoleInRow(CSP<Board, Piece, PieceContent> csp, int row, PieceContent pole) {
        int counter = 0;
        for (int j = 0; j < csp.getProblem().getBoardConfiguration().getCOL_NUM(); j++) {
            Variable<Piece, PieceContent> variable = csp.getVariable(csp.getProblem().getPieceByPos(row, j));
            if (variable.isAssigned() && variable.getValue() == pole) {
                counter++;
            }
        }

        return counter;
    }


    private int countVariablesWithPoleInCol(CSP<Board, Piece, PieceContent> csp, int col, PieceContent pole) {
        int counter = 0;
        for (int i = 0; i < csp.getProblem().getBoardConfiguration().getROW_NUM(); i++) {
            Variable<Piece, PieceContent> variable = csp.getVariable(csp.getProblem().getPieceByPos(i, col));
            if (variable.isAssigned() && variable.getValue() == pole) {
                counter++;
            }
        }

        return counter;
    }

    private int getNumberOfInConsistentValues(Variable<Piece, PieceContent> variable,
                                              CSP<Board, Piece, PieceContent> csp) {
        int counter = 0;

        for (PieceContent value : variable.getDomain().getLegalValues()) {
            if (!csp.canAssignValueToVariable(value, variable.getName())) {
                counter++;
            }
        }

        return counter;

    }

}
