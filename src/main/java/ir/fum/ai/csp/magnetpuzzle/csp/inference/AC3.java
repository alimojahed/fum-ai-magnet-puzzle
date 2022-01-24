package ir.fum.ai.csp.magnetpuzzle.csp.inference;

import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Constraint;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/


public class AC3<PROBLEM_T, VAR_T, DOMAIN_T> {
    private CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp;

    public AC3(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp) {
        this.csp = csp;
    }

    private List<Variable<VAR_T, DOMAIN_T>> currentPropagatedVariables;
    private List<Set<DOMAIN_T>> currentPropagatedRemoveValues;
    private Stack<History<VAR_T, DOMAIN_T>> histories = new Stack<>();


    public boolean ac3() {
        Queue<Arc<VAR_T, DOMAIN_T>> arcs = getArcsFromConstraint();

        return ac3(arcs);
    }

    protected boolean ac3(Queue<Arc<VAR_T, DOMAIN_T>> arcs) {
        boolean consistent = true;

        currentPropagatedVariables = new ArrayList<>();
        currentPropagatedRemoveValues = new ArrayList<>();
        System.out.println(arcs);
        while (!arcs.isEmpty() && consistent) {
            Arc<VAR_T, DOMAIN_T> arc = arcs.poll();
//            System.out.println("ac3 alg" + arc.var1 + arc.var2);
            System.out.println(arc);
            if (revise(arc)) {
                currentPropagatedVariables.add(arc.var1);
                if (arc.var1.getDomain().getLegalValues().isEmpty()) {
                    System.out.println("ac3: not consistent");
                    consistent = false;
                    break;
                }
                System.out.println("REVISEDDDD " + getArcsOfRevisedVariable(arc.var1, arc.var2));
                getArcsOfRevisedVariable(arc.var1, arc.var2).stream()
                        .filter(a -> !arcs.contains(a))
                        .forEach(arcs::add);
//                arcs.addAll(getArcsOfRevisedVariable(arc.var1, arc.var2));
            }

//            try {
//                System.out.println(arcs);
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        histories.add(new History<>(currentPropagatedVariables, currentPropagatedRemoveValues));

        return consistent;
    }

    private boolean revise(Arc<VAR_T, DOMAIN_T> arc) {
        Set<DOMAIN_T> removedValues = new HashSet<>();
        if (arc.var1.isAssigned())
            return false;
        boolean revise = false;
        for (DOMAIN_T value : arc.var1.getDomain().getLegalValues()) {

            if (arc.var2.isAssigned()) {
                if (!csp.canAssignValueToVariable(value, arc.var1.getName())) {
                    csp.getVariable(arc.var1.getName()).getDomain().removeValue(value);
                    removedValues.add(value);
                    revise = true;
                }
            } else {
                csp.assignValueToVariable(value, arc.var1.getName());
                boolean isThisValueGood = false;
                for (DOMAIN_T anotherVarValue : arc.var2.getDomain().getLegalValues()) {
                    if (csp.canAssignValueToVariable(anotherVarValue, arc.var2.getName())) {
                        isThisValueGood = true;
                        break;
                    }
                }
                csp.undoLastAction();

                if (!isThisValueGood) {
                    csp.getVariable(arc.var1.getName()).getDomain().removeValue(value);
                    removedValues.add(value);
                    revise = true;
//                break;
                }
            }

        }
        if (revise) {
            currentPropagatedRemoveValues.add(removedValues);
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

    public void undoRemovedVariables() {

        System.out.println("undo ac3");

        if (histories.empty()) {
            return;
        }

        History<VAR_T, DOMAIN_T> h = histories.pop();

        for (int i = 0; i < h.variables.size(); i++) {
            Variable<VAR_T, DOMAIN_T> variable = csp.getVariable(h.variables.get(0).getName());

            for (DOMAIN_T value : h.removedValues.get(i)) {
                variable.getDomain().addValue(value);
            }

        }
    }

    @ToString
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Arc<VAR_T, DOMAIN_T> {
        @EqualsAndHashCode.Include
        Variable<VAR_T, DOMAIN_T> var1;
        @EqualsAndHashCode.Include
        Variable<VAR_T, DOMAIN_T> var2;
    }

    @AllArgsConstructor
    private class History<VAR_T, DOMAIN_T> {
        List<Variable<VAR_T, DOMAIN_T>> variables;
        List<Set<DOMAIN_T>> removedValues;

    }

}
