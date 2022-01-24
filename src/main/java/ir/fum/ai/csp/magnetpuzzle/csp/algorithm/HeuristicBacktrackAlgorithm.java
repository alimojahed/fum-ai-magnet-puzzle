package ir.fum.ai.csp.magnetpuzzle.csp.algorithm;

import ir.fum.ai.csp.magnetpuzzle.csp.heuristic.DegreeHeuristic;
import ir.fum.ai.csp.magnetpuzzle.csp.heuristic.MRVHeuristic;
import ir.fum.ai.csp.magnetpuzzle.csp.heuristic.ValuePickerHeuristic;
import ir.fum.ai.csp.magnetpuzzle.csp.inference.ForwardChecking;
import ir.fum.ai.csp.magnetpuzzle.csp.inference.InferenceAlgorithm;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.CSP;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Constraint;
import ir.fum.ai.csp.magnetpuzzle.csp.problem.Variable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ali Mojahed on 12/30/2021
 * @project magnet-puzzle
 **/
public class HeuristicBacktrackAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> extends BacktrackAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> {
    Set<String> tracker = new HashSet<>();
    private DegreeHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> degreeHeuristic = new DegreeHeuristic<>();
    private MRVHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> mrvHeuristic = new MRVHeuristic<>();
    private ValuePickerHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> valuePicker;
    private InferenceAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> inferenceAlgorithm;
//    private InferenceAlgorithm<PROBLEM_T, VAR_T, DOMAIN_T> macAlgorithm;
    private CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp;
    private boolean done = false;

    public HeuristicBacktrackAlgorithm(CSP<PROBLEM_T, VAR_T, DOMAIN_T> csp, ValuePickerHeuristic<PROBLEM_T, VAR_T, DOMAIN_T> valuePicker) {
        super(csp);
        this.inferenceAlgorithm = new ForwardChecking<>(csp);
        this.valuePicker = valuePicker;
        this.csp = csp;
    }

    @Override
    protected void solveLevel(int level) {
//        System.out.println(csp.getAssignment());
//        if (tracker.contains(csp.getAssignment().toString())) {
//            done = true;
//            return;
//        }
//        tracker.add(csp.getAssignment().toString());

//        System.out.println(level);

        if (csp.getAssignment().size() == csp.getVariables().size()) {
            if (csp.isProblemSolved()) {
                done = true;
            }

            return;
        }

        Variable<VAR_T, DOMAIN_T> variable = pickUnAssignedVariable();

        for (DOMAIN_T value : getOrderedValues(variable)) {
            if (done)
                break;
            boolean canAssignThisVariable = true;

            if (!csp.canAssignValueToVariable(value, variable.getName()))
                continue;

            csp.assignValueToVariable(value, variable.getName());

            inferenceAlgorithm.inference(variable);

            for (Constraint<VAR_T, DOMAIN_T, PROBLEM_T> constraint : csp.getConstraintsOfVariable(variable)) {
                if (constraint.isAllVariableAreAssigned(csp.getProblem())
                        && !constraint.isConstraintMet(csp.getProblem())) {
                    canAssignThisVariable = false;
                }
            }

            if (canAssignThisVariable) {
                solveLevel(level + 1);
            }

            if (!canAssignThisVariable || !done) {
                csp.undoLastAction();
                inferenceAlgorithm.undoInference();
            }

        }

    }

    @Override
    public Variable<VAR_T, DOMAIN_T> pickUnAssignedVariable() {
        Variable<VAR_T, DOMAIN_T> variable = mrvHeuristic.pickVariable(csp);

        if (variable == null) {
//            System.out.println("value picked by degree");
            variable = degreeHeuristic.pickVariable(csp);
        }

        if (variable == null) {
//            System.out.println("value picker by random");
            variable = super.pickUnAssignedVariable();
        }

        return variable;
    }

    @Override
    public List<DOMAIN_T> getOrderedValues(Variable<VAR_T, DOMAIN_T> variable) {
        return valuePicker.orderValues(csp, variable);
    }
}
