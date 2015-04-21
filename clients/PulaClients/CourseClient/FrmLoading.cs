using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Xml;
using System.Runtime.InteropServices;
using RemoteService;
using PulaRfid;

namespace CourseClient
{
    public partial class FrmLoading : Form
    {
        //loading step

        private int loadingStep = 3;
        private int categoryId =1;
        private string subCategoryName = null;
        //private CourseTask courseTask = new CourseTask();
        private FlashCourseTask flashCourseTask = new FlashCourseTask();

        public FrmMain FrmMain { get; set; }

        private bool flagReadCard = false;


        [DllImport("Kernel32.dll", SetLastError = true)]
        public static extern bool AllocConsole();
        [DllImport("Kernel32.dll")]
        public static extern bool FreeConsole();

        public FrmLoading()
        {
            InitializeComponent();

           // AllocConsole();
            timer1.Enabled = false;

            timer1.Interval = 100;
        }

        private void FrmLoading_Shown(object sender, EventArgs e)
        {
            doLoading();
        }

        private void doLoading()
        {
            loadingStep++;
            if (loadingStep > 6)
            {
                //should end to next ;

                this.DialogResult = DialogResult.OK;
                return;
            }
           
            string fp = System.Environment.CurrentDirectory + "\\swf\\loading" + (loadingStep.ToString()) + ".swf";

            swf.Movie = fp;

            afterLoading(loadingStep);

        }

        private void afterLoading(int loadingStep)
        {
            //要给分类了
            if (loadingStep == 5)
            {

                var text = StoreModel.GetCategoryText(this.categoryId);
                callFunction("pula_sub_category", text); //"大金刚篇,历史篇,混战篇,人类起源篇"
            }
            else if (loadingStep == 6)
            {
                //课程 -- 根据课程篇名

                //callAddCourse("001", "变形金刚1");
                //callAddCourse("002", "变形金刚2");
                var courseList = StoreModel.GetCourses(this.subCategoryName);

                //build a map for courseSelect
                flashCourseTask.ClearMap();

                //make a map 
                foreach(Course k in courseList){
                    flashCourseTask.AddMap(k);

                    //放到画面中
                    callAddCourse(k.No, k.Name);
                }

                //开始读卡

                this.startRead();

            }
        }

        private void callAddCourse(string no, string name)
        {

            swf.CallFunction("<invoke name=\"pula_course\" returntype=\"xml\"><arguments><string>" + no + "</string><string>" + name + "</string></arguments></invoke>");
        }

        private void callFunction(string funName, string arg)
        {
            //C#传给Flash的值
            swf.CallFunction("<invoke name=\"" + funName + "\" returntype=\"xml\"><arguments><string>" + arg + "</string></arguments></invoke>");
        }


        private void FrmLoading_Load(object sender, EventArgs e)
        {
            this.FormBorderStyle = FormBorderStyle.None;
            this.WindowState = FormWindowState.Maximized; 
        }

        private void swf_OnProgress(object sender, AxShockwaveFlashObjects._IShockwaveFlashEvents_OnProgressEvent e)
        {
            Text = e.percentDone.ToString();
        }

        private void swf_FlashCall(object sender, AxShockwaveFlashObjects._IShockwaveFlashEvents_FlashCallEvent e)
        {
            XmlDocument document = new XmlDocument();
            document.LoadXml(e.request);
            // get attributes to see which command flash is trying to call
            XmlAttributeCollection attributes = document.FirstChild.Attributes;
            // get function
            String command = attributes.Item(0).InnerText;
            // get parameters
            XmlNodeList list = document.GetElementsByTagName("arguments");

            var node = list.Item(0);

            //MessageBox.Show(node.InnerXml);

            var paramList = node.ChildNodes;

            processParams(paramList);

            //lblStatus.Text = command + " status=" +; 
        }

        private void processParams(XmlNodeList paramList)
        {
            if (paramList.Count == 1)
            {
                var cmd = paramList.Item(0).InnerText;
                if (this.flashCourseTask.process(cmd, string.Empty))
                {
                    //开课成功
                    //进入开课界面
                    //加载课程内容
                    long courseTaskId = StoreModel.StartCourseTask(this.flashCourseTask.CourseTask);

                    //跳入课程执行的画面

                    //课程的具体存放位置
                    StartCourseFlash(this.flashCourseTask.CourseTask.CourseNo);

                    //传递照片
                    SaveCourseFiles(courseTaskId,this.flashCourseTask.CourseTask);

                    //ShowMain for sync
                    this.FrmMain.ShowDialog();
                }
            }
            else if (paramList.Count == 2)
            {
                var cmd = paramList.Item(0).InnerText;
                var data = paramList.Item(1).InnerText;

                //  var state = paramList.Item(0).InnerText;
                if (cmd.StartsWith("loading_end"))
                {
                    swf.Stop();
                    timer1.Enabled = true;
                    return;
                }
                else
                    //分类数据,需要加载对应的分类信息了!
                    if (cmd.ToLower().Equals("category"))
                    {
                        int.TryParse(data, out this.categoryId);
                        //MessageBox.Show(data);
                        swf.Stop();
                        timer1.Enabled = true;
                        return;
                    }
                    else
                        if (cmd.ToLower().Equals("sub_category"))
                        {
                            this.subCategoryName = data.Trim();
                            //int.TryParse(data, out this.categoryId);
                            //MessageBox.Show(data);
                            swf.Stop();
                            timer1.Enabled = true;
                            return;
                        }
                        else
                            //卡号,手工输入
                            if (cmd.ToLower().Equals("card"))
                            {
                                processCard(data.Trim(), 1);
                                return;
                            }


                //其他命令，先认为是开课指令

                if( !cmd.Equals ("report") && !cmd.Equals ("_log")){
                    this.stopRead();
                }

                if (this.flashCourseTask.process(cmd, data.Trim()))
                {
                    //开课成功
                    //进入开课界面
                    //加载课程内容
                    StoreModel.StartCourseTask(this.flashCourseTask.CourseTask);

                    //这里的语句是不会进入的,因为start_course只传一个参数进来.所以不会关闭

                    //跳入课程执行的画面

                    //课程的具体存放位置


                }



            }
            else if (paramList.Count == 3)
            {
                //当分类扩充后修改

                var cmd = paramList.Item(0).InnerText;
                var data = paramList.Item(1).InnerText;
                var type = paramList.Item(1).InnerText;

                //卡号,手工输入
                if (cmd.ToLower().Equals("card"))
                {
                    int intType = 0;
                    int.TryParse(type, out intType);
                    processCard(data.Trim(), intType);
                }
            }

        }

