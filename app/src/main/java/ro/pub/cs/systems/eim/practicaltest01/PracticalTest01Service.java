package ro.pub.cs.systems.eim.practicaltest01;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.util.Log;

import java.util.Date;
import java.util.Random;

public class PracticalTest01Service extends Service {
    public PracticalTest01Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private ProcessingThread processingThread = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int firstNumber = intent.getIntExtra("firstNumber", -1);
        int secondNumber = intent.getIntExtra("secondNumber", -1);
        processingThread = new ProcessingThread(this, firstNumber, secondNumber);
        processingThread.start();
        return Service.START_REDELIVER_INTENT;
    }

    class ProcessingThread extends Thread {
        Context context;
        int firstNumber;
        int secondNumber;
        private boolean isRunning = true;
        private Random random = new Random();

        public ProcessingThread(Context context, int firstNumber, int secondNumber) {
            this.context = context;
            this.firstNumber = firstNumber;
            this.secondNumber = secondNumber;
        }

        @Override
        public void run() {
            Log.d("[ProcessingThread]", "Thread has started!");
            while (isRunning) {
                sendMessage();
                sleep();
            }
            Log.d("[ProcessingThread]", "Thread has stopped!");
        }

        private void sendMessage() {
            Intent intent = new Intent();
            int rand = random.nextInt() % 3;
            if (rand == 0)
                intent.setAction(PracticalTest01MainActivity.Constants.FIRST_ACTION);
            else if (rand == 1)
                intent.setAction(PracticalTest01MainActivity.Constants.SECOND_ACTION);
            else
                intent.setAction(PracticalTest01MainActivity.Constants.THIRD_ACTION);
            intent.putExtra("message", new Date(System.currentTimeMillis()) + " " + (firstNumber + secondNumber) / 2 + " " + Math.sqrt(firstNumber * secondNumber));
            context.sendBroadcast(intent);
        }

        public void sleep() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        void stopThread() {
            isRunning = false;
        }
    }
}