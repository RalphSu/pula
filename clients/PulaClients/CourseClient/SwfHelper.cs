using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;

namespace CourseClient
{

    public class SwfMethod{
        public string method{get;set;}
        public string param {get;set;}
        public string param2 {get;set;}
    }

    public class SwfHelper
    {
        public static string buildXml(string funName,params string[] arg)
        {
            string sb =  "<invoke name=\"" + funName + "\" returntype=\"xml\"><arguments>";
            foreach(var p in arg){
            
                sb+= "<string>" + p + "</string>";
            }
            sb += "</arguments></invoke>";

            return sb;
        }

        public static string makeKey(string courseKey)
        {
            
           return buildXml("pula_key", AES.AesCtr.encrypt(AES.UnixTool.timestamp().ToString(), courseKey, 256));
        }

        public static string makeSubCategory(IList<string> categorys)
        {
            string s = string.Join(",", categorys.ToArray());
            return buildXml("pula_sub_category", s);            
        }

        public static string makeCard(string no ,string name ,string type )
        {
          
            return buildXml("pula_card ", no,name,type);
        }

        public static string makeCourse(string no ,string name)
        {
            //string s = string.Join(",", categorys.ToArray());
            return buildXml("pula_course", no,name);
        }


        public static SwfMethod read(string request)
        {
            XmlDocument document = new XmlDocument();
            document.LoadXml(request);
            // get attributes to see which command flash is trying to call
            XmlAttributeCollection attributes = document.FirstChild.Attributes;
            // get function
            String command = attributes.Item(0).InnerText;
            // get parameters
            XmlNodeList list = document.GetElementsByTagName("arguments");           

            string param = list.Item(0).InnerText;

            return new SwfMethod { method = command, param = param };
        }

         public static SwfMethod read2(string request)
        {
            XmlDocument document = new XmlDocument();
            document.LoadXml(request);
            // get attributes to see which command flash is trying to call
            XmlAttributeCollection attributes = document.FirstChild.Attributes;
            // get function
            String command = attributes.Item(0).InnerText;
            // get parameters
            XmlNodeList list = document.GetElementsByTagName("arguments");           

            string param = list.Item(0).InnerText;
            string param2 = list.Item(1).InnerText;

            return new SwfMethod { method = command, param = param,param2=param2 };
        }
    }
}
