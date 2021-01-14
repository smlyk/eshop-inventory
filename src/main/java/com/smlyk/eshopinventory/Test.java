package com.smlyk.eshopinventory;

/**
 * @Author: always
 * @Date: 2020/12/30 3:03 下午
 */
public class Test {
    public static void main(String[] args) {

        boolean a= true;
        boolean b= false;
        boolean c= false;

        if(!a){
            //只要打卡有问题，不判断定位和百度校验
            System.out.println("result =0; problem = \"打卡问题\"");
        }else if(!b){
            //打卡没问题的情况
            if(c){
                //百度校验没问题
                System.out.println("result =0; problem = \"定位问题\";");
            }else{
                //百度校验有问题
                System.out.println("result =0; problem = \"定位及校验问题\";");
            }
        }else if(!c){
            //打卡没问题，定位没问题，百度校验有问题
            System.out.println("result =0; problem = \"校验问题\";");
        }else{
            System.out.println("result =1; problem = \"正常\";");
        }



    }
}
