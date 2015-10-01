using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;
using Newtonsoft.Json;

namespace RemoteService
{
    public class RemoteServiceProxy
    {
        public static string ServiceUrl = "http://121.40.151.183:8080/pula-sys/app/";

        private static string GetCourseClientUrl(string u)
        {
            return ServiceUrl + "courseclientservice/" + u;
        }

        private static string GetUsageUrl(string action)
        {
            return ServiceUrl + "timecourseorderusage/" + action;
        }

        public static JsonResult CheckLogin(string usr, string pwd)
        {
            try
            {
                var param_dict = new ParamValueBuilder();
                param_dict.Add("username", usr);
                param_dict.Add("password", pwd);
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetCourseClientUrl("checkLogin"), param_dict.Build(), null, null, Encoding.UTF8, null));

                //JsonResult sr = JSON.parse<JsonResult>(json);
                JsonResult m = JsonConvert.DeserializeObject<JsonResult>(json);
                return m;
            }
            catch (Exception ex)
            {
                return JsonResult.Create(ex);
            }
        }

        public static JsonResult RequestActive(string code, string comments)
        {
            try
            {
                var param_dict = new ParamValueBuilder();
                param_dict.Add("code", code);
                param_dict.Add("comments", comments);
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetCourseClientUrl("requestActive"), param_dict.Build(), null, null, Encoding.UTF8, null));

                //JsonResult sr = JSON.parse<JsonResult>(json);
                JsonResult m = JsonConvert.DeserializeObject<JsonResult>(json);
                return m;
            }
            catch (Exception ex)
            {
                return JsonResult.Create(ex);
            }
        }

        public static JsonResult SyncActive(string code)
        {

            try
            {
                var param_dict = new ParamValueBuilder();
                param_dict.Add("code", code);
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetCourseClientUrl("syncActive"), param_dict.Build(), null, null, Encoding.UTF8, null));

                //JsonResult sr = JSON.parse<JsonResult>(json);
                JsonResult m = JsonConvert.DeserializeObject<JsonResult>(json);
                return m;
            }
            catch (Exception ex)
            {
                return JsonResult.Create(ex);
            }
        }
        public static JsonResultList SyncCourse(string code, string active_code)
        {

            try
            {
                var param_dict = new ParamValueBuilder();
                param_dict.Add("code", code); param_dict.Add("activeCode", active_code);
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetCourseClientUrl("syncCourse"), param_dict.Build(), null, null, Encoding.UTF8, null));

                //JsonResult sr = JSON.parse<JsonResult>(json);
                JsonResultList m = JsonConvert.DeserializeObject<JsonResultList>(json);

               

                return m;
            }
            catch (Exception ex)
            {
                return JsonResultList.Create(ex);
            }
        }

      


        private static string ReadResponseAsString(HttpWebResponse resp)
        {
            StreamReader responseReader = null;
            string responseData = "";

            if (resp == null)
            {
                return responseData;
            }

            try
            {
                responseReader = new StreamReader(resp.GetResponseStream());
                responseData = responseReader.ReadToEnd();
            }
            catch
            {
                return "连接错误";
            }
            finally
            {
                resp.GetResponseStream().Close();
                responseReader.Close();
                responseReader = null;
            }
            return responseData;
        }


        public static JsonResult GetInfo(string cardid, string usr, string pwd)
        {
            try
            {

                var param_dict = new ParamValueBuilder();
                param_dict.Add("rfid", cardid);
                param_dict.Add("username", usr);
                param_dict.Add("password", pwd);
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetCourseClientUrl("getInfo"), param_dict.Build(), null, null, Encoding.UTF8, null));

                //JsonResult sr = JSON.parse<JsonResult>(json);
                JsonResult m = JsonConvert.DeserializeObject<JsonResult>(json);
                return m;
            }
            catch (Exception ex)
            {
                return JsonResult.Create(ex);
            }
        }

        public static JsonResult UploadWork(string fp,string ctrs_id ,string studentId)
        {
            try
            {

                var param_dict = new ParamValueBuilder();
                param_dict.Add("id", ctrs_id);
                param_dict.Add("studentId", studentId);
                string json = ReadResponseAsString(HttpWebResponseUtility.UploadFile(GetCourseClientUrl("reportWork"),fp,param_dict.Build()));

                //JsonResult sr = JSON.parse<JsonResult>(json);
                JsonResult m = JsonConvert.DeserializeObject<JsonResult>(json);
                return m;
            }
            catch (Exception ex)
            {
                return JsonResult.Create(ex);
            }
        }



        public static JsonResultList Report(CourseTask ct, string code, string active_code)
        {
            try
            {

                var param_dict = BuildCourseTaskParams(ct);

                param_dict.Add("code", code); param_dict.Add("activeCode", active_code);
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetCourseClientUrl("report"), param_dict.Build(), null, null, Encoding.UTF8, null));

                //JsonResult sr = JSON.parse<JsonResult>(json);
                JsonResultList m = JsonConvert.DeserializeObject<JsonResultList>(json);
                return m;
            }
            catch (Exception ex)
            {
                return JsonResultList.Create(ex);
            }
        }

        private static ParamValueBuilder BuildCourseTaskParams(CourseTask ct)
        {
            var param_dict = new ParamValueBuilder("bean");
            param_dict.Add("id", ct.Id.ToString());
            param_dict.Add("beginTime", ct.BeginTime.ToString("yyyy-MM-dd HH:mm:ss"));
            param_dict.Add("endTime", ct.EndTime.ToString("yyyy-MM-dd HH:mm:ss"));

            param_dict.Add("masterNo", ct.Master.No);
            param_dict.Add("masterName", ct.Master.Name);
            param_dict.Add("masterRfid", ct.Master.Rfid);

            //a1
            param_dict.Add("assistant1No", ct.Assistant1.No);
            param_dict.Add("assistant1Name", ct.Assistant1.Name);
            param_dict.Add("assistant1Rfid", ct.Assistant1.Rfid);

            if (ct.Assistant2 != null)
            {
                //a2
                param_dict.Add("assistant2No", ct.Assistant2.No);
                param_dict.Add("assistant2Name", ct.Assistant2.Name);
                param_dict.Add("assistant2Rfid", ct.Assistant2.Rfid);
            }
            param_dict.Add("courseNo", ct.CourseNo);

            //students now!
            foreach (var si in ct.Students)
            {
                param_dict.Add("studentRfid", si.Rfid);
                param_dict.Add("studentNo", si.No);
                param_dict.Add("studentName", si.Name);
            }


            return param_dict.ResetPrefix(); ;
        }

        public static List<WorkInfo4Upload> PrepareWork(List<WorkInfo> wis, List<Dictionary<string, string>> results)
        {
            List<WorkInfo4Upload> result = new List<WorkInfo4Upload>();

            var idx = 0;
            int id = 0;
            Dictionary<string, Dictionary<string, string>> map = new Dictionary<string, Dictionary<string, string>>();
            foreach (var r in results)
            {
                if (idx == 0)
                {
                    id = int.Parse(r["id"]);
                }
                else
                {
                    //都是学生数据 +id
                    map.Add(r["no"], r);

                }
                idx++;
            }


            //对应的学生数据
            foreach (var wi in wis)
            {
                if (map.ContainsKey(wi.StudentNo))
                {
                    WorkInfo4Upload u = new WorkInfo4Upload();
                    u.CourseTaskResultId = id;
                    u.FilePath = wi.FilePath;
                    u.ResultStudentId = int.Parse(map[wi.StudentNo]["id"]);
                    u.StudentNo = wi.StudentNo;
                    result.Add(u);
                }
            }


            return result;
        }

        public static JsonResult ReportWork(WorkInfo4Upload w)
        {
            return RemoteServiceProxy.UploadWork(w.FilePath,w.CourseTaskResultId.ToString(), w.ResultStudentId.ToString());
            
        }

        ////course.courseNo:001
