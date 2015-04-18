package pula.web.controllers.teacher;

import java.io.File;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.AttachmentFile;
import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import pula.controllers.http.JsonResultWithMap;
import pula.controllers.http.JsonResultWithPage;
import pula.web.services.SessionUserService;
import pula.web.services.TeacherService;

@Controller
public class ScoreController {

	@Resource
	TeacherService teacherService;
	@Resource
	SessionUserService sessionUserService;

	// list
	@RequestMapping
	@Barrier()
	public ModelAndView entry(
			@RequestParam(value = "p", defaultValue = "1", required = false) Long p) {

		if (p == null) {
			p = 1L;
		}
		JsonResultWithPage results = teacherService.list4score(p,
				sessionUserService.getActorId());

		if (results.isError()) {
			Pe.raise(results.getMessage());

		}

		// 解析明细
		for (MapBean mb : results.getData().getItems()) {
			mb.put("startTime", new Date(mb.asLong("startTime")));
		}

		return new ModelAndView().addObject("results", results.getData());
	}

	// list
	@RequestMapping
	@Barrier()
	public ModelAndView view(@RequestParam(value = "id") long id) {

		JsonResultWithMap jrm = teacherService.getCourseTaskStudent4Score(id,
				sessionUserService.getActorId());

		if (jrm.isError()) {
			Pe.raise(jrm.getMessage());
		}

		MapBean mb = jrm.getData();
		mb.put("startTime", new Date(mb.asLong("startTime")));
		mb.put("endTime", new Date(mb.asLong("endTime")));
		Long st = mb.asLong("scoreTime");
		if (st != null) {
			mb.put("scoreTime", new Date(st));
		}

		return new ModelAndView().addObject("result", mb);
	}

	@RequestMapping
	@ResponseBody
	@Barrier()
	public AttachmentFile viewWork(@RequestParam(value = "id") long id) {

		JsonResultWithMap jrm = teacherService.getStudentWorkFile(id,
				sessionUserService.getActorId());

		if (jrm.isError()) {
			Pe.raise(jrm.getMessage());
		}

		// 获得文件路径

		String fp = jrm.getData().string("filePath");

		AttachmentFile af = AttachmentFile.create(new File(fp),
				MediaType.IMAGE_JPEG);

		af.setBurnAfterReading(false);

		return af;
	}

	// 打分
	@RequestMapping
	@ResponseBody
	@Barrier()
	public JsonResult _score(@RequestParam(value = "id") long id,
			@RequestParam("s1") int s1, @RequestParam("s2") int s2,
			@RequestParam("s3") int s3, @RequestParam("s4") int s4,
			@RequestParam("s5") int s5) {

		JsonResult jr = teacherService.makeScore(id,
				sessionUserService.getActorId(), s1, s2, s3, s4, s5);

		return jr;
	}

}
