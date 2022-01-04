package ir.fum.ai.csp.magnetpuzzle.game.csp;

import com.google.common.collect.Sets;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Constraint;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Domain;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;
import ir.fum.ai.csp.magnetpuzzle.game.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Ali Mojahed on 12/24/2021
 * @project magnet-puzzle
 **/

@Log4j2
public class MagnetPuzzleCSP extends CSP<MagnetPuzzleBoard, Pole, PoleContent> {

    private Stack<ActionHistory> actionHistories = new Stack<>();

    public MagnetPuzzleCSP(MagnetPuzzleBoard problem) {
        super(problem);
    }

    @Override
    protected void createVariablesFromProblem() {
        log.info("createVariablesFromProblem");
        for (int i = 0; i < getProblem().getMagnetPuzzleConfiguration().getROW_NUM(); i++) {
            for (int j = 0; j < getProblem().getMagnetPuzzleConfiguration().getCOL_NUM(); j++) {
                getVariables()
                        .add(new Variable<>(getProblem().getPoles()[i][j],
                                Domain.domainFromEnum(PoleContent.class, Arrays.asList(PoleContent.values())))
                        );
            }
        }
    }

    @Override
    protected void createConstraintsFromProblem() {
        log.info("start creating constraints");
        createTilesConstraints();
        log.info("tile constraint created");
        createRowsAndColumnsCounterConstraint();
        log.info("row column constraint created");
        createNeighborsHasOppositePoleConstraints();
        log.info("neighbors constraint created");

        System.out.println(getConstraints().size());

    }

    private void createTilesConstraints() {
        for (Integer tileId : getProblem().getTiles().keySet()) {
            Magnet magnet = getProblem().getTiles().get(tileId);

            Variable<Pole, PoleContent> first = getVariable(magnet.getFirst());
            Variable<Pole, PoleContent> second = getVariable(magnet.getSecond());

            Predicate<MagnetPuzzleBoard> predicate = board -> board.tilePiecesHasValidPoles(magnet);

            Constraint<Pole, PoleContent, MagnetPuzzleBoard> constraint =
                    new Constraint<>(Sets.newHashSet(first, second), predicate);

            getConstraints().add(constraint);
        }
    }

    private void createRowsAndColumnsCounterConstraint() {
        for (int i = 0; i < getProblem().getMagnetPuzzleConfiguration().getROW_NUM(); i++) {
            createRowConstraints(i);
        }

        for (int j = 0; j < getProblem().getMagnetPuzzleConfiguration().getCOL_NUM(); j++) {
            createColConstraints(j);
        }

    }

    private void createColConstraints(int j) {
        Set<Pole> getPiecesInCol = getProblem().getPiecesInCol(j);
        Set<Variable<Pole, PoleContent>> variables = getVariablesFromPieceSet(getPiecesInCol);

        final int colIndex = j;

        Predicate<MagnetPuzzleBoard> positivePredicate = board ->
                board.countMagnetPolesInCol(colIndex, PoleContent.POSITIVE)
                        < board.getMagnetPuzzleConfiguration().getColPositiveConstraints()[colIndex];

        Predicate<MagnetPuzzleBoard> negativePredicate = board ->
                board.countMagnetPolesInCol(colIndex, PoleContent.NEGATIVE)
                        < board.getMagnetPuzzleConfiguration().getColNegativeConstraints()[colIndex];

        getConstraints().add(new Constraint<>(variables, positivePredicate));
        getConstraints().add(new Constraint<>(variables, negativePredicate));

    }


    private Set<Variable<Pole, PoleContent>> getVariablesFromPieceSet(Set<Pole> poles) {
        return poles.stream()
                .map(piece -> {
                    return new Variable<>(
                            piece,
                            Domain.domainFromEnum(PoleContent.class, Arrays.asList(PoleContent.values()))
                    );
                })
                .collect(Collectors.toSet());
    }

