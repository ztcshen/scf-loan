package com.scf.loan.web.controller;

// import com.scf.loan.biz.service.FinancingOrderService;
// import com.scf.loan.common.dto.FinancingOrderDTO;
// import com.scf.loan.dal.entity.FinancingOrderEntity;
// import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.stream.Collectors;

/**
 * FinancingOrder控制器
 */
@RestController
@RequestMapping("/api/financing-order")
public class FinancingOrderController {

    // @Autowired
    // private FinancingOrderService financingorderService;

    /**
     * 健康检查端点
     */
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    /**
     * 批量创建FinancingOrder
     */
    // @PostMapping("/batch")
    // public boolean batchCreateFinancingOrder(@RequestBody List<FinancingOrderDTO> dtos) {
    //     List<FinancingOrderEntity> entities = dtos.stream().map(dto -> {
    //         FinancingOrderEntity entity = new FinancingOrderEntity();
    //         BeanUtils.copyProperties(dto, entity);
    //         return entity;
    //     }).collect(Collectors.toList());
    //     return financingorderService.batchCreateFinancingOrder(entities);
    // }

    /**
     * 批量更新FinancingOrder
     */
    // @PutMapping("/batch")
    // public boolean batchUpdateFinancingOrder(@RequestBody List<FinancingOrderDTO> dtos) {
    //     List<FinancingOrderEntity> entities = dtos.stream().map(dto -> {
    //         FinancingOrderEntity entity = new FinancingOrderEntity();
    //         BeanUtils.copyProperties(dto, entity);
    //         return entity;
    //     }).collect(Collectors.toList());
    //     return financingorderService.batchUpdateFinancingOrder(entities);
    // }

    /**
     * 分页查询FinancingOrder
     */
    // @GetMapping("/page")
    // public List<FinancingOrderDTO> pageFinancingOrder(
    //         @RequestParam(defaultValue = "1") Integer page,
    //         @RequestParam(defaultValue = "10") Integer size) {
    //     List<FinancingOrderEntity> entities = financingorderService.pageFinancingOrder(page, size);
    //     return entities.stream().map(entity -> {
    //         FinancingOrderDTO dto = new FinancingOrderDTO();
    //         BeanUtils.copyProperties(entity, dto);
    //         return dto;
    //     }).collect(Collectors.toList());
    // }
}