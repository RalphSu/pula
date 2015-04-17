package pula.web.controllers.teacher;

import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.support.utils.DateJedi;
import puerta.support.utils.StringTool;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.web.controllers.http.JsonResultWithList;
import pula.web.services.SessionUserService;
import pula.web.services.TeacherService;

@Controller
public class CalendarController {

	@Resource
	private SessionUserService sessionUserService;
	@Resource
	private TeacherService teacherService;

	@RequestMapping
	@Barrier()
	public ModelAndView entry(
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "month", required = false, defaultValue = "0") int month) {

		Calendar cal = Calendar.getInstance();
		int nowYear = cal.get(Calendar.YEAR);
		int nowMonth = cal.get(Calendar.MONTH);

		if (year == 0) {
			year = nowYear;
			month = nowMonth;
		}

		int prevMonth = month - 1;
		int prevYear = year;
		if (prevMonth < 0) {
			prevMonth = 11;
			prevYear = year - 1;
		}
		int nextMonth = month + 1;
		int nextYear = year;
		if (nextMonth >= 12) {
			nextMonth = 0;
			nextYear = year + 1;
		}

		return new ModelAndView().addObject("year", year)
				.addObject("prevYear", prevYear)
				.addObject("prevMonth", prevMonth).addObject("month", month)
				.addObject("nextMonth", nextMonth)
				.addObject("nextYear", nextYear).addObject("nowYear", nowYear)
				.addObject("nowMonth", nowMonth);
	}

	@RequestMapping
	@Barrier()
	@ResponseBody
	public JsonResult _calendar(
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "month", required = false, defaultValue = "0") int month) {

		Calendar cal = Calendar.getInstance();
		int nowYear = cal.get(Calendar.YEAR);
		int nowMonth = cal.get(Calendar.MONTH);

		if (year == 0) {
			year = nowYear;
			month = nowMonth;
		}

		JsonResultWithList list = teacherService.getCalendar(year, month,
				sessionUserService.getActorId());

		if (list.isError()) {
			Pe.raise(list.getMessage());
		}

		MapBean retMap = new MapBean();

		for (MapBean mb : list.getData()) {
			Long time = mb.asLong("startTime");
			DateJedi dj = DateJedi.createNow().setTimeInMillis(time);
			Calendar cc = dj.to();
			int hour = cc.get(Calendar.HOUR);
			int min = cc.get(Calendar.MINUTE);
			int day = cc.get(Calendar.DAY_OF_MONTH);
			mb.add("day", day)
					.add("hour", hour)
					.add("minute", min)
					.add("name",
							StringTool.fillZero(hour, 2) + ":"
									+ StringTool.fillZero(min, 2) + "<BR/>"
									+ mb.string("courseCategoryName") + "-"
									+ mb.string("courseName"));

			String key = String.valueOf(day);
			MapList ml = null;
			if (retMap.containsKey(key)) {
				ml = (MapList) retMap.get(key);
			} else {
				ml = new MapList();
				retMap.put(key, ml);
			}
			ml.add(mb);
		}

		return JsonResult.s(retMap);
	}
}
