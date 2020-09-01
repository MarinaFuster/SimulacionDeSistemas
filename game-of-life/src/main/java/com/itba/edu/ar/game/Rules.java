package com.itba.edu.ar.game;

public class Rules {

    private Rules() {
        throw new UnsupportedOperationException();
    }

    /*
     * Alive cells with more then 9 and lower than 26 neighbors survive.
     * Empty cells with more than 7 and lower than 19 neighbors have a new cell born at that location.
     * */
    public static CellState applyCloudOneRules(int aliveNeighbours, CellState state) {
        if((state == CellState.ALIVE && aliveNeighbours >= 9 && aliveNeighbours <= 26) ||
                (state == CellState.DEAD && aliveNeighbours >= 7 && aliveNeighbours <=19))
            return CellState.ALIVE;
        return CellState.DEAD;
    }

    public static CellState lessThan40PrimeRule(int aliveNeighbours, CellState state){
        boolean prime = isPrime(aliveNeighbours);
        if(aliveNeighbours < 1 || aliveNeighbours > 40) return CellState.DEAD;

        if(state == CellState.ALIVE && prime) {
            return CellState.ALIVE;
        }
        if(state == CellState.DEAD && prime) {
            return CellState.ALIVE;
        }

        return CellState.DEAD;
    }

    public static CellState between4And20PrimeRule(int aliveNeighbours, CellState state) {
        boolean prime = isPrime(aliveNeighbours);
        if(aliveNeighbours < 3 || aliveNeighbours > 20) return CellState.DEAD;

        if(state == CellState.ALIVE && prime) {
            return CellState.ALIVE;
        }
        if(state == CellState.DEAD && prime && aliveNeighbours < 10) {
            return CellState.ALIVE;
        }

        return CellState.DEAD;
    }

    public static CellState differentMultiplesRule(int aliveNeighbours, CellState state, int aliveCellCount) {
        if(aliveCellCount < 200) return multipleOfThreeRule(aliveNeighbours, state);
        return multipleOfSevenRule(aliveNeighbours, state);
    }

    public static CellState multipleOfThreeRule(int aliveNeighbours, CellState cellState) {
        if(aliveNeighbours < 3 || aliveNeighbours > 20) return CellState.DEAD;

        if(cellState == CellState.ALIVE && (aliveNeighbours % 3 == 0)) {
            return CellState.ALIVE;
        }
        if(cellState == CellState.DEAD && (aliveNeighbours % 3 == 0)) {
            return CellState.ALIVE;
        }

        return CellState.DEAD;
    }

    public static CellState multipleOfSevenRule(int aliveNeighbours, CellState state) {
        if(aliveNeighbours < 3 || aliveNeighbours > 20) return CellState.DEAD;

        if(state == CellState.ALIVE && (aliveNeighbours % 7 == 0)) {
            return CellState.ALIVE;
        }
        if(state == CellState.DEAD && (aliveNeighbours % 7 == 0)) {
            return CellState.ALIVE;
        }

        return CellState.DEAD;
    }

    private static boolean isPrime(int A) {
        if (A == 1) {
            return false;
        }
        double limit = Math.sqrt(A) + 1;
        for (int i = 2; i <= limit; i++) {
            if (A % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static CellState conwayRule(int aliveNeighbours, CellState state) {
        if(state == CellState.ALIVE && (aliveNeighbours == 2 || aliveNeighbours == 3)) {
            return CellState.ALIVE;
        }
        if(state == CellState.DEAD && aliveNeighbours == 3){
            return CellState.ALIVE;
        }
        return CellState.DEAD;
    }

    public static CellState unrestrictedMultipleRule(int aliveNeighbours, CellState state) {
        if(aliveNeighbours == 0) return CellState.DEAD;

        if(state == CellState.ALIVE && aliveNeighbours % 3 == 0) {
            return CellState.ALIVE;
        }
        if(state == CellState.DEAD && aliveNeighbours % 2 == 0){
            return CellState.ALIVE;
        }
        return CellState.DEAD;
    }

    public static CellState testingRule(int aliveNeighbours, CellState state) {
        if(state == CellState.DEAD && aliveNeighbours == 0) return CellState.DEAD;
        if(state == CellState.DEAD && aliveNeighbours == 3) return CellState.ALIVE;

        if(state == CellState.ALIVE && (
                aliveNeighbours % 2 == 0  || aliveNeighbours % 13 == 0 || aliveNeighbours % 29 == 0 ) ) {
            return CellState.ALIVE;
        }
        return CellState.DEAD;
    }
}
