using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RemoteService
{
    public class CardInfo
    {

        public string Rfid { get; set; }
        public string No { get; set; }
        public string Name { get; set; }


        

    }

    public class WorkInfo{
        public string FilePath{get;set;}
        public string StudentNo{get;set;}
    }

    public class WorkInfo4Upload : WorkInfo
    {
        public int ResultStudentId { get; set; }
        public int CourseTaskResultId { get; set; }

    }

    public class Course
    {
        public long Id { get; set; }
        public string No { get; set; }
        public string Name { get; set; }
        public string Key { get; set; }

    }

   public class CourseTask
    {
       public static int STATUS_START = 1;
       public static int STATUS_END = 2;
       public static int STATUS_SUBMITED = 3;

       public long Id { get; set; }
       public long CourseId { get; set; }
       public DateTime BeginTime { get; set; }
       public DateTime EndTime { get; set; }
       public int Status { get; set; }

       public CardInfo Master { get; set; }
       public CardInfo Assistant1 { get; set; }
       public CardInfo Assistant2 { get; set; }

       //学生信息
       public List<CardInfo> Students { get; set; }

       public List<WorkInfo> Works { get; set; }

       public string CourseNo { get; set; }

       public CourseTask()
       {
           Students = new List<CardInfo>();
       }

       public void AddStudent( CardInfo ci){
           this.Students.Add(ci);
       }

       public void AddWork(WorkInfo wi)
       {
           if (this.Works == null)
           {
               this.Works = new List<WorkInfo>();
           }
           this.Works.Add(wi);
       }
    }
}
