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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 从数据源配置（secondary）
 * 注意：不需要加 @Primary 注解
 */
@Configuration
@MapperScan(
        // 从数据源 Mapper 扫描路径
        basePackages = "com.zengyanyu.system.mapper.secondary",
        sqlSessionFactoryRef = "secondarySqlSessionFactory"
)
public class SecondaryDataSourceConfig {

    /**
     * 配置从数据源
     */
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 配置从数据源的 SqlSessionFactory
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory secondarySqlSessionFactory(@Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        // ========== 关键修复：指定 Mapper XML 文件路径 ==========
        // 路径需匹配你的 XML 文件实际位置（根据包名调整）
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 方式1：如果 XML 在 resources 下（推荐）
        bean.setMapperLocations(resolver.getResources("classpath:mapper/secondary/*.xml"));
        return bean.getObject();
    }

    /**
     * 配置从数据源的事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "secondaryTransactionManager")
    public DataSourceTransactionManager secondaryTransactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 配置从数据源的 SqlSessionTemplate
     *
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate secondarySqlSessionTemplate(@Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}