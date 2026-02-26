package com.scf.loan.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SCF Loan Job Application
 * 
 * @author scf-loan
 */
@SpringBootApplication(scanBasePackages = "com.scf.loan")
public class ScfLoanJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScfLoanJobApplication.class, args);
    }
}