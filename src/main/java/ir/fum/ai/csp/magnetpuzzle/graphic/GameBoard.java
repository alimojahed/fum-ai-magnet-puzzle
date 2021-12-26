package ir.fum.ai.csp.magnetpuzzle.graphic;

import ir.fum.ai.csp.magnetpuzzle.game.Board;
import ir.fum.ai.csp.magnetpuzzle.game.BoardConfiguration;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Ali Mojahed on 12/26/2021
 * @project magnet-puzzle
 **/
public class GameBoard extends StackPane {
    private static int CELL_SIZE = 50;
    private Board board;
    private int col, row;
    private Cell[][] cells;

    public GameBoard(Board board) {
        this.board = board;
        this.row = board.getBoardConfiguration().getROW_NUM() + 2;
        this.col = board.getBoardConfiguration().getCOL_NUM() + 2;
        cells = new Cell[this.row][this.col];
        VBox boardBox = new VBox();
        for (int i = 0; i < this.row; i++) {
            HBox eachRow = new HBox();
            for (int j = 0; j < this.col; j++) {
                Cell cell = new Cell(CELL_SIZE);
                cells[i][j] = cell;
                eachRow.getChildren().add(cell);
            }
            boardBox.getChildren().add(eachRow);
        }
        getChildren().add(boardBox);
        setConstraintsOnBoard();
    }

    private void setConstraintsOnBoard() {
        BoardConfiguration config = board.getBoardConfiguration();
        cells[0][0].setLabel("+");
        cells[row - 1][col - 1].setLabel("-");

        // setting positive constraints
        for (int i = 1; i < col - 1; i++)
            cells[0][i].setLabel(String.valueOf(config.getColPositiveConstraints()[i - 1]));
        for (int i = 1; i < row - 1; i++)
            cells[i][0].setLabel(String.valueOf(config.getRowPositiveConstraint()[i - 1]));

        // setting negative constraints
        for (int i = 1; i < col - 1; i++)
            cells[col - 1][i].setLabel(String.valueOf(config.getColNegativeConstraints()[i - 1]));
        for (int i = row - 1; i < col - 1; i++)
            cells[i][col - 1].setLabel(String.valueOf(config.getColNegativeConstraints()[i - 1]));
    }

    private class Cell extends StackPane {
        private int size;
        private Label label;
        private Rectangle rectangle;

        private Cell(int size) {
            this.size = size;
            rectangle = new Rectangle(size, size);
            rectangle.setFill(Color.THISTLE);
            rectangle.setStyle("-fx-border-color: #000;");
            label = new Label("");
            getChildren().addAll(rectangle, label);
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public Label getLabel() {
            return label;
        }

        public void setLabel(String text) {
            this.label.setText(text);
        }

        public Rectangle getRectangle() {
            return rectangle;
        }

        public void setRectangle(Rectangle rectangle) {
            this.rectangle = rectangle;
        }
    }
}
