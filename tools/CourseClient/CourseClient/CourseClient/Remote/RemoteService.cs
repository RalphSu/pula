using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;
using Newtonsoft.Json;

namespace CourseClient.Remote
{
    class RemoteService
    {

        private static string GetURL(string u)
        {
            return "http://localhost:8125/pula-sys/app/remoteservice/"+u;
        }

        public static JsonResult RequestActive(string code, string comments)
        {

            IDictionary<string,string> param_dict = new Dictionary<string,string>();
            param_dict.Add("code",code);
            param_dict.Add("comments", comments);
            string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetURL("requestActive"), param_dict, null, null,Encoding.UTF8, null));

            //JsonResult sr = JSON.parse<JsonResult>(json);
            JsonResult m = JsonConvert.DeserializeObject<JsonResult>(json);
            return m;
        }

        public static JsonResult SyncActive(string code)
        {

            try
            {
                IDictionary<string, string> param_dict = new Dictionary<string, string>();
                param_dict.Add("code", code);
                string json = ReadResponseAsString(HttpWebResponseUtility.CreatePostHttpResponse(GetURL("syncActive"), param_dict, null, null, Encoding.UTF8, null));

                //JsonResult sr = JSON.parse<JsonResult>(json);
                JsonResult m = JsonConvert.DeserializeObject<JsonResult>(json);
                return m;
            }
            catch (Exception ex)
            {
                return JsonResult.Create(ex);
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
        
    }
}
