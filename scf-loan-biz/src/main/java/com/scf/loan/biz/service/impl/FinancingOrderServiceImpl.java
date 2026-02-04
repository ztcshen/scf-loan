package com.scf.loan.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scf.loan.biz.service.FinancingOrderService;
import com.scf.loan.dal.entity.FinancingOrderEntity;
import com.scf.loan.dal.mapper.FinancingOrderMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * FinancingOrderEntity 服务实现类
 */
@Service
public class FinancingOrderServiceImpl extends ServiceImpl<FinancingOrderMapper, FinancingOrderEntity> implements FinancingOrderService {

    @Override
    public boolean createOrder(FinancingOrderEntity entity) {
        return save(entity);
    }

    @Override
    public FinancingOrderEntity getByFinancingOrderId(String financingOrderId) {
        return null;
    }

    @Override
    public FinancingOrderEntity getByMerTransNo(String merTransNo) {
        return null;
    }

    @Override
    public List<FinancingOrderEntity> listValidOrders() {
        return list();
    }

    @Override
    public List<FinancingOrderEntity> listByStatus(String applyStatus, String loanConfirmStatus, String repayStatus) {
        return list();
    }

    @Override
    public boolean updateApplyStatus(Long id, String applyStatus) {
        FinancingOrderEntity entity = getById(id);
        if (entity != null) {
            entity.setApplyStatus(applyStatus);
            return updateById(entity);
        }
        return false;
    }

    @Override
    public boolean updateLoanConfirmStatus(Long id, String loanConfirmStatus) {
        FinancingOrderEntity entity = getById(id);
        if (entity != null) {
            entity.setLoanConfirmStatus(loanConfirmStatus);
            return updateById(entity);
        }
        return false;
    }

    @Override
    public boolean updateRepayStatus(Long id, String repayStatus) {
        FinancingOrderEntity entity = getById(id);
        if (entity != null) {
            entity.setRepayStatus(repayStatus);
            return updateById(entity);
        }
        return false;
    }

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
