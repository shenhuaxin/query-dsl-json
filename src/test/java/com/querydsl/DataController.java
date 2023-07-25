package com.querydsl;

import com.baomidou.mybatisplus.extension.api.R;
import com.querydsl.db.QueryExecutor;
import com.querydsl.spring.DbConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author
 */
@RestController
@RequestMapping("/benAppData")
public class DataController {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @GetMapping("getSql")
    public String test(@RequestParam String id) {
        return "test" + id;
    }

    @PostMapping("querydsl")
    public List querydsl(@RequestBody String requestParam) {
        Map<Integer, Object> params = new HashMap<>();
        SearchBuilder searchBuilder = new SearchBuilder();
        String sql = searchBuilder.parse(requestParam).toSql(params);
        try {

            System.out.println(sql);
            List<Map<String, Object>> data = new QueryExecutor().query(sqlSessionFactory, sql, params);
            return data;
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    @PostMapping("querydsl/getsql")
    public String querydslGetSql(@RequestBody String requestParam) {
        SearchBuilder searchBuilder = new SearchBuilder();
        String sql = searchBuilder.parse(requestParam).toString();
        return (sql);
    }
}
