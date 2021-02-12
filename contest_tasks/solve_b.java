import java.util.Scanner;

public class solve_b {
    public static void main(String[] args){
        try(Scanner myScanner = new Scanner(System.in)) {
            int n;
            n = myScanner.nextInt();
            int[] x = new int[n];
            int[] y = new int[n];
            for(int i = 0; i < n; ++i) {
                x[i] = myScanner.nextInt();
                y[i] = myScanner.nextInt();
            }
            double s = 0;
            for(int i = 1; i < n; ++i) {
                s += (double) (x[i] - x[i - 1]) * (y[i] + y[i - 1]) / 2;
            }
            s += (double) (x[0] - x[n - 1]) * (y[0] + y[n - 1]) / 2;
            s = Math.abs(s);
            System.out.println(s);
        }
    }
}
