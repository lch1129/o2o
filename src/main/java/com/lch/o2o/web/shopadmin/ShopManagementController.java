package com.lch.o2o.web.shopadmin;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lch.o2o.dto.ImageHolder;
import com.lch.o2o.dto.ShopExecution;
import com.lch.o2o.entity.Area;
import com.lch.o2o.entity.PersonInfo;
import com.lch.o2o.entity.Shop;
import com.lch.o2o.entity.ShopCategory;
import com.lch.o2o.enums.ShopStateEnum;
import com.lch.o2o.exceptions.ShopOperationException;
import com.lch.o2o.service.AreaService;
import com.lch.o2o.service.ShopCategoryService;
import com.lch.o2o.service.ShopService;
import com.lch.o2o.util.CodeUtil;
import com.lch.o2o.util.HttpServletRequestUtil;


@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="/getshopinitinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try{
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> registerShop(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码输入错误！");
			return modelMap;
		}
		//1.接受并转化相应的参数，包括店铺信息和图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipareFileResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if(commonsMultipareFileResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "图片不能为空！");
			return modelMap;
		}
		//2.注册店铺
		if(shop!=null && shopImg!=null) {
			/*
			 * 把用户信息在session中获取
			 */
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution shopExecution;
			try {
				ImageHolder thumbnail = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				shopExecution = shopService.addShop(shop, thumbnail);
				if(shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					/*
					 * 取出该用户可以操作的店铺列表，并且把新增加的店铺存入其中，更新到session中
					 */
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList == null || shopList.size()==0) {
						shopList = new ArrayList<Shop>();
					}
					shopList.add(shopExecution.getShop());
					request.getSession().setAttribute("shopList", shopList);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", shopExecution.getStateInfo());
				}
			}catch(ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}catch(IOException e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
	
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息！");
			return modelMap;
		}
		
	}
	
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId > -1) {
			try {
				Shop shop = shopService.getShopById(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			}catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId！");
		}
		return modelMap;
	}
	
	@RequestMapping(value="/modifyshop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> modifyShop(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码输入错误！");
			return modelMap;
		}
		//1.接受并转化相应的参数，包括店铺信息和图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipareFileResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if(commonsMultipareFileResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}
		//2.修改店铺信息
		if(shop!=null && shop.getShopId()!=null) {
			ShopExecution shopExecution;
			try {
				if(shopImg == null)
				{
					shopExecution = shopService.modifyShop(shop, null);
				}else {
					ImageHolder thumbnail = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					shopExecution = shopService.modifyShop(shop, thumbnail);
				}
				if(shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", shopExecution.getStateInfo());
				}
			}catch(ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}catch(IOException e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
	
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺Id！");
			return modelMap;
		}
	}
	
	@RequestMapping(value="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = new PersonInfo();
		user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			request.getSession().setAttribute("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value="/getshopmanagementinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId<=0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if(currentShopObj==null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
}
