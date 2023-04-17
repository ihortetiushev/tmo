import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Lb4 {
    private static final int RAND_NUMBERS_COUNT = 100;
    private static final int N = 27;//момер у журналі
    private static final int M = 1;//момер групи
    private static final int T1 = N+1;
    private static final int T2 = N+200;

    public static void main(String[] args) {
        List<Double> rs = generateRandNumbers();
        System.out.println("ri");
        printList(rs);
        System.out.println("\n");
        Double lambda = calculateLambda();
        System.out.println("lambda = " + lambda);
        System.out.println("\n");
        System.out.println("zi");
        List<Double> zi = calculateZi(rs, lambda);
        printList(zi);

        System.out.println("\n");
        System.out.println("tk");
        List<Double> tk = calculateTk(zi);
        printList(tk);
    }

    private static void printList(List<Double> lst) {
        NumberFormat fmt = NumberFormat.getInstance();
        fmt.setMaximumFractionDigits(3);
        fmt.setMinimumFractionDigits(3);
        lst.forEach(e -> System.out.println(fmt.format(e)));
    }

    private static final List<Double> calculateTk(List<Double> zi) {
        List<Double> listTk = new ArrayList<>();
        for (int k = 0; k < zi.size(); k++) {
            Double sumZi = 0.0;
            for (int i = 0; i <= k; i++) {
                if (i < zi.size()) {
                    sumZi = sumZi + zi.get(i);
                }
            }
            Double tk = T1 + sumZi;
            if (tk > T2) {
                break;
            }
            listTk.add(tk);
        }
        return listTk;
    }

    private static List<Double> calculateZi(List<Double> lst, Double lamdba) {
        List<Double> listZi = new ArrayList<>();
        for (Double num : lst) {
            listZi.add(-(1.0 / lamdba) * Math.log(num));
        }
        return listZi;
    }

    private static Double calculateLambda() {
        return 10 * (double) M / N;
    }

    private static List<Double> generateRandNumbers() {
        List<Double> randNumbers = new ArrayList<>();
        for (int i = 0; i < RAND_NUMBERS_COUNT; i++) {
            randNumbers.add(Math.random());
        }
        return randNumbers;
    }
}
