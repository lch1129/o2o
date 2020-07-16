package com.lch.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lch.o2o.BaseTest;
import com.lch.o2o.dto.ImageHolder;
import com.lch.o2o.dto.ShopExecution;
import com.lch.o2o.entity.Area;
import com.lch.o2o.entity.PersonInfo;
import com.lch.o2o.entity.Shop;
import com.lch.o2o.entity.ShopCategory;
import com.lch.o2o.enums.ShopStateEnum;
import com.lch.o2o.exceptions.ShopOperationException;

public class ShopServiceTest extends BaseTest {
	@Autowired
	private ShopService shopService;
	
	@Test
	public void testAddShop() throws ShopOperationException,FileNotFoundException {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(1L);
		Area area = new Area();
		area.setAreaId(2);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺3");
		shop.setShopDesc("test1");
		shop.setShopAddr("test1");
		shop.setPhone("test1");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		
		File shopImg = new File("C:/Users/刘成辉/Desktop/image/love.jpg");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder thumbnail = new ImageHolder(shopImg.getName(), is);
		ShopExecution shopExecution = shopService.addShop(shop, thumbnail);
		assertEquals(ShopStateEnum.CHECK.getState(), shopExecution.getState());
	}
	
	@Test
	public void testModifyShop() throws ShopOperationException,FileNotFoundException{
		Shop shop = new Shop();
		shop.setShopId(1L);
		shop.setShopName("修改后的店铺名称");
		File shopImg = new File("C:/Users/刘成辉/Desktop/证件.jpg");
		InputStream fis = new FileInputStream(shopImg);
		ImageHolder thumbnail = new ImageHolder(shopImg.getName(), fis);
		ShopExecution shopExecution = shopService.modifyShop(shop, thumbnail);
		System.out.println(shopExecution.getShop().getShopName() + "---" + shopExecution.getShop().getShopImg());
	}
	
	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(1L);
		shopCondition.setShopCategory(shopCategory);
		ShopExecution se =  shopService.getShopList(shopCondition, 2, 3);
		System.out.println(se.getShopList().size());
		System.out.println(se.getCount());
	}
}
