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
import ir.fum.ai.csp.magnetpuzzle.graphic.GameBoard;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.FileNotFoundException;

/**
 * @author Ali Mojahed on 12/21/2021
 * @project magnet-puzzle
 **/
@Log4j2
public class MagnetPuzzleApplication extends Application {
    private Board board;
    private Stage mainStage;

    public static void main(String[] args) throws FileNotFoundException {
//        launch(args); //start of graphic
        log.info("start game");
        solveGame();

    }

    private static void solveGame() throws FileNotFoundException {
        BoardConfiguration boardConfiguration = new FileConfigParser("input1_method2.txt").parseConfig();

        Board board = new Board(boardConfiguration);

        CSP<Board, Piece, PieceContent> problem = new MagnetPuzzleCSP(board);

        CSPSolverAlgorithm<Board, Piece, PieceContent> solver = new BacktrackAlgorithm<>(problem);

        solver.solve();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = new Stage();
        mainStage.setTitle("Drag Test");
        mainStage.setScene(new Scene(getInputFromUser(), 900, 600));
        mainStage.show();
    }

    private Parent getInputFromUser() {
        // labels
        Label label = new Label("Drag the input please.");
        Label dropped = new Label("");
        Label title = new Label("Magnet Puzzle");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
        title.setTextFill(Color.WHEAT);
        title.setPadding(new Insets(100, 20, 50, 20));

        // label box
        VBox dragTarget = new VBox();
        dragTarget.setAlignment(Pos.CENTER);
        dragTarget.getChildren().addAll(title, label, dropped);
        dragTarget.setOnDragOver(event -> {
            if (event.getGestureSource() != dragTarget
                    && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        Button btn = new Button("show board");
        btn.setDisable(true);

        dragTarget.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                dropped.setText(db.getFiles().toString());
                try {
                    readConfigFromFile(db.getFiles().get(0).getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                success = true;
                btn.setDisable(false);
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });

        // button
        btn.setOnMouseClicked(event ->
                mainStage.setScene(new Scene(boardScene(), 900, 600)));
        HBox btnBox = new HBox();
        btnBox.getChildren().add(btn);
        btnBox.setAlignment(Pos.CENTER);

        // layout
        BorderPane root = new BorderPane();
        root.setTop(dragTarget);
        root.setCenter(btnBox);
        StackPane layout = new StackPane();
        layout.getChildren().add(root);
        return layout;
    }

    private void readConfigFromFile(String pathFile) throws FileNotFoundException {
        board = new Board(new FileConfigParser(pathFile).parseConfig());
    }

    private Parent boardScene() {
        // board
        GameBoard graphicalBoard = new GameBoard(board);
        HBox boardBox = new HBox();
        boardBox.getChildren().add(graphicalBoard);
        boardBox.setAlignment(Pos.CENTER);
        boardBox.setPadding(new Insets(100, 20, 20, 20));

        // button
        Button btn = new Button("solve");
        btn.setOnAction(event -> {

        });
        HBox btnBox = new HBox();
        btnBox.getChildren().add(btn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(50));

        BorderPane root = new BorderPane();
        root.setCenter(boardBox);
        root.setBottom(btnBox);

        StackPane layout = new StackPane();
        layout.getChildren().add(root);
        return layout;
    }
}
