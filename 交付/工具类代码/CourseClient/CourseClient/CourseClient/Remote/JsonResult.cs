using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CourseClient.Remote
{
    class JsonResult
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
}
