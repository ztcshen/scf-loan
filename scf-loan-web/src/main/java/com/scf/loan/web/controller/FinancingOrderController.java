package com.scf.loan.web.controller;

import com.scf.loan.biz.service.FinancingOrderService;
import com.scf.loan.common.dto.FinancingOrderDTO;
import com.scf.loan.dal.entity.FinancingOrderEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 融资订单控制器
 */
@RestController
@RequestMapping("/api/financing-order")
public class FinancingOrderController {

    @Autowired
    private FinancingOrderService financingOrderService;

    /**
     * 创建融资订单
     */
    @PostMapping
    public boolean createOrder(@RequestBody FinancingOrderDTO dto) {
        FinancingOrderEntity entity = new FinancingOrderEntity();
        BeanUtils.copyProperties(dto, entity);
        return financingOrderService.createOrder(entity);
    }

    /**
     * 根据ID查询订单
     */
    @GetMapping("/{id}")
    public FinancingOrderDTO getOrderById(@PathVariable Long id) {
        FinancingOrderEntity entity = financingOrderService.getById(id);
        if (entity == null) {
            return null;
        }
        FinancingOrderDTO dto = new FinancingOrderDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    /**
     * 根据融资订单号查询订单
     */
    @GetMapping("/financingOrderId/{financingOrderId}")
    public FinancingOrderDTO getByFinancingOrderId(@PathVariable String financingOrderId) {
        FinancingOrderEntity entity = financingOrderService.getByFinancingOrderId(financingOrderId);
        if (entity == null) {
            return null;
        }
        FinancingOrderDTO dto = new FinancingOrderDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    /**
     * 根据商户订单号查询订单
     */
    @GetMapping("/merTransNo/{merTransNo}")
    public FinancingOrderDTO getByMerTransNo(@PathVariable String merTransNo) {
        FinancingOrderEntity entity = financingOrderService.getByMerTransNo(merTransNo);
        if (entity == null) {
            return null;
        }
        FinancingOrderDTO dto = new FinancingOrderDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    /**
     * 查询所有有效订单
     */
    @GetMapping
    public List<FinancingOrderDTO> listValidOrders() {
        List<FinancingOrderEntity> entities = financingOrderService.listValidOrders();
        return entities.stream().map(entity -> {
            FinancingOrderDTO dto = new FinancingOrderDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 根据状态查询订单
     */
    @GetMapping("/status")
    public List<FinancingOrderDTO> listByStatus(
            @RequestParam(value = "applyStatus", required = false) String applyStatus,
            @RequestParam(value = "loanConfirmStatus", required = false) String loanConfirmStatus,
            @RequestParam(value = "repayStatus", required = false) String repayStatus) {
        List<FinancingOrderEntity> entities = financingOrderService.listByStatus(applyStatus, loanConfirmStatus, repayStatus);
        return entities.stream().map(entity -> {
            FinancingOrderDTO dto = new FinancingOrderDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 更新审批状态
     */
    @PutMapping("/{id}/apply-status/{applyStatus}")
    public boolean updateApplyStatus(@PathVariable Long id, @PathVariable String applyStatus) {
        return financingOrderService.updateApplyStatus(id, applyStatus);
    }

    /**
     * 更新放款状态
     */
    @PutMapping("/{id}/loan-status/{loanConfirmStatus}")
    public boolean updateLoanConfirmStatus(@PathVariable Long id, @PathVariable String loanConfirmStatus) {
        return financingOrderService.updateLoanConfirmStatus(id, loanConfirmStatus);
    }

    /**
     * 更新还款状态
     */
    @PutMapping("/{id}/repay-status/{repayStatus}")
    public boolean updateRepayStatus(@PathVariable Long id, @PathVariable String repayStatus) {
        return financingOrderService.updateRepayStatus(id, repayStatus);
    }

    /**
     * 更新订单
     */
    @PutMapping("/{id}")
    public boolean updateOrder(@PathVariable Long id, @RequestBody FinancingOrderDTO dto) {
        FinancingOrderEntity entity = financingOrderService.getById(id);
        if (entity == null) {
            return false;
        }
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id); // 确保ID不变
        return financingOrderService.updateById(entity);
    }

    /**
     * 删除订单（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public boolean deleteOrder(@PathVariable Long id) {
        FinancingOrderEntity entity = financingOrderService.getById(id);
        if (entity == null) {
            return false;
        }
        entity.setDelFlag(1); // 逻辑删除
        return financingOrderService.updateById(entity);
    }
}
