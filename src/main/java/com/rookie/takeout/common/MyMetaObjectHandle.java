package com.rookie.takeout.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @title: MyMetaObjectHandle
 * @Author: Mrdong
 * @Date: 2022/8/28 11:38
 * @Description:自定义元数据对象处理器
 */
@Component
@Slf4j
public class MyMetaObjectHandle implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]....");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        //从当前线程的副本中取出登录用户的id
        Long id = BaseContext.getCurrentId();

        metaObject.setValue("createUser", id);
        metaObject.setValue("updateUser", id);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]....");

        //从当前线程的副本中取出登录用户的id
        Long id = BaseContext.getCurrentId();

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", id);
    }
}
