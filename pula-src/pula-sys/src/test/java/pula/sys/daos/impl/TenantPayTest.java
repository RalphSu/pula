/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pula.sys.daos.impl;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pula.sys.app.WeiXinPayController;
import pula.sys.vo.WechatNotifyEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/root-context.xml",
		"classpath*:/servlet-context.xml" })
public class TenantPayTest {

	@Autowired
	private WeiXinPayController controller;

	@Test
	public void test() {
		WechatNotifyEntity notifyEntity = new WechatNotifyEntity();
		notifyEntity.setTransaction_id("ididdidi");
		StringBuilder msgBuilder = new StringBuilder();
		StringBuilder codeBuilder = new StringBuilder();
		controller.createNoticeOrder("x", "dd", 1, notifyEntity, msgBuilder,
				codeBuilder);

		controller.createTimeCourseOrder("x", "x", 1, notifyEntity, msgBuilder,
				codeBuilder);
	}

	@Test
	public void test2() throws Exception {
		String contents = FileUtils.readFileToString(new File(
				TenantPayTest.class.getResource("/callback.xml").getPath()));
		String resp = controller.wechatPayNotify(contents,
				createSampleRequest(), createSampleResponse());
		System.out.println(resp);
	}

	private HttpServletRequest createSampleRequest() {
		return new SimpleHttpRequest();
	}

	private HttpServletResponse createSampleResponse() {
		return new SimpleHttpResponse();
	}
}
