public class Channel {
    private Double endProcessingTime;
    private int number;

    Channel(int index) {
        this.number = index + 1;
    }

    public int getNumber() {
        return number;
    }

    boolean takeIntoProcessing(Double nextTk, Double tkEndProcessingTime) {
        if (endProcessingTime == null) {
            //channel was free - take into processing and remember when will be free
            this.endProcessingTime = tkEndProcessingTime;
            return true;
        }
        boolean takenIntoProcessing = false;
        if (endProcessingTime <= nextTk) {
            //if channel is free when next tk is coming, get it into processing, otherwise reject
            endProcessingTime = tkEndProcessingTime;
            takenIntoProcessing = true;
        }
        return takenIntoProcessing;
    }
}
