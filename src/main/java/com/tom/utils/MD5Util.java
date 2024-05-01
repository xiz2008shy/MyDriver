package com.tom.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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


    public static String getPartialFileMD5(String filePath, long start, long length) {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            file.seek(start);
            byte[] buffer = new byte[(int) length];
            file.readFully(buffer);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            return toHexString(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
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
        //dd48541fbf39342afe4026cf8d7d88ec
        String fileMD5 = getFileMD5("C:\\Users\\TOMQI\\Desktop\\v20240413.png");
        System.out.println(fileMD5);
    }
}
