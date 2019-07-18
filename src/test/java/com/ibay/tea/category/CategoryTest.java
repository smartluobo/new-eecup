package com.ibay.tea.category;

import com.ibay.tea.BaseTest;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.cms.scheduled.OrderCloseScheduledTask;
import com.ibay.tea.common.service.PrintService;
import com.ibay.tea.common.utils.PrintUtil;
import com.ibay.tea.dao.TbOrderMapper;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbOrderItem;
import com.ibay.tea.entity.TbStore;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

public class CategoryTest extends BaseTest {

    @Resource
    private PrintUtil printUtil;

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private StoreCache storeCache;

    @Resource
    private PrintService printService;

    @Resource
    private OrderCloseScheduledTask orderCloseScheduledTask;
//    @Resource
//    private CategoryMapper categoryMapper;
//
//    @Test
//    public void testInsert(){
//        Category category = new Category();
//        category.setCategoryName("单元测试2");
//        category.setStatus(1);
//        category.setWeight(6);
//        category.setCreateTime("2018-05-17 18:22:55");
//        categoryMapper.insert(category);
//    }

    @Test
    public void testOrderItem(){
        TbOrder tbOrder = new TbOrder();
        tbOrder.setCreateTime(new Date());
        TbStore store = new TbStore();
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setNum(001);
        orderItem.setOrderId("1000");
        orderItem.setTitle("多肉草莓");
        orderItem.setSkuDetailDesc("大/常温/奶盖");
        printService.printOrderItem(tbOrder,orderItem,store);
    }

    @Test
    public void testOrderClose(){
        orderCloseScheduledTask.orderCloseTaskMethod();
    }

    @Test
    public void testAddPrinter(){
        String snList = "920526457#kawfu49k#E杯中国储能大厦南山分店";
        printUtil.addprinter(snList);
    }

    @Test
    public void testPrintOrder(){
        //printUtil.print("920526457",null);
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey("2019062600342850");
        TbStore store = storeCache.findStoreById(tbOrder.getStoreId());
        printService.printOrder(tbOrder,store,3);
    }
}
