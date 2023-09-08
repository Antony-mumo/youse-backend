package com.company.youse.utils;

import org.apache.commons.lang.RandomStringUtils;

import java.util.UUID;

public class IDUtils {

	public static String generateId() {
		return RandomStringUtils.random(16, true, true);
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}
}
