package com.yuhj.ontheway.clients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yuhj.ontheway.bean.CourseData;
import com.yuhj.ontheway.bean.HuoDongData;
import com.yuhj.ontheway.bean.JingXuanData;
import com.yuhj.ontheway.bean.JingxuanDetailData;
import com.yuhj.ontheway.bean.UserInfo;
import com.yuhj.ontheway.bean.UserInfoData;

/**
 * @name ClientApi
 * @Descripation 杩欐槸涓�涓敤鏉ヨ闂綉缁滅殑绫�<br>
 *               1銆�<br>
 *               2銆�<br>
 *               3銆�<br>
 * @author 绂规収鍐�
 * @date 2014-10-22
 * @version 1.0
 */
public class ClientApi {
	private static String startId;

	public ClientApi() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
    public static JSONObject ParseJson(final String path, final String encode) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParams = httpClient.getParams();
		// HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		// HttpConnectionParams.setSoTimeout(httpParams, 10000);
		HttpPost httpPost = new HttpPost(path);
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity(),
						encode);
				JSONObject jsonObject = new JSONObject(result);
				return jsonObject;
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return null;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			if (httpClient != null)
				httpClient.getConnectionManager().shutdown();
		}
		return null;

	}

	/**
	 * @param Url
	 *            涓嬭浇鐨刄rl
	 * @return
	 */
	public static ArrayList<JingXuanData> getJingXuanData(String Url) {
		ArrayList<JingXuanData> list = new ArrayList<JingXuanData>();
		JSONObject json = ParseJson(Url, "utf-8");

		if (json == null) {
			return null;
		} else {
			try {

				JSONArray Data = json.getJSONObject("obj").getJSONArray("list");
				
				for (int i = 0; i < Data.length(); i++) {
					System.out.println("------->"+i);
					JSONObject data = Data.getJSONObject(i);
					JingXuanData jingXuanData = new JingXuanData();
					jingXuanData.setId(data.optString("id"));
					JSONObject element = data.getJSONObject("tour");
					jingXuanData.setTitle(element.optString("title"));
					jingXuanData.setPubdate(element.optString("startdate"));
					jingXuanData.setPictureCount(element.optString("cntP"));
					jingXuanData.setImage(element.optString("coverpic"));
					jingXuanData.setViewCount(element.optString("pcolor"));
					jingXuanData.setFavoriteCount(element.getString("likeCnt"));
					jingXuanData.setViewCount(element.optString("viewCnt"));
					jingXuanData.setForeword(element.optString("foreword"));
					UserInfo userInfo = new UserInfo();
					JSONObject owner = element.optJSONObject("owner");
					userInfo.setUsername(owner.optString("username"));
					userInfo.setNickname(owner.optString("nickname"));
					userInfo.setUserId(owner.optString("userid"));
					userInfo.setAvatar(owner.getString("avatar"));
					jingXuanData.setUserInfo(userInfo);
					JSONArray dispcitys = element.getJSONArray("dispCities");
					String[] citys = new String[dispcitys.length()];
					for (int j = 0; j < dispcitys.length(); j++) {

						citys[j] = dispcitys.optString(j);
					}
					jingXuanData.setDispCities(citys);
					jingXuanData.setCmtCount(element.getString("cntcmt"));
					// System.out.println("----->"+jingXuanData.getDispCities().length);
					/*
					 * JSONArray cmt = element.optJSONArray("cmt"); Comment[]
					 * comments = new Comment[cmt.length()]; if (cmt!=null) {
					 * 
					 * for (int j = 0; j < cmt.length(); j++) { JSONObject
					 * cmtdata = cmt.getJSONObject(i); Comment comment = new
					 * Comment(); UserInfo uInfo = new UserInfo(); JSONObject
					 * user = cmtdata.getJSONObject("user");
					 * uInfo.setAvatar(user.optString("avatar"));
					 * uInfo.setNickname(user.optString("username"));
					 * uInfo.setNickname(user.optString("nickname"));
					 * comment.setUserInfo(uInfo);
					 * comment.setContent(cmtdata.optString("words"));
					 * comment.setLike(cmtdata.optBoolean("isLiked"));
					 * comments[j] = comment; } }
					 * jingXuanData.setComments(comments);
					 */
					jingXuanData.setTourId(element.optString("id"));
					list.add(jingXuanData);
					if (i == Data.length() - 1) {
						startId = jingXuanData.getId();
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("------->"+list.size());
			return list;

		}

	}

	public static String getStartId() {

		return startId;
	}

	/**
	 * 瑙ｆ瀽瀹炰綋鐨凧son鏁版嵁
	 * 
	 * @param tourId
	 * @return
	 */
	public static ArrayList<JingxuanDetailData> getJingxuanDetailDatas(
			String tourId) {
		ArrayList<JingxuanDetailData> list = new ArrayList<JingxuanDetailData>();
		String jingXuanDetailUrl = "http://app.117go.com/demo27/php/formAction.php?submit=getATour2&tourid="
				+ tourId
				+ "&recType=1&refer=PlazaHome&ID2=1&token=3a79c4024f682aee74723a419f6605f9&v=a5.0.4&vc=anzhi&vd=f2e4ee47505f6fba";
		System.out.println("------>"+jingXuanDetailUrl);
		JSONObject json = ParseJson(jingXuanDetailUrl, "utf-8");
		if (json == null) {
			return null;
		} else {

			try {
				JSONArray Data = json.getJSONObject("obj").getJSONArray(
						"records");
				for (int i = 0; i < Data.length(); i++) {
					JSONObject data = Data.getJSONObject(i);
					JingxuanDetailData jingxuanDetailData = new JingxuanDetailData();
					/*
					 * String location = data.getJSONObject("location")
					 * .optJSONObject("city").optString("city");
					 * jingxuanDetailData.setPoi(location);
					 */
					jingxuanDetailData.setText(data.getString("words"));
					jingxuanDetailData
							.setImage("http://img.117go.com/timg/p308/"
									+ data.getString("picfile"));
					list.add(jingxuanDetailData);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		}

	}
	
	/**
	 * 瑙ｆ瀽瀹炰綋鐨凧son鏁版嵁
	 * 
	 * @param tourId
	 * @return
	 */
    public static ArrayList<CourseData> getCourseDatas() {
		String courseDetailUrl = "http://121.40.151.183:8080/pula-sys/app/timecourse/list";
		JSONObject json = ParseJson(courseDetailUrl, "utf-8");
		ArrayList<CourseData> result = new ArrayList<CourseData>();
        if (json == null) {
            return null;
        } else {
            try {
				JSONArray Data = json.getJSONArray("records");
				
				for (int i = 0; i < Data.length(); i++) {
				    JSONObject data = Data.getJSONObject(i);
                    CourseData courseData = convertToCourse(data);
					result.add(courseData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

    private static CourseData convertToCourse(JSONObject data) throws JSONException {
        CourseData courseData = new CourseData();
        courseData.setId(data.getString("id"));
        courseData.setNo(data.getString("no"));
        courseData.setName(data.getString("name"));
        courseData.setBranchName(data.getString("branchName"));
        courseData.setClassRoomName(data.getString("classRoomName"));
        courseData.setDurationMinute(data.getInt("durationMinute"));
        courseData.setEndTime(data.getString("endTime"));
        courseData.setStartTime(data.getString("startTime"));
        courseData.setMaxStudentNum(data.getInt("maxStudentNum"));
        courseData.setPrice(data.getInt("price"));
        courseData.setStartHour(data.getInt("startHour"));
        courseData.setStartMinute(data.getInt("startMinute"));
        // TODO : use correct data
        courseData.setImage("http://121.40.151.183:8080/pula-sys/app/image/icon?fp=" + "logo.png" /* data.getString("imgPath") */ 
                + "&sub=notice");
        return courseData;
    }
	
    public static ArrayList<CourseData> getCouseDetailData(String searchId) {
        ArrayList<CourseData> list = new ArrayList<CourseData>();
        String Url = "http://121.40.151.183:8080/pula-sys/app/timecourse/get?id=" + searchId;
        JSONObject json = ParseJson(Url, "utf-8");
        if (json == null) {
            return null;
        } else {
            try {
                JSONObject data = json.getJSONObject("data");
                CourseData courseData = convertToCourse(data);
                list.add(courseData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
    }

    public static ArrayList<HuoDongData> getHuoDongList() {
        String jingXuanDetailUrl = "http://121.40.151.183:8080/pula-sys/app/notice/list";
        ArrayList<HuoDongData> list = new ArrayList<HuoDongData>();
        JSONObject json = ParseJson(jingXuanDetailUrl, "utf-8");
        if (json == null) {
            return null;
        } else {
            try {
                JSONArray Data = json.getJSONArray("records");
                for (int i = 0; i < Data.length(); i++) {
                    HuoDongData huoDongData = new HuoDongData();
                    JSONObject data = Data.getJSONObject(i);
                    huoDongData.setId(data.getString("id"));
                    huoDongData.setName(data.getString("no"));
                    huoDongData.setTitle(data.getString("title"));
                    huoDongData.setContent(data.getString("content"));
                    huoDongData.setUpdateTime(data.getString("updateTime"));
                    // FIXME: use real img path
                    huoDongData.setIamge("http://121.40.151.183:8080/pula-sys/app/image/icon?fp=" + "logo.png" /* data.getString("imgPath") */ 
                            + "&sub=notice");
                    huoDongData.setUrlS("http://121.40.151.183:8080/pula-sys/app/notice/appshow?id=" + huoDongData.getId());
                    list.add(huoDongData);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

	public static boolean getLoginStatus(String username, String password) {
		String loginUrl = "http://121.40.151.183:8080/pula-sys/app/studentinterface/login?loginId="
				+ username + "&password=" + password;
		
		boolean result = false;
		
		JSONObject json = ParseJson(loginUrl, "utf-8");
		
		if (json == null) {
			return false;
		} else {
			try {
				
			  result = json.getBoolean("error");
			  	
			} catch (Exception e) {
				e.printStackTrace();
}
		}
		return !result;
	}

	public static UserInfoData getUserInfoData(String username, String password) {
		String getInfoUrl = "http://121.40.151.183:8080/pula-sys/app/studentinterface/info?loginId="
				+ username + "&password=" + password;

		UserInfoData userInfo = new UserInfoData();
		Boolean result = false;
		
		JSONObject json = ParseJson(getInfoUrl, "utf-8");
		if (json == null) {
	
			return null;
		} else {
			try {
			
				result = json.getBoolean("error");
				if (result == false) {
					System.out.println("result != null");
					JSONObject data = json.getJSONObject("data");

					userInfo.setName(data.getString("name"));
					userInfo.setAddress(data.optString("address"));
					userInfo.setId(data.getInt("id"));
					userInfo.setEnabled(data.getBoolean("enabled"));
					userInfo.setNo(data.getString("no"));
					userInfo.setParentName(data.optString("parentName"));
					userInfo.setParentCaption(data.optString("parentCaption"));
					userInfo.setGender(data.getInt("gender"));
					userInfo.setPoints(data.getInt("points"));
					userInfo.setMobile(data.optString("mobile"));
					userInfo.setBirthday(data.optInt("brithday"));
					userInfo.setPhone(data.optInt("phone"));
					userInfo.setZip(data.optInt("zip"));

				}
				
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return userInfo;
	}
}
