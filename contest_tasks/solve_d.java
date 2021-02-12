import java.util.Scanner;

public class solve_d {
    public static void main(String[] args){
        try(Scanner myScanner = new Scanner(System.in)) {
            int n = myScanner.nextInt();
            int m = myScanner.nextInt();
            int[] dp = new int[m + 1];
            for (int i = 1; i < m + 1; ++i){
                dp[i] = 1;
            }
            for(int i = 2; i < n + 1; ++i){
                for(int j = 1; j < m + 1; ++j){
                    dp[j] = 1 + (dp[j] + j - 1) % i;
                }
            }
            System.out.println(dp[m]);
        }
    }
}
