package ir.fum.ai.csp.magnetpuzzle.csp.problem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Ali Mojahed on 12/24/2021
 * @project magnet-puzzle
 **/

@Getter
@Setter
@AllArgsConstructor
public class Constraint<VAR_T, DOMAIN_T, LIMITER_OBJ_T> {
    private Set<Variable<VAR_T, DOMAIN_T>> variables;
    private Predicate<LIMITER_OBJ_T> constraint;

    public boolean isConstraintMet(LIMITER_OBJ_T limiter) {
        return constraint.test(limiter);
    }

    public boolean isAllVariableAreAssigned(LIMITER_OBJ_T limiter) {
        return variables.stream()
                .noneMatch(variable -> variable.getValue() == null);
    }
}
