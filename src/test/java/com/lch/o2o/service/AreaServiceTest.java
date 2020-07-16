package com.lch.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lch.o2o.BaseTest;
import com.lch.o2o.entity.Area;

public class AreaServiceTest extends BaseTest {
	
	@Autowired
	private AreaService areaService;
	@Test
	public void getAreaList() {
		List<Area> areaList = areaService.getAreaList();
		assertEquals("三食堂广场", areaList.get(0).getAreaName());
	}
}
