package pula.web.services;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import puerta.support.utils.JacksonUtil;
import puerta.support.utils.WxlSugar;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import pula.BhzqConstants;
import pula.controllers.http.JsonResultCourseMix;
import pula.controllers.http.JsonResultPointsMix;
import pula.controllers.http.JsonResultWithMap;
import pula.controllers.http.PostParameter;
import pula.controllers.http.Response;
import pula.miscs.MD5Checker;

@Service
public class StudentService {

	@Resource
	private SessionService sessionService;
	@Resource
	ParameterKeeper parameterKeeper;

	pula.controllers.http.HttpClient client = new pula.controllers.http.HttpClient();

	public JsonResultWithMap login(String loginId, String password) {

		String base_url = getBaseUrl();
		try {
			Response r = client.post(
					base_url + "login",
					buildParameters(cpp("loginId", loginId),
							cpp("password", password)));

			JsonResultWithMap m = r.asJsonResultWithMap();

			return m;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonResultWithMap.error(e);
		}
	}

	private String getBaseUrl() {
//		return parameterKeeper.getString(BhzqConstants.BASE_URL,
//				return "http://localhost/pula-sys/app/studentinterface/";
				return "http://localhost/app/studentinterface/";
//				        );
	}

	private PostParameter[] buildParameters(PostParameter... params) {
		List<Object> paramsList = WxlSugar.newArrayList();
		List<PostParameter> ppList = WxlSugar.newArrayList();
		for (int i = 0; i < params.length; i++) {

			paramsList.add(params[i].getValue());

			ppList.add(params[i]); 
		}
		String ip = sessionService.env().getIp();
		ppList.add(cpp("ip", ip));
		paramsList.add(ip);

		ppList.add(cpp("md5", MD5Checker.sign(
				parameterKeeper.getString(BhzqConstants.INTERFACE_CALL_SECRET,
						MD5Checker.DEFAULT_SECRET), paramsList.toArray())));
		ppList.add(cpp("_json", "1"));

		return ppList.toArray(new PostParameter[ppList.size()]);

	}

	public JsonResult logout(String actorId) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(base_url + "logout",
					buildParameters(cpp("actorId", actorId)));

			JsonResult m = r.asJsonResult();

			return m;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonResult.e(e.getMessage());
		}
	}

	public JsonResult updatePassword(String actorId, String oldPassword,
			String newPassword) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(
					base_url + "updatePassword",
					buildParameters(cpp("actorId", actorId),
							cpp("oldPassword", oldPassword),
							cpp("newPassword", newPassword)));

			JsonResult m = r.asJsonResult();

			return m;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonResult.e(e.getMessage());
		}

	}

	public PostParameter cpp(String k, String v) {
		return new PostParameter(k, v);
	}

	public JsonResultPointsMix getPointsLog(Long pageIndex, String actorId) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(
					base_url + "getPointsLog",
					buildParameters(cpp("pageIndex", pageIndex.toString()),
							cpp("actorId", actorId)));

			JsonResultPointsMix jr = JacksonUtil.get(r.asString(),
					JsonResultPointsMix.class);

			return jr;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonResultPointsMix.error(e);
		}
	}

	public JsonResultCourseMix getCourses(int type, String actorId) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(
					base_url + "getCourses",
					buildParameters(cpp("type", String.valueOf(type)),
							cpp("actorId", actorId)));

			JsonResultCourseMix jr = JacksonUtil.get(r.asString(),
					JsonResultCourseMix.class);

			return jr;

		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultCourseMix.error(e);
		}
	}

	public JsonResultWithMap getCoursesGame(long courseId, String actorId) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(
					base_url + "getCoursesGame",
					buildParameters(
							cpp("courseTaskResultStudentId",
									String.valueOf(courseId)),
							cpp("actorId", actorId)));

			JsonResultWithMap jr = JacksonUtil.get(r.asString(),
					JsonResultWithMap.class);

			return jr;

		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultWithMap.error(e);
		}
	}

	public JsonResult takeGamePoints(long courseTaskResultStudentId,
			String actorId) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(
					base_url + "takeGamePoints",
					buildParameters(
							cpp("courseTaskResultStudentId",
									String.valueOf(courseTaskResultStudentId)),
							cpp("actorId", actorId)));

			JsonResult jr = JacksonUtil.get(r.asString(), JsonResult.class);

			return jr;

		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.e(e.getMessage());
		}
	}

	public JsonResultWithMap getRadar(String actorId) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(base_url + "getRadar",
					buildParameters(cpp("actorId", actorId)));

			JsonResultWithMap jr = JacksonUtil.get(r.asString(),
					JsonResultWithMap.class);

			return jr;

		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultWithMap.error(e);
		}
	}
}
