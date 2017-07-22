package cn.smart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 2017/7/21.
 */
@Controller
@EnableAutoConfiguration
@ComponentScan
public class Starter {

    private static Logger log = LoggerFactory.getLogger(Starter.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/hello")
    public String hello(){
        List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from USER ");
        log.info("yes");
        return "hello.html";
    }




    public static void main(String[] args) {
        SpringApplication.run(Starter.class,args);
    }

}
