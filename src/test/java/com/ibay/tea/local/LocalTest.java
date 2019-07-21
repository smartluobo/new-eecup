package com.ibay.tea.local;

import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.PriceCalculateUtil;
import com.ibay.tea.entity.TbApiUserAddress;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbOrderItem;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LocalTest {




    public static void main(String[] args) {

        int i = PriceCalculateUtil.intOrderTbPrice(new BigDecimal(String.valueOf(22.40)));
        System.out.println(i);

//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 10 ; i++) {
//            list.add(i);
//        }
//        List<Integer> newList = new ArrayList<>();
//        newList.addAll(list);
//
//        newList.removeIf(integer -> integer % 2 == 0);
//
//        System.out.println(newList);
//        System.out.println(list);


//        for (int i = 0; i < 10; i++) {
//            System.out.println(i);
//            if (i > 5){
//                System.out.println("ok");
//                break;
//            }
//        }
//        Random random = new Random();
//        for (int i = 0; i < 100; i++) {
//            System.out.print(random.nextInt(3)+"\t");
//        }


//        Random random = new Random();
//
//        int firstCount = 0;
//        int secondCount = 0;
//        int threeCount = 0;
//        int fourCount = 0;
//        for (int i = 0; i < 100; i++) {
//            int r = random.nextInt(100);
//            if (r <= 10){
//                firstCount++;
//            }else if (secondCount <= 30){
//                secondCount++;
//            }else if (r <= 60){
//                threeCount++;
//            }else {
//                fourCount++;
//            }
//        }
//        System.out.println("firstCount : "+firstCount+" secondCount : "+ secondCount+" threeCount : "+ threeCount+" fourCount : "+fourCount);
    }
}
