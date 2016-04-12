/**
 * 
 */
package pula.sys.daos.impl;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pula.sys.app.ReportController;
import pula.sys.daos.TimeCourseUsageDao;
import pula.sys.domains.TimeCourseOrderUsage;

/**
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/root-context.xml",
		"classpath*:/servlet-context.xml" })
public class ReportTest {

	@Resource
	TimeCourseUsageDao usageDao;

	@Test
	public void testLogin() {

	    DateTime dt = DateTime.parse("2016-04-12");

		List<TimeCourseOrderUsage> usages = ReportController.readCourseUsage(usageDao, "文峰店", dt.toDate());
		
		System.out.print(usages.size());
		
		System.out.println(usages);
	}

}
