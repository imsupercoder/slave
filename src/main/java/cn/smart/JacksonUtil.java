package cn.smart;

/**
 * Created by user on 2017/7/22.
 */
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * bean转json格式或者json转bean格式, 项目中我们通常使用这个工具类进行json---java互相转化
 */
public class JacksonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object obj)  {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e){

        }
        return "";
    }

    public static <T> T json2Bean(String jsonStr, Class<T> objClass)
            throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(jsonStr, objClass);
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("key","中文");
        System.out.println(JacksonUtil.toJson(map));
    }
}
