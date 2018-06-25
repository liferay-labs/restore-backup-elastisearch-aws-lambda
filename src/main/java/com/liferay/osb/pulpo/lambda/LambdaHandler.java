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

package com.liferay.osb.pulpo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.liferay.osb.pulpo.lambda.handler.RestoreBackupRequest;
import com.liferay.osb.pulpo.lambda.handler.elasticsearch.ElasticSearchAWSUtil;

/**
 * @author David Arques
 */
public class LambdaHandler
	implements RequestHandler<RestoreBackupRequest, String> {

	@Override
	public String handleRequest(
		RestoreBackupRequest restoreBackupRequest, Context context) {

		LambdaLogger logger = context.getLogger();

		logger.log("Restoring backup with " + restoreBackupRequest + "\n");

		return ElasticSearchAWSUtil.restoreBackup(restoreBackupRequest, logger);
	}

}