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
        setValuesOnBoard();

    }

    private void setConstraintsOnBoard() {
        BoardConfiguration config = board.getBoardConfiguration();
        cells[0][0].setLabel("+");
        cells[row - 1][col - 1].setLabel("-");
        cells[0][col - 1].setLabel("#");
        cells[row - 1][0].setLabel("#");

        // setting positive constraints
        for (int i = 1; i < col - 1; i++)
            cells[0][i].setLabel(String.valueOf(config.getColPositiveConstraints()[i - 1]));
        for (int i = 1; i < row - 1; i++)
            cells[i][0].setLabel(String.valueOf(config.getRowPositiveConstraint()[i - 1]));

        // setting negative constraints
        for (int i = 1; i < col - 1; i++)
            cells[row - 1][i].setLabel(String.valueOf(config.getColNegativeConstraints()[i - 1]));
        for (int i = 1; i < row - 1; i++)
            cells[i][col - 1].setLabel(String.valueOf(config.getRowNegativeConstraints()[i - 1]));
    }

    private void setValuesOnBoard() {
        BoardConfiguration config = board.getBoardConfiguration();
        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                cells[i][j].setValue(config.getTileIdsForPieces()[i - 1][j - 1]);
            }
        }
        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                if ((i + 1 < row - 1 || j + 1 < col - 1)
                        && !cells[i][j].isColored()) {
                    int val = cells[i][j].getValue();
                    if (cells[i][j + 1].getValue() == val) {
                        cells[i][j].setStyle("-fx-border-style: solid hidden solid solid;");
                        cells[i][j + 1].setStyle("-fx-border-style: solid solid solid hidden;");
                        cells[i][j].setStyle("-fx-border-color: black thistle black black");
                        cells[i][j + 1].setStyle("-fx-border-color: black black black thistle");
                        cells[i][j].setColored(true);
                        cells[i][j + 1].setColored(true);
                    } else if (cells[i + 1][j].getValue() == val) {
                        cells[i][j].setStyle("-fx-border-style: solid solid hidden solid;");
                        cells[i + 1][j].setStyle("-fx-border-style: hidden solid solid solid;");
                        cells[i][j].setStyle("-fx-border-color: black black thistle black");
                        cells[i + 1][j].setStyle("-fx-border-color: thistle black black black");
                        cells[i][j].setColored(true);
                        cells[i + 1][j].setColored(true);
                    }
                }
            }
        }
    }

    private class Cell extends StackPane {
        private int size;
        private Label label;
        private int value;
        private Rectangle rectangle;
        private boolean isColored;

        private Cell(int size) {
            this.size = size;
            rectangle = new Rectangle(size, size);
            rectangle.setFill(Color.THISTLE);
            this.setStyle("-fx-border-style: solid;");
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

        public void changeColorOfCell(Color color) {
            this.rectangle.setFill(color);
            this.rectangle.setStroke(color);
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public boolean isColored() {
            return isColored;
        }

        public void setColored(boolean colored) {
            isColored = colored;
        }
    }
}
