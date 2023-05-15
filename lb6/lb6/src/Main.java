import java.text.NumberFormat;
import java.util.*;

public class Main {

    private static final int N = 27;//номер у журналі
    private static final int M = 1;//номер групи
    private static final int N_CH = 5;//кількість каналів
    private static final double H = 90;//середній час обслуговування
    private static final int T1 = N + 1;
    private static final int T2 = N + 200;
    private static final String COL_FORMAT = "%-10s";

    private static final NumberFormat NUM_FORMAT = NumberFormat.getInstance();

    public static void main(String[] args) {
        NUM_FORMAT.setMinimumFractionDigits(3);
        NUM_FORMAT.setMaximumFractionDigits(3);
        List<Channel> channels = initializeChannels();
        Double lambda = calculateLambda();
        System.out.println("lambda: " + NUM_FORMAT.format(lambda) + " (викл/хв)");
        double ro = lambda * H;
        double sumZi = 0.0;
        printHeader();
        Queue<Candidate> queue = new LinkedList<>();
        int queuedCount = 0;
        int totalCount = 0;
        while (true) {
            Double ri = getNextRandNumber();
            Double zi = calculateZi(ri, lambda);
            sumZi = sumZi + zi;
            Double ksi = calculateKsi(ri);
            Double tk = generateTk(sumZi, queue.size());
            if (tk == null) {
                //end simulating
                break;
            }
            totalCount ++;
            if (queue.size() > 0) {
                if (tk < T2) {
                    //do not accept requests any more if out of range
                    printItem(ri, zi, ksi, tk, tk + ksi, null);
                    queue.offer(new Candidate(ksi));
                    queuedCount++;
                }
                Candidate first = queue.peek();
                Integer assignedChannel = proposeCandidate(tk, tk + first.processingTime, channels);
                if (assignedChannel != null) {
                    //removing from queue - report processing
                    queue.poll();
                    printItemFromQueue(first.processingTime, tk, tk + first.processingTime, assignedChannel);
                }
            } else {
                Integer assignedChannel = proposeCandidate(tk, tk + ksi, channels);
                if (assignedChannel == null) {
                    queue.offer(new Candidate(ksi));
                    queuedCount++;
                }
                printItem(ri, zi, ksi, tk, tk + ksi, assignedChannel);
            }
        }
        System.out.println("Всьго було в черзі:" + queuedCount);


        //Double pReject = (double) accumulate / tk.size();
        //System.out.println("K(н.) = " + accumulate);
        System.out.println("К(вим.) = " + totalCount);
        //System.out.println("Модельна ймовірність відмови = " + NUM_FORMAT.format(pReject));
        System.out.println("p = " + ro);
        System.out.println("h = " + H + " хв.");
        System.out.println("P0 = " + calcP0(ro));
        System.out.println("Р(чер.) = " + calculatePQueue(ro));
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

    private static Integer proposeCandidate(double nextTk, double tkEndProcessingTime, List<Channel> channels) {
        Integer processorChannelNum = null;
        for (int k = 0; k < channels.size(); k++) {
            Channel candidate = channels.get(k);
            boolean isAccepted = candidate.takeIntoProcessing(nextTk, tkEndProcessingTime);
            if (isAccepted) {
                processorChannelNum = candidate.getNumber();
                break;
            }
        }
        return processorChannelNum;
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
        String chNo = assignedChannel != null ? assignedChannel.toString() : "В чергу";
        line.append(String.format(COL_FORMAT, chNo));
        System.out.println(line);
    }

    private static void printItemFromQueue(Double ksi, Double tk, Double tFree, Integer assignedChannel) {
        StringBuilder line = new StringBuilder();
        line.append(String.format(COL_FORMAT, "-"));
        line.append(String.format(COL_FORMAT, "-"));
        line.append(String.format(COL_FORMAT, NUM_FORMAT.format(ksi)));
        line.append(String.format(COL_FORMAT, NUM_FORMAT.format(tk)));
        line.append(String.format(COL_FORMAT, NUM_FORMAT.format(tFree)));
        line.append(String.format(COL_FORMAT, "З черги->" + assignedChannel));
        System.out.println(line);
    }

    private static Double calculateKsi(Double ri) {
        return -H * Math.log(ri);
    }

    private static Double generateTk(Double sumZi, int queueSize) {
        Double tk = T1 + sumZi;
        if (tk > T2 && queueSize == 0) {
            return null;
        }
        return tk;
    }

    private static Double calculateZi(Double r, Double lambda) {
        return -(1.0 / lambda) * Math.log(r);
    }

    private static Double calculateLambda() {
        return 12 * (double) M / (N * N_CH);
    }

    private static Double getNextRandNumber() {
        return Math.random();
    }

    private static Double calculatePQueue(double ro) {
        double numerator = Math.pow(ro, N_CH + 1);
        double denominator = factorial(N_CH) * (N_CH - ro);

        return (numerator / denominator) * calcP0(ro);
    }

    private static Double calcP0(double ro) {
        double numerator = 1;
        double denominator = 0;
        for (int k = 0; k <= N_CH; k++) {

            denominator = denominator + Math.pow(ro, k) / factorial(k);
        }
        denominator += (Math.pow(ro, N_CH + 1) / (factorial(N_CH) * (N_CH - ro)));
        return numerator / denominator;
    }
}
