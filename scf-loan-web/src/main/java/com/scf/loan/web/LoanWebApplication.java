package com.scf.loan.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scf.loan")
public class LoanWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanWebApplication.class, args);
    }
}
