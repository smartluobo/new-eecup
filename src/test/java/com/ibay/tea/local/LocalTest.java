package com.ibay.tea.local;

public class LocalTest {




    public static void main(String[] args) {

        String st = "0.8";
        String[] split = st.split("\\.");
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
        int ratio = Integer.valueOf(split[1]);
        System.out.println(ratio);


//        int i = PriceCalculateUtil.intOrderTbPrice(new BigDecimal(String.valueOf(22.40)));
//        System.out.println(i);

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
