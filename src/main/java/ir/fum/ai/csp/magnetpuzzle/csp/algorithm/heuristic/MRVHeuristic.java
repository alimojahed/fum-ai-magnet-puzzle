package ir.fum.ai.csp.magnetpuzzle.csp.algorithm.heuristic;

import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;

import java.util.*;

/**
 * @author Mahya Ehsanimehr on 12/26/2021
 * @project magnet-puzzle
 **/
public class MRVHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> implements VariablePickerHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> {
    @Override
    public Variable<VAR_T, DOMAIN_T> pickVariable(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp) {
        Map<Variable<VAR_T, DOMAIN_T>, Integer> legalValuesCounter = new HashMap<>();
        Set<Integer> legalValuesDuplicationTracker = new HashSet<>();
        for (Variable<VAR_T, DOMAIN_T> variable : csp.getVariables()) {
            legalValuesCounter.put(variable, variable.getDomain().getLegalValues().size());
            legalValuesDuplicationTracker.add(variable.getDomain().getLegalValues().size());
        }

        Variable<VAR_T, DOMAIN_T> variable = null;

        if (legalValuesDuplicationTracker.size() > 1 || csp.getVariables().size() == 1) {
            variable = legalValuesCounter.entrySet().stream()
                    .min(Comparator.comparing(Map.Entry::getValue))
                    .get()
                    .getKey();
        }

        return variable;
    }
}
