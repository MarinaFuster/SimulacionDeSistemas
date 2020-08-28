package com.itba.edu.ar.config;

import java.io.*;

public class StaticConfigLoader {
    public static void save(StaticConfig staticConfig, String path) throws IOException {
        FileWriter fw = new FileWriter(path);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(staticConfig.getDimension());
        pw.println(staticConfig.getSideLength());
        pw.println(staticConfig.getEpochs());
        pw.println(staticConfig.getAlivePercentage());
        pw.println(staticConfig.getCenter());
        pw.close();
    }
}
