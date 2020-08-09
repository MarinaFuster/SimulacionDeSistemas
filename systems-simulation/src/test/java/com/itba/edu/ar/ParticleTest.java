package com.itba.edu.ar;

import com.itba.edu.ar.config.Boundary;
import com.itba.edu.ar.config.StaticConfig;
import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;

public class ParticleTest {

    private StaticConfig mockStaticConfig = new StaticConfig(1,5, 1, 1,
            1.5, Boundary.CLOSED, new HashMap<>());

    private class MockParticle extends Particle {
        @Override
        public StaticConfig getStaticConfig() {
            return mockStaticConfig;
        }

        public MockParticle(double radius) {
            super(radius);
        }

        public MockParticle(double radius, Point position) {
            super(radius, position);
        }

        public MockParticle(double radius, Point position, Point speed) {
            super(radius, position, speed);
        }
    }

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
    public void testOverlaps () {


        // Source
        Particle p1 = new MockParticle(1, new MockPoint(1,1));

        // Tests subjects
        Particle p2 = new MockParticle(1, new MockPoint(2,1));
        Particle p3 = new MockParticle(1, new MockPoint(3,1));
        Particle p4 = new MockParticle(1, new MockPoint(3.0001,1));

        Assert.assertTrue(p1.overlaps(p2));
        Assert.assertTrue(p1.overlaps(p3));
        Assert.assertFalse(p1.overlaps(p4));


        // Radius 0
        Particle p5 = new MockParticle(0, new MockPoint(0.9999, 0));
        Particle p6 = new MockParticle(0, new MockPoint(1, 0));

        Assert.assertFalse(p1.overlaps(p5));
        Assert.assertTrue(p1.overlaps(p6));
    }

    @Test
    public void testInteracts () {
        Particle p1 = new MockParticle(1, new MockPoint(1,1));


    }

}
