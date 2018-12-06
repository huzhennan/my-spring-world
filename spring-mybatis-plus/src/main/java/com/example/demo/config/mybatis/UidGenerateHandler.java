package com.example.demo.config.mybatis;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UidGenerateHandler implements MetaObjectHandler {
    private final static Logger log = LoggerFactory.getLogger(UidGenerateHandler.class);

    @Autowired
    private DefaultUidGenerator uidGenerator;

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("id", uidGenerator.getUID(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // do nothing.
    }
}
