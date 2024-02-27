package fr.cenotelie.training.concurrency;

public class SynchronizedDataRace {

    public static void main(String[] args) {
        //TODO: run 2 instances of Counter Thread
        // And display counter
    }

    static class CounterThread extends Thread {
        static int counter = 0;

        @Override
        public void run() {
            //TODO: increment counter into a loop with 1_000_000 iterations
            // Try to synchronize on different objects
        }
    }
}
