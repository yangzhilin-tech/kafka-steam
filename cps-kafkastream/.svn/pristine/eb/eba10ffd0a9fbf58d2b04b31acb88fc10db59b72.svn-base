package com.wgmf.web;



/**
 * 从配置文件里读取api文档信息
 * 
 *
 */
public final class ApiDocConfigPropReader {


	private static final String API_NAME = "apiName";

	private static final String API_DESC = "apiDesc";

	private static final String API_VERSION = "apiVersion";

	private static final String TERM_OF_SERVICE = "tos";

	private static final String AUTHOR = "author";

	private static final String API_LICENSE = "apiLicense";

	private static final String API_LICENSE_URL = "apiLicenseURL";

	private ApiDocConfigPropReader() {
		// empty
	}

	public static String getApiName() {
		return getConfigProperty(API_NAME);
	}

	public static String getApiDesc() {
		return getConfigProperty(API_DESC);
	}

	public static String getApiVersion() {
		return getConfigProperty(API_VERSION);
	}

	public static String getTos() {
		return getConfigProperty(TERM_OF_SERVICE);
	}

	public static String getAuthor() {
		return getConfigProperty(AUTHOR);
	}

	public static String getApiLicense() {
		return getConfigProperty(API_LICENSE);
	}

	public static String getApiLicenseUrl() {
		return getConfigProperty(API_LICENSE_URL);
	}

	private static String getConfigProperty(String key) {
		return key;
	}
}
