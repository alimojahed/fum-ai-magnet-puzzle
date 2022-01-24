package ir.fum.ai.csp.magnetpuzzle.csp.problem;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ali Mojahed on 12/24/2021
 * @project magnet-puzzle
 **/

@Getter
@Setter
@EqualsAndHashCode
public class Variable<VAR_T, DOMAIN_T> {
    @EqualsAndHashCode.Exclude
    private DOMAIN_T value = null;

    @EqualsAndHashCode.Include
    private VAR_T name;

    @EqualsAndHashCode.Exclude
    private Domain<DOMAIN_T> domain;

    public Variable(VAR_T name, Domain<DOMAIN_T> domain) {
        this.name = name;
        this.domain = domain;
    }


    public boolean isAssigned() {
        return value != null;
    }

    @Override
    public String toString() {
        return "{ " +
                name.toString() +
                " = " +
                value +
                " , " +
                "domain = " +
                domain +
                "}";

    }
}
