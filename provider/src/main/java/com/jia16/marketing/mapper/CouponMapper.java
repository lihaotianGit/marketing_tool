package com.jia16.marketing.mapper;

import com.jia16.marketing.domain.Coupon;
import org.apache.ibatis.annotations.Param;


/**
 * @author winter
 * @version 1.0
 * @since 1.0
 * @Date: 2017-01-04
 * @Time: 17:10:06
 */

public interface CouponMapper {

	Coupon findById(@Param("id") long id);

}

