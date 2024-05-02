package com.tom.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5Util {

    public static String getFileMD5(String filePath) throws IOException, NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        int len;
        int times = 0;
        while ((len = fis.read(buffer)) != -1) {
            md.update(buffer, 0, len);
            times++;
        }
        System.out.println(STR."times = \{times}");
        fis.close();
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
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

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        //123 - 8f953fdc81fef2380f56de87bdda4b63  456 - ead02d5bc2b10688f11f0baf066339cd
        String fileMD5 = partialFileMD5(new File("C:\\Users\\TOMQI\\Desktop\\123.png"));
        System.out.println(fileMD5);
    }
}
