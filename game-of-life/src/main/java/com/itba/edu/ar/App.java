package com.itba.edu.ar;

import com.itba.edu.ar.config.StaticConfig;
import com.itba.edu.ar.config.StaticConfigLoader;

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
            StaticConfigLoader.save(config, "./sample/staticConfigOutput.txt");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static StaticConfig generateStaticConfig() {
        Scanner s = new Scanner(System.in);

        int dimension = getInteger(s, "Insert dimension (2 | 3): ");
        int sideLength = getInteger(s, "Insert side length: ");
        int epochs = getInteger(s, "Insert epochs: ");
        return new StaticConfig(dimension, sideLength, epochs);
    }

    public static int getInteger(Scanner s, String message) {
        System.out.println(message);
        return s.nextInt();
    }
}
