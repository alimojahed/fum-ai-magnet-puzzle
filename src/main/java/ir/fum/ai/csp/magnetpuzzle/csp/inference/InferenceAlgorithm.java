package ir.fum.ai.csp.magnetpuzzle.csp.inference;

import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;
import lombok.Getter;

/**
 * @author Ali Mojahed on 12/30/2021
 * @project magnet-puzzle
 **/

@Getter
public abstract class InferenceAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> {
    protected CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp;

    public InferenceAlgorithm(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp) {
        this.csp = csp;
    }


    public abstract boolean inference(Variable<VAR_T, DOMAIN_T> variable);

    public abstract void undoInference();

}
