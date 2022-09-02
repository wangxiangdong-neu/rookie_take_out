package com.rookie.takeout;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @title: RookieApplication
 * @Author: Mrdong
 * @Date: 2022/8/26 15:48
 * @Description:
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.rookie.takeout.mapper")
@ServletComponentScan
@EnableTransactionManagement
public class RookieApplication {

    public static void main(String[] args) {
        SpringApplication.run(RookieApplication.class, args);
        log.info("项目创建日期：2022年8月26日，开始运行...");
    }
}
