package com.util;
import java.util.Map;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;


import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;


/**
 * 
 * @ClassName: MessagePushUtil
 * @Description: TODO(消息推送工具类)
 * @author llj
 * @date 2015年12月22日 下午2:36:45
 *
 */
public class JpushUtil{
	protected static Log LOG = LogFactory.getLog(JpushUtil.class);
	private static final String MASTER_SECRET = "7031489f63fbacb81a7bbfb0";
	private static final String APP_KEY = "da6e9368bcbb50d2d0436b6b";
	private static PushPayload pushPayload ;
	private static Builder builder; 
	static {
	builder = PushPayload.newBuilder();
	pushPayload = builder
	.setNotification(Notification.alert(""))
	.setAudience(Audience.all())
	.setPlatform(Platform.all())
	.setOptions(Options.newBuilder().setTimeToLive(0L).build())
	.build();
	}
	public static final String BEEN_CONCERNED_ALERT = "您有一个新粉丝";
	
	public static final String SOUND = "default.mp3";
	
	public static final int BADGE = 1;
	/**
	* 
	* @Title: pushMessage 
	* @Description: TODO(推送消息) 
	* @param @param json
	* @param @return    设定文件 
	* @return HttpResponse    返回类型 
	* @throws
	*/
	public static PushResult sendPush(PushPayload payload) throws APIConnectionException, APIRequestException{
		JPushClient jPushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		return jPushClient.sendPush(payload);
	}
	
	/** 
	* @Title: sendPushTryCatch 
	* @Description: try catch 推送
	* @param @param payload
	* @param @param logger
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public static void sendPushTryCatch(PushPayload payload, Log logger) {
		JPushClient jPushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		try {
		PushResult pushResult = jPushClient.sendPush(payload);
		logger.info("返回结果" + pushResult);
		} catch (APIConnectionException e) {
		logger.error("连接错误，稍后尝试" + e);
		} catch (APIRequestException e) {
		logger.error("极光推送内部错误", e);
		logger.info("网络请求状态" + e.getStatus());
		logger.info("错误状态码" + e.getErrorCode());
		logger.info("错误信息" + e.getErrorMessage());
		logger.info("信息ID" + e.getMsgId());
		logger.info("极光推送错误信息:" + e.getErrorMessage());
		}
	}
	
	/**
	* 
	* @Title: buildPushObjectAllAllAlert 
	* @Description: TODO(快捷地构建推送对象：所有平台，所有设备，内容为 alert 的通知) 
	* @param @param alert
	* @param @return    设定文件 
	* @return PushPayload    返回类型 
	* @throws
	*/
	@SuppressWarnings("static-access")
	public static PushPayload buildPushObjectAllAllAlert(String alert) {
	   return pushPayload.alertAll(alert);
	}
	
	/** 
	* @Title: buildPushObjectAliasAlert 
	* @Description: TODO(所有平台，推送目标是别名为 alias，通知内容为 alert) 
	* @param @param alert
	* @param @param alias
	* @param @return    设定文件 
	* @return PushPayload    返回类型 
	* @throws 
	*/
	public static PushPayload buildPushObjectAliasAlert(String alert, String... alias) {
		return builder
		.setPlatform(Platform.android_ios())
		.setAudience(Audience.alias(alias))
		.setNotification(Notification.newBuilder()
		.setAlert(alert)
		.addPlatformNotification(
		AndroidNotification.newBuilder()
		.addExtra("sign", "5")
		.build())
		.addPlatformNotification(IosNotification.newBuilder()
		.addExtra("sign", "5")
		.build())
		.build())
		.build();
	}
/**
* 
* @Title: buildPushObjectIos 
* @Description: TODO(iOS推送 badge  sound) 
* @param @param alias
* @param @param alert
* @param @param badge
* @param @return    设定文件 
* @return PushPayload    返回类型 
* @throws
*/
public static PushPayload buildPushObjectIosAndroid(Map<String, String> params,
    String[] alias, String alert, int badge, String sound, String msgContent) {
       return builder
               .setPlatform(Platform.android_ios())
               .setAudience(Audience.alias(alias))
               .setNotification(Notification.newBuilder()
               .addPlatformNotification(IosNotification.newBuilder()
               .setAlert(alert)
               .setBadge(badge)
               .addExtras(params)
               .setSound(sound)
               .build())
               .addPlatformNotification(AndroidNotification.newBuilder()
               .setAlert(alert)
               .addExtras(params)
               .build())
               .build())
               .setMessage(Message.newBuilder()
               .setMsgContent(msgContent)
               .build())
               .build();
   }
 
