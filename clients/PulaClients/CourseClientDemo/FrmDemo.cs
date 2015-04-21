using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Security.Cryptography;
using System.Data.SQLite;
using System.IO;
using System.Net;
using RemoteService;
using System.Reflection;
using Newtonsoft.Json;

namespace CourseClient
{
    public partial class FrmDemo : Form
    {
        private string code = null;
        private string active_code = null;
        public FrmDemo()
        {
            InitializeComponent();

            
            SqlLiteHelper.ConnectionString = "Datasource=pula.db;Pooling=true;FailIfMissing=false";

            string code = HardwareInfo.GetHardDiskID();// +":" + HardwareInfo.GetCpuID();

            string pwd = this.Encrypt(code);
            this.code = pwd; AddLog("CODE=" + this.code);

            LoadSystem();
        }

        private void LoadSystem()
        {
            DataSet ds = SqlLiteHelper.ExecuteQuery("select machine_code,node_name,active_code from system");
            if (ds.Tables[0].Rows.Count > 0)
            {
                string machine_code = ds.Tables[0].Rows[0]["machine_code"].ToString();
                string name = ds.Tables[0].Rows[0]["node_name"].ToString();
                string active_code = ds.Tables[0].Rows[0]["active_code"].ToString();

                //激活码
                this.active_code = active_code;
                this.code = machine_code;

            }
        }

        private void AddLog(string msg){
            txtLog.Text += msg +"\r\n";
        }

        private void BtnMachineNo_Click(object sender, EventArgs e)
        {
 
            //txtLog.Text += "CODE=" + pwd +"\n\r";
            

            
            //SqlLiteHelper.Password = "pula_db_secret_hidden";
            //看看是否存在一行
            DataSet ds = SqlLiteHelper.ExecuteQuery("select machine_code,node_name from system");
            AddLog("Count=" + ds.Tables.Count.ToString());
            if(ds.Tables[0].Rows.Count>0){
                string machine_code = ds.Tables[0].Rows[0]["machine_code"].ToString();
                string name = ds.Tables[0].Rows[0]["node_name"].ToString();
                if (String.Equals(machine_code, this.code))
                {
                    AddLog("machine code match!");
                }
                else
                {
                    //need update
                    AddLog("machine code not match ,old =" + machine_code);
                    SqlLiteHelper.ExecuteNonQuery("update system set machine_code=? ", this.code);
                    AddLog("updated");
                }
                AddLog("machine name=" + name);
            }else{
                AddLog("don't have machine code yet ");
                int n = SqlLiteHelper.ExecuteNonQuery("insert into system( machine_code) values(?)", this.code);
                AddLog("inserted :"+n.ToString());
            }

            
        }

        public string Encrypt(string strPwd)
        {
            MD5 md5 = new MD5CryptoServiceProvider();
            byte[] data = System.Text.Encoding.Default.GetBytes(strPwd);//将字符编码为一个字节序列 
            byte[] md5data = md5.ComputeHash(data);//计算data字节数组的哈希值 
            md5.Clear();
            string str = "";
            for (int i = 0; i < md5data.Length - 1; i++)
            {
                str += md5data[i].ToString("x").PadLeft(2, '0');
            }
            return str;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            JsonResult jr = RemoteServiceProxy.RequestActive(this.code, "我要申请激活");
            if (jr.error)
            {
                AddLog("error=" + jr.message);
            }
            else
            {
                AddLog("success=" + jr.message);
            }
        }

        private void BtnSyncActive_Click(object sender, EventArgs e)
        {
            JsonResult jr = RemoteServiceProxy.SyncActive(this.code);
            if (jr.error)
            {
                AddLog("error=" + jr.message);
            }
            else
            {
               // AddLog("success=" + jr.message);
                //save
                int n = SqlLiteHelper.ExecuteNonQuery("update system set active_code=?,node_name=?,active_time=datetime('now','localtime'),node_id=?",jr.data["licenseKey"],jr.data["name"],jr.data["classroomId"]);
               // int n = SqlLiteHelper.ExecuteNonQuery("update system set node_id=?", jr.data["classroomId"]);
                AddLog("result=" + n.ToString());
            }

        }

        private void BtnChangePassword_Click(object sender, EventArgs e)
        {
           // SqlLiteHelper.ConnectionString = "Datasource=pula.db;Pooling=true;FailIfMissing=false";
            //SqlLiteHelper.ChangePassword("pula_db_secret_hidden");
        }

