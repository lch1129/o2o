package com.lch.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lch.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	public List<ProductCategory> queryProductCategoryList(long shopId);
	
	/*
	 * 批量新增商品类别
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	/*
	 * 删除指定商品类别
	 */
	int deleteProductCategory(@Param("productCategory") long productCategoryId, @Param("shopId") long shopId);
}
