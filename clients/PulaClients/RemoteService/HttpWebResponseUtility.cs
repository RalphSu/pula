using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Net;
using System.Net.Security;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.IO;

namespace RemoteService
{

    public class ParamValue
    {
        public string Name { get; set; }
        public string Value { get; set; }

    }

    public class ParamValueBuilder
    {
        private IList<ParamValue> mList;

        private string mPrefix;

        public ParamValueBuilder()
        {
            mList = new List<ParamValue>();


        }

        public ParamValueBuilder(string prefix):this()
        {
            
            this.mPrefix = prefix;

        }

        public IList<ParamValue> Build()
        {
            return mList;
        }

        public ParamValueBuilder Add(string k, string v)
        {

            if (string.IsNullOrEmpty(this.mPrefix))
            {

            }
            else
            {
                k = this.mPrefix + "." + k;
            }

            mList.Add(new ParamValue { Name = k, Value = v });

            return this;
        }

        internal ParamValueBuilder ResetPrefix()
        {
            this.mPrefix = null;
            return this;  
        }
    }


    /// <summary>  
    /// 有关HTTP请求的辅助类  
    /// </summary>  
    public class HttpWebResponseUtility
    {
        private static readonly string DefaultUserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)";
        /// <summary>  
        /// 创建GET方式的HTTP请求  
        /// </summary>  
        /// <param name="url">请求的URL</param>  
        /// <param name="timeout">请求的超时时间</param>  
        /// <param name="userAgent">请求的客户端浏览器信息，可以为空</param>  
        /// <param name="cookies">随同HTTP请求发送的Cookie信息，如果不需要身份验证可以为空</param>  
        /// <returns></returns>  
        public static HttpWebResponse CreateGetHttpResponse(string url, int? timeout, string userAgent, CookieCollection cookies)
        {
            if (string.IsNullOrEmpty(url))
            {
                throw new ArgumentNullException("url");
            }
            HttpWebRequest request = WebRequest.Create(url) as HttpWebRequest;
            request.Method = "GET";
            request.UserAgent = DefaultUserAgent;
            if (!string.IsNullOrEmpty(userAgent))
            {
                request.UserAgent = userAgent;
            }
            if (timeout.HasValue)
            {
                request.Timeout = timeout.Value;
            }
            if (cookies != null)
            {
                request.CookieContainer = new CookieContainer();
                request.CookieContainer.Add(cookies);
            }
            return request.GetResponse() as HttpWebResponse;
        }
        /// <summary>  
        /// 创建POST方式的HTTP请求  
        /// </summary>  
        /// <param name="url">请求的URL</param>  
        /// <param name="parameters">随同请求POST的参数名称及参数值字典</param>  
        /// <param name="timeout">请求的超时时间</param>  
        /// <param name="userAgent">请求的客户端浏览器信息，可以为空</param>  
        /// <param name="requestEncoding">发送HTTP请求时所用的编码</param>  
        /// <param name="cookies">随同HTTP请求发送的Cookie信息，如果不需要身份验证可以为空</param>  
        /// <returns></returns>  
        public static HttpWebResponse CreatePostHttpResponse(string url, IList<ParamValue> parameters, int? timeout, string userAgent, Encoding requestEncoding, CookieCollection cookies)
        {
            if (string.IsNullOrEmpty(url))
            {
                throw new ArgumentNullException("url");
            }
            if (requestEncoding == null)
            {
                throw new ArgumentNullException("requestEncoding");
            }
            HttpWebRequest request = null;
            //如果是发送HTTPS请求  
            if (url.StartsWith("https", StringComparison.OrdinalIgnoreCase))
            {
                ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback(CheckValidationResult);
                request = WebRequest.Create(url) as HttpWebRequest;
                request.ProtocolVersion = HttpVersion.Version10;
            }
            else
            {
                request = WebRequest.Create(url) as HttpWebRequest;
            }
            request.Method = "POST";
            request.ContentType = "application/x-www-form-urlencoded";

            if (!string.IsNullOrEmpty(userAgent))
            {
                request.UserAgent = userAgent;
            }
            else
            {
                request.UserAgent = DefaultUserAgent;
            }

            if (timeout.HasValue)
            {
                request.Timeout = timeout.Value;
            }
            if (cookies != null)
            {
                request.CookieContainer = new CookieContainer();
                request.CookieContainer.Add(cookies);
            }
            //如果需要POST数据  
            using (Stream stream = request.GetRequestStream())
            {
                writePostParamToStream(parameters, requestEncoding, stream);
            }
            return request.GetResponse() as HttpWebResponse;
        }

