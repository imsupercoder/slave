package cn.smart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/7/21.
 */
@Service
public class Custom {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String,Object> calculate(List<Map<String,Object>> row){
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("msg","work done.");
        return result;
    }
    public List<Map<String,Object>> getData(){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        Map<String,Object> rowMap;
        List<Map<String,Object>> rowData;
        for(int i=0;i<20;i++){
            rowMap = new HashMap<String, Object>();
            rowMap.put("rowTitle","数据行"+(i+1));
            rowData = jdbcTemplate.queryForList("select * from USER ");
            rowMap.put("rowData",rowData);
            result.add(rowMap);
        }
        return result;
    }
}
