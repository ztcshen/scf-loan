package com.scf.loan.job.handler;

import com.xxl.job.core.biz.model.ReturnT;

/**
 * Simple AbstractJobHandler Test Class
 * For verifying basic functionality of the abstract class
 * 
 * @author scf-loan
 */
public class SimpleAbstractJobHandlerTest {

    /**
     * Test concrete processor implementation
     */
    private static class TestJobHandler extends AbstractJobHandler {
        private boolean shouldSucceed = true;
        private boolean shouldThrowException = false;
        private ReturnT<String> customResult = null;

        public void setShouldSucceed(boolean shouldSucceed) {
            this.shouldSucceed = shouldSucceed;
        }

        public void setShouldThrowException(boolean shouldThrowException) {
            this.shouldThrowException = shouldThrowException;
        }

        public void setCustomResult(ReturnT<String> customResult) {
            this.customResult = customResult;
        }

        @Override
        protected ReturnT<String> doExecute(String param) throws Exception {
            if (shouldThrowException) {
                throw new RuntimeException("Test exception");
            }
            
            if (customResult != null) {
                return customResult;
            }
            
            return shouldSucceed ? ReturnT.SUCCESS : ReturnT.FAIL;
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting AbstractJobHandler simple test...");
        
        try {
            testSuccessScenario();
            testFailureScenario();
            testExceptionScenario();
            testCustomResult();
            testGetJobName();
            
            System.out.println("All tests passed!");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testSuccessScenario() {
        System.out.println("Test 1: Job execution success scenario");
        
        TestJobHandler handler = new TestJobHandler();
        handler.setShouldSucceed(true);
        
        try {
            ReturnT<String> result = handler.doExecute("test-param");
            
            if (result.getCode() == ReturnT.SUCCESS_CODE) {
                System.out.println("[PASS] Job execution success test passed");
            } else {
                throw new RuntimeException("Expected job success, but got: " + result.getCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Job execution success test failed", e);
        }
    }

    private static void testFailureScenario() {
        System.out.println("Test 2: Job execution failure scenario");
        
        TestJobHandler handler = new TestJobHandler();
        handler.setShouldSucceed(false);
        
        try {
            ReturnT<String> result = handler.doExecute("test-param");
            
            if (result.getCode() == ReturnT.FAIL_CODE) {
                System.out.println("[PASS] Job execution failure test passed");
            } else {
                throw new RuntimeException("Expected job failure, but got: " + result.getCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Job execution failure test failed", e);
        }
    }

    private static void testExceptionScenario() {
        System.out.println("Test 3: Job execution exception scenario");
        
        TestJobHandler handler = new TestJobHandler();
        handler.setShouldThrowException(true);
        
        try {
            ReturnT<String> result = handler.doExecute("test-param");
            throw new RuntimeException("Expected exception, but job returned normally: " + result.getCode());
        } catch (RuntimeException e) {
            if ("Test exception".equals(e.getMessage())) {
                System.out.println("[PASS] Job execution exception test passed");
            } else {
                throw new RuntimeException("Exception message doesn't match", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Job execution exception test failed", e);
        }
    }

    private static void testCustomResult() {
        System.out.println("Test 4: Custom return result scenario");
        
        TestJobHandler handler = new TestJobHandler();
        ReturnT<String> customResult = new ReturnT<>(500, "Custom error");
        handler.setCustomResult(customResult);
        
        try {
            ReturnT<String> result = handler.doExecute("test-param");
            
            if (result.getCode() == 500 && "Custom error".equals(result.getMsg())) {
                System.out.println("[PASS] Custom return result test passed");
            } else {
                throw new RuntimeException("Expected custom result, but got: " + result.getCode() + ", " + result.getMsg());
            }
        } catch (Exception e) {
            throw new RuntimeException("Custom return result test failed", e);
        }
    }

    private static void testGetJobName() {
        System.out.println("Test 5: Get job name");
        
        TestJobHandler handler = new TestJobHandler();
        
        try {
            String jobName = handler.getJobName();
            
            if ("TestJobHandler".equals(jobName)) {
                System.out.println("[PASS] Get job name test passed");
            } else {
                throw new RuntimeException("Expected job name TestJobHandler, but got: " + jobName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Get job name test failed", e);
        }
    }
}