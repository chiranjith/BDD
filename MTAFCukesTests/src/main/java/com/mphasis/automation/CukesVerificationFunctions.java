package com.mphasis.automation;

import org.junit.Assert;

public class CukesVerificationFunctions {

	public static void verifyEquals(String expected, String actual) {

		Assert.assertEquals(expected, actual);
	}

}
