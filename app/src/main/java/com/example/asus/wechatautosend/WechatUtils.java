package com.example.asus.wechatautosend;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WechatUtils {
    /**
     * 在当前窗口显示的结点中找名字是text的，点击之
     * @param accessibilityService
     * @param text
     * @return 如果点击成功， 返回true；如果由于某种原因找不到这样的结点，或者点不了，返回false
     */
    public static Boolean findTextAndClick(AccessibilityService accessibilityService, String text){
        // 获取当前活动窗口根结点
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null){
            return false;
        }
        // 获取当前活动窗口根结点的全部名字里有text的子结点的信息
        List<AccessibilityNodeInfo> allnode = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        Log.v("num", String.valueOf(allnode.size()));
        if (allnode != null && !allnode.isEmpty()){
            // 遍历子结点
            for (AccessibilityNodeInfo node: allnode){
                // 必须找到确实名字叫text的子结点，点击之，而不是仅名字里带text的
                if (node != null && (text.equals(node.getText().toString()))) {
                    Boolean check = performClick(node);
                    if (check){
                        return true;
                    }
                }
            }
            return false;
        } else {
            Log.v("notfound", text);
            return false;
            //findTextAndClick(accessibilityService, text);
        }
    }

    /**
     * 在当前窗口显示的结点中找resourceid是id的，点击之
     * @param accessibilityService
     * @param id
     */
    public static void findIdAndClick(AccessibilityService accessibilityService, String id){
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null){
            return;
        }
        List<AccessibilityNodeInfo> allnode = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        Log.v("idnum", String.valueOf(allnode.size()));
        if (allnode != null && !allnode.isEmpty()){
            for (AccessibilityNodeInfo nodeInfo:allnode){
                if (nodeInfo != null) {
                    Boolean check = performClick(nodeInfo);
                    if (check){
                        break;
                    }
                }
            }
        }else{
            Log.v("idnotfound", id);
        }
    }

    /**
     * 在标签联系人页面，获取叫name的联系人，点击之
     * @param accessibilityService
     * @param name
     */
    public static void findContactNameAndClick(AccessibilityService accessibilityService, String name){
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        // 获取列表结点，该结点为scrollable结点，以便后续滑动页面，获得并没有在屏幕上显示的结点
        AccessibilityNodeInfo list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_CONTACT_LIST_ID).get(0);
        while (true){
            // 真正的获得当前显示的所有联系人的结点信息
            List<AccessibilityNodeInfo> namelist = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_CONTACT_NAME_ID);
            for (AccessibilityNodeInfo nodeInfo:namelist){
                if (name.equals(nodeInfo.getText().toString())){
                    performClick(nodeInfo);
                    return;
                }
            }
            // 如果没找到，说明当前没显示出来，向上滑
            list.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
    }

    /**
     * 在群聊联系人页面，获取叫name的群聊，点击之
     * @param accessibilityService
     * @param name
     */
    public static void findGroupNameAndClick(AccessibilityService accessibilityService, String name){
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        // 获取列表结点，该结点为scrollable结点，以便后续滑动页面，获得并没有在屏幕上显示的结点
        AccessibilityNodeInfo list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_CHATROOM_LIST_ID).get(0);
        while(true){
            // 真正的获得当前显示的所有联系人的结点信息
            List<AccessibilityNodeInfo> namelist = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_CHATROOM_NAME_ID);
            for (AccessibilityNodeInfo nodeInfo:namelist){
                if (name.equals(nodeInfo.getText().toString())){
                    performClick(nodeInfo);
                    return;
                }
            }
            // 如果没找到，说明当前没显示出来，向上滑
            list.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
    }

    //TODO
    /**
     *
     * @param accessibilityService
     * @param name
     * @return
     */
    public static int findLabelNameAndClick(AccessibilityService accessibilityService, String name){
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        AccessibilityNodeInfo labellist = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHATID_LABEL_LIST_ID).get(0);
        Log.v("childnum", String.valueOf(labellist.getChildCount()));
        while(true){
            List<AccessibilityNodeInfo> namelist = labellist.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHATID_LABEL_NAME_ID);
            List<AccessibilityNodeInfo> numlist = labellist.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHATID_LABEL_NUM_ID);
            for(int i = 0; i < namelist.size(); ++i){
                Log.v("labelname", namelist.get(i).getText().toString());
                if (name.equals(namelist.get(i).getText().toString())){
                    performClick(namelist.get(i));
                    String temp = numlist.get(i).getText().toString();
                    return Integer.valueOf(temp.substring(1, temp.length()-1));
                }
            }
            if (labellist.isScrollable()) {
                labellist.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
        }
    }

    /**
     * 在标签联系人页面，获取所有人人名（仅在所有人名字都一一不同的时候才能正常执行）
     * @param accessibilityService
     * @param num
     * @return 标签联系人页面所有人人名列表
     */
    public static List<String> getAllContactName(AccessibilityService accessibilityService, int num){
        HashSet<String> res = new HashSet<>();
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        AccessibilityNodeInfo list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_CONTACT_LIST_ID).get(0);
        //TODO 需要异常处理
        while (res.size() < num){
            List<AccessibilityNodeInfo> namelist = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_CONTACT_NAME_ID);
            for (AccessibilityNodeInfo nodeInfo:namelist){
                res.add(nodeInfo.getText().toString());
            }
            list.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
        List<String> r = new ArrayList<>();
        r.addAll(res);
        return r;
    }

    //TODO
    /**
     *
     * @param accessibilityService
     * @return
     */
    public static List<String> getAllGroupName(AccessibilityService accessibilityService){
        HashSet<String> res = new HashSet<>();
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        AccessibilityNodeInfo list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_CHATROOM_LIST_ID).get(0);
        int sizelasttime = 0;
        int sizesametimes = 0;
        while(true){
            List<AccessibilityNodeInfo> namelist = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_CHATROOM_NAME_ID);
            for (AccessibilityNodeInfo nodeInfo: namelist){
                res.add(nodeInfo.getText().toString());
            }
            list.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            if(sizelasttime == res.size()){
                if (sizesametimes > 5){
                    break;
                } else {
                    sizesametimes++;
                    continue;
                }
            }
            sizelasttime = res.size();
            sizesametimes = 0;
        }
        List<String> r = new ArrayList<>();
        r.addAll(res);
        return r;
    }

    //TODO
    /**
     *
     * @param accessibilityService
     * @param id
     * @param text
     */
    public static void findIdAndPast(AccessibilityService accessibilityService, String id, String text){
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        List<AccessibilityNodeInfo> editnode = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (editnode != null && !editnode.isEmpty()){
            Bundle b = new Bundle();
            b.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            Boolean ch = editnode.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, b);
            Log.v("粘贴情况", String.valueOf(ch));
        }else{
            Log.v("idnotfound", id);
        }
    }

    /**
     * 发送搜索到的第一条收藏
     * @param accessibilityService
     */
    public static void clickFav(AccessibilityService accessibilityService){
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null){
            Log.v("什么bug", "??");
            return;
        }
        List<AccessibilityNodeInfo> nodeInfo = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(WechatWrapper.WechatId.WECHAT_FAV_LIST_ID);
        if (nodeInfo != null && !nodeInfo.isEmpty()){
            //默认发送搜索到的第一条收藏
            AccessibilityNodeInfo fav = nodeInfo.get(0).getChild(1);
            Boolean ch = performClick(fav);
            if (!ch){
                Log.v("没点到", "??");
            }else {
                Log.v("点到了", "??");
            }
        }else {
            Log.v("收藏list没找到", "cc");
            //clickFav(accessibilityService);
        }
    }

    // TODO
    /**
     *
     * @param accessibilityNodeInfo
     */
    public static void recycle(AccessibilityNodeInfo accessibilityNodeInfo){
        if (accessibilityNodeInfo.getChildCount() == 0){
            if (accessibilityNodeInfo.getViewIdResourceName() != null){
                Log.v("resid", accessibilityNodeInfo.getViewIdResourceName());
            }
        }else {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); ++i){
                if (accessibilityNodeInfo.getChild(i) != null){
                    recycle(accessibilityNodeInfo.getChild(i));
                }
            }
        }
    }

    /**
     * 能点就点，点不了就点父节点
     * @param accessibilityNodeInfo
     * @return 点到了，返回true；无论如何都点不到，返回false
     */
    public static Boolean performClick(AccessibilityNodeInfo accessibilityNodeInfo){
        if (accessibilityNodeInfo != null){
            if (accessibilityNodeInfo.isClickable()){
                return accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }else {
                try{
                    Log.d("qqqqq",accessibilityNodeInfo.getParent().getText().toString());
                } catch (Exception e){

                }
                return performClick(accessibilityNodeInfo.getParent());
            }
        }
        return false;
    }
}
