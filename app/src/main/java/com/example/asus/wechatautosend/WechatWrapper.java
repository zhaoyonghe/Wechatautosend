package com.example.asus.wechatautosend;

public class WechatWrapper {
    public static final String WECAHT_PACKAGENAME = "com.tencent.mm";

    public static class WechatClass{
        //微信首页
        public static final String WECHAT_CLASS_LAUNCHUI = "com.tencent.mm.ui.LauncherUI";
        //微信联系人页面
        public static final String WECHAT_CLASS_CONTACTINFOUI = "com.tencent.mm.plugin.profile.ui.ContactInfoUI";
        //标签页面
        public static final String WECHAT_CONTACT_LABEL = "com.tencent.mm.plugin.label.ui.ContactLabelManagerUI";
        //微信聊天页面
        public static final String WECHAT_CLASS_CHATUI = "com.tencent.mm.ui.chatting.ChattingUI";
        //标签联系人列表页面
        public static final String WECHAT_LABEL_EDIT = "com.tencent.mm.plugin.label.ui.ContactLabelEditUI";
        //收藏选择页面
        public static final String WECHAT_FAVUI = "com.tencent.mm.plugin.fav.ui.FavSelectUI";
        //收藏搜索页面
        public static final String WECHAT_FAV_SEARCH_UI = "com.tencent.mm.plugin.fav.ui.FavSearchUI";
        //群聊页面
        public static final String WECHAT_CHATROOMCONTACTUI = "com.tencent.mm.ui.contact.ChatroomContactUI";
    }


    public static class WechatId{
        /**
         * 通讯录界面
         */
        public static final String WECHATID_CONTACTUI_LISTVIEW_ID = "com.tencent.mm:id/ih";
        public static final String WECHATID_CONTACTUI_ITEM_ID = "com.tencent.mm:id/m_";
        public static final String WECHATID_CONTACTUI_NAME_ID = "com.tencent.mm:id/mc";

        /**
         * 聊天界面
         */
        public static final String WECHATID_CHATUI_EDITTEXT_ID = "com.tencent.mm:id/aie";
        public static final String WECHATID_CHATUI_USERNAME_ID = "com.tencent.mm:id/j6";
        public static final String WECHATID_CHATUI_RETURN_ID = "com.tencent.mm:id/j5";
        public static final String WECHATID_CHATUI_SWITCH_ID = "com.tencent.mm:id/aic";
        public static String WECHATID_PLUS_ID = "com.tencent.mm:id/aij";
        /**
         * 标签界面
         */
        public static String WECHATID_LABEL_LIST_ID = "com.tencent.mm:id/ayc";
        public static String WECHATID_LABEL_NAME_ID = "com.tencent.mm:id/ay9";
        public static String WECHATID_LABEL_NUM_ID = "com.tencent.mm:id/ay_";
        /**
         * 标签内联系人页面
         */
        public static String WECHAT_CONTACT_LIST_ID = "android:id/list";
        public static String WECHAT_CONTACT_PART_ID = "com.tencent.mm:id/cwt";
        public static String WECHAT_CONTACT_NAME_ID = "com.tencent.mm:id/dnr";
        /**
         * 收藏页面
         */
        public static String WECHAT_FAV_SELECT_ID = "com.tencent.mm:id/j1";
        public static String WECHAT_FAV_EDIT_ID = "com.tencent.mm:id/ji";
        public static String WECHAT_FAV_LIST_ID = "com.tencent.mm:id/bj9";
        public static String WECHAT_FAV_RETURN_ID = "com.tencent.mm:id/jc";
        /**
         * 群聊页面
         */
        public static String WECHAT_CHATROOM_LIST_ID = "com.tencent.mm:id/li";
        public static String WECHAT_CHATROOM_NAME_ID = "com.tencent.mm:id/m6";
    }

}
