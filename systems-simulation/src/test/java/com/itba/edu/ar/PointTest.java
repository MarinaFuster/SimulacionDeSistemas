package com.itba.edu.ar;

import org.junit.Test;
import org.junit.Assert;

public class PointTest {

    @Test
    public void testDistance1() {
        Point a = new Point(1,1);
        Point b = new Point(2, 1);
        Assert.assertEquals(a.distance(b), 1, 0.0001);
    }

    @Test
    public void testDistance2() {
        Point a = new Point(1,1);
        Point b = new Point(4, 5);
        Assert.assertEquals(a.distance(b), 5, 0.0001);
    }
}
