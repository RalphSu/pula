using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RemoteService;

namespace CourseClient
{
    class FlashCourseTask
    {

        private CourseTask courseTask = new CourseTask();
        //cache
        private Dictionary<string, Course> courseMap = new Dictionary<string, Course>();
        private Dictionary<string, CardInfo> teacherMap = new Dictionary<string, CardInfo>();
        private Dictionary<string, CardInfo> studentMap = new Dictionary<string, CardInfo>();

        public CourseTask CourseTask
        {
            get
            {
                return this.courseTask;
            }

        }
 
        internal bool process(string cmd, string data)
        {

            if (cmd.Equals("course"))
            {
                //根据data (课程编号) 找到课程id
                if (courseMap.ContainsKey(data))
                {
                    courseTask.CourseId = courseMap[data].Id;
                    courseTask.CourseNo = data;
                }
                
            }
            else if (cmd.Equals("master"))
            {
                //主教师
                //
                courseTask.Master = GetTeacher(data);

            }
            else if (cmd.Equals("assistant1"))
            {
                courseTask.Assistant1 = GetTeacher(data);
            }
            else if (cmd.Equals("assistant2"))
            {
                courseTask.Assistant2 = GetTeacher(data);
            }
            else if (cmd.Equals("student"))
            {
                CardInfo s = GetStudent(data);
                if (s != null)
                {
                    courseTask.Students.Add(s);
                }
            }
            else if (cmd.Equals("start_course"))
            {
                return true;
            }

            return false;

        }

        private CardInfo GetTeacher(string data)
        {
            if (teacherMap.ContainsKey(data))
            {
                return teacherMap[data];
            }

            return null;
        }

        private CardInfo GetStudent(string data)
        {

            if (studentMap.ContainsKey(data))
            {
                return studentMap[data];
            }
            return null;
        }


        internal void ClearMap()
        {
            this.courseMap.Clear();
        }

        internal void AddMap(Course k)
        {
            this.courseMap.Add(k.No, k);
        }

        //缓存数据,并非真实待提交的,
        internal void AddTeacher(CardInfo t)
        {
            if(!this.teacherMap.ContainsKey(t.No)){
                this.teacherMap.Add(t.No, t);
            }
        }
        //缓存数据,并非真实待提交的,
        internal void AddStudent(CardInfo s)
        {
            if (!this.studentMap.ContainsKey(s.No))
            {
                this.studentMap.Add(s.No, s);
            }

        }
    }
}
