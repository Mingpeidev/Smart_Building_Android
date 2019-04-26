package com.mao.smart_building.Util;

/**
 * Created by Mingpeidev on 2019/4/25.
 */

public class CharFormatUtil {

    /**
     * 异或算法
     *
     * @param strHex_X
     * @param strHex_Y
     * @return
     */
    public static String Xor(String strHex_X, String strHex_Y) {

        // 将x、y转成二进制形式
        String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));

        String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));

        String result = "";

        // 判断是否为8位二进制，否则左补零
        if (anotherBinary.length() != 8) {
            for (int i = anotherBinary.length(); i < 8; i++) {

                anotherBinary = "0" + anotherBinary;
            }
        }
        if (thisBinary.length() != 8) {
            for (int i = thisBinary.length(); i < 8; i++) {
                thisBinary = "0" + thisBinary;
            }
        }

        // 异或运算
        for (int i = 0; i < anotherBinary.length(); i++) {
            // 如果相同位置数相同，则补0，否则补1
            if (thisBinary.charAt(i) == anotherBinary.charAt(i))
                result += "0";
            else {
                result += "1";
            }
        }
        return Integer.toHexString(Integer.parseInt(result, 2));
    }

    /**
     * 得出校验码
     *
     * @param para
     * @return
     */
    public static String checkcode(String para) {
        int length = para.length() / 2;
        String[] dateArr = new String[length];

        for (int i = 0; i < length; i++) {
            dateArr[i] = para.substring(i * 2, i * 2 + 2);
        }
        String code = "00";
        for (int i = 0; i < dateArr.length; i++) {
            code = Xor(code, dateArr[i]);
        }
        return code;
    }

    /**
     * 交换十六进制字符并转为十进制
     *
     * @param string
     * @return
     */
    public static double exchange(String string) {
        String a = string.substring(0, 2);
        String b = string.substring(2, 4);
        String temp = "";

        temp = a;
        a = b;
        b = temp;

        return Integer.parseInt(a + b, 16) / 100.00;
    }

    /**
     * 十六进制字符串转换为Byte值 字符串间无分割
     *
     * @param src
     * @return
     */
    public static byte[] hexStr2Bytes(String src) {
        /* 对输入值进行规范化整理 */
        src = src.trim().replace(" ", "").toUpperCase();

        int m = 0, n = 0;
        int l = src.length() / 2;// 几位
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
        }
        return ret;
    }

    /**
     * 字节转换成十六进制字符串，无空格
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * bytes转换成十六进制字符串 ,每个Byte值之间空格分隔
     *
     * @param b
     * @return
     */
    public static String byte2HexStr(byte[] b)

    {
        String stmp = "";

        StringBuilder sb = new StringBuilder("");

        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);

            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);

            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * bytes转换成十六进制字符串 ,每个Byte值之间空格分隔。可定义长度
     *
     * @param b
     * @param len
     * @return
     */
    public static String byte2HexStr(byte[] b, int len) {
        String stmp = "";
        StringBuilder sb = new StringBuilder();

        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * 十六进制转4位二进制
     *
     * @param hexString
     * @return
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * 二进制转十六进制
     *
     * @param bString
     * @return
     */
    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 4 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }
}
