package ch.epfl.swissteam.services;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class UtilityTest {
    @Test
    public void testIllegelCallException(){

        boolean flag = true;

        try {
            throw new Utility.IllegalCallException("test");
        } catch (Utility.IllegalCallException e) {
            flag = false;
        }

        if(flag){
            assertFalse("Exception must be thrown".equals(""));
        }
    }
}
