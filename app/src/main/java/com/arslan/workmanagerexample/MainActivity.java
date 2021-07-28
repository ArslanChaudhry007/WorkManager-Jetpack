package com.arslan.workmanagerexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating a data object
        //to pass the data with workRequest
        //we can put as many variables needed
        Data data = new Data.Builder()
                .putString(MyWorker.TASK_DESC, "The task data passed from MainActivity")
                .build();


        //This is the subclass of our WorkRequest
      //  final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class).setInputData(data).build();
        final PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(MyWorker.class,10, TimeUnit.SECONDS).setInputData(data).build();


        //A click listener for the button
        //inside the onClick method we will perform the work
        findViewById(R.id.buttonEnqueue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enqueuing the work request
                WorkManager.getInstance().enqueue(workRequest);
            }
        });

        //Getting the TextView
        final TextView textView = findViewById(R.id.textViewStatus);

        //Listening to the work status
        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {

                        //Displaying the status into TextView
                        textView.append(workInfo.getState().name() + "\n");
                    }
                });
    }
}
