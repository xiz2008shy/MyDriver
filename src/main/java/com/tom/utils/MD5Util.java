package com.tom.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5Util {

    public static String getFileMD5(File file){
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 8];
            int len;
            int times = 0;
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
                times++;
            }
            fis.close();
            byte[] digest = md.digest();

            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }


    public static String partialFileMD5(File file) {
        long length = file.length();
        long s = length / 3;
        int sLimit = 512 * 1024;
        if (s > sLimit){
            s = sLimit;
        }
        int p = sLimit / 10 * 6;
        return partialFileMD5(file,p,s);
    }

    public static String partialFileMD5(File file, int preSumLength, long skipLength) {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long size = file.length();
            long start = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[preSumLength];
            while ( (start + preSumLength) < size) {
                raf.seek(start);
                raf.readFully(buffer);
                md.update(buffer);
                start += skipLength;
            }

            return toHexString(md.digest());
        } catch (Exception e) {
            log.error("MD5Util.partialFileMD5 occurred an error,cause:",e);
            return null;
        }
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }



    public static String getMacByIP() throws Exception {
        return getMacByIP(InetAddress.getLocalHost().getHostAddress());
    }

    public static String getMacByIP(String IP) throws Exception {
        InetAddress ia = InetAddress.getByName(IP);
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            String hexString = Integer.toHexString(mac[i] & 0xFF);
            sb.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return sb.toString().toUpperCase();
    }
}
