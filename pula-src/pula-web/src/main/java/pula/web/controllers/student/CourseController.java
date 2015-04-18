package pula.web.controllers.student;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.support.utils.FreemarkerUtil;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import pula.controllers.http.JsonResultCourseMix;
import pula.controllers.http.JsonResultWithMap;
import pula.services.SessionUserService;
import pula.services.StudentService;

@Controller
public class CourseController {
	@Resource
	private SessionUserService sessionUserService;
	@Resource
	private StudentService studentService;

	@RequestMapping
	@Barrier()
	public ModelAndView entry(
			@RequestParam(value = "type", required = false, defaultValue = "1") int type) {

		// 所有课程数据+ 当前学员的课程数据
		JsonResultCourseMix jr = studentService.getCourses(type,
				sessionUserService.getActorId());

		if (jr.isError()) {
			Pe.raise(jr.getMessage());
		}

		SelectOptionList courseTypes = SelectOption.getList(type, new int[] {
				1, 2, 3, 4, 5 }, new String[] { "A", "B", "C", "D", "E" });

		Map<Integer, MapBean> hitsMap = WxlSugar.newHashMap();
		for (MapBean mb : jr.getData().getHits()) {
			hitsMap.put(mb.asInteger("courseId"), mb);
		}

		return new ModelAndView()
				.addObject("hits", FreemarkerUtil.hash(hitsMap))
				.addObject("results", jr.getData().getData())
				.addObject("courseTypes", courseTypes);

	}

	@RequestMapping
	@Barrier()
	public ModelAndView game(@RequestParam("id") long courseId) {

		// 所有课程数据+ 当前学员的课程数据
		JsonResultWithMap jr = studentService.getCoursesGame(courseId,
				sessionUserService.getActorId());

		if (jr.isError()) {
			Pe.raise(jr.getMessage());
		}

		return new ModelAndView().addObject("results", jr.getData());

	}

	@RequestMapping
	@ResponseBody
	@Barrier()
	public JsonResult _points(@RequestParam("k") String k) {

		// k 是加密的courseId

		long courseId = 0;
		// 所有课程数据+ 当前学员的课程数据
		JsonResult jr = studentService.takeGamePoints(courseId,
				sessionUserService.getActorId());

		return jr;

	}

	@RequestMapping
	@Barrier()
	public ModelAndView stat() {
		// 雷达图
		JsonResultWithMap jr = studentService.getRadar(sessionUserService
				.getActorId());

		if (jr.isError()) {
			Pe.raise(jr.getMessage());
		}
		return new ModelAndView().addObject("result", jr.getData());
	}

	@RequestMapping
	@Barrier()
	public ModelAndView _stat() {

		JsonResultWithMap jr = studentService.getRadar(sessionUserService
				.getActorId());

		if (jr.isError()) {
			Pe.raise(jr.getMessage());
		}

		// 生成jfreechart 文件

		return new ModelAndView().addObject("result", jr.getData());
	}

	// @RequestMapping
	// @ResponseBody
	// @Barrier()
	// public AttachmentFile statRadar() {
	//
	// JsonResultWithMap jr = studentService.getRadar(sessionUserService
	// .getActorId());
	//
	// if (jr.isError()) {
	// Pe.raise(jr.getMessage());
	// }
	//
	// // 生成jfreechart 文件
	//
	// return null;
	// }

}
