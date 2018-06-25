/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.osb.pulpo.lambda.handler.http;

import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.util.IOUtils;

import java.io.IOException;

/**
 * Simple implementation to return a String.
 *
 * @author David Arques
 */
public class StringResponseHandler
	implements HttpResponseHandler<AmazonWebServiceResponse<String>> {

	@Override
	public AmazonWebServiceResponse<String> handle(HttpResponse response)
		throws IOException {

		AmazonWebServiceResponse<String> awsResponse =
			new AmazonWebServiceResponse<>();

		//putting response string in the result, available outside the handler
		String s = IOUtils.toString(response.getContent());

		awsResponse.setResult(s);

		return awsResponse;
	}

	@Override
	public boolean needsConnectionLeftOpen() {
		return false;
	}

}