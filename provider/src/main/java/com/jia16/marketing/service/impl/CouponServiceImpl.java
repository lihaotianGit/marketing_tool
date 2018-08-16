package com.jia16.marketing.service.impl;

import com.jia16.marketing.domain.Coupon;
import com.jia16.marketing.mapper.CouponMapper;
import com.jia16.marketing.service.CouponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("couponService")
public class CouponServiceImpl implements CouponService {

    @Resource
    private CouponMapper couponMapper;

    @Override
    @Transactional(readOnly = true)
    public Coupon findById(long id) {
        return couponMapper.findById(id);
    }

}
