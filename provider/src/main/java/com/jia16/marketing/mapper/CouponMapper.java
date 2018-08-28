package com.jia16.marketing.mapper;

import com.jia16.marketing.domain.Coupon;
import org.apache.ibatis.annotations.Param;

public interface CouponMapper {

	Coupon findById(@Param("id") long id);

}

