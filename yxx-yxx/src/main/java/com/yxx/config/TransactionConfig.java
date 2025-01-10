package com.yxx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TransactionConfig {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionTemplate transactionTemplate() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        // 设置事务传播行为为REQUIRED，表示如果当前存在事务，则加入该事务；如果不存在，则创建一个新的事务
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // 设置事务隔离级别为READ_COMMITTED，表示一个事务可能看到另一个事务已提交的数据
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

        // 设置事务超时时间为30秒，如果在30秒内事务没有完成，则自动回滚
        transactionTemplate.setTimeout(30);

        return transactionTemplate;
    }
}