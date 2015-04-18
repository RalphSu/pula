package pula.web.controllers.student;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.system.vo.MapBean;
import pula.controllers.http.JsonResultPointsMix;
import pula.services.SessionUserService;
import pula.services.StudentService;
import pula.vo.PageInfo;

@Controller
public class PointsController {
	@Resource
	private SessionUserService sessionUserService;
	@Resource
	private StudentService studentService;

	@RequestMapping
	@Barrier()
	public ModelAndView entry(
			@RequestParam(value = "p", defaultValue = "1", required = false) Long p) {

		JsonResultPointsMix jr = studentService.getPointsLog(p,
				sessionUserService.getActorId());

		if (jr.isError()) {
			Pe.raise(jr.getMessage());

		}

		PageInfo<MapBean> results = jr.getData().getData();

		// 解析明细
		for (MapBean mb : results.getItems()) {
			mb.put("createdTime", new Date(mb.asLong("createdTime")));
		}

		return new ModelAndView().addObject("totalPoints",
				jr.getData().getTotalPoints()).addObject("results", results);

	}

}
