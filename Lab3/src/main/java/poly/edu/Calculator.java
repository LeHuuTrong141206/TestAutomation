package poly.edu;

import static java.lang.System.out;

public class Calculator {
//    public int add(int a, int b){
//        return a+b;
//    }
//
//    public int subtract(int a, int b){
//        return a-b;
//    }
//
//    public int multiply(int a, int b){
//        return a * b;
//    }
//

    //Phương thức dễ lỗi ArithmeticException nếu b = 0
    public int divide(int a, int b){
        return a / b;
    }

    // Phương thức gây lỗi NumberFormatException nếu chuỗi không phải là số
    public int parseStringToInt(String s) {
        return Integer.parseInt(s);
    }

    // Phương thức gây lỗi NullPointerException nếu chuỗi là null
    public int getStringLength(String s) {
        return s.length();
    }


}
