package ir.fum.ai.csp.magnetpuzzle.config.reader;

import ir.fum.ai.csp.magnetpuzzle.game.BoardConfiguration;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/
public class CommandLineConfigParser extends ScannerConfigParser {

    public CommandLineConfigParser() {
        super(System.in);
    }

    @Override
    public BoardConfiguration parseConfig() {
        return super.parseConfig();
    }
}
