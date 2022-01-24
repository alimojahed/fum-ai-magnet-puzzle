package ir.fum.ai.csp.magnetpuzzle.game.csp;

import ir.fum.ai.csp.magnetpuzzle.csp.heuristic.ValuePickerHeuristic;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Constraint;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;
import ir.fum.ai.csp.magnetpuzzle.game.MagnetPuzzleBoard;
import ir.fum.ai.csp.magnetpuzzle.game.Pole;
import ir.fum.ai.csp.magnetpuzzle.game.PoleContent;
import ir.fum.ai.csp.magnetpuzzle.util.Util;

import java.util.*;

/**
 * @author Ali Mojahed on 12/30/2021
 * @project magnet-puzzle
 **/
public class MagnetPuzzleLCVHeuristic implements ValuePickerHeuristic<MagnetPuzzleBoard, Pole, PoleContent> {


    @Override
    public List<PoleContent> orderValues(CSP<MagnetPuzzleBoard, Pole, PoleContent> csp,
                                         Variable<Pole, PoleContent> variable) {
        Map<PoleContent, Integer> valueMark = new HashMap<>();
        for (PoleContent value : variable.getDomain().getLegalValues()) {
            if (csp.canAssignValueToVariable(value, variable.getName())) {
                if (value == PoleContent.None) {
                    int row = variable.getName().getPosition().getX();
                    int col = variable.getName().getPosition().getY();

                    int totalCol = csp.getProblem().getMagnetPuzzleConfiguration().getCOL_NUM();
                    int totalRow = csp.getProblem().getMagnetPuzzleConfiguration().getROW_NUM();

                    int countNonePoleVariablesInRow = countVariablesWithPoleInRow(csp, row, PoleContent.None);

                    int countNonePoleVariableInCol = countVariablesWithPoleInCol(csp, col, PoleContent.None);

                    int sumOfRowConstraint = csp.getProblem().getMagnetPuzzleConfiguration().getRowPositiveConstraint()[row] +
                            csp.getProblem().getMagnetPuzzleConfiguration().getRowNegativeConstraints()[row];

                    int sumOfColConstraint = csp.getProblem().getMagnetPuzzleConfiguration().getColPositiveConstraints()[col] +
                            csp.getProblem().getMagnetPuzzleConfiguration().getColNegativeConstraints()[col];

                    if (countNonePoleVariableInCol < totalCol - sumOfColConstraint && countNonePoleVariablesInRow < totalRow - sumOfRowConstraint) {
                        valueMark.put(PoleContent.None, Integer.MAX_VALUE);
                    }

                } else {
                    int consistencyCounter = 0;
                    for (Constraint<Pole, PoleContent, MagnetPuzzleBoard> constraint : csp.getConstraintsOfVariable(variable)) {
                        for (Variable<Pole, PoleContent> neighbor : constraint.getVariables()) {
                            if (!variable.isAssigned()) {
                                csp.assignValueToVariable(value, variable.getName());

                                consistencyCounter += getNumberOConsistentValues(neighbor, csp);

                                csp.undoLastAction();
                            }
                        }
                    }

                    valueMark.put(value, consistencyCounter);

                }
            }
        }
//        System.out.println(variable + " " + new ArrayList<>(Util.sortByValue(valueMark).keySet()));
        List<PoleContent> orderedValues = new ArrayList<>(Util.sortByValue(valueMark).keySet());
        Collections.reverse(orderedValues);
        return orderedValues;
    }


    private int countVariablesWithPoleInRow(CSP<MagnetPuzzleBoard, Pole, PoleContent> csp, int row, PoleContent pole) {
        int counter = 0;
        for (int j = 0; j < csp.getProblem().getMagnetPuzzleConfiguration().getCOL_NUM(); j++) {
            Variable<Pole, PoleContent> variable = csp.getVariable(csp.getProblem().getPieceByPos(row, j));
            if (variable.isAssigned() && variable.getValue() == pole) {
                counter++;
            }
        }

        return counter;
    }


    private int countVariablesWithPoleInCol(CSP<MagnetPuzzleBoard, Pole, PoleContent> csp, int col, PoleContent pole) {
        int counter = 0;
        for (int i = 0; i < csp.getProblem().getMagnetPuzzleConfiguration().getROW_NUM(); i++) {
            Variable<Pole, PoleContent> variable = csp.getVariable(csp.getProblem().getPieceByPos(i, col));
            if (variable.isAssigned() && variable.getValue() == pole) {
                counter++;
            }
        }

        return counter;
    }

    private int getNumberOConsistentValues(Variable<Pole, PoleContent> variable,
                                           CSP<MagnetPuzzleBoard, Pole, PoleContent> csp) {
        int counter = 0;

        for (PoleContent value : variable.getDomain().getLegalValues()) {
            if (csp.canAssignValueToVariable(value, variable.getName())) {
                counter++;
            }
        }

        return counter;

    }

}
