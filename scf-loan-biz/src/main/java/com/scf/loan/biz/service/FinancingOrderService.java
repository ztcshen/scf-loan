package com.scf.loan.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scf.loan.dal.entity.FinancingOrderEntity;

import java.util.List;

/**
 * 融资订单服务接口
 */
public interface FinancingOrderService extends IService<FinancingOrderEntity> {

    /**
     * 批量创建融资订单
     * @param entities 融资订单实体列表
     * @return 是否创建成功
     */
    boolean batchCreateFinancingOrder(List<FinancingOrderEntity> entities);

    /**
     * 批量更新融资订单
     * @param entities 融资订单实体列表
     * @return 是否更新成功
     */
    boolean batchUpdateFinancingOrder(List<FinancingOrderEntity> entities);

    /**
     * 分页查询融资订单
     * @param page 页码
     * @param size 每页大小
     * @return 融资订单列表
     */
    List<FinancingOrderEntity> pageFinancingOrder(Integer page, Integer size);
}
