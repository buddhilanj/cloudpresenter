/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.utils;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class ByteConvert {

    private static final int longsize = 8;
    private static final int intsize = 4;

    public static void main(String[] args) {
        try {
            byte[] b = new ByteConvert().intToByteArr(88888);
            //System.out.println("reply:" + new ByteConvert().byteArrToInt(b));
        } catch (ParseException ex) {
            Logger.getLogger(ByteConvert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static byte[] longToByteArr(long val) throws ParseException {
        byte nums[] = new byte[longsize];
        int count = 1;
        if (val < 0) {
            throw new ParseException("The Long should be positive", 0);
        }
        for (; val > 256; count++) {
            if (count > longsize) {
                throw new ParseException("This is not parsable to " + longsize + " bytes", count);
            }
            nums[longsize - count] = (byte) ((val % 256) - 128l);
            val /= 256;
        }
        if (count > longsize) {
            throw new ParseException("This is not parsable to " + longsize + " bytes", count);
        }
        nums[longsize - count] = (byte) (val - 128l);
        for (count++; count <= longsize; count++) {
            nums[longsize - count] = (byte) (-128);
        }
        return nums;
    }

    public static long byteArrToLong(byte[] arr) throws ParseException {
        if (arr.length != longsize) {
            throw new ParseException("String is not " + longsize + " long", 0);
        }
        long val = 0;
        for (int i = 0; i < longsize; i++) {
            long temp = arr[i] + 128;
            for (int j = 1; j < longsize - i; j++) {
                temp *= 256;
            }
            val += temp;
        }

        return val;
    }

    public static byte[] intToByteArr(int val) throws ParseException {
        byte nums[] = new byte[intsize];
        int count = 1;
        if (val < 0) {
            throw new ParseException("The Long should be positive", 0);
        }
        for (; val > 256; count++) {
            if (count > intsize) {
                throw new ParseException("This is not parsable to " + intsize + " bytes", count);
            }
            nums[intsize - count] = (byte) ((val % 256) - 128l);
            val /= 256;
        }
        if (count > intsize) {
            throw new ParseException("This is not parsable to " + intsize + " bytes", count);
        }
        nums[intsize - count] = (byte) (val - 128l);
        for (count++; count <= intsize; count++) {
            nums[intsize - count] = (byte) (-128);
        }
        return nums;
    }

    public static int byteArrToInt(byte[] arr) throws ParseException {
        if (arr.length != intsize) {
            throw new ParseException("String is not " + intsize + " long", 0);
        }
        int val = 0;
        for (int i = 0; i < intsize; i++) {
            int temp = arr[i] + 128;
            for (int j = 1; j < intsize - i; j++) {
                temp *= 256;
            }
            val += temp;
        }

        return val;
    }
}
