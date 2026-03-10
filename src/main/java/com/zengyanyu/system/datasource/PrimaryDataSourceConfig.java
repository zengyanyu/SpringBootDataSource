package com.zengyanyu.system.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 主数据源配置（primary）
 * 1. @Primary 标记为主数据源（必须，否则会冲突）
 * 2. @MapperScan 指定该数据源扫描的 Mapper 包路径
 */
@Configuration
@MapperScan(
        // 主数据源 Mapper 扫描路径
        basePackages = "com.zengyanyu.system.mapper.primary",
        sqlSessionFactoryRef = "primarySqlSessionFactory"
)
public class PrimaryDataSourceConfig {

    /**
     * 配置主数据源
     */
    @Bean(name = "primaryDataSource")
    @Primary // 标记为主数据源，解决多数据源冲突
    @ConfigurationProperties(prefix = "spring.datasource.primary") // 绑定配置文件中的参数
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 配置主数据源的 SqlSessionFactory
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        // ========== 关键修复：指定 Mapper XML 文件路径 ==========
        // 路径需匹配你的 XML 文件实际位置（根据包名调整）
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 方式1：如果 XML 在 resources 下（推荐）
        bean.setMapperLocations(resolver.getResources("classpath:mapper/primary/*.xml"));

        return bean.getObject();
    }

    /**
     * 配置主数据源的事务管理器
     *
     * @param dataSource
     * @return
     */
    @Primary
    @Bean(name = "primaryTransactionManager")
    public DataSourceTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 配置主数据源的 SqlSessionTemplate
     *
     * @param sqlSessionFactory
     * @return
     */
    @Primary
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}