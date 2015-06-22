using System;
using System.Collections.Generic;
using System.Data.Odbc;
using System.Diagnostics;
using System.Linq;
using System.Text;
using Newtonsoft.Json.Serialization;
using RemoteService;

namespace CourseClient
{
    class TestUploadWork
    {

        [STAThread]
        public static void Main()
        {
            // setup trace
            TextWriterTraceListener traceLog = new TextWriterTraceListener("log.txt");
            Debug.Listeners.Add(traceLog);

            Trace.WriteLine("start at : " + DateTime.Now.ToLongDateString());

            //// create a fake course task
            //long id = 1;
            ////CourseTask ct = StoreModel.GetById(id);
            //CourseTask ct = new CourseTask();
            //ct.Master = new CardInfo()
            //{
            //    No = "400001",
            //    Name = "舒凡",
            //    Rfid = "T:5"
            //};
            //ct.BeginTime = DateTime.Now;
            //ct.EndTime = DateTime.Now;
            //ct.CourseId = 1;
            //ct.CourseNo = "123";
            //ct.Assistant1 = new CardInfo()
            //{
            //    No = "500001",
            //    Name = "舒凡",
            //    Rfid = "T:5"
            //};
            //ct.Id = 18;
            //ct.Status = CourseTask.STATUS_START;
            //ct.AddStudent(new CardInfo()
            //{
            //    No = "17",
            //    Name = "苏鹂",
            //    Rfid = "S:23"
            //});
            //ct.AddWork(new WorkInfo()
            //{
            //    FilePath = "work1.png",
            //    StudentNo = "17",
            //});
            //JsonResultList list = RemoteServiceProxy.Report(ct, "730427b2ec993b13b3f6697faab1cf",
            //    "abc");
            //if (list.error)
            //{
            //    Trace.TraceWarning("report course failed! Exit!!!");
            //    return;
            //}

            //List<WorkInfo4Upload> works = RemoteServiceProxy.PrepareWork(ct.Works, list.data);

            ////然后传到服务器!--作品!
            //bool error = false;
            //foreach (var w in works)
            //{

            WorkInfo4Upload w = new WorkInfo4Upload()
            {
                FilePath = "student-button.png",
                ResultStudentId=17,
                CourseTaskResultId=16,
            };
                JsonResult jr = RemoteServiceProxy.ReportWork(w);
                if (jr.error)
                {
                    //Trace.TraceWarning("同步作品失败:" + id.ToString() + " 作品=" + w.StudentNo);
                    //error = true;
                    //continue;
                }
            //}

            //if (error)
            //{
            //    Trace.TraceError("There are errors when handle the work uploading.");
            //}
        }
    }
}