        private static void writePostParamToStream(IList<ParamValue> parameters, Encoding requestEncoding, Stream stream)
        {
            if (!(parameters == null || parameters.Count == 0))
            {
                StringBuilder buffer = new StringBuilder();
                int i = 0;
                foreach (var v in parameters)
                {
                    if (i > 0)
                    {
                        buffer.AppendFormat("&{0}={1}", v.Name, v.Value);
                    }
                    else
                    {
                        buffer.AppendFormat("{0}={1}", v.Name, v.Value);
                    }
                    i++;
                }
                byte[] data = requestEncoding.GetBytes(buffer.ToString());

                stream.Write(data, 0, data.Length);

            }
        }

        private static bool CheckValidationResult(object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors errors)
        {
            return true; //总是接受  
        }

        public static HttpWebResponse UploadFile(string url, string localFilePath, IList<ParamValue> parameters)
        {
            /*HttpWebRequest req = (HttpWebRequest)WebRequest.Create(url);

            req.Method = "PUT";
            req.ContentType = "application/octet-stream";

            using (Stream reqStream = req.GetRequestStream())
            {

                using (Stream inStream = new FileStream(localFilePath, FileMode.Open, FileAccess.Read, FileShare.Read))
                {
                    inStream.CopyTo(reqStream, 4096);
                }

                reqStream.Flush();
            }

            HttpWebResponse response = (HttpWebReponse)req.GetResponse();
            return response*/
            //Identificate separator

            string boundary = "---------------------------" + DateTime.Now.Ticks.ToString("x");
            //Encoding
            byte[] boundarybytes = System.Text.Encoding.ASCII.GetBytes("\r\n--" + boundary + "\r\n");

            //Creation and specification of the request
            HttpWebRequest wr = (HttpWebRequest)WebRequest.Create(url); //sVal is id for the webService
            wr.ContentType = "multipart/form-data; boundary=" + boundary;
            wr.Method = "POST";
            wr.KeepAlive = true;
            wr.Credentials = System.Net.CredentialCache.DefaultCredentials;

            //string sAuthorization = "login:password";//AUTHENTIFICATION BEGIN
            //byte[] toEncodeAsBytes = System.Text.ASCIIEncoding.ASCII.GetBytes(sAuthorization);
            //string returnValue = System.Convert.ToBase64String(toEncodeAsBytes);
            //wr.Headers.Add("Authorization: Basic " + returnValue); //AUTHENTIFICATION END
            Stream rs = wr.GetRequestStream();

            if (!(parameters == null || parameters.Count == 0))
            {
                string headerTemplate1 = "Content-Disposition: form-data; name=\"{0}\"\r\n\r\n{1}\r\n";
                StringBuilder buffer1 = new StringBuilder();
                
                foreach (var v in parameters)
                {
                    buffer1.Append("\r\n--" + boundary + "\r\n");
                    buffer1.AppendFormat(headerTemplate1, v.Name, v.Value);                    
                }
                byte[] data = System.Text.Encoding.UTF8.GetBytes(buffer1.ToString());
                rs.Write(data, 0, data.Length);
            }

           // writePostParamToStream(parameters, Encoding.UTF8, rs);

            //string formdataTemplate = "Content-Disposition: form-data; name=\"{0}\"\r\n\r\n{1}"; //For the POST's format

            //Writting of the file
            rs.Write(boundarybytes, 0, boundarybytes.Length);
            byte[] formitembytes = System.Text.Encoding.UTF8.GetBytes(localFilePath);
            rs.Write(formitembytes, 0, formitembytes.Length);

            rs.Write(boundarybytes, 0, boundarybytes.Length);

            string headerTemplate = "Content-Disposition: form-data; name=\"{0}\"; filename=\"{1}\"\r\nContent-Type: {2}\r\n\r\n";
            string header = string.Format(headerTemplate, "file", Path.GetFileName(localFilePath), "image/pjpeg");
            byte[] headerbytes = System.Text.Encoding.UTF8.GetBytes(header);
            rs.Write(headerbytes, 0, headerbytes.Length);

            FileStream fileStream = new FileStream(localFilePath, FileMode.Open, FileAccess.Read);
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = fileStream.Read(buffer, 0, buffer.Length)) != 0)
            {
                rs.Write(buffer, 0, bytesRead);
            }
            fileStream.Close();

            byte[] trailer = System.Text.Encoding.ASCII.GetBytes("\r\n--" + boundary + "--\r\n");
            rs.Write(trailer, 0, trailer.Length);
            rs.Close();
            rs = null;

            return (HttpWebResponse) wr.GetResponse(); ;
        }
    }  
}