        private void SaveCourseFiles(long courseTaskId,CourseTask ct)
        {
            FrmCapture frmCapture = new FrmCapture();
            frmCapture.CourseTaskId = courseTaskId;
            frmCapture.Students = ct.Students;
            if (frmCapture.ShowDialog() == DialogResult.OK)
            {

                //result
                /*foreach (var item in frmCapture.Photos)
                {
                    AddLog(item.Key + "=" + item.Value);
                }*/

                StoreModel.FinishCourseTask(courseTaskId, frmCapture.Photos);
            }
        }

        private void StartCourseFlash(string p)
        {
            //呼叫任务开始
            FrmCourse frmCourse = new FrmCourse();
            frmCourse.CourseNo = p;
            frmCourse.ShowDialog();
        }


        private void startRead()
        {
            flagReadCard = true;
            timerRead.Enabled = true;
        }

        private void stopRead()
        {
            flagReadCard = false;
            timerRead.Enabled = false;
        }

        private void processCard(string p,int type)
        {
            //卡片信息,需要呼叫回传资料

            CardInfo ci =  StoreModel.GetCardInfo(p, type);

            if (ci == null)
            {
                MessageBox.Show("卡号:" + p + " 不存在");
                return;
            }

            //放入缓存,并非提交数据
            if (type == 1)
            {
                //student
                flashCourseTask.AddStudent(ci);
            }
            else
            {
                flashCourseTask.AddTeacher(ci);
            }

            var no = ci.No;
            var name = ci.Name;
            
            swf.CallFunction("<invoke name=\"pula_card\" returntype=\"xml\"><arguments><string>" + no + "</string><string>" + name + "</string><number>" + type.ToString() + "</number></arguments></invoke>");

        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            timer1.Enabled = false;
            doLoading();
        }

        private void FrmLoading_FormClosed(object sender, FormClosedEventArgs e)
        {
            //FreeConsole();
        }

        private void FrmLoading_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.S && e.Control)
            {
              this.FrmMain.ShowDialog();
            }
            //Console.WriteLine("Form1_KeyDown  {0}", e.KeyCode);
        }

        private void timerFinish_Tick(object sender, EventArgs e)
        {

        }

        //读卡
        private void timerRead_Tick(object sender, EventArgs e)
        {
            timerRead.Enabled = false;
            //定时扫描卡
            var reader = FrmMain.reader; 
            string cardid = reader.GetCard();

            if (!string.IsNullOrEmpty(cardid))
            {
                reader.Buzz(1);
                //TssCardRfid.Text = cardid;
                //TbCardId.Text = cardid;
            }
            else
            {
                //未读到卡

                timerRead.Enabled = flagReadCard;
                return;
            }


            //

            CardMeta rm = null;

            try
            {
                reader.PrepareCheck(cardid);
                rm = reader.ReadMeta();

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message + " 可能是未初始化,或其他未经认证的卡片");
                timerRead.Enabled = flagReadCard;
                return;
            }

            if (string.IsNullOrEmpty(rm.name))
            {
                //LblName.Text = "空白卡";
                MessageBox.Show("空白卡");
            }
            else
            {
                /*LblName.Text = rm.name;
                LblNo.Text = rm.no;
                LblType.Text = GetTypeName(rm.type);*/

                CardInfo ci = new CardInfo { Name = rm.name, No = rm.no,Rfid = cardid };
                //放入缓存,并非提交数据
                if (rm.type == 1)
                {
                    //student
                    flashCourseTask.AddStudent(ci);
                }
                else
                {
                    flashCourseTask.AddTeacher(ci);
                }


                //var cmd = SwfHelper.makeCard(rm.no, rm.name, rm.type.ToString());
                swf.CallFunction("<invoke name=\"pula_card\" returntype=\"xml\"><arguments><string>" + rm.no
                    + "</string><string>" + rm.name + "</string><number>" + rm.type.ToString() + "</number></arguments></invoke>");

                //swf.CallFunction(cmd);
            }

            //避免关掉后又被开启,所以认全局标记
            timerRead.Enabled = flagReadCard;
        }

        
    }
}
