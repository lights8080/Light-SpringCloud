package com.light.core.config.mybatis;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * MybatisPlus配置类
 *
 * @author lihaipeng
 * @date 2020-09-29
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(PaginationInterceptor.class)
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启部分count查询的left join优化
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        List<ISqlParser> sqlParserList = new ArrayList<>();
        // 攻击SQL阻断解析器，阻止恶意的全表更新删除
        sqlParserList.add(new BlockAttackSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }
}