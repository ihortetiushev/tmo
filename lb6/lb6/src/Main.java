import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int RAND_NUMBERS_COUNT = 200;

    private static final int N = 27;//номер у журналі
    private static final int M = 1;//номер групи
    private static final int N_CH = 5;//кількість каналів
    private static final int H = 90;//середній час обслуговування
    private static final int T1 = N + 1;
    private static final int T2 = N + 200;
    private static final String COL_FORMAT = "%-10s";

    private static final NumberFormat NUM_FORMAT = NumberFormat.getInstance();

    public static void main(String[] args) {
        NUM_FORMAT.setMinimumFractionDigits(3);
        NUM_FORMAT.setMaximumFractionDigits(3);
        int K_lost = 0;
        List<Channel> channels = initializeChannels();
        List<Double> ri = generateRandNumbers();
        Double lambda = calculateLambda();
        double ro = lambda * H;
        System.out.println("lambda: " + NUM_FORMAT.format(lambda));
        List<Double> zi = calculateZi(ri, lambda);
        List<Double> ksiList = calculateKsi(ri);
        List<Double> tk = calculateTk(zi);
        int accumulate = simulateProcessing(ri, zi, ksiList, tk, channels);

        Double pReject = (double) accumulate / tk.size();
        System.out.println("K(н.) = " + accumulate);
        System.out.println("К(вим.) = " + tk.size());
        System.out.println("Модельна ймовірність відмови = " + NUM_FORMAT.format(pReject));
        /*System.out.println("ro = " + ro);
        System.out.println("lambda = " + lambda);
        System.out.println("H = " + H);*/
        System.out.println("P0 = " + calcP0(ro));
        System.out.println("Р(чер.) = " + calculatePQueue(ro));
        //System.out.println("Ймовірність відмови (Ерланг): " + NUM_FORMAT.format(calculateP(lambda)));
        System.out.println();

    }

    private static int factorial(int n) {
        int res = 1;
        for (int k = 1; k <= n; k++) {
            res = res * k;
        }
        return res;
    }

    static List<Channel> initializeChannels() {
        List<Channel> channels = new ArrayList<>();
        for (int i = 0; i < N_CH; i++) {
            channels.add(new Channel(i));
        }
        return channels;
    }

    private static int simulateProcessing(List<Double> ri, List<Double> zi, List<Double> ksiList,
                                          List<Double> tk, List<Channel> channels) {
        int rejectNumber = 0;
        printHeader();
        for (int i = 0; i < tk.size(); i++) {
            double nextTk = tk.get(i);
            double tkEndProcessingTime = nextTk + ksiList.get(i);
            Integer processorChannelNum = null;
            for (int k = 0; k < channels.size(); k++) {
                Channel candidate = channels.get(k);
                boolean isAccepted = candidate.takeIntoProcessing(nextTk, tkEndProcessingTime);
                if (isAccepted) {
                    processorChannelNum = candidate.getNumber();
                    break;
                }
            }
            if (processorChannelNum == null) {
                rejectNumber++;
            }
            printItem(ri.get(i), zi.get(i), ksiList.get(i), tk.get(i), tkEndProcessingTime, processorChannelNum);
        }
        return rejectNumber;
    }

    private static void printHeader() {
        StringBuilder header = new StringBuilder();
        header.append(String.format(COL_FORMAT, "r"));
        header.append(String.format(COL_FORMAT, "z"));
        header.append(String.format(COL_FORMAT, "E(ksi)"));
        header.append(String.format(COL_FORMAT, "t(пост)"));
        header.append(String.format(COL_FORMAT, "T(звіл)"));
        header.append(String.format(COL_FORMAT, "N каналу"));
        System.out.println(header);
    }

    private static void printItem(Double r, Double z, Double ksi, Double tk, Double tFree, Integer assignedChannel) {
        StringBuilder line = new StringBuilder();
        line.append(String.format(COL_FORMAT, NUM_FORMAT.format(r)));
        line.append(String.format(COL_FORMAT, NUM_FORMAT.format(z)));
        line.append(String.format(COL_FORMAT, NUM_FORMAT.format(ksi)));
        line.append(String.format(COL_FORMAT, NUM_FORMAT.format(tk)));
        line.append(String.format(COL_FORMAT, NUM_FORMAT.format(tFree)));
        String chNo = assignedChannel != null ? assignedChannel.toString() : "Втрата";
        line.append(String.format(COL_FORMAT, chNo));
        System.out.println(line);
    }

    private static List<Double> calculateKsi(List<Double> ri) {
        List<Double> ksiList = new ArrayList<>();
        for (Double r : ri) {
            //1/(1/H)==H
            ksiList.add(-H * Math.log(r));
        }
        return ksiList;
    }

    private static List<Double> calculateTk(List<Double> zi) {
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

    private static List<Double> calculateZi(List<Double> lst, Double lambda) {
        List<Double> listZi = new ArrayList<>();
        for (Double num : lst) {
            listZi.add(-(1.0 / lambda) * Math.log(num));
        }
        return listZi;
    }

    private static Double calculateLambda() {
        return 12 * (double) M / (N * N_CH);
    }

    private static List<Double> generateRandNumbers() {
        List<Double> randNumbers = new ArrayList<>();
        for (int i = 0; i < RAND_NUMBERS_COUNT; i++) {
            randNumbers.add(Math.random());
        }
        return randNumbers;
    }

    private static Double calculatePQueue(double ro) {
        double numerator = Math.pow(ro, N + 1);
        double denominator = factorial(N) * (N - ro);

        return (numerator / denominator) * calcP0(ro);
    }
    private static Double calcP0(double ro){
        double numerator = 1;
        double denominator = 0;
        for (int k = 0; k <= N; k++) {

            denominator = denominator + Math.pow(ro, k) / factorial(k);
        }
        denominator += ( Math.pow(ro, N + 1)/ ( factorial(N) * (N - ro) ) );
        return numerator / denominator;
    }
}
