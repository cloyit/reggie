package com.cloyit.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */

@Slf4j
@SpringBootApplication
public class RegggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegggieApplication.class, args);
        log.info("spring启动成功");
    }
}
