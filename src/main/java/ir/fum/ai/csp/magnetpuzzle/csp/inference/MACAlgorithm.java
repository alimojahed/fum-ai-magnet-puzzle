package ir.fum.ai.csp.magnetpuzzle.csp.inference;

import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Constraint;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Ali Mojahed on 1/4/2022
 * @project magnet-puzzle
 **/
public class MACAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> extends InferenceAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> {

    private AC3<PROBLEM_T, VAR_T, DOMAIN_T> ac3;

    public MACAlgorithm(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp) {
        super(csp);
        ac3 = new AC3<>(csp);
    }

    @Override
    public boolean inference(Variable<VAR_T, DOMAIN_T> variable) {
        Queue<AC3.Arc<VAR_T, DOMAIN_T>> queue = new LinkedList<>();

        for (Constraint<VAR_T, DOMAIN_T, PROBLEM_T> constraint : csp.getConstraintWithOrderAndVar(2, variable)) {
            List<Variable<VAR_T, DOMAIN_T>> variables = new ArrayList<>(constraint.getVariables());
            queue.add(new AC3.Arc<>((variables.get(0).equals(variable) ? variables.get(1) : variables.get(0)), variable));
//            queue.add(new AC3.Arc<>(variable, (variables.get(0).equals(variable) ? variables.get(1) : variables.get(0))));
        }
        System.out.println(variable);
        return ac3.ac3(queue);
    }

    @Override
    public void undoInference() {
        ac3.undoRemovedVariables();
    }
}
