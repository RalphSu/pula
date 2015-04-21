using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;
using RemoteService;
using System.IO;

namespace CourseClient
{
    public class StoreModel
    {


        internal static long StartCourseTask(CourseTask ct)
        {
            //课程允许开课

            //准备好教师和学生数据

           long? master_id=  PrepareTeacher(ct.Master);
           long? assistant1_id = PrepareTeacher(ct.Assistant1);
           long? assistant2_id = PrepareTeacher(ct.Assistant2);

            //main table insert 
           string sql = "insert into course_task (begin_time,status,master_id,assistant1_id,assistant2_id,status,course_id) values( datetime('now','localtime'),?,?,?,?,?,?)";
           SqlLiteHelper.ExecuteNonQuery(sql, CourseTask.STATUS_START, master_id, assistant1_id, assistant2_id, CourseTask.STATUS_START,ct.CourseId);
            //student?
           object result = SqlLiteHelper.ExecuteScalar("SELECT last_insert_rowid()");

            //get id = result;
            //to students table ;

           List<long> students = PrepareStudents(ct.Students);

            //insert students 
           sql = "insert into course_task_student (course_task_id,student_id) values(?,?)";
           foreach(long sid in students){
                SqlLiteHelper.ExecuteNonQuery(sql, result, sid); 
           }


           return (long)result;

        }

        private static List<long> PrepareStudents(List<CardInfo> list)
        {
            List<long> results = new List<long>();
            foreach(var ci in list){
                long studentId= PrepareStudent(ci);
                results.Add(studentId);
            }
            return results;
        }

        private static long PrepareStudent(CardInfo cardInfo)
        {
            string sql = "select student_id,student_no,student_name from student where student_rfid=?";
            var ds = SqlLiteHelper.ExecuteQuery(sql, cardInfo.Rfid);
            bool update = false;
            long student_id = 0;
            foreach (DataRow row in ds.Tables[0].Rows)
            {
                string student_no = row["student_no"].ToString();
                string student_name = row["student_name"].ToString();
                student_id = (long)row["student_id"];
                if (string.Equals(student_name, cardInfo.Name) && string.Equals(student_no, cardInfo.No))
                {
                    return student_id;
                }
                update = true;
            }

            //need insert or update
            if (update)
            {
                SqlLiteHelper.ExecuteNonQuery("update student set student_name=?,student_no=?,updated_time=datetime('now','localtime') where student_id=?", cardInfo.Name, cardInfo.No, student_id);
                return student_id;
            }
            else
            {
                SqlLiteHelper.ExecuteNonQuery("insert into student( student_name,student_no,student_rfid,created_time,updated_time) values(?,?,?,datetime('now','localtime'),datetime('now','localtime'))", cardInfo.Name, cardInfo.No, cardInfo.Rfid);
                object result = SqlLiteHelper.ExecuteScalar("SELECT last_insert_rowid()");
                return (long)result;
            }
        }

        private static long? PrepareTeacher(CardInfo cardInfo)
        {

            if (cardInfo == null)
            {

                return null;
            }

            string sql = "select teacher_id,teacher_no,teacher_name from teacher where teacher_rfid=?";
            var ds = SqlLiteHelper.ExecuteQuery(sql, cardInfo.Rfid);
            bool update = false;
            long teacher_id = 0 ;
            foreach (DataRow row in ds.Tables[0].Rows)
            {
                string teacher_no = row["teacher_no"].ToString();
                string teacher_name = row["teacher_name"].ToString();
                 teacher_id = (long) row["teacher_id"];
                if (string.Equals(teacher_name, cardInfo.Name) && string.Equals(teacher_no, cardInfo.No))
                {
                    return teacher_id;
                }
                update = true;
            }

            //need insert or update
            if (update)
            {
                SqlLiteHelper.ExecuteNonQuery("update teacher set teacher_name=?,teacher_no=?,updated_time=datetime('now','localtime') where teacher_id=?", cardInfo.Name, cardInfo.No, teacher_id);
                return teacher_id;
            }
            else
            {
                SqlLiteHelper.ExecuteNonQuery("insert into teacher( teacher_name,teacher_no,teacher_rfid,updated_time,created_time) values(?,?,?,datetime('now','localtime'),datetime('now','localtime'))", cardInfo.Name, cardInfo.No, cardInfo.Rfid);
                object result = SqlLiteHelper.ExecuteScalar("SELECT last_insert_rowid()");
                return (long)result;
            }

        }

