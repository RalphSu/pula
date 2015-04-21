using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;

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
           string sql = "insert into course_task (begin_time,status,master_id,assistant1_id,assistant2_id,status) values( datetime('now','localtime'),?,?,?,?,1)";
           SqlLiteHelper.ExecuteNonQuery(sql, CourseTask.STATUS_START, master_id, assistant1_id, assistant2_id);
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

        internal static void FinishCourseTask(long id)
        {
            //关闭课程
            string sql = "update course_task set end_time=datetime('now','localtime'),status=? where course_task_id=?";
            int n = SqlLiteHelper.ExecuteNonQuery(sql,CourseTask.STATUS_END,  id);

        }
    }
}
