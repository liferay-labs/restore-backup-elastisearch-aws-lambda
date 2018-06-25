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

package com.liferay.osb.pulpo.lambda.handler.elasticsearch;

import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.util.StringUtils;

import com.liferay.osb.pulpo.lambda.handler.RestoreBackupRequest;
import com.liferay.osb.pulpo.lambda.handler.http.SimpleHttpErrorResponseHandler;
import com.liferay.osb.pulpo.lambda.handler.http.StringResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.net.URI;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author David Arques
 */
public class ElasticSearchAWSUtil {

	/**
	 * Lists the snapshots made in the given AWS Elasticsearch repository.
	 *
	 * @param request input request
	 * @param logger lambda logger
	 * @see <a href='https://www.elastic.co/guide/en/elasticsearch/reference/current/cat-snapshots.html'>cat-snapshots</a>
	 * @return String containing the result of the request
	 */
	public static String restoreBackup(
		RestoreBackupRequest request, LambdaLogger logger) {

		logger.log("Restoring backup with  " + request);

		_validateInputRequest(request);

		_checkBackupExistence(request, logger);

		_deleteAllIndices(request, logger);

		return _restoreBackup(request, logger);
	}

	private static void _checkBackupExistence(
		RestoreBackupRequest restoreBackupRequest, LambdaLogger lambdaLogger) {

		Request<Void> awsRequest = _createAwsRequest(
			restoreBackupRequest.getHost(),
			String.format(
				"/_snapshot/%s/%s", restoreBackupRequest.getBucket(),
				restoreBackupRequest.getBackup()),
			Collections.emptyMap(), HttpMethodName.GET, null);

		lambdaLogger.log(_EXECUTING_AWS_REQUEST + awsRequest);

		Response<AmazonWebServiceResponse<String>> awsResponse =
			_executeAwsRequest(awsRequest);

		lambdaLogger.log(
			_AMAZON_WEB_SERVICE_RESPONSE +
				awsResponse.getAwsResponse().getResult());

		if (awsResponse.getHttpResponse().getStatusCode() == 404) {
			throw new IllegalArgumentException(
				"Bucket " + restoreBackupRequest.getBackup() + " not found");
		}
	}

	private static Request<Void> _createAwsRequest(
		String host, String path, Map<String, List<String>> params,
		HttpMethodName httpMethodName, String content) {

		Request<Void> request = new DefaultRequest<>("es");

		request.setHttpMethod(httpMethodName);

		request.setEndpoint(URI.create(host));

		request.setResourcePath(path);

		if (!params.isEmpty()) {
			request.setParameters(params);
		}

		if (content != null) {
			InputStream contentInputStream = new ByteArrayInputStream(
				content.getBytes());

			request.setContent(contentInputStream);
		}

		AWS4Signer signer = new AWS4Signer();

		signer.setServiceName(request.getServiceName());
		signer.setRegionName(_REGION);
		signer.sign(
			request, new DefaultAWSCredentialsProviderChain().getCredentials());

		return request;
	}

	private static void _deleteAllIndices(
		RestoreBackupRequest restoreBackupRequest, LambdaLogger logger) {

		Request<Void> awsRequest = _createAwsRequest(
			restoreBackupRequest.getHost(), "_all", Collections.emptyMap(),
			HttpMethodName.DELETE, null);

		logger.log(_EXECUTING_AWS_REQUEST + awsRequest);

		Response<AmazonWebServiceResponse<String>> awsResponse =
			_executeAwsRequest(awsRequest);

		logger.log(
			_AMAZON_WEB_SERVICE_RESPONSE +
				awsResponse.getAwsResponse().getResult());
	}

	private static Response<AmazonWebServiceResponse<String>>
		_executeAwsRequest(Request<Void> request) {

		ClientConfiguration config = new ClientConfiguration();

		AmazonHttpClient.RequestExecutionBuilder builder = new AmazonHttpClient(
			config).requestExecutionBuilder();

		return builder.executionContext(
			new ExecutionContext(true)
		).request(
			request
		).errorResponseHandler(
			new SimpleHttpErrorResponseHandler()
		).execute(
			new StringResponseHandler()
		);
	}

	private static String _restoreBackup(
		RestoreBackupRequest request, LambdaLogger logger) {

		Request<Void> awsRequest = _createAwsRequest(
			request.getHost(),
			String.format(
				"/_snapshot/%s/%s/_restore", request.getBucket(),
				request.getBackup()),
			Collections.emptyMap(), HttpMethodName.POST, null);

		logger.log(_EXECUTING_AWS_REQUEST + awsRequest);

		Response<AmazonWebServiceResponse<String>> awsResponse =
			_executeAwsRequest(awsRequest);

		logger.log(
			_AMAZON_WEB_SERVICE_RESPONSE +
				awsResponse.getAwsResponse().getResult());

		return awsResponse.getAwsResponse().getResult();
	}

	private static void _validateInputRequest(
		RestoreBackupRequest restoreBackupRequest) {

		if (restoreBackupRequest == null) {
			throw new IllegalArgumentException(
				"RestoreBackupRequest must not be null");
		}

		if (StringUtils.isNullOrEmpty(restoreBackupRequest.getHost())) {
			throw new IllegalArgumentException("Host must not be empty");
		}

		if (StringUtils.isNullOrEmpty(restoreBackupRequest.getBucket())) {
			throw new IllegalArgumentException("Bucket name must not be empty");
		}

		if (StringUtils.isNullOrEmpty(restoreBackupRequest.getBackup())) {
			throw new IllegalArgumentException("Backup id must not be empty");
		}
	}

	private static final String _AMAZON_WEB_SERVICE_RESPONSE = "\nRESPONSE: ";

	private static final String _EXECUTING_AWS_REQUEST = "\nEXECUTE: ";

	private static final String _REGION = System.getenv(
		SDKGlobalConfiguration.AWS_REGION_ENV_VAR);

}