        internal static void FinishCourseTask(long id,IDictionary<string,string> files)
        {
            //关闭课程
            string sql = "update course_task set end_time=datetime('now','localtime'),status=? where course_task_id=?";
            int n = SqlLiteHelper.ExecuteNonQuery(sql,CourseTask.STATUS_END,  id);

            //提交作品内容
            sql = "insert into course_task_file ( student_no,file_path,course_task_id) values( ?,?,?)";

            foreach (var item in files)
            {
                SqlLiteHelper.ExecuteNonQuery(sql, item.Key, item.Value,id);
            }

        }

        //用以上报
        internal static List<long> GetFinishCourseTaskIds()
        {
            List<long> ids = new List<long>();
            string sql = "select course_task_id as id from course_task where status=?";
            var ds = SqlLiteHelper.ExecuteQuery(sql, CourseTask.STATUS_END);
            
            foreach (DataRow row in ds.Tables[0].Rows)
            {
                long id = (long)row["id"];
                ids.Add(id);
            }

            return ids;
        }


        //处理未激活的错误信息
        internal static void ProcessNotActived(RemoteService.JsonResultList jr)
        {
            if (jr.error && "999".Equals(jr.no))
            {
                FrmActive.activeCode = null;
                //删除注册信息
                SqlLiteHelper.ExecuteNonQuery("update system set active_code=null");
            }
        }

        internal static CourseTask GetById(long id)
        {

            string sql = @"select ct.*,m.teacher_no as masterNo,m.teacher_name as masterName,m.teacher_rfid as masterRfid,
                            a1.teacher_no as a1No,a1.teacher_name as a1Name,a1.teacher_rfid as a1Rfid,
                            a2.teacher_no as a2No,a2.teacher_name as a2Name,a2.teacher_rfid as a2Rfid,
                            c.course_no
                            from course_task ct left join teacher m on m.teacher_id = ct.master_id 
                            left join teacher a1 on a1.teacher_id = ct.assistant1_id
                            left join teacher a2 on a2.teacher_id = ct.assistant2_id
                            ,course c where ct.course_task_id=? and c.course_id=ct.course_id";
            var ds = SqlLiteHelper.ExecuteQuery(sql, id);

            CourseTask ct = new CourseTask();
            ct.Id = id;
            foreach (DataRow row in ds.Tables[0].Rows)
            {
                DateTime beginTime = (DateTime) row["begin_time"];
                DateTime endTime = (DateTime)row["end_time"];

                ct.CourseNo = row["course_no"].ToString();
                ct.Master = new CardInfo{ No= row["masterNo"].ToString(), Name = row["masterName"].ToString(),Rfid = row["masterRfid"].ToString() };
                ct.Assistant1 = new CardInfo { No = row["a1No"].ToString(), Name = row["a1Name"].ToString(), Rfid = row["a1Rfid"].ToString() };

                if (!row.IsNull("a2No"))
                {
                    ct.Assistant2 = new CardInfo { No = row["a2No"].ToString(), Name = row["a2Name"].ToString(), Rfid = row["a2Rfid"].ToString() };
                }
                ct.BeginTime = beginTime;
                ct.EndTime = endTime;
                
            }

            //学生数据
            sql = @"select s.student_no,s.student_name,s.student_rfid from course_task_student cts left join student s where s.student_id=cts.student_id and cts.course_task_id=?";
            ds = SqlLiteHelper.ExecuteQuery(sql, id);

            foreach (DataRow row in ds.Tables[0].Rows)
            {
                ct.AddStudent(new CardInfo { No = row["student_no"].ToString(), Name = row["student_name"].ToString(), Rfid = row["student_rfid"].ToString() });              
            }

            //作品数据
            sql = @"select student_no,file_path from course_task_file where course_task_id=?";
            ds = SqlLiteHelper.ExecuteQuery(sql, id);
            foreach (DataRow row in ds.Tables[0].Rows)
            {
                ct.AddWork(new WorkInfo { StudentNo = row["student_no"].ToString(), FilePath = row["file_path"].ToString() });
            }

            return ct;

        }

