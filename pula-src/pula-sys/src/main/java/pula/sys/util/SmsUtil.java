/**
 * 
 */
package pula.sys.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pula.sys.domains.Audition;

/**
 * @author Liangfei
 *
 */
public class SmsUtil {

    private static final Logger logger = LoggerFactory.getLogger(SmsUtil.class);
    
    public static class SendResult {
        public boolean succeed;
        public String message;
        
        public SendResult(boolean ok, String msg) {
            this.succeed = ok;
            this.message = msg;
        }
    }
    
    /**
     * 短信接口二，触发类模板短信接口，可以设置动态参数变量。适合：验证码、订单短信等。
     */
    public static SendResult SendBookingMessage(Audition audition) {
        Map<String, String> para = new HashMap<String, String>();
        /**
         * 目标手机号码，多个以“,”分隔，一次性调用最多100个号码，示例：139********,138********
         */
        para.put("mob", audition.getPhone());
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
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (audition.getBranch() == null) {
            return new SendResult(false, "没有分支机构！");
        }
        String branchAddres = audition.getBranch().getAddress();
        // "浦东新区 张杨北路801号(近五莲路)文峰广场四楼"
        if (StringUtils.isEmpty(branchAddres)) {
            return new SendResult(false, "没有分支机构地址！");
        }
        String mobile = audition.getBranch().getMobile();
        if (StringUtils.isEmpty(mobile)) {
            mobile = "13661663024";
        }
        String contactor = audition.getBranch().getLinkman();
        if (!StringUtils.isEmpty(contactor)) {
            contactor = "Yuki";
        }
        
        para.put("p1", audition.getParent());
        para.put("p2", audition.getPlan1());
        para.put("p3", branchAddres);
        para.put("p4", mobile);
        para.put("p5", contactor);

        try {
//            HttpClient client = new HttpClient();
//            HttpMethod get = new GetMethod("http://api.weimi.cc/2/sms/send.html");
//            HttpMethodParams paras = new HttpMethodParams();
//            for(Entry<String,String> e : para.entrySet()) {
//                paras.setParameter(e.getKey(), e.getValue());
//            }
//            get.setParams(paras);
//            client.executeMethod(get);
            String respBody = HttpClientHelper.convertStreamToString(
                    HttpClientHelper.get("http://api.weimi.cc/2/sms/send.html", para), "UTF-8");
            if (respBody.contains("短信接口调用成功")) {
                return new SendResult(true, respBody);
            } else {
                return new SendResult(false, respBody);
            }
        } catch (Exception e) {
            logger.error("发送短信失败！ ", e);
            return new SendResult(false, e.getMessage());
        }
    }

}
