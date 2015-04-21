using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using RemoteService;
using SendCard;
using PulaRfid;

namespace CourseClient
{
    public partial class FrmMain : Form, IComSupport
    {

        private CardConfig config;
        public PulaCardReader reader = new PulaCardReader();
        private bool opened = false;


        public FrmMain()
        {
            InitializeComponent();

            BtnCheckCourse.Visible = false;

            opened = false;
            config = new CardConfig(Application.StartupPath + "\\config.ini");

            btnCourseStart.Visible = false;
            btnCourseEnd.Visible = false;

        }

        private void AddLog(string msg)
        {
            tbLog.Text +=  System.DateTime.Now +" "+ msg + "\r\n";
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

        private void btnSyncCourse_Click(object sender, EventArgs e)
        {
            JsonResultList jr = RemoteServiceProxy.SyncCourse(FrmActive.machineCode, FrmActive.activeCode);

            StoreModel.ProcessNotActived(jr);

            if (jr.error)
            {
                AddLog("错误:" + jr.message);
                UIHelper.ShowAlert(jr.message);
                return;
            }
            // AddLog("success=" + jr.message);
            //save
            string sql = @"select course_no,removed from course";

            //load all
            DataSet ds = SqlLiteHelper.ExecuteQuery(sql);
            Dictionary<string, bool> exists_courses = new Dictionary<string, bool>();
            foreach (DataRow row in ds.Tables[0].Rows)
            {
                string course_no = row["course_no"].ToString();
                bool removed = (bool)row["removed"];

                exists_courses.Add(course_no, removed);
            }

            //所有数据是否存在,所有都要更新,多出来的还没删掉的,要标记为删除
            int updateCount = 0;
            int insertCount = 0;
            foreach (var row in jr.data)
            {
                int idx = GetCourseCategoryIndex(row["categoryNo"]);
                //去除击中的
                string course_no = row["courseNo"];
                if (exists_courses.ContainsKey(course_no))
                {
                    //update
                    int n = SqlLiteHelper.ExecuteNonQuery("update course set course_name=?,key=?,course_category=?,sub_category=?,updated_time=datetime('now','localtime'),removed=? where course_no=?", row["courseName"], row["courseKey"],
                        idx, row["subCategoryName"], false, course_no);
                    //remove hits
                    exists_courses.Remove(course_no);

                    //AddLog("update result=" + n.ToString());
                    updateCount++;
                }
                else
                {
                    //insert
                    int n = SqlLiteHelper.ExecuteNonQuery(@"insert into course (course_name,key,course_no,course_category,sub_category,created_time,updated_time,removed)
                                    values( ?,?,?,?,?,datetime('now','localtime'),datetime('now','localtime'),?)",
                                row["courseName"], row["courseKey"], row["courseNo"], idx, row["subCategoryName"], false);


                    //AddLog("insert result=" + n.ToString());
                    insertCount++;
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

            AddLog("新增课程:" + insertCount.ToString());
            AddLog("更新课程:" + updateCount.ToString());
            AddLog("移除课程:" + cc.ToString());



        }

        private void FrmMain_Load(object sender, EventArgs e)
        {
            UpdateLeftCourseCount();
        }

        private void UpdateLeftCourseCount()
        {
            //查找已完成但未上报的数量
            object obj = SqlLiteHelper.ExecuteScalar("select count(1) from course_task where status=2");
            long l = (long)obj;
            if (l == 0)
            {
                lblSyncLeft.Text = "暂无待同步的内容";
            }
            else
            {
                lblSyncLeft.Text = "尚有"+l.ToString()+"笔数据未同步";
            }
        }

        private void btnSyncCourseTask_Click(object sender, EventArgs e)
        {
            /*JsonResult jr = RemoteServiceProxy.UploadWork(@"C:\Users\乙郎\Pictures\7cbc9488-0a4b-4ab5-a6ed-70e319096680.png", "12", "14");
           AddLog("result=" + jr.message);*/

            EnableButton(false);
            List<long> courseTaskIds = StoreModel.GetFinishCourseTaskIds();
            pb.Maximum = courseTaskIds.Count;
            int syncCount = 0;
            int successCount = 0;
            int errorCount = 0;
            foreach( var id in courseTaskIds){
                syncCount++;
                pb.Value = syncCount;
              

                CourseTask ct = StoreModel.GetById( id);
                JsonResultList list =  RemoteServiceProxy.Report(ct,FrmActive.machineCode,FrmActive.activeCode  );

                if (list.error)
                {
                    AddLog("同步课程任务失败:" + id.ToString() +" 错误信息:"+list.message);
                    errorCount++;
                    continue;
                }


                List<WorkInfo4Upload> works = RemoteServiceProxy.PrepareWork(ct.Works, list.data);

                //然后传到服务器!--作品!
                bool error = false;
                foreach (var w in works)
                {
                    JsonResult jr = RemoteServiceProxy.ReportWork(w);
                    if (jr.error)
                    {
                        AddLog("同步作品失败:" + id.ToString() + " 作品=" + w.StudentNo);
                        error = true;
                        continue;
                    }
                }
                if (!error)
                {
                    StoreModel.FinishReportCourseTask(id);
                    StoreModel.RemoveFiles(ct.Works);
                    successCount++;
                }
                else
                {
                    errorCount++;
                }
                
            }

            //全部更新完成了
            //界面刷新一下
            this.UpdateLeftCourseCount();
            pb.Value = 0;
            EnableButton(true);

            AddLog("同步数量:" + syncCount.ToString() + " 成功数量:" + successCount.ToString() + " 错误数量:" + errorCount.ToString());
        }

        private void EnableButton(bool p)
        {
            btnSyncCourse.Enabled = p;
            btnSyncCourseTask.Enabled = p;
        }

        private void btnCourseStart_Click(object sender, EventArgs e)
        {
            CourseTask ct = new CourseTask();
            ct.CourseId = 5;
            ct.Master = new CardInfo { Rfid = "1234567", No = "0001", Name = "张三" };
            ct.Assistant1 = new CardInfo { Rfid = "1234568", No = "0002", Name = "李四" };
            ct.Assistant2 = new CardInfo { Rfid = "1234569", No = "0003", Name = "王五" };

            //xuesheng
            ct.Students.Add(new CardInfo { Rfid = "C23456A", No = "JQ00002", Name = "张小虎" });
            ct.Students.Add(new CardInfo { Rfid = "D23456B", No = "JQ00003", Name = "李小龙" });

            long id = StoreModel.StartCourseTask(ct);

            AddLog("id=" + id.ToString());

            temp_id = id;
            temp_course_task = ct; 

        }

        private long temp_id = 0;
        private CourseTask temp_course_task = null;

        private void btnCourseEnd_Click(object sender, EventArgs e)
        {
            //根据刚才存的临时id,呼叫拍照,然后存储拍照数据,最后形成完整的课程结果
            FrmCapture frmCapture = new FrmCapture();
            frmCapture.CourseTaskId = temp_id;
            frmCapture.Students = temp_course_task.Students;
            if (frmCapture.ShowDialog() == DialogResult.OK)
            {

                //result
                foreach (var item in frmCapture.Photos)
                {
                    AddLog(item.Key + "=" + item.Value);
                }

                StoreModel.FinishCourseTask(temp_id, frmCapture.Photos);
            }


        }

        private void BtnCheckCourse_Click(object sender, EventArgs e)
        {

        }

        private void BtnSetup_Click(object sender, EventArgs e)
        {
            //
            this.showSetup();
        }

        public void showSetup()
        {
            var frmSetup = new FrmSetup();
            frmSetup.Config = this.config;
            frmSetup.ComSupport = this;

            frmSetup.ShowDialog();
        }

        public bool OpenComm(int p)
        {

            opened = reader.Open(p);

            //UpdateButtons();

            return opened;
        }

        internal bool OpenComm()
        {
            if (!OpenComm(config.ComPort))
            {
                MessageBox.Show("无法打开串口,请重新设置");
                this.showSetup();
                return false;
            }
            return true;

        }
    }
}
