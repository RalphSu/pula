using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RemoteService
{
    public class JsonResult
    {

        public string no { get; set; }
        public string message { get; set; }
        public bool error { get; set; }

        public Dictionary<string, string> data {get;set;}


        internal static JsonResult Create(Exception ex)
        {
            JsonResult r = new JsonResult();
            r.error = true;
            r.message = ex.Message;
            return r; 
        }
    }


    public class JsonResultList
    {


        public string no { get; set; }
        public string message { get; set; }
        public bool error { get; set; }

        public List<Dictionary<string, string>> data { get; set; }


        internal static JsonResultList Create(Exception ex)
        {
            JsonResultList r = new JsonResultList();
            r.error = true;
            r.message = ex.Message;
            return r;
        }
    }

    public class JsonResultMap
    {


        public string no { get; set; }
        public string message { get; set; }
        public bool error { get; set; }

        public Dictionary<string, string> data { get; set; }


        internal static JsonResultMap Create(Exception ex)
        {
            JsonResultMap r = new JsonResultMap();
            r.error = true;
            r.message = ex.Message;
            return r;
        }
    }
}
