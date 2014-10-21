/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cloudpresenter.utils;

import java.nio.ByteBuffer;

/**
 *
 * @author Nawanjana
 */
public class ByteUtils {
    private static ByteBuffer buffer = ByteBuffer.allocate(8);

    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}
