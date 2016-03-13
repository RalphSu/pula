/**
 * 
 */
package pula.sys.daos.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pula.sys.app.ReportController;
import pula.sys.daos.TimeCourseUsageDao;

/**
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/root-context.xml",
		"classpath*:/servlet-context.xml" })
public class ReportTest {

//	@Resource
//	ReportController report;
	@Resource
	TimeCourseUsageDao usageDao;

	@Test
	public void testLogin() {
		
		ReportController.readCourseUsage(usageDao, "文峰分校", new Date());
	}

}
