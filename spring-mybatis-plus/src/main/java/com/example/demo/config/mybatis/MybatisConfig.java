package com.example.demo.config.mybatis;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.example.demo", "com.baidu.fsg.uid"})
public class MybatisConfig {
    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    public DefaultUidGenerator defaultUidGenerator() {
        DefaultUidGenerator uidGenerator = new DefaultUidGenerator();
        uidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner());
        uidGenerator.setTimeBits(29);
        uidGenerator.setWorkerBits(21);
        uidGenerator.setSeqBits(13);
        uidGenerator.setEpochStr("2018-12-01");

        return uidGenerator;
    }
}
