package ti.jobservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import java.util.Random;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

public class TiJobService extends JobService
{
	private static final String LCAT = "TiJobserviceModule";

	@Override
	public boolean onStartJob(JobParameters jobParameters)
	{
		Log.d(LCAT, "Job started");

		Intent si = new Intent();
		si.setClassName(TiApplication.getInstance().getApplicationContext(),
						jobParameters.getExtras().getString("service"));
		// si.putExtra("customData", "1");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			TiApplication.getInstance().getApplicationContext().startForegroundService(si);
		} else {
			TiApplication.getInstance().getApplicationContext().startService(si);
		}

		jobFinished(jobParameters, false);
		return false;
	}

	@Override
	public boolean onStopJob(JobParameters jobParameters)
	{
		Log.w(LCAT, "Job stopped");
		return false;
	}
}
