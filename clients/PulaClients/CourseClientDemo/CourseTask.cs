using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CourseClient
{
    public class CardInfo
    {

        public string Rfid { get; set; }
        public string No { get; set; }
        public string Name { get; set; }


        

    }

   public class CourseTask
    {
       public static int STATUS_START = 1;
       public static int STATUS_END = 2;

       public long CourseId { get; set; }
       public DateTime BeginTime { get; set; }
       public DateTime EndTime { get; set; }
       public int Status { get; set; }

       public CardInfo Master { get; set; }
       public CardInfo Assistant1 { get; set; }
       public CardInfo Assistant2 { get; set; }

       //学生信息
       public List<CardInfo> Students { get; set; }

       public CourseTask()
       {
           Students = new List<CardInfo>();
       }

       public void AddStudent( CardInfo ci){
           this.Students.Add(ci);
       }
    }
}
