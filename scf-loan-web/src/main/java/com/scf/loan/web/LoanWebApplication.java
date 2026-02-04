package com.scf.loan.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 供应链金融贷款系统Web应用
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scf.loan")
@MapperScan(basePackages = "com.scf.loan.dal.mapper")
public class LoanWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanWebApplication.class, args);
    }
}
