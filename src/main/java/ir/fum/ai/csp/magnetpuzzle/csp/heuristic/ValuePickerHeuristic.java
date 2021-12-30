package ir.fum.ai.csp.magnetpuzzle.csp.heuristic;

import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;

import java.util.List;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/
public interface ValuePickerHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> {
    List<DOMAIN_T> orderValues(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp, Variable<VAR_T, DOMAIN_T> variable);


}
