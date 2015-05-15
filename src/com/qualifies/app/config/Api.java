package com.qualifies.app.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import org.json.JSONException;
import org.json.JSONObject;

public class Api {

    private static Context mContext;

    private static String url = "http://www.qualifes.com:80/app_api/v_1.03/api.php?service=";

    public static String url(String service) {
        return url + service;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static void dealSuccessRes(JSONObject response, Message msg) {
        if (!response.has("status")) {
            msg.what = 1;
            msg.obj = "连接服务器失败";
        } else {
            try {
                if (response.getInt("status") == 401) {
                    SharedPreferences sp = mContext.getSharedPreferences("user", Activity.MODE_PRIVATE);
                    sp.edit().remove("token").apply();
                }
                if (response.getInt("status") < 200 || response.getInt("status") >= 300)
                    msg.what = 1;
                else
                    msg.what = 0;
                msg.obj = Api.statuCode(response.getInt("status"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dealFailRes(Message msg) {
        msg.what = 1;
        msg.obj = "连接服务器失败";
    }

    public static String statuCode(int code) {
        switch (code) {
            case 100:
                return "失败";
            case 200:
                return "";
            case 201:
                return "正确";
            case 203:
                return "登录成功";
            case 204:
                return "注册成功";
            case 205:
                return "更新成功";
            case 206:
                return "发送成功";
            case 207:
                return "添加成功";
            case 208:
                return "删除成功";

            case 209:
                return "认证成功";

            case 210:
                return "取消成功";


            case 300:
                return "非法请求";

            case 301:
                return "请勿重复请求";


            case 400:
                return "网络错误";

            case 401:
                return "请重新登录";

            case 402:
                return "请求错误";

            case 403:
                return "错误";

            case 404:
                return "登录失败";

            case 405:
                return "注册失败";

            case 406:
                return "更新失败";

            case 407:
                return "发送失败";

            case 408:
                return "添加失败";

            case 409:
                return "删除失败";

            case 410:
                return "没有数据";

            case 411:
                return "异常错误";

            case 412:
                return "身份证认证失败";

            case 413:
                return "取消失败";

            case 414:
                return "支付方式有误";

            case 415:
                return "支付驱动异常错误";


            case 500:
                return "服务器错误";


            case 1000:
                return "新的设备登录，请短信验证";

            case 1001:
                return "手机号不能为空";

            case 1002:
                return "手机格式错误";

            case 1003:
                return "手机号不存在";

            case 1004:
                return "手机号已存在";

            case 1005:
                return "密码不能为空";

            case 1006:
                return "密码格式错误";

            case 1007:
                return "密码错误";

            case 1008:
                return "验证码不能为空";

            case 1009:
                return "验证码不存在或已过期";

            case 1010:
                return "验证码错误";

            case 1011:
                return "请先获取验证码";

            case 1012:
                return "该微信用户不存在";

            case 1013:
                return "收货人不能为空";

            case 1014:
                return "别名不能为空";

            case 1015:
                return "省份不能为空";

            case 1016:
                return "城市不能为空";

            case 1017:
                return "区县不能为空";

            case 1018:
                return "详细地址不能为空";

            case 1019:
                return "商品id不能为空";

            case 1020:
                return "详细地址不能为空";

            case 1021:
                return "该商品已收藏";

            case 1022:
                return "收藏id不能为空";

            case 1023:
                return "商品不存在或已删除";

            case 1024:
                return "新密码不能为空";

            case 1025:
                return "新密码格式错误";

            case 1026:
                return "新密码不能和旧密码一样";

            case 1027:
                return "关注商品id不能为空";

            case 1028:
                return "已经关注过";

            case 1029:
                return "名称不能为空";

            case 1030:
                return "品牌不能为空";

            case 1031:
                return "产地不能为空";

            case 1032:
                return "该记录已经存在";

            case 1033:
                return "反馈内容不能为空";

            case 1034:
                return "反馈内容长度错误";

            case 1035:
                return "收货地址不能超过6条";

            case 1036:
                return "购买数量超出库存数量";

            case 1037:
                return "购物车id不能为空";

            case 1038:
                return "请选择需要购买的商品";

            case 1039:
                return "请选择您的收货地址";

            case 1040:
                return "收货地址不存在";

            case 1041:
                return "收货地址不完整";

            case 1042:
                return "配送方式错误";

            case 1043:
                return "支付方式错误";

            case 1044:
                return "支付积分不能大于商品的积分数";

            case 1045:
                return "支付积分大于用户可用积分";

            case 1046:
                return "订单id不能为空";

            case 1047:
                return "订单不存在或已支付";

            case 1048:
                return "订单金额出错";

            case 1049:
                return "地址id不能为空";

            case 1050:
                return "您好，根据海关规定，含多个商品的订单金额不能超过1000元，请分多个订单提交结算。";

            case 1051:
                return "没有可配送的快递";

            case 1052:
                return "身份证号码不能为空";

            case 1053:
                return "身份证号码错误";

            case 1054:
                return "地区校验错误";

            case 1055:
                return "生日校验错误";

            case 1056:
                return "身份证号码错误";

            case 1057:
                return "姓名不能为空";

            case 1058:
                return "分类id不能为空";

            case 1059:
                return "该分类下没有属性或分类不存在";

            case 1060:
                return "已认证过";

            case 1061:
                return "订单id不能为空";

            case 1062:
                return "订单id错误";

            case 1063:
                return "记录id不能为空";

            case 1064:
                return "此帐号已经绑定过微信";

            case 1065:
                return "商品最低({###})件起售";

            case 1066:
                return "活动不存在或未启用";

            case 1067:
                return "推荐不存在或未启用";

            case 1068:
                return "（{###}）商品不在[{***}]地区销售范围";

            case 1069:
                return "pz_goods_number_nuit()";

            case 1070:
                return "商品已下架";

            case 1071:
                return "红包码不能为空";

            case 1072:
                return "红包码不存在";

            case 1073:
                return "红包已被其他人领取";

            case 1074:
                return "你已领取过该红包啦";

            case 1075:
                return "网站暂时关闭了红包功能";

            case 1076:
                return "红包不存在或已过期";

            case 1077:
                return "版本号不能为空";

            case 1078:
                return "网站暂时关闭了积分功能";

            case 10000:
                return "暂无数据";
        }
        return "";
    }
}
