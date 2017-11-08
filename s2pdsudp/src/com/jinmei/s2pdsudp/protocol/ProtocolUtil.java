/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jinmei.s2pdsudp.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 生成序号器
 *
 * @author hanzy
 */
public class ProtocolUtil {

    private static int max_seq = 0xffff;
    private static int currentSeq = 0x00;

    /**
     * 得到通信序号
     *
     * @return
     */
    public static int getSeq() {
        currentSeq = currentSeq + 1;
        if (currentSeq == max_seq) {
            currentSeq = 0x0000;
        }
        return currentSeq;
    }

    /**
     * 得到序号
     *
     * @return
     */
    public static byte[] getSeq(String str) {
        return getByteInt2(++currentSeq);
    }

    /**
     *
     * @param a
     * @return
     */
    public static byte[] getByteInt2(int a) {
        byte[] tmp = new byte[2];
        tmp[0] = (byte) ((a & 0xff00) >> 8);
        tmp[1] = (byte) (a & 0x00ff);
        return tmp;
    }

    /**
     * 合并字节数组
     *
     * @param a 数组1
     * @param b 数组2
     * @return
     */
    public static byte[] merge(byte[] a, byte[] b) {
        byte[] tmp = new byte[a.length + b.length];
        System.arraycopy(a, 0, tmp, 0, a.length);
        System.arraycopy(b, 0, tmp, a.length, b.length);
        return tmp;
    }

    /**
     * 返回16字节的摘要
     *
     * @param source 要计算摘要的字符串
     * @return 16字节长度的摘要
     */
    public static byte[] toAbstract(String source) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

            if (source != null) {
                md.update(source.getBytes(Protocol.CODE));
                byte tmp[] = md.digest();
                return tmp;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回16字节的摘要
     *
     * @param source 要计算摘要的字节数组
     * @return 16字节长度的摘要
     */
    public static byte[] toAbstract(byte[] source) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            if (source != null) {
                md.update(source);
                byte tmp[] = md.digest();
                return tmp;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将int类型转换成字节流
     *
     * @param src 源
     * @return 将src转换后的字节流数组
     */
    public static byte[] intToBytes2(int src) {
        byte[] result = new byte[2];
        result[1] = (byte) (src & 0xff);
        result[0] = (byte) (src >>> 8 & 0xff);
        return result;
    }

    /**
     * 将int类型转换成字节流
     *
     * @param src 源
     * @return 将src转换后的字节流数组
     */
    public static byte[] intToBytes4(int src) {
        byte[] result = new byte[4];
        result[3] = (byte) (src & 0xff);
        result[2] = (byte) (src >>> 8 & 0xff);
        result[1] = (byte) (src >>> 16 & 0xff);
        result[0] = (byte) (src >>> 24 & 0xff);
        return result;
    }

    public static byte[] longToBytes4(long src) {
        byte[] result = new byte[4];
        result[3] = (byte) (src & 0xff);
        result[2] = (byte) (src >>> 8 & 0xff);
        result[1] = (byte) (src >>> 16 & 0xff);
        result[0] = (byte) (src >>> 24 & 0xff);
        return result;
    }
    public static long byte4ToLong(byte[] bytes){
        return bytes[3] & 0xff | ((bytes[2] & 0xff) << 8) | ((bytes[1] & 0xff) << 16) | ((bytes[0] & 0xff) << 24);
    }
    public static long byte4ToLong(byte[] bytes,int startPos){
        byte[] temp = new byte[4];
         System.arraycopy(bytes, startPos, temp, 0, 4);
        return byte4ToLong(temp);
    }

    /**
     * 将指定位置转为长度
     *
     * @param bytes
     * @param startPos
     * @return
     */
    public static int bytes2ToInt(byte[] bytes, int startPos) {
        byte[] temp = new byte[2];
        System.arraycopy(bytes, startPos, temp, 0, 2);
        return bytes2ToInt(temp);
    }

    public static int byte4ToInt(byte[] bytes, int startPos) {
        byte[] temp = new byte[4];
        System.arraycopy(bytes, startPos, temp, 0, 4);
        return bytes4ToInt(temp);
    }

    /**
     * 将字节数组转换成int类型
     *
     * @param bytes 字节数组
     * @return 字节数据对于的int值
     */
    public static int bytes2ToInt(byte[] bytes) {
        return bytes[1] & 0xff | ((bytes[0] & 0xff) << 8);
    }

    /**
     * 将字节数组转换成int类型
     *
     * @param bytes 字节数组
     * @return 字节数据对于的int值
     */
    public static int bytes4ToInt(byte[] bytes) {
        return bytes[3] & 0xff | ((bytes[2] & 0xff) << 8) | ((bytes[1] & 0xff) << 16) | ((bytes[0] & 0xff) << 24);
    }
        /**
         * 返回子数组
         * @param bytes
         * @param startPos
         * @param endPos
         * @return 
         */
        public static byte[] subBytes(byte[] bytes,int startPos,int endPos){
            if(endPos<startPos)return null;
            if(startPos>bytes.length || endPos>bytes.length){
                
            }
            byte[] tmp=new byte[endPos-startPos];
            
            System.arraycopy(bytes, startPos, tmp, 0, endPos-startPos);
            return tmp;
        }
        
     /**
      * 将file2合并进file1
     * @param file1
     * @param file2
     * @throws IOException
     */
    public static void merge(File file1,File file2) throws IOException{
		FileOutputStream out;
		out = new FileOutputStream(file1, true);
		FileInputStream in = new FileInputStream(file2);
		byte[] buf=new byte[2048];
		int readLen=0;
		while((readLen=in.read(buf))>0){
			out.write(buf, 0, readLen);
		}
		
		out.close();
		in.close();
    	 
     }
}