////course.studentNo:2200001
////course.orderNo:
////course.usedCount:0
////course.usedGongfangCount:1
////course.usedHuodongCount:1
////course.comments:
////course.id:
        /// <summary>
        /// course.no:usage004
        /// </summary>
        /// <param name="courseCout"></param>
        /// <param name="gongfangCount"></param>
        /// <param name="huodongCount"></param>
        /// <param name="specialCourseCount"></param>
        /// <param name="cardId"></param>
        /// <param name="clientCardNo"></param>
        /// <param name="clientCardStudentName"></param>
        /// <param name="orderNo"></param>
        /// <param name="username"></param>
        /// <param name="password"></param>
        /// <returns></returns>
        public static JsonResult AddTimeCourseUsage(int courseCout, int gongfangCount, int huodongCount, int specialCourseCount, string cardId, string clientCardNo, string clientCardStudentName, string orderNo, string username, string password)
        {
            string createUsage = GetCourseClientUrl("addStudentUsage");
            var paramBuilder = BuildCourseUsageParams(courseCout, gongfangCount,
                huodongCount, specialCourseCount,
                cardId, clientCardNo, clientCardStudentName,
                orderNo,
                username, password);

            try
            {
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(createUsage,
                    paramBuilder.Build(), null, null, Encoding.UTF8, null));

                var m = JsonConvert.DeserializeObject<JsonResult>(json);
                return m;
            }
            catch (Exception ex)
            {
                return JsonResult.Create(ex);
            }
        }

        private static ParamValueBuilder BuildCourseUsageParams(int courseCout, int gongfangCount, int huodongCount, int specialCourseCount, string cardId, string clientCardNo, string clientCardStudentName, string orderNo, string username, string password)
        {
            var paramDict = new ParamValueBuilder("course");
            // paramDict.Add("studentNo", studentNo);
            paramDict.Add("usedCount", "" + courseCout);
            paramDict.Add("usedGongfangCount", ""+ gongfangCount);
            paramDict.Add("usedHuodongCount", "" + huodongCount);
            paramDict.Add("orderNo", orderNo);
            paramDict.Add("usedSpecialCourseCount", "" + specialCourseCount);
            paramDict.ResetPrefix();
            // 增加卡的验证信息
            paramDict.Add("cardId", cardId);
            paramDict.Add("clientCardNo", clientCardNo);
            paramDict.Add("clientCardStudentName", clientCardStudentName);
            paramDict.Add("username", username);
            paramDict.Add("password", password);
            return paramDict;
        }



        public static JsonOrderResult<TimeCourseOrder> GetUserCourseOrders(string studentNo)
        {
            string listOrderUrl = ServiceUrl + string.Format("/timecourseorder/list?condition.studentNo={0}", studentNo);

            try
            {
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(listOrderUrl,
                    new List<ParamValue>(), null, null, Encoding.UTF8, null));

                var m = JsonConvert.DeserializeObject<JsonOrderResult<TimeCourseOrder>>(json);

                return m;
            }
            catch (Exception ex)
            {
                return null;
            }
        }
    }
}
