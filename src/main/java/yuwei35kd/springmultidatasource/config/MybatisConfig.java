package yuwei35kd.springmultidatasource.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan("yuwei35kd.springmultidatasource.mapper")
public class MybatisConfig {
	private static final Logger LOG = LoggerFactory.getLogger(MybatisConfig.class);
	@Bean
	@Qualifier("source")
	@ConfigurationProperties(prefix="spring.datasource.source")
	public DataSource dataSource(){
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@Qualifier("source1")
	@ConfigurationProperties(prefix="spring.datasource.source1")
	public DataSource dataSource1(){
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@Qualifier("source2")
	@ConfigurationProperties(prefix="spring.datasource.source2")
	public DataSource dataSource2(){
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception{
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		return sqlSessionTemplate;
	}

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:/mybatis.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }
    
    @Bean
    public DynamicDataSource dynamicDataSource(@Qualifier("source1") DataSource dataSource1
    		,@Qualifier("source2") DataSource dataSource2){
    	DynamicDataSource dynamicDataSource = new DynamicDataSource();
    	Map<Object,Object> targetDataSources = new HashMap<Object,Object>();
    	targetDataSources.put("source1",dataSource1);
    	targetDataSources.put("source2",dataSource2);
    	LOG.info("<====================================="+targetDataSources.toString()+"=================================>");
    	dynamicDataSource.setTargetDataSources(targetDataSources);
    	dynamicDataSource.setDefaultTargetDataSource(dataSource1);
    	return dynamicDataSource;
    }
    
}