        private void BtnSyncCourse_Click(object sender, EventArgs e)
        {
            //
            JsonResultList jr = RemoteServiceProxy.SyncCourse(this.code, this.active_code);
            if (jr.error)
            {
                AddLog("error=" + jr.message);
            }
            else
            {
                // AddLog("success=" + jr.message);
                //save
                string sql = @"select course_no,removed from course";

                //load all
                DataSet ds = SqlLiteHelper.ExecuteQuery(sql);
                Dictionary<string,bool> exists_courses = new Dictionary<string,bool>();
                foreach (DataRow row in ds.Tables[0].Rows)
                {
                    string course_no = row["course_no"].ToString();
                    bool removed = (bool)row["removed"];

                    exists_courses.Add(course_no, removed);
                }

                //所有数据是否存在,所有都要更新,多出来的还没删掉的,要标记为删除
                foreach (var row in jr.data)
                {
                    int idx = GetCourseCategoryIndex( row[ "categoryNo"] );
                    //去除击中的
                    string course_no = row["courseNo"];
                    if (exists_courses.ContainsKey(course_no))
                    {
                        //update
                        int n = SqlLiteHelper.ExecuteNonQuery("update course set course_name=?,key=?,course_category=?,sub_category=?,updated_time=datetime('now','localtime'),removed=? where course_no=?", row["courseName"], row["courseKey"],
                            idx, row["subCategoryName"],false, course_no);
                        //remove hits
                        exists_courses.Remove(course_no);

                        AddLog("update result=" + n.ToString());
                    }
                    else
                    {
                        //insert
                        int n=  SqlLiteHelper.ExecuteNonQuery(@"insert into course (course_name,key,course_no,course_category,sub_category,created_time,updated_time,removed)
                                    values( ?,?,?,?,?,datetime('now','localtime'),datetime('now','localtime'),?)",
                                    row["courseName"], row["courseKey"], row["courseNo"], idx, row["subCategoryName"], false);


                        AddLog("insert result=" + n.ToString());
                    }
                }

                //尚未集中的,如果没有标为删除,则标为删除
                int cc = 0;
                foreach (var row in exists_courses)
                {
                    if (!row.Value)
                    {
                        SqlLiteHelper.ExecuteNonQuery("update course set removed=?,updatedTime=datetime('now','localtime') where course_no=?", true, row.Key);
                        cc++;
                    }
                }

                AddLog("removed=" + cc.ToString());


                //int n = SqlLiteHelper.ExecuteNonQuery("update system set active_code=?,node_name=?,active_time=datetime('now','localtime'),node_id=?", jr.data["licenseKey"], jr.data["name"], jr.data["classroomId"]);
                // int n = SqlLiteHelper.ExecuteNonQuery("update system set node_id=?", jr.data["classroomId"]);
                //AddLog("result=" + n.ToString());
            }
        }

        private int GetCourseCategoryIndex(string p)
        {
            string idx = p.Substring(p.Length - 1);
            int n = 0;
            if (int.TryParse(idx, out n))
            {
                return n;
            }
            return 0;
        }

        private void BtnCourseAll_Click(object sender, EventArgs e)
        {
            string sql = @"select * from course";

            //load all
            DataSet ds = SqlLiteHelper.ExecuteQuery(sql);
            Dictionary<string, bool> exists_courses = new Dictionary<string, bool>();
            foreach (DataRow row in ds.Tables[0].Rows)
            {
                string course_no = row["course_no"].ToString();
                string course_name = row["course_name"].ToString();
                long course_id = (long )row["course_id"];
                bool removed = (bool)row["removed"];
                string key = row["key"].ToString();
                long course_category = (long )row["course_category"];
                string sub_category = row["sub_category"].ToString();
                string course_comments = row["course_comments"].ToString();
                DateTime dt = (DateTime) row["created_time"];

                AddLog(var_dump(new {
                course_no= course_no,
                course_name = course_name,
                course_id = course_id,
                removed = removed,
                key = key,
                course_category = course_category,
                sub_category = sub_category,
                course_comments = course_comments,
                created_time = dt 
                
            
            }));
            }
        }
        ///<summary>
        /// equiv of PHP's var dump for an object’s properties because i cbf writing all the properties out.
        ///</summary>
        ///<param name="info"></param>
        private static string var_dump(object info)
        {
            StringBuilder sb = new StringBuilder();

            Type t = info.GetType();
            PropertyInfo[] props = t.GetProperties();
            sb.AppendFormat("{0,-18} {1}", "Name", "Value");
            sb.AppendLine();
            foreach (PropertyInfo prop in props)
            {
                sb.AppendFormat("{0,-18} {1}", prop.Name, prop.GetValue(info, null).ToString());
                sb.AppendLine();
            }

            return sb.ToString();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            CourseTask ct = new CourseTask();
            ct.CourseId = 5;
            ct.Master = new CardInfo { Rfid = "1234567", No = "0001", Name = "张三" };
            ct.Assistant1 = new CardInfo { Rfid = "1234568", No = "0002", Name = "李四" };
            ct.Assistant2 = new CardInfo { Rfid = "1234569", No = "0003", Name = "王五" };

            //xuesheng
            ct.Students.Add(new CardInfo { Rfid = "C23456A", No = "SS0001", Name = "张小虎" });
            ct.Students.Add(new CardInfo { Rfid = "D23456B", No = "SS0003", Name = "李小龙" });

            long id = StoreModel.StartCourseTask(ct);

            AddLog("id=" + id.ToString());
 
        }

        private void BtnCloseCourseTask_Click(object sender, EventArgs e)
        {
            StoreModel.FinishCourseTask(3);
        }

        private void BtnCapture_Click(object sender, EventArgs e)
        {
            FrmCapture fc = new FrmCapture();


            fc.CourseTaskId = 10;

            List<CardInfo> students = new List<CardInfo>();


            students.Add(new CardInfo { Name="张小里",No="001001" });
            students.Add(new CardInfo { Name = "王小虎", No = "001002" });
            students.Add(new CardInfo { Name = "大牛", No = "001003" });

            fc.Students = students;

            fc.ShowDialog();

            //result
            foreach (var item in fc.Photos)
            {
                AddLog(item.Key + "=" + item.Value);
            }
        }

        private void btnTestJson_Click(object sender, EventArgs e)
        {
            string json = @"{'message':'\u6df1\u5733\u5e02'}";
            JsonResult jr =  JsonConvert.DeserializeObject<JsonResult>(json);
            Console.Write(jr.message);
        }


      
    }
}
