import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class WaterJugProblem {
    private int jug1Capacity;
    private int jug2Capacity;
    private int targetAmount;
    private HashSet<State> visited;

    // Constructor
    public WaterJugProblem(int jug1Capacity, int jug2Capacity, int targetAmount) {
        this.jug1Capacity = jug1Capacity;
        this.jug2Capacity = jug2Capacity;
        this.targetAmount = targetAmount;
        this.visited = new HashSet<State>();
    }

    // State class
    private class State {
        public int jug1Amount;
        public int jug2Amount;

        public State(int amount1, int amount2){
            this.jug1Amount = amount1;
            this.jug2Amount = amount2;
        }

    }

    // Override equals and hashCode methods for HashSet
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return jug1Capacity == state.jug1Amount &&
                jug2Capacity == state.jug2Amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jug1Capacity
                , jug2Capacity);
    }


    // Get successors for a given state
    private LinkedList<State> getSuccessors(State state) {
        int jug1Amount = state.jug1Amount;
        int jug2Amount = state.jug2Amount;
        LinkedList<State> successors = new LinkedList<>();

        // Fill jug1
        successors.add(new State(jug1Capacity, jug2Amount));

        // Fill jug2
        successors.add(new State(jug1Amount, jug2Capacity));

        // Empty jug1
        successors.add(new State(0, jug2Amount));

        // Empty jug2
        successors.add(new State(jug1Amount, 0));

        // Pour jug1 into jug2 until jug2 is full or jug1 is empty
        int amountToPour = Math.min(jug1Amount, jug2Capacity - jug2Amount);
        successors.add(new State(jug1Amount - amountToPour, jug2Amount + amountToPour));

        // Pour jug2 into jug1 until jug1 is full or jug2 is empty
        amountToPour = Math.min(jug2Amount, jug1Capacity - jug1Amount);
        successors.add(new State(jug1Amount + amountToPour, jug2Amount - amountToPour));

        return successors;
    }

    // Check if a state is the goal state
    private boolean isGoal(State state) {
        return state.jug1Amount == targetAmount || state.jug2Amount == targetAmount;
    }

    // BFS algorithm
    public State bfs() {
        Queue<State> queue = new LinkedList<>();
        State startState = new State(0, 0);
        queue.add(startState);
        visited.add(startState);

        while (!queue.isEmpty()) {
            State state = queue.poll();

            if (isGoal(state)) {
                return state;
            }

            LinkedList<State> successors = getSuccessors(state);
            for (State successor : successors) {
                if (!visited.contains(successor)) {
                    queue.add(successor);
                    visited.add(successor);
                }
            }
        }

        return null;
    }
    // DFS algorithm
    public State dfs() {
        LinkedList<State> stack = new LinkedList<>();
        State startState = new State(0,0);
        stack.push(startState);
        visited.add(startState);

        while (!stack.isEmpty()) {
            State state = stack.pop();

            if (isGoal(state)) {
                return state;
            }

            LinkedList<State> successors = getSuccessors(state);
            for (State successor : successors) {
                if (!visited.contains(successor)) {
                    stack.push(successor);
                    visited.add(successor);
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        WaterJugProblem problem = new WaterJugProblem(4, 3, 2);
        System.out.println("BFS solution: " + problem.bfs().jug2Amount);
        //System.out.println("DFS solution: " + problem.dfs());
    }
}