package ti.jobservice;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import java.util.HashMap;
import java.util.Map;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiConvert;
import org.json.JSONObject;

@Kroll.module(name = "TiJobservice", id = "ti.jobservice")
public class TiJobserviceModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "TiJobserviceModule";
	private static final boolean DBG = TiConfig.LOGD;
	static TiApplication context = TiApplication.getInstance();

	public TiJobserviceModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
	}

	// Methods
	@Kroll.method
	public void startJob(KrollDict dict)
	{
		scheduleJob(dict);
	}

	public static void scheduleJob(KrollDict dict)
	{
		String serviceName = dict.getString("service");
		int interval = TiConvert.toInt(dict.get("interval"), -1);
		int minLat = TiConvert.toInt(dict.get("minLatency"), -1);
		int deadline = TiConvert.toInt(dict.get("deadline"), -1);
		boolean persistant = TiConvert.toBoolean(dict.get("persistant"), false);
		boolean reqCharging = TiConvert.toBoolean(dict.get("requireCharging"), false);
		boolean reqIdle = TiConvert.toBoolean(dict.get("requireIdle"), false);
		Map<String, String> extras = (HashMap) dict.get("extras");
		JSONObject js = new JSONObject();
		if (extras != null && !extras.isEmpty()) {
			js = new JSONObject(extras);
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("tiJob_service", serviceName);
		editor.putInt("tiJob_interval", interval);
		editor.putInt("tiJob_minLatency", minLat);
		editor.putInt("tiJob_dedline", deadline);
		editor.putBoolean("tiJob_persistant", persistant);
		editor.putBoolean("tiJob_requireCharging", reqCharging);
		editor.putBoolean("tiJob_requireIdle", reqIdle);
		editor.putString("tiJob_extras", js.toString());
		editor.apply();

		JobScheduler jobScheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
		ComponentName jobService = new ComponentName(context.getApplicationContext(), TiJobService.class);

		PersistableBundle bundle = new PersistableBundle();
		bundle.putString("service", serviceName);
		bundle.putString("extras", js.toString());

		JobInfo.Builder builder = new JobInfo.Builder(0, jobService);
		builder.setExtras(bundle);
		if (interval > -1) {
			builder.setPeriodic(interval);
		} else {
			if (minLat != -1) {
				builder.setMinimumLatency(minLat); // wait at least
			}
			if (deadline != -1) {
				builder.setOverrideDeadline(deadline); // maximum delay
			}
		}
		// builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
		builder.setRequiresDeviceIdle(reqIdle);
		builder.setRequiresCharging(reqCharging);
		builder.setPersisted(persistant);

		int result = jobScheduler.schedule(builder.build());

		if (result == JobScheduler.RESULT_SUCCESS) {
			Log.d(LCAT, "Successfully scheduled job: " + result);
		} else {
			Log.e(LCAT, "Error: " + result);
		}
	}

	// Methods
	@Kroll.method
	public void stopAllJobs()
	{
		JobScheduler jobScheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
		Log.d(LCAT, "Stopping all jobs...");
		jobScheduler.cancelAll();
	}
}
