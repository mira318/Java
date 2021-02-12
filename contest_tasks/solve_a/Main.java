package solve_a;

import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        int[] a;
        int[] b;
        try(Scanner myScan = new Scanner(System.in)) {
            int n = myScan.nextInt();
            a = new int[n];
            b = new int[n];
            for(int i = 0; i < n; ++i){
                a[i] = myScan.nextInt();
            }
            for(int i = 0; i < n; ++i) {
                b[i] = myScan.nextInt();
            }
            int[] cumMax = new int[n];
            int[] cumIndex = new int[n];
            cumMax[0] = a[0];
            cumIndex[0] = 0;
            for(int i = 1; i < n; ++i){
                if(a[i] > cumMax[i - 1]){
                    cumIndex[i] = i;
                    cumMax[i] = a[i];
                } else {
                    cumIndex[i] = cumIndex[i - 1];
                    cumMax[i] = cumMax[i - 1];
                }
            }
            int aIndex = 0;
            int bIndex = 0;
            int ans = b[0] + a[0];
            for(int i = 1; i < n; ++i) {
                if(b[i] + cumMax[i] > ans){
                    ans = b[i] + cumMax[i];
                    aIndex = cumIndex[i];
                    bIndex = i;
                }
            }
            System.out.println(aIndex + " " + bIndex);

        }
    }
}
