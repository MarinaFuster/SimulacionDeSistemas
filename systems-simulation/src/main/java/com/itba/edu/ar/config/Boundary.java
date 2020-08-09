package com.itba.edu.ar.config;

import java.util.Arrays;
import java.util.Optional;

public enum Boundary {
    CLOSED(0), INFINITE(1);

    private final int value;

    Boundary(int value) {
        this.value = value;
    }

    public static Optional<Boundary> valueOf(int value) {
        return Arrays.stream(values())
                .filter(boundary -> boundary.value == value)
                .findFirst();
    }
}
