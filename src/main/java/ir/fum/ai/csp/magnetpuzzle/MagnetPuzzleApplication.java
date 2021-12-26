package ir.fum.ai.csp.magnetpuzzle;

import ir.fum.ai.csp.magnetpuzzle.game.Board;
import ir.fum.ai.csp.magnetpuzzle.game.BoardConfiguration;
import ir.fum.ai.csp.magnetpuzzle.graphic.GameBoard;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/

public class MagnetPuzzleApplication extends Application {
    private Board board;
    private Stage mainStage;

    public static void main(String[] args) {
        launch(args); //start of graphic
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = new Stage();
        mainStage.setTitle("Drag Test");
        mainStage.setScene(new Scene(getInputFromUser(), 900, 600));
        mainStage.show();
    }

    private Parent getInputFromUser() {
        Label label = new Label("Drag the input please.");
        Label dropped = new Label("");
        VBox dragTarget = new VBox();
        dragTarget.getChildren().addAll(label, dropped);
        dragTarget.setOnDragOver(event -> {
            if (event.getGestureSource() != dragTarget
                    && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dragTarget.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                dropped.setText(db.getFiles().toString());
                readConfigFromFile(db.getFiles().get(0).getAbsolutePath());
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
        Button btn = new Button("test");
        btn.setOnMouseClicked(event -> {
            mainStage.setScene(new Scene(firstScene(), 900, 600));
        });
        StackPane root = new StackPane();
        root.getChildren().addAll(dragTarget, btn);

        return root;
    }

    private void readConfigFromFile(String pathFile) {
        try {
            File myObj = new File(pathFile);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                int row_num = myReader.nextInt();
                int col_num = myReader.nextInt();
                int[] positiveRows = new int[row_num];
                int[] negativeRows = new int[row_num];
                int[] positiveCols = new int[row_num];
                int[] negativeCols = new int[row_num];
                int[][] tileIds = new int[row_num][col_num];
                BoardConfiguration boardConfig = new BoardConfiguration();
                for (int i = 0; i < row_num; i++)
                    positiveRows[i] = myReader.nextInt();

                for (int i = 0; i < row_num; i++)
                    negativeRows[i] = myReader.nextInt();

                for (int i = 0; i < col_num; i++)
                    positiveCols[i] = myReader.nextInt();

                for (int i = 0; i < col_num; i++)
                    negativeCols[i] = myReader.nextInt();

                for (int i = 0; i < row_num; i++)
                    for (int j = 0; j < col_num; j++)
                        tileIds[i][j] = myReader.nextInt();

                boardConfig.setROW_NUM(row_num);
                boardConfig.setCOL_NUM(col_num);
                boardConfig.setRowPositiveConstraint(positiveRows);
                boardConfig.setRowNegativeConstraints(negativeRows);
                boardConfig.setColPositiveConstraints(positiveCols);
                boardConfig.setColNegativeConstraints(negativeCols);
                boardConfig.setTileIdsForPieces(tileIds);
                board = new Board(boardConfig);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private Parent firstScene() {
        GameBoard graphicalBoard = new GameBoard(board);
        BorderPane root = new BorderPane();
        root.setCenter(graphicalBoard);
        StackPane layout = new StackPane();
        layout.getChildren().add(root);
        return layout;
    }
}
