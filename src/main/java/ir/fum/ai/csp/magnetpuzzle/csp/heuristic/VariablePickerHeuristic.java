package ir.fum.ai.csp.magnetpuzzle.csp.heuristic;

import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/


public interface VariablePickerHeuristic<PROBLEM_T,VAR_T, DOMAIN_T> {
    Variable<VAR_T, DOMAIN_T> pickVariable(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp);
}
