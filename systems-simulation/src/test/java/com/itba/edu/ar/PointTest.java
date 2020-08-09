package com.itba.edu.ar;

import com.itba.edu.ar.config.Boundary;
import com.itba.edu.ar.config.StaticConfig;
import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;

public class PointTest {

    private StaticConfig mockStaticConfig = new StaticConfig(1,5, 1, 1,
            1.5, Boundary.CLOSED, new HashMap<>());

    private class MockPoint extends Point {
        public MockPoint(double x, double y) {
            super(x, y);
        }

        @Override
        public StaticConfig getStaticConfig() {
            return mockStaticConfig;
        }

    }

    @Test
    public void testDistance1() {
        Point a = new MockPoint(1,1);
        Point b = new MockPoint(2, 1);
        Assert.assertEquals(a.distance(b), 1, 0.0001);
    }

    @Test
    public void testDistance2() {
        Point a = new MockPoint(1,1);
        Point b = new MockPoint(4, 5);
        Assert.assertEquals(a.distance(b), 5, 0.0001);
    }
}
