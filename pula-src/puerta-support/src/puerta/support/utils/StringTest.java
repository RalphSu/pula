package puerta.support.utils;    
   
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
   
/**   
 * 字符串工具集合   
 *    
 * @author Liudong   
 */   
public class StringTest {    
   
    private static final String PASSWORD_CRYPT_KEY = "12345678";// 密钥    
   
    private final static String DES = "DES";//DES算法名称    
   
    private String realKey;// 真实注册码    
   
    /**   
     *    
     * StringTest的构造器.   
     *    
     * @param arg   
     */   
    public StringTest(boolean arg) {    
        Random r = new Random();    
        if(!arg){    
            realKey = r.nextLong()+":"+String.valueOf(System.currentTimeMillis());    
        } else{    
            DateFormat df = DateFormat.getDateInstance();    
            Date d = null;    
            try {    
                d = df.parse("1981-06-27 00:00:02");    
            } catch (ParseException e) {    
                e.printStackTrace();    
            }    
            realKey = r.nextLong()+":"+String.valueOf(d.getTime());    
        }    
   
    }    
   
    /**   
     * 加密   
     *    
     * @param src   
     *            数据源   
     * @param key   
     *            密钥，长度必须是8的倍数   
     * @return 返回加密后的数据   
     * @throws Exception   
     */   
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {    
        // DES算法要求有一个可信任的随机数源    
        SecureRandom sr = new SecureRandom();    
        // 从原始密匙数据创建DESKeySpec对象    
        DESKeySpec dks = new DESKeySpec(key);    
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成    
        // 一个SecretKey对象    
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);    
        SecretKey securekey = keyFactory.generateSecret(dks);    
        // Cipher对象实际完成加密操作    
        Cipher cipher = Cipher.getInstance(DES);    
        // 用密匙初始化Cipher对象    
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);    
        // 现在，获取数据并加密    
        // 正式执行加密操作    
        return cipher.doFinal(src);    
    }    
   
    /**   
     * 解密   
     *    
     * @param src   
     *            数据源   
     * @param key   
     *            密钥，长度必须是8的倍数   
     * @return 返回解密后的原始数据   
     * @throws Exception   
     */   
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {    
        // DES算法要求有一个可信任的随机数源    
        SecureRandom sr = new SecureRandom();    
        // 从原始密匙数据创建一个DESKeySpec对象    
        DESKeySpec dks = new DESKeySpec(key);    
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成    
        // 一个SecretKey对象    
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);    
        SecretKey securekey = keyFactory.generateSecret(dks);    
        // Cipher对象实际完成解密操作    
        Cipher cipher = Cipher.getInstance(DES);    
        // 用密匙初始化Cipher对象    
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);    
        // 现在，获取数据并解密    
        // 正式执行解密操作    
        return cipher.doFinal(src);    
    }    
   
    /**   
     *    
     * 二行制转字符串   
     *    
     * @param b   
     *    
     * @return   
     *    
     */   
   
    public static String byte2hex(byte[] b) {    
   
        String hs = "";    
   
        String stmp = "";    
   
        for (int n = 0; n < b.length; n++) {    
   
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));    
            if (stmp.length() == 1)    
                hs = hs + "0" + stmp;    
            else   
                hs = hs + stmp;    
        }    
   
        return hs.toUpperCase();    
   
    }    
   
    /**   
     *    
     * 功能:字符串二行制   
     *    
     * @param b   
     * @return   
     *    
     */   
    public static byte[] hex2byte(byte[] b) {    
   
        if ((b.length % 2) != 0)    
            throw new IllegalArgumentException("长度不是偶数");    
        byte[] b2 = new byte[b.length / 2];    
        for (int n = 0; n < b.length; n += 2) {    
            String item = new String(b, n, 2);    
            b2[n / 2] = (byte) Integer.parseInt(item, 16);    
        }    
        return b2;    
    }    
   
    /*   
     * (non-Javadoc)   
     *    
     * @see java.lang.Object#toString()   
     */   
    @Override   
    public String toString() {    
   
        byte[] b = null;    
        try {    
            b = encrypt(realKey.getBytes(), PASSWORD_CRYPT_KEY.getBytes());    
        } catch (Exception e) {    
   
            e.printStackTrace();    
        }    
        String encryptKey = byte2hex(b);    
        return encryptKey;    
    }    
   
    public static void main(String[] args) throws Exception {    
            
        /*   
         * 1.生成注册码   
         */   
        String data = new StringTest(false).toString();    
        System.out.println("未注册注册码:"+data);    
        System.out.println("未注册注册码长度:"+data.length());    
        /*   
         * 2.写入属性文件   
         */   
            
        Properties pro = null;    
            
        File file = new File("d:\\key.properties");    
        if(file.exists()){//文件已存在    
            InputStream in = new FileInputStream("d:\\key.properties");    
            pro = new Properties();    
            pro.load(in);    
            pro.setProperty("key.data", data);    
            OutputStream out = new FileOutputStream("d:\\key.properties");    
            pro.store(out, "key");    
        } else{//文件不存在    
            pro = new Properties();    
            pro.setProperty("key.data", data);    
            OutputStream out = new FileOutputStream("d:\\key.properties");    
            pro.store(out, "key");    
        }    
        /*   
         * 3.解密注册码   
         */   
        String decryptKey = new String(decrypt(hex2byte(data.getBytes()), PASSWORD_CRYPT_KEY    
                .getBytes()));    
        System.out.println("解密后注册码:" + decryptKey);    
        Date d = new Date();    
        d.setTime(Long.valueOf(decryptKey.substring(    
                decryptKey.lastIndexOf(":") + 1, decryptKey.length())));    
        DateFormat df = DateFormat.getDateInstance();    
        System.out.println("解密后有效信息:"+df.format(d));    
    }    
}    
