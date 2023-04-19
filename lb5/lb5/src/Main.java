public class Main {
    public static int getF(int f) {
        int result = 1;
        for (int i = 1; i <= f; i++) {
            result = result * i;
        }
        return result;
    }
    public static void main(String[] args) {
        final int Nn = 27;
        final int m = 1;
        final int N = 5;

        double lambda = 15.0 * ((double) m / (Nn * N));
        double V = 3.0 * ((m + 2.0 + (Nn / 5.0)) / (Nn * N));
        double p = lambda / V;
        double Pk;
        System.out.println("Task1");
        System.out.println("lambda = " + lambda);
        System.out.println("V = " + V);
        System.out.println("p = " + p);


        System.out.println("P(k)");

        for(int k = 0; k <= N; k++) {
            double sum_p = 0 ;
            for (int i =0; i <= N; i++) {
                double x = Math.pow(p,i)/getF(i);
                sum_p += x;
            }
            Pk = (Math.pow(p,k)/getF(k))/(sum_p);
            System.out.println("P"+ k +" "+ Pk);
        }

        System.out.println();


    }
}