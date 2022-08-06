package com.cloyit.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 */

@Slf4j
@SpringBootApplication
@ServletComponentScan //开启对servlet容器相关组件的扫描（特别是过滤器）
@EnableTransactionManagement
@EnableCaching
public class RegggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegggieApplication.class, args);
        log.info("spring启动成功");
    }
}
