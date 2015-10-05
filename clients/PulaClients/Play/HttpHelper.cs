using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Security;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using RemoteService;

namespace Play
{
    public class HttpHelper
    {
        private static bool HttpGet(string url)
        {
            try
            {
                var resp = HttpWebResponseUtility.CreateGetHttpResponse(url, 10000,
                    null, null);
                if (resp.StatusCode != HttpStatusCode.OK)
                {
                    return false;
                }
                string json = RemoteServiceProxy.ReadResponseAsString(resp);
                var m = JsonConvert.DeserializeObject<JsonResult>(json);
                if (m != null)
                {
                    return !m.error;
                }
                return false;
            }
            catch (Exception e)
            {
                return false;
            }
        }

        public static bool validate(string url, string username, string password)
        {
            // http://121.40.151.183:8080/pula-sys/app/
            string ip = LocalHelper.GetLocalIpAddress();
            string md5 = LocalHelper.GetMd5String(url, username);
            string completeUrl = String.Format("{0}/teacherinterface/login?loginId={1}&password={2}&ip={3}&md5={4}", 
                url,
                username,
                password,
                ip, md5);

            return HttpGet(completeUrl);
        }
    }
}
