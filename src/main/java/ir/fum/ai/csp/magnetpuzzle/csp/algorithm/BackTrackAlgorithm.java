package ir.fum.ai.csp.magnetpuzzle.csp.algorithm;

import ir.fum.ai.csp.magnetpuzzle.csp.algorithm.heuristic.ValuePickerHeuristic;
import ir.fum.ai.csp.magnetpuzzle.csp.algorithm.heuristic.VariablePickerHeuristic;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;

import java.util.List;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/


public class BackTrackAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> {
    private CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp;
    private List<VariablePickerHeuristic<PROBLEM_T, VAR_T, DOMAIN_T>> selectVariableHeuristics;
    private ValuePickerHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> valuePickerHeuristic;

    public BackTrackAlgorithm(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp,
                              List<VariablePickerHeuristic<PROBLEM_T, VAR_T, DOMAIN_T>> selectVariableHeuristics,
                              ValuePickerHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> valuePickerHeuristic) {
        this.csp = csp;
        this.selectVariableHeuristics = selectVariableHeuristics;
        this.valuePickerHeuristic = valuePickerHeuristic;
    }

    public void solveCSP() {
        
    }

}
