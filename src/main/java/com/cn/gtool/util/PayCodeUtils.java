package com.cn.gtool.util;

import java.util.UUID;

/**
 * @Auther: yg
 * @Date: 2019/12/10 11:26
 * @Description: 支付码生成
 */
public class PayCodeUtils {

    public static String generatePayCode(int payType){
        String fix = "";
        if (payType ==1){//日卡
            fix = "D-";
        }
        if (payType ==2){//周卡
            fix = "W-";
        }
        if (payType ==3){//月卡
            fix = "M-";
        }
        return fix + UUID.randomUUID().toString().substring(24);
    }

    public static void main(String[] args) {
        System.out.println("generatePayCode(1) = " + generatePayCode(2));
    }
}
