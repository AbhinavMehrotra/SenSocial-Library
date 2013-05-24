package SenSocialManager;


public enum Activity {
	act1("standing",getSensorNameById(Sensors.SENSOR_TYPE_ACCELEROMETER)),
	act2("sitting",getSensorNameById(Sensors.SENSOR_TYPE_ACCELEROMETER));
	
	private final String activity;
	private final String sensor;
	
	Activity(String activityName, String sensorName){
		activity=activityName;
		sensor=sensorName;
	}
	
	public String getActivityName(){
		return activity;
	}
	
	public String getSensorName(){
		return sensor;
	}
	
	private static String getSensorNameById(int id){
		String sensor=null;
		if(id==Sensors.SENSOR_TYPE_ACCELEROMETER) sensor="accelerometer";
		else if(id==Sensors.SENSOR_TYPE_BLUETOOTH) sensor="bluetooth";
		else if(id==Sensors.SENSOR_TYPE_LOCATION) sensor="location";
		else if(id==Sensors.SENSOR_TYPE_MICROPHONE) sensor="microphone";
		else if(id==Sensors.SENSOR_TYPE_WIFI) sensor="wifi";		
		return sensor;
	}
}
