package de.sensorcloud.android.helpertools;

import java.util.UUID;

public class Helper {

	public static String BASE_URL = "http://10.0.2.2:8080";
	
	public static String generateUUID(){
		return UUID.randomUUID().toString();
	}
}
