package com.itba.edu.ar.game;

import com.itba.edu.ar.config.ConfigConst;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

public abstract class Space {
    protected int dimension = 0;
    protected int sideLength = 0;
    protected int center = 0;
    protected Rule rule;
    protected int aliveCellCount;

    protected static String timeStamp = String.valueOf(new Timestamp(new Date().getTime()));


    public abstract void save(int epoch) throws IOException;

    public abstract void applyRules();

    protected abstract void randomAliveSpace(int from, int to, int aliveCells);

    protected abstract void sortedAliveSpace(int from, int to, int aliveCells);

    protected abstract void deadSpace();

    protected String getOutputFileName(int epoch) {
        return String.format("%sdynamic_output%s.xyz", ConfigConst.OUTPUT_FOLDER, timeStamp);
    }

    protected int getPeriodicIndex(int index) {
        if(index == -1) return sideLength-1;
        if(index == sideLength) return 0;
        else return index;
    }

    protected static int randIndex(int from, int to) {
        Random r = new Random();
        return r.nextInt((to-from)+1) + from;
    }

    public void initializeRandomSpace(double initPercentage, double alivePercentage, Object cells) {
        if(cells == null) throw new IllegalArgumentException("Cells should not be null");

        this.deadSpace();

        int from = center - (int) Math.ceil((initPercentage/2)*sideLength);
        int to = center + (int) Math.ceil((initPercentage/2)*sideLength);
        int aliveCells = (int) Math.floor(Math.pow(from-to, 2)*alivePercentage);

        final Duration timeout = Duration.ofSeconds(GameConst.TIMEOUT); // Timeout of one minute
        ExecutorService executor = Executors.newSingleThreadExecutor();

        final Future<String> handler = executor.submit(() -> {
            if(alivePercentage > 0.7){
                sortedAliveSpace(from, to, aliveCells);
            }
            else{
                randomAliveSpace(from, to, aliveCells);
            }
            return "Completed";
        });

        try {
            handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            this.aliveCellCount = aliveCells;
        } catch (TimeoutException e) {
            handler.cancel(true);
            System.out.print("Random Space Initialization took to long\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();
    }

}
