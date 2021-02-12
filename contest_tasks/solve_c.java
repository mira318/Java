import java.util.Scanner;

public class solve_c {
    public static void main(String[] args){
        try(Scanner myScanner = new Scanner(System.in)) {
            int n, m;
            n = myScanner.nextInt();
            int[] a = new int[n];
            for(int i = 0; i < n; ++i) {
                a[i] = myScanner.nextInt();
            }
            m = myScanner.nextInt();
            int[] b = new int[m];
            for(int i = 0; i < m; ++i) {
                b[i] = myScanner.nextInt();
            }
            int k = myScanner.nextInt();
            int currentB = m - 1;
            int ans = 0;
            for(int i = 0; i < n; ++i) {
                while((currentB >= 0) && (a[i] + b[currentB] > k)){
                    currentB--;
                }
                while((currentB >= 0) && (a[i] + b[currentB] == k)){
                    currentB--;
                    ans++;
                }
            }
            System.out.println(ans);
        }
    }
}
