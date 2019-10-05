package com.ibay.tea.common.service.impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.PrintService;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.common.utils.OrderItemPrintUtil;
import com.ibay.tea.common.utils.PrintUtil;
import com.ibay.tea.common.utils.YiLianYunPrintUtil;
import com.ibay.tea.dao.TbApiUserAddressMapper;
import com.ibay.tea.dao.TbOrderItemMapper;
import com.ibay.tea.dao.TbPrinterMapper;
import com.ibay.tea.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class PrintServiceImpl implements PrintService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintServiceImpl.class);

    @Resource
    private PrintUtil printUtil;

    @Resource
    private TbPrinterMapper tbPrinterMapper;

    @Resource
    private TbOrderItemMapper tbOrderItemMapper;

    @Resource
    private OrderItemPrintUtil orderItemPrintUtil;

    @Resource
    private TbApiUserAddressMapper tbApiUserAddressMapper;

    @Resource
    private YiLianYunPrintUtil yiLianYunPrintUtil;


    @Override
    public void addPrinter(int printerId) {
        TbPrinter tbPrinter = tbPrinterMapper.findById(printerId);
        String addPrinterInfo = tbPrinter.getPrinterSn()+"#"+tbPrinter.getPrinterKey()+"#"+tbPrinter.getPrinterRemark();
        printUtil.addprinter(addPrinterInfo);
    }

    @Override
    public String printOrder(TbOrder tbOrder, TbStore store,int printType) {
        if (printType == ApiConstant.PRINT_TYPE_ORDER_ALL){
            //打印订单明细
            List<TbOrderItem> orderItems = tbOrderItemMapper.findOrderItemByOrderId(tbOrder.getOrderId());
            printOrderItem(tbOrder,orderItems,store);
            //打印订单信息
            TbPrinter printer = tbPrinterMapper.findById(store.getOrderPrinterId());
            if (printer.getPrinterBrand() == 0){
                //飞蛾打印机打应订单
                String printContent = buildOrderPrintContent(tbOrder);
                printUtil.print(printer.getPrinterSn(),printContent);
            }else if (printer.getPrinterBrand() == 1){
                //易联云打印机打印订单
                String content = buildOrderPrintByYiLianYun(tbOrder);
                yiLianYunPrintUtil.sendContent(printer,content);
            /*    for (int i = 0; i < 2; i++) {
                    yiLianYunPrintUtil.sendContent(printer,content);
                }*/
            }

        }else if (printType == ApiConstant.PRINT_TYPE_ORDER){
            TbPrinter printer = tbPrinterMapper.findById(store.getOrderPrinterId());
            if (printer.getPrinterBrand() == 0){
                String printContent = buildOrderPrintContent(tbOrder);
                printUtil.print(printer.getPrinterSn(),printContent);
            } else if(printer.getPrinterBrand() == 1){

                yiLianYunPrintUtil.sendContent(printer,buildOrderPrintByYiLianYun(tbOrder));

            }

        }else if (printType == ApiConstant.PRINT_TYPE_ORDER_ITEM){
            List<TbOrderItem> orderItems = tbOrderItemMapper.findOrderItemByOrderId(tbOrder.getOrderId());
            printOrderItem(tbOrder,orderItems,store);
        }
       return null;
    }

    private String buildOrderPrintByYiLianYun(TbOrder tbOrder) {

        List<TbOrderItem> orderItemList = tbOrderItemMapper.findOrderItemByOrderId(tbOrder.getOrderId());

        String productList = "";

        for (TbOrderItem orderItem : orderItemList) {
            productList += orderItem.getTitle() +"   X"+ orderItem.getNum()+"    "+orderItem.getPrice()+"\\r\n";
        }




        String sendStr = "";
        if (tbOrder.getSelfGet() == 1){
            TbApiUserAddress tbApiUserAddress = tbApiUserAddressMapper.selectByPrimaryKey(tbOrder.getUserAddressId());
            LOGGER.info("tbApiUserAddress  :{}",tbApiUserAddress);
            sendStr += "电话:"+tbApiUserAddress.getPhoneNum()+"("+tbApiUserAddress.getUserName()+")";
            String addressName = tbApiUserAddress.getName() == null ? tbApiUserAddress.getAdname():tbApiUserAddress.getName();

            sendStr += "\\r\n地址: "+ addressName +"\\r\n";
            if (StringUtils.isNotBlank(tbApiUserAddress.getHouseNumber())){
                sendStr += "详细地址: "+tbApiUserAddress.getHouseNumber();
            }
        }

        String printContent = "<center><FH2><FS><FW>  EECUP TCL国际E店 </FS></FW></FH2>\\r\\r</center>" +
                "<center><FH2>取餐码:"+ tbOrder.getTakeCode()+" </FH2>\\r\\r</center>" +
                "................................\\r\n" +
                "下单时间："+tbOrder.getCreateDateStr()+"\\r\n" +
                "订单编号："+ tbOrder.getOrderId()+"\\r\n" +
                "*************商品***************\\r<FH><FW> \n" +
                productList+
  /*              "同步菜      x1    10.00\\r\n" +
                "Dan         x1    7.80\\r\n" +
                "</FW></FH>\n" +*/
                "************************<FH>\n" +
                "订单金额：￥"+tbOrder.getOrderPayment()+"\\r\n" +
                "实付金额：￥"+tbOrder.getPayment()+"\\r\n" +
                 sendStr;

               /* "配送地址：棕榈南岸 4栋3单元404号\\r\n" +
                "牟（先生）：186-9830-9092\\r\n" +*/

        if (StringUtils.isNotEmpty(tbOrder.getBuyerMessage())){
            printContent +=  "\\r\n订单备注："+tbOrder.getBuyerMessage()+"\\r";
        }
        return printContent;
    }

    private String buildOrderPrintContent(TbOrder tbOrder) {
        List<TbOrderItem> orderItemList = tbOrderItemMapper.findOrderItemByOrderId(tbOrder.getOrderId());
        String printContent = "";
        printContent += "<CB>eecup南山分店</CB><BR>";
        printContent += "<CB>结账单</CB><BR>";
        printContent += "时间: "+tbOrder.getCreateDateStr()+"<BR>";
        printContent += "单号: "+tbOrder.getTakeCode()+"<BR>";
        printContent += "名称          数量         单价<BR>";
        printContent += "-----------------------------<BR>";
        for (TbOrderItem orderItem : orderItemList) {
            printContent += orderItem.getTitle()+"   X"+orderItem.getNum()+"       "+orderItem.getTotalFee()+"<BR>";
        }
        printContent += "-----------------------------<BR>";
        printContent += "合计：                 "+tbOrder.getPayment()+"<BR>";
        if (tbOrder.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
            TbApiUserAddress tbApiUserAddress = tbApiUserAddressMapper.selectByPrimaryKey(tbOrder.getUserAddressId());
            printContent += "-----------------------------<BR>";
            printContent += "电话: "+tbApiUserAddress.getPhoneNum()+"("+tbApiUserAddress.getUserName()+")<BR>";
            String addressName = tbApiUserAddress.getName() == null ? tbApiUserAddress.getAdname():tbApiUserAddress.getName();
            String address = "";
            if (addressName.length() > 12){
                for (int i = 0; ; i++) {
                    if ((i+1)*12 >= addressName.length()){
                        address += addressName.substring(i*12);
                        break;
                    }
                    address += addressName.substring(i*12,(i+1)*12)+"<BR>    ";
                }
            }else {
                address = addressName;
            }
            printContent += "地址: "+ address +"<BR>";
            if (StringUtils.isNotBlank(tbApiUserAddress.getHouseNumber())){
                printContent += "详细地址: "+tbApiUserAddress.getHouseNumber()+"<BR>";
            }
            if (tbOrder.getBuyerMessage() != null){
                printContent += "订单备注: "+tbOrder.getBuyerMessage()+"<BR>";
            }
        }

        return printContent;
    }

    @Override
    public String queryPrinterStatus(String sn) {
        return null;
    }

    @Override
    public boolean queryOrderPrintStatus(String orderPrintId) {
        return false;
    }

    @Override
    public String printOrderItem(TbOrder tbOrder,TbOrderItem orderItem,TbStore store) {

        TbPrinter printer = tbPrinterMapper.findById(store.getOrderItemPrinterId());

        for (int i = 0;i < orderItem.getNum();i++){
            String printContent = buildOrderItemPrintContent(store,tbOrder,orderItem);
            orderItemPrintUtil.sendContent(printer,printContent);
        }
        return null;
    }

    @Override
    public String printOrderItem(TbOrder tbOrder,List<TbOrderItem> orderItemList, TbStore store) {
        tbOrder.setCurrentIndex(1);
        for (TbOrderItem orderItem : orderItemList) {
            printOrderItem(tbOrder,orderItem,store);
        }
        return null;
    }

    private String buildOrderItemPrintContent(TbStore store,TbOrder tbOrder,TbOrderItem orderItem) {
        String away = "外送";
        if (tbOrder.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SELF_GET){
            away = "自提";
        }

        //订单商品数量
        int goodsCount = tbOrder.getGoodsTotalCount();
        int currentIndex = tbOrder.getCurrentIndex();
        String printContent = "";
        printContent += "\r   订单号:"+tbOrder.getTakeCode()+"   数量:"+currentIndex+"/"+goodsCount+"\r\r";
        printContent += "   <FB><FS>"+orderItem.getTitle()+"</FS></FB>\r";
        printContent += "   "+orderItem.getSkuDetailDesc()+"\r\\r\"";

        printContent += "   时间:"+ DateUtil.viewDateFormat(tbOrder.getCreateTime())+"\r";
        printContent += "           "+away+"/"+store.getStoreName();
        tbOrder.setCurrentIndex(++currentIndex);
        return printContent;
    }
}
