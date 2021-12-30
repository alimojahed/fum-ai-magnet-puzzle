package ir.fum.ai.csp.magnetpuzzle.csp.algorithm;

import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Constraint;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;
import ir.fum.ai.csp.magnetpuzzle.csp.solver.CSPSolverAlgorithm;
import lombok.extern.log4j.Log4j2;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/

@Log4j2
public class BacktrackAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> implements CSPSolverAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> {
    private CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp;
    private boolean done = false;

    public BacktrackAlgorithm(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp) {
        this.csp = csp;
    }

    @Override
    public void solve() {
        solveLevel(0);
    }

    protected void solveLevel(int level) {
        System.out.println(csp.getAssignment());
        if (csp.getAssignment().size() == csp.getVariables().size()) {
            if (csp.isProblemSolved()) {
                done = true;
            }
            return;
        }

        Variable<VAR_T, DOMAIN_T> variable = pickUnAssignedVariable();

        for (DOMAIN_T value : variable.getDomain().getLegalValues()) {
            if (done)
                break;
            boolean canAssignThisVariable = true;

            csp.assignValueToVariable(value, variable.getName());

            for (Constraint<VAR_T, DOMAIN_T, PROBLEM_T> constraint : csp.getConstraintsOfVariable(variable)) {
                if (constraint.isAllVariableAreAssigned(csp.getProblem())
                        && !constraint.isConstraintMet(csp.getProblem())) {
                    canAssignThisVariable = false;
                }
            }

            if (canAssignThisVariable) {
                solveLevel(level + 1);
            }

            if (!canAssignThisVariable || !done) {
                csp.undoLastAction();
            }

        }

    }

    @Override
    public Variable<VAR_T, DOMAIN_T> pickUnAssignedVariable() {
        //because we using a hash set the ordering of elements is by random and according to a hash table
        Variable<VAR_T, DOMAIN_T> v = csp.getVariables().stream()
                .filter(variable -> !csp.getAssignment().contains(variable))
                .findFirst()
                .orElse(null);

        System.out.println(csp.getAssignment().contains(v));
        System.out.println(v);
        System.out.println(csp.getAssignment());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public DOMAIN_T pickValue() {
        //not used in this algorithm
        return null;
    }


}