        internal static void FinishReportCourseTask(long id)
        {
            //关闭课程
            string sql = "update course_task set submit_time=datetime('now','localtime'),status=? where course_task_id=?";
            int n = SqlLiteHelper.ExecuteNonQuery(sql, CourseTask.STATUS_SUBMITED, id);
        }

        internal static void RemoveFiles(List<WorkInfo> list)
        {
            IList<string> dirs = new List<string>();
            foreach(var fi in list){

                string dir = Path.GetDirectoryName(fi.FilePath);

                if (!dirs.Contains(dir))
                {
                    dirs.Add(dir);
                }
                try
                {
                    File.Delete(fi.FilePath);
                }
                catch (Exception ex)
                {

                }
            }

            //判断文件夹是否为空,为空则删掉

            foreach (var dir in dirs)
            {
                System.IO.DirectoryInfo di = new System.IO.DirectoryInfo(dir);
                if (di.GetFiles().Length + di.GetDirectories().Length == 0)
                {
                    //目录为空
                    try
                    {
                        Directory.Delete(dir);
                    }
                    catch (Exception ex)
                    {

                    }
                }
            }
            
        }


        //"大金刚篇,历史篇,混战篇,人类起源篇"
        internal static string GetCategoryText(int categoryId)
        {
            //所有分类,
            var sql = @"select distinct sub_category from course where removed=? and course_category=?";
            var ds = SqlLiteHelper.ExecuteQuery(sql, false, categoryId);
            var list = new List<string>();
            foreach (DataRow row in ds.Tables[0].Rows)
            {
                //ct.AddWork(new WorkInfo { StudentNo = row["student_no"].ToString(), FilePath = row["file_path"].ToString() });
                list.Add(row["sub_category"].ToString());
            }

         

            string resultingString = String.Join( ",", list);

            return resultingString;
        }

        internal static List<Course> GetCourses(string p)
        {
            var sql = @"select course_id,course_name,course_no from course where removed=? and sub_category=? order by course_no";
            var ds = SqlLiteHelper.ExecuteQuery(sql, false, p);
            var list = new List<Course>();
            foreach (DataRow row in ds.Tables[0].Rows)
            {
                list.Add(new Course { No = row["course_no"].ToString(), Name= row["course_name"].ToString(),Id= (long) row["course_id"] });
            }

            return list;
        }

        internal static CardInfo GetCardInfo(string p, int type)
        {
            DataSet ds =  null ; 
            if (type == 1)
            {

                //student

                var sql = @"select student_no as no ,student_name as name ,student_rfid as rfid from student where student_no=?";
                ds = SqlLiteHelper.ExecuteQuery(sql, p);

            }
            else
            {
                var sql = @"select teacher_no as no ,teacher_name as name ,teacher_rfid as rfid from student where teacher_no=?";
                ds = SqlLiteHelper.ExecuteQuery(sql, p);
            }

            //first one 

            foreach (DataRow row in ds.Tables[0].Rows)
            {
                return (new CardInfo { No = row["no"].ToString(), Name = row["name"].ToString(), Rfid = row["rfid"].ToString() });
            }

            return null;
        }

        internal static Course GetCourse(string p)
        {
            var sql = @"select course_id,course_name,course_no,key from course where removed=? and course_no=?";
            var ds = SqlLiteHelper.ExecuteQuery(sql, false, p);
            var list = new List<Course>();
            foreach (DataRow row in ds.Tables[0].Rows)
            {
                //first one 
                return (new Course { No = row["course_no"].ToString(), Name = row["course_name"].ToString(), Id = (long)row["course_id"] , Key = row["key"].ToString() });
            }

            return null;
        }
    }
}
