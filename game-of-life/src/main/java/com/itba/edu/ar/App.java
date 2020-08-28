package com.itba.edu.ar;

import com.itba.edu.ar.config.ConfigConst;
import com.itba.edu.ar.config.StaticConfig;
import com.itba.edu.ar.config.StaticConfigLoader;
import com.itba.edu.ar.game.GameOfLife;

import java.io.IOException;
import java.util.Scanner;

public class App {

    public static void main( String[] args ) {
        runConfigWithStdInput();
    }

    public static void runConfigWithStdInput() {
        StaticConfig config = generateStaticConfig();
        if (!config.isValid()) throw new IllegalArgumentException("Invalid static config parameters");
        try {
            StaticConfigLoader.save(config, ConfigConst.OUTPUT_FOLDER + "staticConfigOutput.txt");
            startGame(config);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void startGame(StaticConfig config) throws IOException {
        GameOfLife game = new GameOfLife(
                config.getDimension(), config.getSideLength(), config.getEpochs(), config.getAlivePercentage(), config.getCenter());
        game.run();
    }

    public static StaticConfig generateStaticConfig() {
        Scanner s = new Scanner(System.in);

        int dimension = getInteger(s, "Insert dimension (2 | 3): ");
        int sideLength = getInteger(s, "Insert side length: ");
        int epochs = getInteger(s, "Insert epochs: ");
        double alivePercentage = getDouble(s, "Insert starting alive percentage: ");
        return new StaticConfig(dimension, sideLength, epochs, alivePercentage);
    }

    public static int getInteger(Scanner s, String message) {
        System.out.println(message);
        return s.nextInt();
    }

    public static double getDouble(Scanner s, String message) {
        System.out.println(message);
        return s.nextDouble();
    }
}
