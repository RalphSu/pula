package pula.services;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import puerta.support.utils.JacksonUtil;
import puerta.support.utils.WxlSugar;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import pula.BhzqConstants;
import pula.controllers.http.JsonResultWithList;
import pula.controllers.http.JsonResultWithMap;
import pula.controllers.http.JsonResultWithPage;
import pula.controllers.http.PostParameter;
import pula.controllers.http.Response;
import pula.miscs.MD5Checker;

@Service
public class TeacherService {

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
		return parameterKeeper.getString(BhzqConstants.BASE_URL,
				"http://localhost:8125/pula-sys/app/teacherinterface/");
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

	// 待评分的信息
	public JsonResultWithPage list4score(Long pageIndex, String actorId) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(
					base_url + "list4score",
					buildParameters(cpp("pageIndex", pageIndex.toString()),
							cpp("actorId", actorId)));

			JsonResultWithPage m = r.asJsonResultWithPage();

			return m;

		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultWithPage.error(e);
		}
	}

	public JsonResultWithMap getCourseTaskStudent4Score(long id, String actorId) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(
					base_url + "getCourseTaskStudent4Score",
					buildParameters(cpp("id", String.valueOf(id)),
							cpp("actorId", actorId)));

			JsonResultWithMap m = r.asJsonResultWithMap();

			return m;

		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultWithMap.error(e);
		}
	}

	public JsonResultWithMap getStudentWorkFile(long id, String actorId) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(
					base_url + "getStudentWorkFile",
					buildParameters(cpp("id", String.valueOf(id)),
							cpp("actorId", actorId)));

			JsonResultWithMap m = r.asJsonResultWithMap();

			return m;

		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultWithMap.error(e);
		}
	}

	public JsonResult makeScore(long id, String actorId, int s1, int s2,
			int s3, int s4, int s5) {
		String base_url = getBaseUrl();
		try {
			Response r = client.post(
					base_url + "makeScore",
					buildParameters(cpp("id", String.valueOf(id)),
							cpp("s1", String.valueOf(s1)),
							cpp("s2", String.valueOf(s2)),
							cpp("s3", String.valueOf(s3)),
							cpp("s4", String.valueOf(s4)),
							cpp("s5", String.valueOf(s5)),
							cpp("actorId", actorId)));

			JsonResult m = r.asJsonResult();

			return m;

		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.e(e.getMessage());
		}
	}

	public JsonResultWithList getCalendar(int year, int month, String actorId) {
		String base_url = getBaseUrl();

		try {
			Response r = client.post(
					base_url + "getCalendar",
					buildParameters(cpp("year", String.valueOf(year)),
							cpp("month", String.valueOf(month)),
							cpp("actorId", actorId)));

			JsonResultWithList jr = JacksonUtil.get(r.asString(),
					JsonResultWithList.class);

			return jr;

		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultWithList.error(e);
		}
	}
}
