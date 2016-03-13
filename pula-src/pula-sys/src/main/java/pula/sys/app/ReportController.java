/**
 * 
 */
package pula.sys.app;

import java.util.Date;
import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pula.sys.daos.TimeCourseUsageDao;

/**
 * Provide daily order report based on branch 每日全部 每日某个分部的上课单子 某个学生的全部上课订单
 * *消费页可直接查*
 * 
 * @author Administrator
 *
 */
@Controller
public class ReportController {

	@Autowired
	private TimeCourseUsageDao usageDao;

	@RequestMapping("/")
	public ModelAndView report(@RequestParam("branch") String branch,
			@RequestParam(value = "date", required = false) Date startDate) {
		if (startDate == null) {
			startDate = new Date();
		}

		List<Object> usages = readCourseUsage(usageDao, branch, startDate);
		// TODO

		ModelAndView view = new ModelAndView();
		return view;
	}

	public static List<Object> readCourseUsage(TimeCourseUsageDao usageDao, String branch, Date startDate) {
		String hql = " select usage " + " From TimeCourseOrderUsage usage "
				+ " ,"
				+ " TimeCourse course "
				+ " WHERE usage.usageTime = '%s' and course.branchName = '%s' AND usage.courseNo = course.no";
		hql = String.format(hql,
				DateTimeFormat.longDate().print(startDate.getTime()), branch);

		List<Object> courseUsages = usageDao.find(hql, branch, startDate);
		return courseUsages;
	}

}
