using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Security.AccessControl;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace Play
{
    public class LocalHelper
    {
        public static string GetMd5String(string source1, string source2)
        {
            MD5 md5 = MD5.Create();
            md5.Initialize();
            string input = source1 + source2;
            byte[] bytes = md5.ComputeHash(Encoding.Default.GetBytes(input));
            return Convert.ToBase64String(bytes);
        }

        public static string GetLocalIpAddress()
        {
            var host = Dns.GetHostEntry(Dns.GetHostName());
            foreach (var ip in host.AddressList)
            {
                if (ip.AddressFamily == AddressFamily.InterNetwork)
                {
                    return ip.ToString();
                }
            }
            throw new Exception("Local IP Address Not Found!");
        }

        public static bool CheckFilePermission(string localFolder, bool needRead, bool needWrite)
        {
            // TODO : permission
            var fi = new DirectoryInfo(localFolder);
            if (!fi.Exists)
            {
                return false;
            }

            return true;
        }

    }
}
