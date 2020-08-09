package com.itba.edu.ar.config;

public class AppConfig {

    public String outputPath;
    public int maxTime = 0;
    public boolean printInteractionsList = false;

    public AppConfig(String outputPath) {
        this.outputPath = outputPath;
    }
}
