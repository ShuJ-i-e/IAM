package com.shujie;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class Utility {
	public static String getSiteURL(ServerHttpRequest request) {
	    String siteURL = request.getPath().toString();
	    return siteURL.replace(request.getPath().contextPath().value(), "");
	}
}
