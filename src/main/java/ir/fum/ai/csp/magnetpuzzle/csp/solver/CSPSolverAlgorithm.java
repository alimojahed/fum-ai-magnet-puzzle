package ir.fum.ai.csp.magnetpuzzle.csp.solver;

import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;

/**
 * @author Ali Mojahed on 12/29/2021
 * @project magnet-puzzle
 **/
public interface CSPSolverAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> {
    void solve();

    Variable<VAR_T, DOMAIN_T> pickUnAssignedVariable();

    DOMAIN_T pickValue();
}
