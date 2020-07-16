package com.lch.o2o.service;

import java.util.List;

import com.lch.o2o.dto.ProductCategoryExecution;
import com.lch.o2o.entity.ProductCategory;
import com.lch.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	public List<ProductCategory> getProductCategoryList(long shopId);
	
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
		throws ProductCategoryOperationException;
	
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
		throws ProductCategoryOperationException;
	
}
