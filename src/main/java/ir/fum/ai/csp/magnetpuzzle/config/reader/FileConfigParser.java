package ir.fum.ai.csp.magnetpuzzle.config.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/

public class FileConfigParser extends ScannerConfigParser {


    public FileConfigParser(String filename) throws FileNotFoundException {
        super(new FileInputStream(new File(filename)));
    }

}
