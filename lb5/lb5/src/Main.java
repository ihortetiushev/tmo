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

        System.out.println("а) P(k):");
        for(int k = 0; k <= N; k++) {
            double P = (Math.pow(p, k) / getF(k)) * P0;
            System.out.println("P"+ k +" = "+ P);
        }

        System.out.println();
        double P_bz = (Math.pow(p,N) / (getF(N - 1) * (N - p))) * P0;
        System.out.println("б) Р(зайн.) = " + P_bz);

        double M_req;
        double sum_M_req = 0;
        for (int k =0; k <= N - 1; k++) {
            double x = Math.pow(p,k)/getF(k);
            sum_M_req += x;
        }
        System.out.println();
        System.out.println("в) Сума знаменника M(вимог.) = " + sum_M_req);
        M_req = P0 * ( (p * sum_M_req) + ( Math.pow(p,N+1) * (N + 1 - p) ) / ( getF(N-1)) * Math.pow((N-p), 2) );
        System.out.println("M(вимог.) = " + M_req);

        double M_queue;
        M_queue = (Math.pow(p,N+1) * P0) / (getF(N -1) * Math.pow((N -p),2));
        System.out.println();
        System.out.println("г) M(черги) = " + M_queue);

        double M_free;
        double M_busy = p;
        M_free = N - M_busy;
        System.out.println();
        System.out.println("ґ) М(вільн.) = " + M_free);

        System.out.println();
        System.out.println("д) M(зайн.) = " + M_busy);

        double T_exp = ( ( Math.pow(p,N) ) / ( V * getF(N - 1) * Math.pow((N - p),2) ) ) * P0;
        System.out.println();
        System.out.println("е) Т(оч.) = " + T_exp);

        double T_gen_exp = ( ( Math.pow(p,N + 1) ) / ( getF(N - 1) * Math.pow((N - p),2) ) ) * P0;

        System.out.println();
        System.out.println("є) T(з.оч) = " + T_gen_exp);


        double T_req = T_exp + (1 / V);
        System.out.println();
        System.out.println("ж) T(вим.) = " + T_req);

        double T_sum_req = T_gen_exp + p;
        System.out.println();
        System.out.println("з) T(с.вим.) = " + T_sum_req);
    }
}