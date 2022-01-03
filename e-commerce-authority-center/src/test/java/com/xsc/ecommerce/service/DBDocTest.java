package com.xsc.ecommerce.service;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Jakexsc
 * 2021/12/27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DBDocTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void buildDBDoc() {
        DataSource dataSourceMysql = applicationContext.getBean(DataSource.class);
        EngineConfig engineConfig = EngineConfig.builder()
                .fileOutputDir("/Users/jakesc/project/e-commerce-springcloud")
                .openOutputDir(false)
                .fileType(EngineFileType.HTML)
                .produceType(EngineTemplateType.freemarker)
                .build();

        // 生成文档配置，包含自定义
        // 数据库名
        Configuration configuration = Configuration.builder()
                .version("1.0")
                .dataSource(dataSourceMysql)
                .description("e-commerce-springcloud")
                .engineConfig(engineConfig)
                .produceConfig(getProduceConfig())
                .build();
        new DocumentationExecute(configuration).execute();

    }

    private ProcessConfig getProduceConfig() {
        // 忽略数据表
        List<String> ignoreTableName = Collections.singletonList("undo_log");
        // 忽略以a，b前缀的表
        List<String> ignorePrefix = Arrays.asList("a", "b");

        List<String> ignoreSuffix = Arrays.asList("_test", "_Test");

        return ProcessConfig.builder()
                // 根据名称指定表生成
                .designatedTableName(Collections.emptyList())
                // 根据指定前缀表生成
                .designatedTablePrefix(Collections.emptyList())
                // 根据指定后缀表生成
                .designatedTableSuffix(Collections.emptyList())
                // 忽略生成某些表
                .ignoreTableName(ignoreTableName)
                // 忽略生成某些前缀表
                .ignoreTablePrefix(ignorePrefix)
                // 忽略生成某些后缀表
                .ignoreTableSuffix(ignoreSuffix)
                .build();
    }
}
