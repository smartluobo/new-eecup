package com.ibay.tea;

import com.ibay.tea.api.service.pay.ApiPayService;
import com.ibay.tea.common.utils.WechatSignUtil;
import com.ibay.tea.dao.TbOrderMapper;
import com.ibay.tea.entity.TbOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.security.KeyStore;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeaApplicationTests {

	@Resource
	private TbOrderMapper tbOrderMapper;

	@Resource
	private ApiPayService apiPayService;
	@Test
	public void orderPay() throws Exception{
		KeyStore keyStore = WechatSignUtil.getKeyStore("12345678");
		TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey("2019061016121150");
		apiPayService.createPayOrderToWechat(tbOrder);
	}



}
