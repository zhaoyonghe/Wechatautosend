package com.example.asus.wechatautosend;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import static com.example.asus.wechatautosend.WechatUtils.performClick;

public class AutoSendMsgService extends AccessibilityService {
    public static String LABELNAME, COLLECTNAME;
    public static Boolean CONTACTDONE = false;
    public static Boolean GROUPDONE = false;
    public static Boolean SENT = false;
    private static int contactnum = -1, contactcuridx = -1;
    private static int groupnum = -1, groupcuridx = -1;
    private static List<String> contactnamelist = null;

    private static List<String> groupnamelist = null;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventtype = event.getEventType();

        if (eventtype == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            String  curactivity = event.getClassName().toString();
            Log.v("actname", curactivity);
            if (CONTACTDONE && GROUPDONE) {//都发完了，挂机
                Log.d("fuckyou", "fuckyou");
            }else if (!CONTACTDONE){//没发完，先发联系人
                Log.d("groupgroup", String.valueOf(GROUPDONE));
                //首页和联系人页
                if (curactivity.equals(WechatWrapper.WechatClass.WECHAT_CLASS_LAUNCHUI)) {
                    getInLabel();
                    sleep();
                }else if(curactivity.equals(WechatWrapper.WechatClass.WECHAT_CONTACT_LABEL)) {
                    contactnum = WechatUtils.findLabelNameAndClick(this, LABELNAME);
                    Log.v("数量", String.valueOf(contactnum));
                }else if (curactivity.equals(WechatWrapper.WechatClass.WECHAT_LABEL_EDIT)){
                    getInContact();
                }else if (curactivity.equals(WechatWrapper.WechatClass.WECHAT_CLASS_CONTACTINFOUI)){
                    sleep();
                    WechatUtils.findTextAndClick(this, "发消息");
                    //说明这个人有没有被发送过
                    SENT = false;
                }else if (curactivity.equals(WechatWrapper.WechatClass.WECHAT_CLASS_CHATUI)) {
                    if (!SENT){
                        getInFav();
                    }else {
                        if (contactcuridx == contactnamelist.size()){
                            Log.v("结束", "??");
                            CONTACTDONE = true;
                        }
                        WechatUtils.findIdAndClick(this, WechatWrapper.WechatId.WECHATID_CHATUI_RETURN_ID);
                    }
                }else if (curactivity.equals(WechatWrapper.WechatClass.WECHAT_FAVUI)){
                    if (!SENT){
                        //点击搜索
                        WechatUtils.findIdAndClick(this, WechatWrapper.WechatId.WECHAT_FAV_SELECT_ID);
                        sleep();
                    }else {
                        WechatUtils.findIdAndClick(this, WechatWrapper.WechatId.WECHAT_FAV_RETURN_ID);
                    }
                }else if (curactivity.equals(WechatWrapper.WechatClass.WECHAT_FAV_SEARCH_UI)){
                    if (!SENT){
                        sendMsg();
                        ++contactcuridx;
                    }
                }
            } else if (!GROUPDONE){//联系人发完了，发群
                if (curactivity.equals(WechatWrapper.WechatClass.WECHAT_CLASS_LAUNCHUI)){
                    getInGroupChatContact();
                } else if(curactivity.equals(WechatWrapper.WechatClass.WECHAT_CHATROOMCONTACTUI)){
                    Log.d("=","++++++++++++++++++++++++++++++++++++");
                    Log.d("smile",String.valueOf(this.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aig").size()));
                    Log.d("bitch",String.valueOf(this.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/v_").size()));
                    Log.d("=","++++++++++++++++++++++++++++++++++++");
                    if (this.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/v_").size() == 0){
                        SENT = false;
                        getInGroupChat();
                    }else{
                        if (!SENT){
                            getInFav();
                        } else{
                            if (groupcuridx == groupnamelist.size()){
                                GROUPDONE = true;
                            }
                            WechatUtils.findIdAndClick(this, WechatWrapper.WechatId.WECHATID_CHATUI_RETURN_ID);
                        }
                    }
                    /*
                    Log.d("fucking", "fucking1");

                    if(groupnamelist == null){
                        groupnamelist = WechatUtils.getAllGroupName(this);
                        groupcuridx = 0;
                        for (String s: groupnamelist){
                            Log.d("jiba", s);
                        }
                    }
                    Log.v("这一个group", groupnamelist.get(groupcuridx));
                    //WechatUtils.findGroupNameAndClick(this, groupnamelist.get(groupcuridx));
                    AccessibilityNodeInfo accessibilityNodeInfo = this.getRootInActiveWindow();
                    // 获取列表结点，该结点为scrollable结点，以便后续滑动页面，获得并没有在屏幕上显示的结点
                    AccessibilityNodeInfo list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_CHATROOM_LIST_ID).get(0);
                    while(true){
                        // 真正的获得当前显示的所有联系人的结点信息
                        List<AccessibilityNodeInfo> namelist = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_CHATROOM_NAME_ID);
                        for (AccessibilityNodeInfo nodeInfo:namelist){
                            if (groupnamelist.get(groupcuridx).equals(nodeInfo.getText().toString())){
                                WechatUtils.findTextAndClick(this, nodeInfo.getText().toString());
                                //performClick(nodeInfo);
                                return;
                            }
                        }
                        // 如果没找到，说明当前没显示出来，向上滑
                        list.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    }

                    //SENT = false;
                    //Log.d("fucking", "fucking3");*/
                } else if (curactivity.equals(WechatWrapper.WechatClass.WECHAT_CLASS_CHATUI)){
                    Log.d("=","++++++++++++++++++++++++++++++++++++");
                    Log.d("smile",String.valueOf(this.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aig")));
                    Log.d("=","++++++++++++++++++++++++++++++++++++");
                    Log.d("===", "=======================================");
                    Log.d("swnt", String.valueOf(SENT));
                    Log.d("===", "=======================================");
                    if (!SENT){
                        getInFav();
                    } else{
                        if (groupcuridx == groupnamelist.size()){
                            GROUPDONE = true;
                        }
                        WechatUtils.findIdAndClick(this, WechatWrapper.WechatId.WECHATID_CHATUI_RETURN_ID);
                    }
                }else if (curactivity.equals(WechatWrapper.WechatClass.WECHAT_FAVUI)){
                    if (!SENT){
                        //点击搜索
                        WechatUtils.findIdAndClick(this, WechatWrapper.WechatId.WECHAT_FAV_SELECT_ID);
                        sleep();
                    }else {
                        WechatUtils.findIdAndClick(this, WechatWrapper.WechatId.WECHAT_FAV_RETURN_ID);
                    }
                }else if (curactivity.equals(WechatWrapper.WechatClass.WECHAT_FAV_SEARCH_UI)){
                    if (!SENT){
                        sendMsg();
                        ++groupcuridx;
                    }
                }
            }
        }
    }
    /**
     * 选择收藏并发送
     */
    public void sendMsg(){
        //输入收藏标签名称
        //WechatUtils.findIdAndPast(this, WechatWrapper.WechatId.WECHAT_FAV_EDIT_ID, COLLECTNAME);
        Log.v("收藏名称", COLLECTNAME);
        while (true){
            Boolean ch = WechatUtils.findTextAndClick(this, COLLECTNAME);
            if (ch){
                break;
            }
        }
        sleep();
        //点击收藏 默认点第一个
        WechatUtils.clickFav(this);
        sleep();
        //点击发送
        WechatUtils.findTextAndClick(this, "发送");
        SENT = true;

        Log.v("IDX", String.valueOf(contactcuridx));

    }
    /**
     * 进入收藏页面
     */
    public void getInFav(){
        Log.d("hhhhh","hhhhh1");
        //点击加号
        WechatUtils.findIdAndClick(this, WechatWrapper.WechatId.WECHATID_PLUS_ID);
        sleep();
        //点击我的收藏
        WechatUtils.findTextAndClick(this, "我的收藏");
        Log.d("hhhhh","hhhhh2");
    }
    /**
     * 点进具体联系人页面
     */
    public void getInContact(){
        if (contactnamelist == null && contactnum != -1) {
            contactnamelist = WechatUtils.getAllContactName(this, contactnum);
            contactcuridx = 0;
            for (String s:contactnamelist){
                Log.v("名字", s);
            }
        }
        Log.v("这一个", contactnamelist.get(contactcuridx));
        WechatUtils.findContactNameAndClick(this, contactnamelist.get(contactcuridx));
    }

    /**
     * 点进具体群聊
     */
    public void getInGroupChat(){
        if(groupnamelist == null){
            groupnamelist = WechatUtils.getAllGroupName(this);
            groupcuridx = 0;
            for (String s: groupnamelist){
                Log.d("jiba", s);
            }
        }
        Log.v("这一个group", groupnamelist.get(groupcuridx));
        WechatUtils.findGroupNameAndClick(this, groupnamelist.get(groupcuridx));
    }

    /**
     * 点进标签页面
     */
    public void getInLabel(){
        //先点到通讯录
        WechatUtils.findTextAndClick(this, "通讯录");
        sleep();
        //再点一次到顶部
        WechatUtils.findTextAndClick(this, "通讯录");
        //点进标签
        sleep();
        WechatUtils.findTextAndClick(this, "标签");
    }

    /**
     * 点进群聊列表页面
     */
    public void getInGroupChatContact(){
        //先点到通讯录
        WechatUtils.findTextAndClick(this, "通讯录");
        sleep();
        //再点一次到顶部
        WechatUtils.findTextAndClick(this, "通讯录");
        //点进群聊
        sleep();
        WechatUtils.findTextAndClick(this, "群聊");
    }



    public void sleep() {
        try {
            Thread.currentThread().sleep(150);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onInterrupt() {

    }

    public static void setContactnum(int contactnum) {
        AutoSendMsgService.contactnum = contactnum;
    }

    public static void setContactcuridx(int contactcuridx) {
        AutoSendMsgService.contactcuridx = contactcuridx;
    }

    public static void setGroupnum(int groupnum) {
        AutoSendMsgService.groupnum = groupnum;
    }

    public static void setGroupcuridx(int groupcuridx) {
        AutoSendMsgService.groupcuridx = groupcuridx;
    }

    public static void setContactnamelist(List<String> contactnamelist) {
        AutoSendMsgService.contactnamelist = contactnamelist;
    }

    public static void setGroupnamelist(List<String> groupnamelist) {
        AutoSendMsgService.groupnamelist = groupnamelist;
    }
}
