package eliascregard;

import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Scanner data = new Scanner(System.in);
        while (data.hasNextInt()) {
            int n = data.nextInt();
            System.out.println((16*n) + " x " + (9*n));
        }
    }
}

