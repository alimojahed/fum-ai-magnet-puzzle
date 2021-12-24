package ir.fum.ai.csp.magnetpuzzle.csp.problem;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Ali Mojahed on 12/24/2021
 * @project magnet-puzzle
 **/

@Getter
@Setter
public abstract class CSP<PROBLEM_T, VAR_T, DOMAIN_T> {

    private PROBLEM_T problem;
    private Set<Variable<VAR_T, DOMAIN_T>> variables = new HashSet<>();
    private Set<Constraint<VAR_T, DOMAIN_T, ?>> constraints = new HashSet<>();

    public CSP(PROBLEM_T problem) {
        this.problem = problem;
        createVariablesFromProblem();
        createConstraintsFromProblem();
    }

    protected abstract void createVariablesFromProblem();

    protected abstract void createConstraintsFromProblem();

    protected abstract void assignValueToVariable(DOMAIN_T value, VAR_T variable);

    protected abstract boolean canAssignValueToVariable(DOMAIN_T value, VAR_T variable);

    protected abstract void undoLastAction();

    protected abstract boolean isProblemSolved();

    public Variable<VAR_T, DOMAIN_T> getVariable(VAR_T varId) {
        return variables.stream()
                .filter(var -> Objects.equals(var.getName(), varId))
                .findFirst()
                .orElse(null);
    }

}
