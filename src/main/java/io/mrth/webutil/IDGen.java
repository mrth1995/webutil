package io.mrth.webutil;

import java.util.UUID;

public class IDGen {
	
	public static String generate() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
