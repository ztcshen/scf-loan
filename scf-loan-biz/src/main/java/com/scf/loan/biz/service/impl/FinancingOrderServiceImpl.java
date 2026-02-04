package com.scf.loan.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scf.loan.biz.service.FinancingOrderService;
import com.scf.loan.dal.entity.FinancingOrderEntity;
import com.scf.loan.dal.mapper.FinancingOrderMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 融资订单服务实现类
 */
@Service
public class FinancingOrderServiceImpl extends ServiceImpl<FinancingOrderMapper, FinancingOrderEntity> implements FinancingOrderService {

    @Override
    public boolean batchCreateFinancingOrder(List<FinancingOrderEntity> entities) {
        return saveBatch(entities);
    }

    @Override
    public boolean batchUpdateFinancingOrder(List<FinancingOrderEntity> entities) {
        return updateBatchById(entities);
    }

    @Override
    public List<FinancingOrderEntity> pageFinancingOrder(Integer page, Integer size) {
        return page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size)).getRecords();
    }
}
