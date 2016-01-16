using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Play
{
    public class PlayConfig
    {

        public PlayConfig()
        {
            Server = "http://localhost:8080/app/";
            UserName = "0001";
            Password = "123456";
            LocalFolder = "./";

            EnforceUserCheck = false;
            CourseMaker = false;

            MajorVersion = 1;
            MinorVersion = 0;
        }

        public int MajorVersion { get; set; }
        public int MinorVersion { get; set; }

        public bool EnforceUserCheck { get; set; }

        public bool CourseMaker { get; set; }

        public String Server { get; set; }
        public String UserName { get; set; }
        public String Password { get; set; }

        public String LocalFolder { get; set; }
    }
}
