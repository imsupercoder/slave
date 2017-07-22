package cn.smart.controller;

import cn.smart.Custom;
import cn.smart.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by user on 2017/7/21.
 */
@RestController
@RequestMapping("/cal")
public class Caculate {

    public static ConcurrentHashMap<String,Map<String,Object>> states = new ConcurrentHashMap<String, Map<String, Object>>();
    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    private Custom custom;

    @RequestMapping(value = "/start",method = RequestMethod.POST)
    public String start(){
        ExecutorService pool = Executors.newFixedThreadPool(3);
        String batchNo = getBatchNo();
        List<Map<String,Object>> list = custom.getData();
        for(Map<String,Object> row : list){
            Map<String,Object> state = new HashMap<String, Object>();
            String uid = UUID.randomUUID().toString();
            state.put("startTime",new Date());
            state.put("title",row.get("rowTitle"));
            state.put("batchNo",batchNo);
            state.put("status","运行中");
            state.put("rows",0);
            state.put("result","");
            state.put("total",((List<Map<String,Object>>)row.get("rowData")).size());
            pool.submit(new Worker((List<Map<String,Object>>)row.get("rowData"),custom,uid));
            states.put(uid,state);
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("flag","true");
        return JacksonUtil.toJson(map);
    }

    @RequestMapping(value = "/status",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String status(){
        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
        for(Map.Entry<String,Map<String,Object>> entry : states.entrySet()){
            Map<String,Object> state = new HashMap<String, Object>();
            state.putAll(entry.getValue());
            Date start = (Date) state.get("startTime");
            long diff = new Date().getTime()-start.getTime();
            long hours = diff/(60*60*1000);
            long minutes = diff/(60*1000)-hours*60;
            long seconds = diff/1000-hours*60*60-minutes*60;
            state.put("cost",hours+"小时"+minutes+"分钟"+seconds+"秒");
            result.add(state);
        }
        return JacksonUtil.toJson(result);
    }

    public String getBatchNo(){
        return dateFormat.format(new Date());
    }
}
class Worker implements Runnable {

    private List<Map<String,Object>> rowData;
    private Custom custom;
    private String uid;

    public Worker(List<Map<String,Object>> rowData,Custom custom,String uid){
        this.rowData = rowData;
        this.custom = custom;
        this.uid = uid;
    }

    @Override
    public void run() {
        int rows = 0;
        try {
            for (Map<String,Object> map :rowData){
                TimeUnit.SECONDS.sleep(1);
                Caculate.states.get(uid).put("rows",++rows);
                throw new RuntimeException("试错");
            }
        } catch (Exception e){
            Caculate.states.get(uid).put("result","计算失败:"+JacksonUtil.toJson(e));
        }
        Map<String,Object> result = custom.calculate(rowData);
        Caculate.states.get(uid).put("result",result.get("msg"));
        Caculate.states.get(uid).put("status","完成");
    }
}

