package com.lch.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lch.o2o.entity.Shop;

public interface ShopDao {
	
	/*
	 * 分页查询店铺，可输入的条件有：店铺名（模糊），店铺状态，店铺类别，区域Id，owner
	 * rowIndex:从第几行开始取
	 * pageIndex:取几条数据
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
			@Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	
	/*
	 * 返回queryShopList总数
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
	
	int insertShop(Shop shop);
	
	int updateShop(Shop shop);
	
	Shop queryByShopId(long shopId);
}