    /** 
    * @Title: buildPushObjectAllAliasAlert 
    * @Description: TODO(所有平台，推送目标是别名为 alias，通知标题为 title，通知内容为 alert) 
    * @param @param params
    * @param @param alias
    * @param @return    设定文件 
    * @return PushPayload    返回类型 
    * @throws 
    */
public static PushPayload buildPushObjectAllAliasAlert(Map<String, String> params, String alert, String title, String... alias) {
    return builder
                .setPlatform(Platform.android_ios()) 
                .setAudience(Audience.alias(alias))  
                .setNotification(Notification.newBuilder()  
                        .setAlert(alert)
                        .addPlatformNotification(AndroidNotification.newBuilder()  
                                .setTitle(title)
                                .addExtras(params)
                                .build())  
                        .addPlatformNotification(IosNotification.newBuilder()
                        .addExtras(params)
                                .build())  
                        .build())  
                .build();  
    }


    /** 
    * @Title: buildPushObjectAndroidTagAlertWithTitle 
    * @Description: TODO(平台是 Android，目标是 tag 为 tag 的设备，内容是 Android 通知 alert，并且标题为 title) 
    * @param @param tag
    * @param @param alert
    * @param @param title
    * @param @return    设定文件 
    * @return PushPayload    返回类型 
    * @throws 
    */
public static PushPayload buildPushObjectAndroidTagAlertWithTitle(String tag, String alert, String title) {
        return builder
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(tag))
                .setNotification(Notification.android(alert, title, null))
                .build();
    }
    
    /** 
    * @Title: buildPushObjectIosTagAndAlertWithExtrasAndMessage 
    * @Description: TODO(
    * 构建推送对象：平台是 iOS，推送目标是 tag, tagAll 的交集，
    * 推送内容同时包括通知与消息 - 通知信息是 alert，角标数字为 number，
    * 消息内容是 msgContent。
    * 通知是 APNs 推送通道的，消息是 JPush 应用内消息通道的。
    * APNs 的推送环境是“开发”（如果不显式设置的话，Library 会默认指定为开发）
    * True 表示推送生产环境，False 表示要推送开发环境 
    *  )
    * @param @param tag
    * @param @param tagAll
    * @param @param number
    * @param @param alert
    * @param @param msgContent
    * @param @return    设定文件 
    * @return PushPayload    返回类型 
    * @throws 
    */
    public static PushPayload buildPushObjectIosTagAndAlertWithExtrasAndMessage(
    String tag, String tagAll, int number, String alert, String msgContent) {
        return builder
        .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and(tag, tagAll))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(alert)
                                .setBadge(number)
                                .build())
                        .build())
                 .setMessage(Message.content(msgContent))
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(false)
                         .build())
                 .build();
    }
    
    /** 
     * 构建推送对象：平台是 Andorid 与 iOS，
    * 推送目标是 （tag1 与 tag2 的并集），
    * 推送内容是 - 内容为 msgContent 的消息
    * @Title: buildPushObjectIosAudienceMoreMessageWithExtras 
    * @Description: TODO() 
    * @param @param tag1
    * @param @param tag2
    * @param @param msgContent
    * @param @return    设定文件 
    * @return PushPayload    返回类型 
    * @throws 
    */
    public static PushPayload buildPushObjectIosAudienceMoreMessageWithExtras(
    String tag1, String tag2, String msgContent) {
        return builder
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag(tag1, tag2))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .build())
                .build();
    }
}