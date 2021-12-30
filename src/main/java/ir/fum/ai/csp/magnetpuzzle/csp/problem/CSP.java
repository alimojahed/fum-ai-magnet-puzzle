package ir.fum.ai.csp.magnetpuzzle.csp.problem;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ali Mojahed on 12/24/2021
 * @project magnet-puzzle
 **/

@Getter
@Setter
public abstract class CSP<PROBLEM_T, VAR_T, DOMAIN_T> {

    private PROBLEM_T problem;
    private Set<Variable<VAR_T, DOMAIN_T>> variables = new HashSet<>();
    private Set<Constraint<VAR_T, DOMAIN_T, PROBLEM_T>> constraints = new HashSet<>();
    private List<Variable<VAR_T, DOMAIN_T>> assignment = new ArrayList<>();

    public CSP(PROBLEM_T problem) {
        this.problem = problem;
        createVariablesFromProblem();
        createConstraintsFromProblem();
    }

    protected abstract void createVariablesFromProblem();

    protected abstract void createConstraintsFromProblem();

    public abstract void assignValueToVariable(DOMAIN_T value, VAR_T variable);

    public abstract boolean canAssignValueToVariable(DOMAIN_T value, VAR_T variable);

    public abstract void undoLastAction();

    public abstract boolean isProblemSolved();

    public Variable<VAR_T, DOMAIN_T> getVariable(VAR_T varId) {
        return variables.stream()
                .filter(var -> Objects.equals(var.getName(), varId))
                .findFirst()
                .orElse(null);
    }

    public Set<Constraint<VAR_T, DOMAIN_T, PROBLEM_T>> getConstraintsOfVariable(Variable<VAR_T, DOMAIN_T> variable) {
        return constraints.stream()
                .filter(constraint -> constraint.getVariables().contains(variable))
                .collect(Collectors.toSet());
    }


    public Set<Constraint<VAR_T, DOMAIN_T, PROBLEM_T>> getConstraintsOfOrder(int order) {
        return constraints.stream()
                .filter(constraint -> constraint.getVariables().size() == order)
                .collect(Collectors.toSet());
    }

    public Set<Constraint<VAR_T, DOMAIN_T, PROBLEM_T>> getConstraintWithOrderAndVar(int order,
                                                                                    Variable<VAR_T, DOMAIN_T> variable){
        return constraints.stream()
                .filter(constraint -> constraint.getVariables().size() == order && constraint.getVariables().contains(variable))
                .collect(Collectors.toSet());
    }

}
