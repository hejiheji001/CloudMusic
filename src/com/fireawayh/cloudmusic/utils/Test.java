package com.fireawayh.cloudmusic.utils;

/**
 * Created by FireAwayH on 15/11/17.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        byte[] encoding1 = "你好吗".getBytes("UTF-8");
        String string1 = new String(encoding1, "ISO8859-1");
        for (byte b : encoding1) {
            System.out.printf("%2x ", b);
        }
        System.out.println();
        byte[] encoding2 = string1.getBytes("UTF-8");
        for (byte b : encoding2) {
            System.out.printf("%2x ", b);
        }
        System.out.println();
    }
}
