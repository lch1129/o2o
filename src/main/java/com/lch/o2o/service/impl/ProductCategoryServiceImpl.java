package com.lch.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lch.o2o.dao.ProductCategoryDao;
import com.lch.o2o.dao.ProductDao;
import com.lch.o2o.dto.ProductCategoryExecution;
import com.lch.o2o.entity.ProductCategory;
import com.lch.o2o.enums.ProductCategoryStateEnum;
import com.lch.o2o.exceptions.ProductCategoryOperationException;
import com.lch.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Autowired
	private ProductDao productDao;
	
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if(productCategoryList !=null && productCategoryList.size()>0) {
			try {
				int effectdNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if(effectdNum<=0) {
					throw new ProductCategoryOperationException("店铺类别创建失败！");
				}else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			}catch(Exception e) {
				throw new ProductCategoryOperationException("batchAddInsertProductCategory error:" + e.getMessage());
			}
		}else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		try {
			int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
			if(effectedNum<0) {
				throw new ProductCategoryOperationException("商品类别删除失败！");
			}
		}catch(Exception e) {
			throw new RuntimeException("商品类别删除失败！" + e.toString());
		}
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum<=0) {
				throw new ProductCategoryOperationException("商品类别删除失败！");
			}else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:" + e.getMessage());
		}
	}
}
