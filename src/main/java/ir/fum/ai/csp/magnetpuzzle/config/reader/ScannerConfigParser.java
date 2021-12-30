package ir.fum.ai.csp.magnetpuzzle.config.reader;

import ir.fum.ai.csp.magnetpuzzle.game.BoardConfiguration;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/

@Log4j2
public class ScannerConfigParser implements ConfigParser {

    private InputStream inputStream;

    public ScannerConfigParser(InputStream inputStream) {
        log.info("start getting config from stream");
        this.inputStream = inputStream;
    }

    @Override
    public BoardConfiguration parseConfig() {
        Scanner myReader = new Scanner(inputStream);

        BoardConfiguration boardConfig = new BoardConfiguration();
        while (myReader.hasNextLine()) {
            int row_num = myReader.nextInt();
            int col_num = myReader.nextInt();
            int[] positiveRows = new int[row_num];
            int[] negativeRows = new int[row_num];
            int[] positiveCols = new int[row_num];
            int[] negativeCols = new int[row_num];
            int[][] tileIds = new int[row_num][col_num];

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

        }
        myReader.close();

        return boardConfig;
    }
}

