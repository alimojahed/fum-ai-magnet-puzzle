package ir.fum.ai.csp.magnetpuzzle.csp.inference;

import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Constraint;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/


public class AC3<PROBLEM_T, VAR_T, DOMAIN_T> {
    private CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp;

    public AC3(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp) {
        this.csp = csp;
    }


    public boolean ac3() {
        Queue<Arc<VAR_T, DOMAIN_T>> arcs = getArcsFromConstraint();

        boolean inConsistent = false;
        while (!arcs.isEmpty() && !inConsistent) {
            Arc<VAR_T, DOMAIN_T> arc = arcs.poll();

            if (revise(arc)) {

                if (arc.var1.getDomain().getLegalValues().isEmpty()) {
                    inConsistent = true;
                }
                arcs.addAll(getArcsOfRevisedVariable(arc.var1, arc.var2));
            }
        }

        return inConsistent;
    }

    private boolean revise(Arc<VAR_T, DOMAIN_T> arc) {
        boolean revise = false;
        for (DOMAIN_T value : arc.var1.getDomain().getLegalValues()) {
            csp.assignValueToVariable(value, arc.var1.getName());
            boolean isThisValueGood = false;
            for (DOMAIN_T anotherVarValue : arc.var2.getDomain().getLegalValues()) {
                if (csp.canAssignValueToVariable(anotherVarValue, arc.var2.getName())) {
                    isThisValueGood = true;
                }
            }
            csp.undoLastAction();

            if (!isThisValueGood) {
                csp.getVariable(arc.var1.getName()).getDomain().removeValue(value);
                revise = true;
            }

        }
        return revise;
    }


    private Queue<Arc<VAR_T, DOMAIN_T>> getArcsFromConstraint() {
        Queue<Arc<VAR_T, DOMAIN_T>> queue = new LinkedList<>();

        for (Constraint<VAR_T, DOMAIN_T, PROBLEM_T> constraint : csp.getConstraintsOfOrder(2)) {
            List<Variable<VAR_T, DOMAIN_T>> variables = new ArrayList<>(constraint.getVariables());
            queue.add(new Arc<>(variables.get(0), variables.get(1)));
        }

        return queue;
    }

    private List<Arc<VAR_T, DOMAIN_T>> getArcsOfRevisedVariable(Variable<VAR_T, DOMAIN_T> origin,
                                                                Variable<VAR_T, DOMAIN_T> except) {
        List<Arc<VAR_T, DOMAIN_T>> arcsFromVariable = new ArrayList<>();

        for (Constraint<VAR_T, DOMAIN_T, PROBLEM_T> constraint : csp.getConstraintWithOrderAndVar(2, origin)) {
            if (!constraint.getVariables().contains(except)) {
                List<Variable<VAR_T, DOMAIN_T>> variables = new ArrayList<>(constraint.getVariables());
                Variable<VAR_T, DOMAIN_T> first = variables.get(0).equals(origin) ? variables.get(1) : variables.get(0);
                arcsFromVariable.add(new Arc<>(first, origin));
            }
        }

        return arcsFromVariable;
    }

    @AllArgsConstructor
    private static class Arc<VAR_T, DOMAIN_T> {
        Variable<VAR_T, DOMAIN_T> var1;
        Variable<VAR_T, DOMAIN_T> var2;
    }

}
