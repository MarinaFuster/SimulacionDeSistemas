package com.itba.edu.ar;

import com.itba.edu.ar.config.ConfigConst;
import com.itba.edu.ar.config.StaticConfig;
import com.itba.edu.ar.config.StaticConfigLoader;
import com.itba.edu.ar.game.GameOfLife;
import com.itba.edu.ar.game.Rule;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class App {

    public static void main( String[] args ) {

        runConfigWithStdInput();
//        runConfigMultipleTimes();
    }

    public static void runConfigMultipleTimes() {
        int dimension = 2;
        int sideLength = 100;
        int epochs = 250;
        Rule rule = Rule.CONWAY_2D_RULE_SET;
        String ruleName = "conway100";
        String name;
        for (int initialCells = 1; initialCells <= 10; initialCells++) {
            for (int i = 1; i <= 100; i++) {
                String outputFolder = String.format("%s%s/%s_0%d", ConfigConst.OUTPUT_FOLDER, ruleName, ruleName, initialCells);
                createDirectoryIfDoesntExist(outputFolder);
                name = String.format("%s/%s_0%d/%s_0%d_%d", ruleName, ruleName, initialCells, ruleName, initialCells, i);
                StaticConfig config = new StaticConfig(dimension, sideLength, epochs, initialCells/10.0, rule, name);
                if (!config.isValid()) throw new IllegalArgumentException("Invalid static config parameters");
                runConfig(config, 0);
            }
        }
    }



    public static void runConfig(StaticConfig config, int loopNumber) {
        try {
            String staticOutputName, dynamicOutputName;
            staticOutputName = String.format("%s%s_static.txt", ConfigConst.OUTPUT_FOLDER, config.getName());
            if (loopNumber == 0 ) {
                dynamicOutputName = String.format("%s%s_dynamic.xyz", ConfigConst.OUTPUT_FOLDER, config.getName());
            } else {
                dynamicOutputName = String.format("%s%s_dynamic_%d_multiple.xyz", ConfigConst.OUTPUT_FOLDER, config.getName(), loopNumber);
            }

            try {
                File f = new File(dynamicOutputName);
                if(f.delete()) System.out.printf("[WARNING] Dynamic file with same name already existed and was deleted (%s)\n", dynamicOutputName);
            } catch (Exception e) {}
            StaticConfigLoader.save(config, staticOutputName);
            startGame(config, dynamicOutputName);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void runConfigWithStdInput() {
        StaticConfig config = getStaticConfigFromStdIn();
        if (!config.isValid()) throw new IllegalArgumentException("Invalid static config parameters");
        runConfig(config, 0); // The 0 means that this is not a looped run, but a unique one
    }

    public static void startGame(StaticConfig config, String dynamicOutputName) throws IOException {
        GameOfLife game = new GameOfLife(
                config.getDimension(), config.getSideLength(), config.getEpochs(),
                config.getAlivePercentage(), config.getCenter(), config.getRule(), dynamicOutputName);
        game.run();
    }

    public static StaticConfig getStaticConfigFromStdIn() {
        Scanner s = new Scanner(System.in);

        int dimension = getInteger(s, "Insert dimension (2 | 3): ");
        int sideLength = getInteger(s, "Insert side length: ");
        int epochs = getInteger(s, "Insert epochs: ");
        double alivePercentage = getDouble(s, "Insert starting alive percentage: ");
        Rule rule = dimension == 2 ? get2DRule(s) : get3DRule(s);
        String name = getString(s, "Insert name for simulation");
        return new StaticConfig(dimension, sideLength, epochs, alivePercentage, rule, name);
    }

    public static Rule get2DRule(Scanner s) {
        int rule = getInteger(s,
                "Choose 2D rule\n" +
                        "Select number according to the rule you want\n" +
                        "1. Conway Rule: cells remain alive with 2 or 3 alive neighbours. Dead cells are born if they have 3 alive neighbours\n" +
                        "2. Unrestricted Multiple Rule: cells remain alive with multiple of 3 neighbours. Dead cells are born with multiple of 2 neighbours.\n" +
                        "In both cases, neighbours are bigger than zero.\n" +
                        "3. Numbers Rule: cells remain alive if neighbours are multiple of 2, 13 or 29. Dead cells are born with multiple of 3 neighbours.\n");
        if(rule == 1)
            return Rule.CONWAY_2D_RULE_SET;
        if(rule == 2)
            return Rule.UNRESTRICTED_MULTIPLE_2D_RULE_SET;
        if(rule == 3)
            return Rule.NUMBERS_2D_RULE_SET;
        throw new IllegalArgumentException("You must choose a number between 0 and 2\n");
    }

    public static Rule get3DRule(Scanner s) {
        int rule = getInteger(s,
                "Choose 3D rule\n" +
                        "Select number according to the rule you want\n" +
                        "1. Small Primes Rule: cells remain alive or dead cells are born if neighbours is a prime (bigger than 3 and lower than 21) number\n" +
                        "2. Big Primes Rule: cells remain alive or dead cells are born if neighbours is a prime number lower than 40\n" +
                        "3. Different multiples Rule:\n" +
                        "\tif alive cells count is lower than 200, cells remain alive or dead cells are born if neighbours is a multiple of 3 bigger than 3 and lower than 20\n" +
                        "\telse, cells remain alive or new cells are born if neighbours is a multiple of 7 bigger than 3 and lower than 20\n");
        if(rule == 1)
            return Rule.PRIME_BETWEEN_4_AND_20_PRIME_RULE_SET;
        if(rule == 2)
            return Rule.PRIME_LESS_40_PRIME_3D_RULE_SET;
        if(rule == 3)
            return Rule.DIFFERENT_MULTIPLES_RULE_SET;
        throw new IllegalArgumentException("You must choose a number between 0 and 2\n");
    }

    public static int getInteger(Scanner s, String message) {
        System.out.println(message);
        return s.nextInt();
    }

    public static double getDouble(Scanner s, String message) {
        System.out.println(message);
        return s.nextDouble();
    }

    public static String getString(Scanner s, String message) {
        System.out.println(message);
        return s.next();
    }

    private static void createDirectoryIfDoesntExist(String path) {
        File dir = new File(path);
        if ( !dir.exists()) dir.mkdir();
    }
}
