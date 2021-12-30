package ir.fum.ai.csp.magnetpuzzle.csp.heuristic;


import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Constraint;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;

import java.util.*;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/
public class DegreeHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> implements VariablePickerHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> {
    @Override
    public Variable<VAR_T, DOMAIN_T> pickVariable(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp) {
        Set<Integer> variableCounterDuplicationTracker = new HashSet<>();

        Map<Variable<VAR_T, DOMAIN_T>, Integer> variableCounter = new HashMap<>();
        for (Variable<VAR_T, DOMAIN_T> variable : csp.getVariables()) {
            for (Constraint<VAR_T, DOMAIN_T, ?> constraint : csp.getConstraints()) {
                int counter = 0;
                if (constraint.getVariables().contains(variable)) {
                    counter ++;
                }

                variableCounter.put(variable, counter);
                variableCounterDuplicationTracker.add(counter);
            }
        }


        Variable<VAR_T, DOMAIN_T> var = null;
        if ((variableCounterDuplicationTracker.size() <= 1 && csp.getVariables().size() <= 1)) {
            var = variableCounter.entrySet()
                    .stream()
                    .max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
        }

        return var;
    }
}
