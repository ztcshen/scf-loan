package com.scf.loan.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scf.loan.dal.entity.FinancingOrderEntity;

import java.util.List;

/**
 * FinancingOrderEntity 服务接口
 */
public interface FinancingOrderService extends IService<FinancingOrderEntity> {

    /**
     * 创建FinancingOrder
     * @param entity FinancingOrderEntity实体
     * @return 是否创建成功
     */
    boolean createOrder(FinancingOrderEntity entity);

    /**
     * 根据融资订单号查询订单
     * @param financingOrderId 融资订单号
     * @return FinancingOrderEntity实体
     */
    FinancingOrderEntity getByFinancingOrderId(String financingOrderId);

    /**
     * 根据商户订单号查询订单
     * @param merTransNo 商户订单号
     * @return FinancingOrderEntity实体
     */
    FinancingOrderEntity getByMerTransNo(String merTransNo);

    /**
     * 查询所有有效订单
     * @return FinancingOrderEntity列表
     */
    List<FinancingOrderEntity> listValidOrders();

    /**
     * 根据状态查询订单
     * @param applyStatus 审批状态
     * @param loanConfirmStatus 放款状态
     * @param repayStatus 还款状态
     * @return FinancingOrderEntity列表
     */
    List<FinancingOrderEntity> listByStatus(String applyStatus, String loanConfirmStatus, String repayStatus);

    /**
     * 更新审批状态
     * @param id 订单ID
     * @param applyStatus 审批状态
     * @return 是否更新成功
     */
    boolean updateApplyStatus(Long id, String applyStatus);

    /**
     * 更新放款状态
     * @param id 订单ID
     * @param loanConfirmStatus 放款状态
     * @return 是否更新成功
     */
    boolean updateLoanConfirmStatus(Long id, String loanConfirmStatus);

    /**
     * 更新还款状态
     * @param id 订单ID
     * @param repayStatus 还款状态
     * @return 是否更新成功
     */
    boolean updateRepayStatus(Long id, String repayStatus);

    /**
     * 批量创建FinancingOrder
     * @param entities FinancingOrderEntity实体列表
     * @return 是否创建成功
     */
    boolean batchCreateFinancingOrder(List<FinancingOrderEntity> entities);

    /**
     * 批量更新FinancingOrder
     * @param entities FinancingOrderEntity实体列表
     * @return 是否更新成功
     */
    boolean batchUpdateFinancingOrder(List<FinancingOrderEntity> entities);

    /**
     * 分页查询FinancingOrder
     * @param page 页码
     * @param size 每页大小
     * @return FinancingOrderEntity列表
     */
    List<FinancingOrderEntity> pageFinancingOrder(Integer page, Integer size);
}