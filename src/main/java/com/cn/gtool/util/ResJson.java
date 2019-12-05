package com.cn.gtool.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import lombok.Data;
import org.springframework.core.ParameterizedTypeReference;

import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class ResJson {

    public static final int STATUS_SUCCESS = 0;

    public static final int STATUS_FAIL = 1;

    public static final int STATUS_PART_SUCCESS = 3;

    // 会员API接口调用失败
    public static final int CUSTOMER_API_FAIL = 61;

    public static final int IMPORT_PROD_HAS_ERROR_ROW = 91;

    public static final String DESC_SERVER_FAIL = "操作异常,请联系管理员";

    public static final String DESC_SUCCESS = "success";

    public static final String SESSION_LOSE = "SESSION 失效！";
    public static final String COMPANY_ID_NOT_NULL = "公司编号不能为空";

    private static String activeProfile = null;

    static {
        //global close asm check for fastJson deserializer
        ParserConfig.getGlobalInstance().setAsmEnable(false);
    }

    private int retStatus;

    private String retDesc;

    private Object retData;

    /**
     * default successful
     *
     * @author wanghaiyang
     */
    public ResJson() {
        this.retStatus = STATUS_SUCCESS;
        this.retDesc = DESC_SUCCESS;
    }

    public ResJson(int retStatus, String retDesc) {
        this.retStatus = retStatus;
        this.retDesc = retDesc;
    }

    public ResJson(int retStatus, String retDesc, Object retData) {
        this.retStatus = retStatus;
        this.retDesc = retDesc;
        this.retData = retData;
    }

    public boolean isSuccess() {
        return STATUS_SUCCESS == retStatus;
    }

    public <T> T getRetObject(Class<T> clz) {
        try {
            if (isSuccess()) {
                if (retData != null) {
                    if (retData instanceof String) {
                        if (isJson((String) retData)) {
                            return JSON.parseObject((String) retData, clz);
                        }
                    } else {
                        return JSON.parseObject(JSON.toJSONString(retData), clz);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getRetObject(ParameterizedTypeReference<T> castType) {
        try {
            if (isSuccess()) {
                if (retData != null) {
                    if (retData instanceof String) {
                        if (isJson((String) retData)) {
                            return JSON.parseObject((String) retData, castType.getType());
                        }
                    } else {
                        return JSON.parseObject(JSON.toJSONString(retData), castType.getType());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> getRetObjectList(Class<T> clz) {
        try {
            if (isSuccess()) {
                if (retData != null) {
                    if (retData instanceof String) {
                        if (isJson((String) retData)) {
                            return JSON.parseArray((String) retData, clz);
                        }
                    } else {
                        return JSON.parseArray(JSON.toJSONString(retData), clz);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static boolean isJson(String test) {
        try {
            JSONObject.parseObject(test);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    public static ResJson success(Object retData) {
        return new ResJson(STATUS_SUCCESS, DESC_SUCCESS, retData);
    }

    public static ResJson success(Object retData, String msg) {
        return new ResJson(STATUS_SUCCESS, msg, retData);
    }

    public static ResJson success() {
        return new ResJson(STATUS_SUCCESS, DESC_SUCCESS, null);
    }

    public static ResJson successWhenNull(Object retData, String nullMessage) {
        ResJson result = new ResJson(STATUS_SUCCESS, DESC_SUCCESS, retData);
        if (retData == null || (retData instanceof Collection && ((Collection) retData).isEmpty()) || (
                retData instanceof Map && ((Map) retData).isEmpty())) {
            result.setRetDesc(nullMessage);
        }
        return result;
    }

    public static ResJson failed(final int status, final String msg) {
        return new ResJson(status, msg, null);
    }

    public static ResJson failed(final int status, final String msg, final Object data) {
        return new ResJson(status, msg, data);
    }

    public static ResJson failed(final String msg) {
        return new ResJson(ResJson.STATUS_FAIL, msg);
    }

    public static ResJson failed(Exception exception) {
        return failed(DESC_SERVER_FAIL, exception);
    }

    public static ResJson failed(String localMessage, Exception exception) {

        if (exception instanceof SocketTimeoutException) {
            localMessage = DESC_SERVER_FAIL;
        }
        return new ResJson(ResJson.STATUS_FAIL, localMessage, exception.getLocalizedMessage());
    }
}
