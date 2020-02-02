package ti.jobservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.appcelerator.kroll.KrollDict;

public class TiJobReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		String serviceName = prefs.getString("tiJob_service", "");
		int interval = prefs.getInt("tiJob_interval", -1);
		int minLat = prefs.getInt("tiJob_minLatency", -1);
		int deadline = prefs.getInt("tiJob_dedline", -1);
		boolean persistant = prefs.getBoolean("tiJob_persistant", false);
		boolean reqCharging = prefs.getBoolean("tiJob_requireCharging", false);
		boolean reqIdle = prefs.getBoolean("tiJob_requireIdle", false);

		KrollDict kd = new KrollDict();

		kd.put("service", serviceName);
		kd.put("interval", interval);
		kd.put("minLatency", minLat);
		kd.put("deadline", deadline);
		kd.put("persistant", persistant);
		kd.put("requiredCharging", reqCharging);
		kd.put("requiredIdle", reqIdle);
		TiJobserviceModule.scheduleJob(kd);
	}
}
