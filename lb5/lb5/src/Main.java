public class Main {
    public static void main(String[] args) {
        final int Nn = 27;
        final int m = 1;
        final int N = 5;

        double lambda = 15.0 * ((double) m / (Nn * N));
        double V = 3.0 * ((m + 2.0 + (Nn / 5.0)) / (Nn * N));
        double p = lambda / V;
        System.out.println("Task1");
        System.out.println("lambda = " + lambda);
        System.out.println("V = " + V);
        System.out.println("p = " + p);



    }
}