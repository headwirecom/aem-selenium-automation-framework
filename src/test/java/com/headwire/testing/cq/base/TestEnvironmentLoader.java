package com.headwire.testing.cq.base;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;

/**
 * Helper class for loading a TestEnvironment from a json file 
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class TestEnvironmentLoader {
	public static TestEnvironmentLoader INSTANCE = new TestEnvironmentLoader();
	
	private TestEnvironmentLoader() {}
	
	public TestEnvironment loadConfiguration(String env) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader("settings.json"));
		TestEnvironment environment = new Gson().fromJson(br, TestEnvironment.class); 
		return environment;
	}
}
