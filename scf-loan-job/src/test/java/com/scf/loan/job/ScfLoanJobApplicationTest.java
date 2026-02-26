package com.scf.loan.job;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ScfLoanJobApplication 单元测试
 * 
 * @author scf-loan
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ScfLoanJobApplication.class)
class ScfLoanJobApplicationTest {

    @Test
    void contextLoads() {
        // 验证Spring上下文可以正常加载
        // 这个测试会验证所有的配置类和组件都能正确初始化
    }

    @Test
    void testMainMethod() {
        // 测试主方法可以正常执行
        // 注意：这不会真正启动整个Spring Boot应用，只是验证方法存在
        assertDoesNotThrow(() -> {
            ScfLoanJobApplication.main(new String[]{});
        });
    }
}