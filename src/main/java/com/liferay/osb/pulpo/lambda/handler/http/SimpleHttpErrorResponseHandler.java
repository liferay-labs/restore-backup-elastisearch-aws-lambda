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

import com.amazonaws.AmazonServiceException;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.HttpResponseHandler;

import java.nio.charset.StandardCharsets;

import java.util.Scanner;

/**
 * @author Cristina González
 */
public class SimpleHttpErrorResponseHandler implements HttpResponseHandler {

	@Override
	public Object handle(HttpResponse response) {
		String errorMessage = null;

		try (Scanner scanner = new Scanner(
				response.getContent(), StandardCharsets.UTF_8.name())) {

			Scanner scannerWithDelimiter = scanner.useDelimiter("\\A");

			errorMessage = scannerWithDelimiter.next();
		}

		AmazonServiceException ase = new AmazonServiceException(
			response.getStatusText());

		ase.setStatusCode(response.getStatusCode());
		ase.setServiceName("es");
		ase.setErrorMessage(errorMessage);

		return ase;
	}

	@Override
	public boolean needsConnectionLeftOpen() {
		return false;
	}

}