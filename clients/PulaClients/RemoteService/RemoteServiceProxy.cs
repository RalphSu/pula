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

        private static string GetURL(string u)
        {
            //return "http://localhost:8080/app/courseclientservice/"+u;
            return "http://121.40.151.183:8080/pula-sys/app/courseclientservice/" + u;
        }

        public static JsonResult CheckLogin(string usr, string pwd)
        {
            try
            {
                var param_dict = new ParamValueBuilder();
                param_dict.Add("username", usr);
                param_dict.Add("password", pwd);
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetURL("checkLogin"), param_dict.Build(), null, null, Encoding.UTF8, null));

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
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetURL("requestActive"), param_dict.Build(), null, null, Encoding.UTF8, null));

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
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetURL("syncActive"), param_dict.Build(), null, null, Encoding.UTF8, null));

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
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetURL("syncCourse"), param_dict.Build(), null, null, Encoding.UTF8, null));

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
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetURL("getInfo"), param_dict.Build(), null, null, Encoding.UTF8, null));

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
                string json = ReadResponseAsString(HttpWebResponseUtility.UploadFile(GetURL("reportWork"),fp,param_dict.Build()));

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
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetURL("report"), param_dict.Build(), null, null, Encoding.UTF8, null));

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
    }
}
