package com.pula.sms;

import java.util.HashMap;
import java.util.Map;

public class AccountDemo {
    /**
     * 微米账号查询
     */
    public static void account_api() {
        Map<String, String> para = new HashMap<String, String>();
        /**
         * 微米账号的接口UID
         */
        para.put("uid", "<enter your UID>");
        /**
         * 微米账号的接口密码
         */
        para.put("pas", "<enter your UID Pass>");
        /**
         * 接口返回类型：json、xml、txt。默认值为txt
         */
        para.put("type", "json");
        try {
//            System.out.println(HttpClientHelper.convertStreamToString(
//                    HttpClientHelper.get("http://api.weimi.cc/2/account/balance.html", para), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] a) {
        // 测试账号查询API
        account_api();
    }
}