    private void createRowConstraints(int i) {
        Set<Pole> getPiecesInRow = getProblem().getPiecesInRow(i);

        Set<Variable<Pole, PoleContent>> variables = getVariablesFromPieceSet(getPiecesInRow);

        final int rowIndex = i;

        Predicate<MagnetPuzzleBoard> positivePredicate = board ->
                board.countMagnetPolesInRow(rowIndex, PoleContent.POSITIVE)
                        == board.getMagnetPuzzleConfiguration().getRowPositiveConstraint()[rowIndex];

        Predicate<MagnetPuzzleBoard> negativePredicate = board ->
                board.countMagnetPolesInRow(rowIndex, PoleContent.NEGATIVE)
                        == board.getMagnetPuzzleConfiguration().getRowNegativeConstraints()[rowIndex];

        getConstraints().add(new Constraint<>(variables, positivePredicate));
        getConstraints().add(new Constraint<>(variables, negativePredicate));
    }

    private void createNeighborsHasOppositePoleConstraints() {

        final int ROWS_NUM = getProblem().getMagnetPuzzleConfiguration().getROW_NUM();
        final int COLS_NUM = getProblem().getMagnetPuzzleConfiguration().getCOL_NUM();

        for (int i = 0; i < ROWS_NUM; i++) {
            for (int j = 0; j < COLS_NUM; j++) {
                if (i + 1 < ROWS_NUM) {
                    createConstraintOnNeighbor(i, j, i + 1, j);
                }

                if (j + 1 < COLS_NUM) {
                    createConstraintOnNeighbor(i, j, i, j + 1);
                }

            }
        }
    }

    private void createConstraintOnNeighbor(int currentX, int currentY, int neighborX, int neighborY) {
        Set<Pole> poles = Sets.newHashSet(getProblem().getPieceByPos(currentX, currentY),
                getProblem().getPieceByPos(neighborX, neighborY));

        Set<Variable<Pole, PoleContent>> variables = getVariablesFromPieceSet(poles);


        Predicate<MagnetPuzzleBoard> predicate = board -> {
            return board.piecesHasOppositePole(board.getPieceByPos(currentX, currentY)
                    , board.getPieceByPos(neighborX, neighborY));
        };

        getConstraints().add(new Constraint<>(variables, predicate));

    }

    @Override
    public void assignValueToVariable(PoleContent value, Pole variable) {

        Pole pole = getProblem().getPoles()[variable.getPosition().getX()][variable.getPosition().getY()];

        actionHistories.push(new ActionHistory(pole, getVariable(pole).getValue(), value));

        getVariable(pole).getDomain().removeValue(value);

        getProblem().setPoleOn(pole.getPosition().getX(), pole.getPosition().getY(), value);
        getVariable(pole).setValue(value);

        getAssignment().add(getVariable(pole));
    }

    @Override
    public boolean canAssignValueToVariable(PoleContent value, Pole variable) {
        return getProblem().canSetPoleOn(variable.getPosition().getX(), variable.getPosition().getY(), value);
    }

    @Override
    public void undoLastAction() {
        if (!actionHistories.empty()) {

            ActionHistory lastAction = actionHistories.pop();

            Position lastActionPosition = lastAction.pole.getPosition();

            getProblem()
                    .getPoles()[lastActionPosition.getX()][lastActionPosition.getY()]
                    .setContent(lastAction.previous == null ? PoleContent.None : lastAction.previous);

            getVariable(lastAction.pole).setValue(lastAction.previous);
            getVariable(lastAction.pole).getDomain().addValue(lastAction.newVal);
            getAssignment().remove(getVariable(lastAction.pole));

        }
    }

    @Override
    public boolean isProblemSolved() {
        return getProblem().isSolved();
    }

    @AllArgsConstructor
    private static class ActionHistory {
        Pole pole;
        PoleContent previous;
        PoleContent newVal;
    }

}
