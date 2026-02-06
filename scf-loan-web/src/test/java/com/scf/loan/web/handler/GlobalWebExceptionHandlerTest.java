package com.scf.loan.web.handler;

import com.scf.loan.common.enums.ResultCodeEnum;
import com.scf.loan.common.exception.ServiceException;
import com.scf.loan.common.exception.ValidateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GlobalWebExceptionHandlerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private GlobalWebExceptionHandler globalWebExceptionHandler;

    @BeforeEach
    public void setup() {
        // Standalone setup for testing just the exception handler
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(globalWebExceptionHandler)
                .build();
    }

    @Test
    public void testHandleServiceException() throws Exception {
        mockMvc.perform(get("/test/service-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("600"))
                .andExpect(jsonPath("$.message").value("Business Error Occurred"));
    }

    @Test
    public void testHandleValidateException() throws Exception {
        mockMvc.perform(get("/test/validate-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("参数错误:Invalid Parameter"));
    }

    @Test
    public void testHandleException() throws Exception {
        mockMvc.perform(get("/test/exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("500"))
                .andExpect(jsonPath("$.message").value("系统异常"));
    }

    /**
     * Test Controller to trigger exceptions
     */
    @RestController
    static class TestController {

        @GetMapping("/test/service-exception")
        public void throwServiceException() {
            throw new ServiceException("600", "Business Error Occurred");
        }

        @GetMapping("/test/validate-exception")
        public void throwValidateException() {
            throw new ValidateException("Invalid Parameter");
        }

        @GetMapping("/test/exception")
        public void throwException() throws Exception {
            throw new Exception("Unexpected Error");
        }
    }
}
