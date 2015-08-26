package com.pula.sms;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;

import pula.sys.util.HttpClientHelper;

public class SmsDemo {
    /**
     * 短信接口一，自写短信内容。该接口提交的短信均由人工审核，下发后请联系在线客服。适合：节假日祝福、会员营销群发等。
     */

    public static void sms_api1() {
        Map<String, String> para = new HashMap<String, String>();
        /**
         * 目标手机号码，多个以“,”分隔，一次性调用最多100个号码，示例：139********,138********
         */
        para.put("mob", "15821155653");
        /**
         * 微米账号的接口UID
         */
        para.put("uid", "vvOcK30SPG2x");
        /**
         * 微米账号的接口密码
         */
        para.put("pas", "86vq37m2");
        /**
         * 接口返回类型：json、xml、txt。默认值为txt
         */
        para.put("type", "json");
        /**
         * 短信内容。必须设置好短信签名，签名规范： 
         * 1、短信内容一定要带签名，签名放在短信内容的最前面；
         * 2、签名格式：【***】，签名内容为三个汉字以上（包括三个）；
         * 3、短信内容不允许双签名，即短信内容里只有一个“【】”
         * 
         */
        para.put("con", "【微米】您的验证码是：610912，3分钟内有效。如非您本人操作，可忽略本消息。by slf");
        try {
//            Client client = JerseyClientBuilder.newClient();
//            UriBuilder builder = new JerseyUriBuilder().uri("http://api.weimi.cc/2/sms/send.html");
//            URI uri = builder.buildFromMap(para);
//            Response resp = client.target(uri).request().get();
//            System.out.println(resp.getEntity().toString());
            System.out.println(HttpClientHelper.convertStreamToString(
                    HttpClientHelper.get("http://api.weimi.cc/2/sms/send.html",
                            para), "UTF-8"));
//            System.out.println(HttpClientHelper.convertStreamToString(
//                    HttpClientHelper.post(
//                            "http://api.weimi.cc/2/sms/send.html", para),
//                    "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 短信接口二，触发类模板短信接口，可以设置动态参数变量。适合：验证码、订单短信等。
     */
    @Test
    public static void sms_api2() {
        Map<String, String> para = new HashMap<String, String>();
        /**
         * 目标手机号码，多个以“,”分隔，一次性调用最多100个号码，示例：139********,138********
         */
        para.put("mob", "15921938585,15821155653");
        /**
         * 微米账号的接口UID
         */
        para.put("uid", "vvOcK30SPG2x");
        /**
         * 微米账号的接口密码
         */
        para.put("pas", "86vq37m2");
        /**
         * 接口返回类型：json、xml、txt。默认值为txt
         */
        para.put("type", "json");
        /**
         * 短信模板cid，通过微米后台创建，由在线客服审核。必须设置好短信签名，签名规范： 
         * 1、模板内容一定要带签名，签名放在模板内容的最前面；
         * 2、签名格式：【***】，签名内容为三个汉字以上（包括三个）；
         * 3、短信内容不允许双签名，即短信内容里只有一个“【】”
         */
        para.put("cid", "KmWVLyxmhW0p");
        /**
         * 传入模板参数。
         * 
         * 短信模板示例：
         * 【微米】您的验证码是：%P%，%P%分钟内有效。如非您本人操作，可忽略本消息。
         * 
         * 传入两个参数：
         * p1：610912
         * p2：3
         * 最终发送内容：
         * 【微米】您的验证码是：610912，3分钟内有效。如非您本人操作，可忽略本消息。
         */
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        para.put("p1", "李子田");
        para.put("p2", format.format(new Date()));
        para.put("p3", "文峰广场四楼");
        para.put("p4", "15921938585");
        para.put("p5", "Yuki");
        try {
//            HttpClient client = new HttpClient();
//            HttpMethod method = new GetMethod("http://api.weimi.cc/2/sms/send.html");
//            client.executeMethod(method);
            
            System.out.println(HttpClientHelper.convertStreamToString(
                    HttpClientHelper.get("http://api.weimi.cc/2/sms/send.html",
                            para), "UTF-8"));
//            System.out.println(HttpClientHelper.convertStreamToString(
//                    HttpClientHelper.get("http://api.weimi.cc/2/sms/send.html",
//                            para), "UTF-8"));
//            System.out.println(HttpClientHelper.convertStreamToString(
//                    HttpClientHelper.post(
//                            "http://api.weimi.cc/2/sms/send.html", para),
//                    "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] a) {
        // 测试短信接口一
//        sms_api1();
        
        // 测试短信接口二
        sms_api2();
    }

}
 
