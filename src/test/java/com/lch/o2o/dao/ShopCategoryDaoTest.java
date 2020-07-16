package com.lch.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lch.o2o.BaseTest;
import com.lch.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	@Test
	public void queryShopCategoryTest() {
		
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(new ShopCategory());
		assertEquals(1, shopCategoryList.size());
		/*ShopCategory category = new ShopCategory();
		category.setShopCategoryId(2L);
		ShopCategory parentcategory = new ShopCategory();
		parentcategory.setShopCategoryId(1L);
		category.setParent(parentcategory);
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(category);
		assertEquals(1, shopCategoryList.size());
		System.out.println(shopCategoryList.get(0).getShopCategoryName());*/
	}
}
