package ir.fum.ai.csp.magnetpuzzle.csp.inference;

import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Constraint;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;
import lombok.AllArgsConstructor;

import java.util.*;

/**
 * @author Ali Mojahed on 12/30/2021
 * @project magnet-puzzle
 **/

public class ForwardChecking<PROBLEM_T, VAR_T, DOMAIN_T> extends InferenceAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> {

    private Stack<InferenceHistory> inferenceHistories = new Stack<>();

    public ForwardChecking(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp) {
        super(csp);
    }

    @Override
    public boolean inference(Variable<VAR_T, DOMAIN_T> variable) {

        List<Variable<VAR_T, DOMAIN_T>> variableHistory = new ArrayList<>();
        List<Set<DOMAIN_T>> removedValueHistory = new ArrayList<>();

        for (Constraint<VAR_T, DOMAIN_T, PROBLEM_T> constraint : csp.getConstraintsOfVariable(variable)) {
            for (Variable<VAR_T, DOMAIN_T> neighbor : constraint.getVariables()) {

                if (!neighbor.equals(variable)) {

                    variableHistory.add(neighbor);
                    Set<DOMAIN_T> removedValues = new HashSet<>();

                    for (DOMAIN_T value : neighbor.getDomain().getLegalValues()) {
                        if (!csp.canAssignValueToVariable(value, neighbor.getName())) {
                            neighbor.getDomain().removeValue(value);
                            removedValues.add(value);
                        }
                    }

                    removedValueHistory.add(removedValues);

                    if (neighbor.getDomain().getLegalValues().isEmpty()) {
                        inferenceHistories.push(new InferenceHistory(variableHistory, removedValueHistory));
                        return false;
                    }
                }
            }
        }

        inferenceHistories.push(new InferenceHistory(variableHistory, removedValueHistory));

        return true;

    }

    @Override
    public void undoInference() {
        if (inferenceHistories.empty()) {
            return;
        }

        InferenceHistory lastInference = inferenceHistories.pop();

        for (int i = 0; i < lastInference.variables.size(); i++) {
            Variable<VAR_T, DOMAIN_T> variable = csp.getVariable(lastInference.variables.get(i).getName());

            for (DOMAIN_T value : lastInference.removedValues.get(i)) {
                variable.getDomain().addValue(value);
            }

        }

    }


    @AllArgsConstructor
    private class InferenceHistory {
        List<Variable<VAR_T, DOMAIN_T>> variables;
        List<Set<DOMAIN_T>> removedValues;
    }

}
