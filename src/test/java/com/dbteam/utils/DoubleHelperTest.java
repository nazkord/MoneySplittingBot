package com.dbteam.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleHelperTest {

    @Test
    public void roundTest() {
        double d1 = 21.456D;
        double d2 = 0.442D;


        assertEquals(21.46D, DoubleHelper.round(d1, 2));
        assertEquals(0.44D, DoubleHelper.round(d2, 2));
    }

}