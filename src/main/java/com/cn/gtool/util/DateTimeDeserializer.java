package com.cn.gtool.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class DateTimeDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize (JsonParser parser, DeserializationContext context) throws IOException {
        try {
            if(StringUtils.isNotBlank(parser.getText())){
                return DateUtils.parseDate(parser.getText(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
