/**
 * 
 */
package pula.sys.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pula.sys.daos.BranchDao;
import pula.sys.daos.TimeCourseUsageDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Student;
import pula.sys.domains.TimeCourse;
import pula.sys.domains.TimeCourseOrderUsage;

/**
 * Provide daily order report based on branch <br/>
 * 每日全部 每日某个分部的上课单子 某个学生的全部上课订单 *消费页可直接查*
 * 
 * @author Administrator
 *
 */
@Controller
public class ReportController {

	@Autowired
	private TimeCourseUsageDao usageDao;
	
	@Autowired
	private BranchDao branchDao;

	@RequestMapping
	public ModelAndView entry(
			@RequestParam(value = "branch", required = false) String branch,
			@RequestParam(value = "date", required = false) String startDate) {
		Date givenDate = null;
		if (StringUtils.isEmpty(startDate)) {
			givenDate = new Date();
		} else {
			try {
				DateTime dt = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(startDate);
				givenDate = dt.toDate();
			} catch (Exception e) {
				givenDate = new Date();
			}
		}

		List<TimeCourseOrderUsage> usages = new ArrayList<TimeCourseOrderUsage>();
		usages = readCourseUsage(usageDao, branch, givenDate);
		List<Branch> branches = branchDao.loadEnabled();
		@SuppressWarnings("unchecked")
		List<String> branchNames = (List<String>)CollectionUtils.collect(branches, new Transformer() {
			
			@Override
			public Object transform(Object input) {
				return ((Branch)input).getName();
			}
		});

		ModelAndView view = new ModelAndView();
		view.addObject("branches", branchNames);
		view.addObject("usages", usages);
		view.addObject("branch", branch);
		view.addObject("date", startDate);
		return view;
	}

	public static List<TimeCourseOrderUsage> readCourseUsage(
			TimeCourseUsageDao usageDao, String branch, Date startDate) {
		String hql = " select usage, student, course " + " From "
				+ " TimeCourseOrderUsage usage," + " TimeCourse course, "
				+ " Student student "
				+ " WHERE usage.usageTime = '%s' and course.branchName = '%s' "
				+ " AND usage.courseNo = course.no "
				+ " AND usage.studentNo = student.no "
				+ " AND usage.removed = 0 AND usage.enabled=1";
		hql = String.format(
				hql,
				DateTimeFormat.forPattern("yyyy-MM-dd").print(
						startDate.getTime()), branch);

		List<Object[]> result = usageDao.find(hql);

		List<TimeCourseOrderUsage> courseUsages = new ArrayList<TimeCourseOrderUsage>();
		for (Object[] pair : result) {
			TimeCourseOrderUsage u = (TimeCourseOrderUsage) pair[0];
			Student stu = (Student) pair[1];
			TimeCourse course = (TimeCourse) pair[2];
			u.setStudentName(stu.getName());
			u.setCourseName(course.getName());
			courseUsages.add(u);
		}

		return courseUsages;
	}

}
