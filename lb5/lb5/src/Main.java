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
        double P0;
        System.out.println("Task1");
        System.out.println("lambda = " + lambda);
        System.out.println("V = " + V);
        System.out.println("p = " + p);

        double sum_p0 = 0;
        for (int k =0; k <= N; k++) {
            double x = Math.pow(p,k)/getF(k);
            sum_p0 += x;
        }
        System.out.println("Сума знаменника P0 = " + sum_p0);
        P0 = 1 / (sum_p0 + (Math.pow(p,(N + 1) / (getF(N) * (N -p) ))));
        System.out.println("P0 = " + P0);
        System.out.println();

        System.out.println("P(k):");
        for(int k = 0; k <= N; k++) {
            double P = (Math.pow(p, k) / getF(k)) * P0;
            System.out.println(P);
        }

        System.out.println();
        double P_bz = (Math.pow(p,N) / (getF(N - 1) * (N - p))) * P0;
        System.out.println("Р(зайн.) = " + P_bz);

    }
}