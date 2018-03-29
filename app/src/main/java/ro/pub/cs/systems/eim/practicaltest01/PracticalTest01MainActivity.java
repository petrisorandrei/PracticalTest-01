package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    interface Constants {

        String TEXT1 = "";
        String TEXT2 = "";

        String FIRST_ACTION = "first";
        String SECOND_ACTION = "second";
        String THIRD_ACTION = "third";
        int NUMBER_OF_CLICKS_THRESHOLD = 10;
    }

    private IntentFilter intentFilter = new IntentFilter();
    private static final int SECONDARY_ACTIVITY_REQUEST_CODE = 1;
    Button button1,button2, button3;
    EditText textView1, textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int nr = Integer.valueOf(textView1.getText().toString());
                textView1.setText(String.valueOf(nr+1));
                //D.1
                if (Integer.parseInt(textView1.getText().toString()) + Integer.parseInt(textView2.getText().toString()) > Constants.NUMBER_OF_CLICKS_THRESHOLD
                        ) {
                    System.out.println("//////////////");
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                    intent.putExtra("firstNumber", Integer.parseInt(textView1.getText().toString()));
                    intent.putExtra("secondNumber", Integer.parseInt(textView2.getText().toString()));
                    startService(intent);
                }
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int nr = Integer.valueOf(textView2.getText().toString());
                textView2.setText(String.valueOf(nr+1));
            }
        });

        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                int numberOfClicks = Integer.parseInt(textView1.getText().toString()) +
                        Integer.parseInt(textView2.getText().toString());
                intent.putExtra("numberOfClicks", numberOfClicks);
                startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        outState.putString("Text1", textView1.getText().toString());
        outState.putString("Text2", textView1.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);

        textView1.setText(savedInstanceState.getString("Text1"));
        textView2.setText(savedInstanceState.getString("Text2"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }

    // D.2
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);

    }

    //D.2
    @Override
    public void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();

    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }
}
