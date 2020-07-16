package com.lch.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lch.o2o.BaseTest;
import com.lch.o2o.entity.ProductCategory;

public class ProductCategoryDaoTest extends BaseTest{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	public void testQueryProductCategoryList() {
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(1L);
		System.out.println(productCategoryList.size());
	}
	
	@Test
	public void testBatchInsertProductCategory() {
		ProductCategory p1 = new ProductCategory();
		p1.setProductCategoryName("果汁饮料");
		p1.setPriority(3);
		p1.setCreateTime(new Date());
		p1.setShopId(1L);
		ProductCategory p2 = new ProductCategory();
		p2.setProductCategoryName("奶茶咖啡");
		p2.setPriority(4);
		p2.setCreateTime(new Date());
		p2.setShopId(1L);
		List<ProductCategory> list = new ArrayList<ProductCategory>();
		list.add(p1);
		list.add(p2);
		int effectedNum = productCategoryDao.batchInsertProductCategory(list);
		System.out.println(effectedNum);
	}
	
	@Test
	public void testDeleteProductCategory() {
		List<ProductCategory> pcList = productCategoryDao.queryProductCategoryList(1L);
		for(ProductCategory pc : pcList) {
			if(pc.getProductCategoryName().equals("冰淇淋圣代") || pc.getProductCategoryName().equals("养乐多系列")) {
				int effectedNum = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), pc.getShopId());
				assertEquals(1, effectedNum);
			}
		}
	}
}
