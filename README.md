# Ti.Jobservice<br/><small>background job execution for Android using JobServices</small>
## Methods

### startJob();
Starts a new job that will execute a service at the given time or interval.

#### Parameter
* service: service name, see below
* interval: periodic execution interval (minimum 15 minutes)
* minLatency: delayed by the provided amount of time
* deadline: maximum scheduling latency. The job will be run by this deadline even if other requirements are not met
* persistant: Set whether or not to persist this job across device reboots
* requireCharging: Specify that to run this job, the device must be charging,
* requireIdle: When set true, ensure that this job will not run if the device is in active use
* extras: [] // not yet used

### stopAllJobs();
Stops all current jobs

## Service

create a service, add it to the tiapp.xml file:
```xml
<android xmlns:android="http://schemas.android.com/apk/res/android">
    <manifest>
	 	<!-- YOUR MANIFEST CODES HERE -->
    </manifest>
    <services>
        <service type="interval" url="ti_service.js"/>
    </services>
</android>
```
Check `/build/android/AndroidManifest.xml` for something like `com.test.job.Ti_serviceService`. Copy that string and set it as a `service` inside `startJob()`.

## Example

```javascript
var job = require("ti.jobservice");

var win = Ti.UI.createWindow({
	title: "title",
	backgroundColor: '#fff'
});

var btn1 = Ti.UI.createButton({
	title: "start",
	top: 10
});
var btn2 = Ti.UI.createButton({
	title: "stop all jobs",
	top: 100
});

btn1.addEventListener("click", function() {
	job.startJob({
		service: "com.test.job.Ti_serviceService",
		interval: 15000,
		//minLatency: 15000,
		//deadline: 15000,
		persistant: true,
		//requireCharging: false,
		//requireIdle: false,
		//extras: [],

	});
	console.log("Start", new Date());
});
btn2.addEventListener("click", function() {
	job.stopAllJobs();
	console.log("Stop all jobs", new Date());
});


win.add(btn1)
win.add(btn2)
win.open();
```
