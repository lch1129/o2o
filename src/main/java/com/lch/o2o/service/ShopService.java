package com.lch.o2o.service;


import com.lch.o2o.dto.ImageHolder;
import com.lch.o2o.dto.ShopExecution;
import com.lch.o2o.entity.Shop;
import com.lch.o2o.exceptions.ShopOperationException;

public interface ShopService {
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail)
			throws ShopOperationException;
	
	public Shop getShopById(long shopId);
	
	public ShopExecution modifyShop(Shop shop,ImageHolder thumbnail)
			throws ShopOperationException;
	
	/*
	 * 分页返回相应列表数据
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize); 
}
