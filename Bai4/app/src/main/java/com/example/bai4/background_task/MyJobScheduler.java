package com.example.bai4.background_task;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class MyJobScheduler extends JobService {

    private JobAsyncTask mJobAsyncTask;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("BBB", "hi 2");
        mJobAsyncTask = new JobAsyncTask();
        mJobAsyncTask.execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("EEE", "jhaoz");
        if (mJobAsyncTask != null) {
            if (mJobAsyncTask.isCancelled()) {
                return true;
            }
            mJobAsyncTask.cancel(true);
        }
        return false;
    }

    private class JobAsyncTask extends AsyncTask<JobParameters, Void, JobParameters> {
        // JobParameters contains the parameters used to configure/identify the job.
        // You do not create this object yourself,
        // instead it is handed in to your application by the System.

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            Toast.makeText(getApplicationContext(), "HELLO", Toast.LENGTH_LONG).show();
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobFinished(jobParameters, false);
        }
    }
}
