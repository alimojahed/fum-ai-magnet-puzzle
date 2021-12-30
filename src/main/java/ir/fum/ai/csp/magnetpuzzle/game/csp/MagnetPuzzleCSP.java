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
public class MagnetPuzzleCSP extends CSP<Board, Piece, PieceContent> {

    private Stack<ActionHistory> actionHistories = new Stack<>();

    public MagnetPuzzleCSP(Board problem) {
        super(problem);
    }

    @Override
    protected void createVariablesFromProblem() {
        log.info("createVariablesFromProblem");
        for (int i = 0; i < getProblem().getBoardConfiguration().getROW_NUM(); i++) {
            for (int j = 0; j < getProblem().getBoardConfiguration().getCOL_NUM(); j++) {
                getVariables()
                        .add(new Variable<>(getProblem().getPieces()[i][j],
                                Domain.domainFromEnum(PieceContent.class, Arrays.asList(PieceContent.values())))
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
            Tile tile = getProblem().getTiles().get(tileId);

            Variable<Piece, PieceContent> first = getVariable(tile.getFirst());
            Variable<Piece, PieceContent> second = getVariable(tile.getSecond());

            Predicate<Board> predicate = board -> board.tilePiecesHasValidPoles(tile);

            Constraint<Piece, PieceContent, Board> constraint =
                    new Constraint<>(Sets.newHashSet(first, second), predicate);

            getConstraints().add(constraint);
        }
    }

    private void createRowsAndColumnsCounterConstraint() {
        for (int i = 0; i < getProblem().getBoardConfiguration().getROW_NUM(); i++) {
            createRowConstraints(i);
        }

        for (int j = 0; j < getProblem().getBoardConfiguration().getCOL_NUM(); j++) {
            createColConstraints(j);
        }

    }

    private void createColConstraints(int j) {
        Set<Piece> getPiecesInCol = getProblem().getPiecesInCol(j);
        Set<Variable<Piece, PieceContent>> variables = getVariablesFromPieceSet(getPiecesInCol);

        final int colIndex = j;

        Predicate<Board> positivePredicate = board ->
                board.countMagnetPolesInCol(colIndex, PieceContent.POSITIVE)
                        < board.getBoardConfiguration().getColPositiveConstraints()[colIndex];

        Predicate<Board> negativePredicate = board ->
                board.countMagnetPolesInCol(colIndex, PieceContent.NEGATIVE)
                        < board.getBoardConfiguration().getColNegativeConstraints()[colIndex];

        getConstraints().add(new Constraint<>(variables, positivePredicate));
        getConstraints().add(new Constraint<>(variables, negativePredicate));

    }


    private Set<Variable<Piece, PieceContent>> getVariablesFromPieceSet(Set<Piece> pieces) {
        return pieces.stream()
                .map(piece -> {
                    return new Variable<>(
                            piece,
                            Domain.domainFromEnum(PieceContent.class, Arrays.asList(PieceContent.values()))
                    );
                })
                .collect(Collectors.toSet());
    }

    private void createRowConstraints(int i) {
        Set<Piece> getPiecesInRow = getProblem().getPiecesInRow(i);

        Set<Variable<Piece, PieceContent>> variables = getVariablesFromPieceSet(getPiecesInRow);

        final int rowIndex = i;

        Predicate<Board> positivePredicate = board ->
                board.countMagnetPolesInRow(rowIndex, PieceContent.POSITIVE)
                        == board.getBoardConfiguration().getRowPositiveConstraint()[rowIndex];

        Predicate<Board> negativePredicate = board ->
                board.countMagnetPolesInRow(rowIndex, PieceContent.NEGATIVE)
                        == board.getBoardConfiguration().getRowNegativeConstraints()[rowIndex];

        getConstraints().add(new Constraint<>(variables, positivePredicate));
        getConstraints().add(new Constraint<>(variables, negativePredicate));
    }

    private void createNeighborsHasOppositePoleConstraints() {

        final int ROWS_NUM = getProblem().getBoardConfiguration().getROW_NUM();
        final int COLS_NUM = getProblem().getBoardConfiguration().getCOL_NUM();

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
        Set<Piece> pieces = Sets.newHashSet(getProblem().getPieceByPos(currentX, currentY),
                getProblem().getPieceByPos(neighborX, neighborY));

        Set<Variable<Piece, PieceContent>> variables = getVariablesFromPieceSet(pieces);


        Predicate<Board> predicate = board -> {
            return board.piecesHasOppositePole(board.getPieceByPos(currentX, currentY)
                    , board.getPieceByPos(neighborX, neighborY));
        };

        getConstraints().add(new Constraint<>(variables, predicate));

    }

    @Override
    public void assignValueToVariable(PieceContent value, Piece variable) {
        Piece piece = getProblem().getPieces()[variable.getPosition().getX()][variable.getPosition().getX()];

        actionHistories.push(new ActionHistory(piece, getVariable(piece).getValue(), value));

        if (getVariable(piece).getValue() != null) {
            getVariable(piece).getDomain().removeValue(value);
        }

        getProblem().setPoleOn(piece.getPosition().getX(), piece.getPosition().getY(), value);
        getVariable(piece).setValue(value);

        getAssignment().add(getVariable(piece));
    }

    @Override
    public boolean canAssignValueToVariable(PieceContent value, Piece variable) {
        return getProblem().canSetPoleOn(variable.getPosition().getX(), variable.getPosition().getY(), value);
    }

    @Override
    public void undoLastAction() {
        if (!actionHistories.empty()) {

            ActionHistory lastAction = actionHistories.pop();

            Position lastActionPosition = lastAction.piece.getPosition();

            getProblem()
                    .getPieces()[lastActionPosition.getX()][lastActionPosition.getY()]
                    .setContent(lastAction.previous == null ? PieceContent.None : lastAction.previous);

            getVariable(lastAction.piece).setValue(lastAction.previous);
            getVariable(lastAction.piece).getDomain().addValue(lastAction.newVal);
            getAssignment().remove(getVariable(lastAction.piece));

        }
    }

    @Override
    public boolean isProblemSolved() {
        return getProblem().isSolved();
    }

    @AllArgsConstructor
    private static class ActionHistory {
        Piece piece;
        PieceContent previous;
        PieceContent newVal;
    }

}
