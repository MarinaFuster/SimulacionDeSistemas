package com.itba.edu.ar.game;

import java.io.IOException;

public interface Space {
    void initializeRandomSpace(double alivePercentage);

    void applyRules();

    void save(int epoch) throws IOException;
}
