using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Xml;
using AES;
using RemoteService;
using System.IO;

namespace CourseClient
{
    public partial class FrmCourse : Form
    {

        public string CourseNo { get; set; }
        private string CourseKey;
        private bool inited = false;
        private bool second = false;

        public FrmCourse()
        {
            InitializeComponent();
        }


        private void FrmCourse_Load(object sender, EventArgs e)
        {
            //根据CourseNo 获得数据

            Course c = StoreModel.GetCourse(this.CourseNo);

            this.CourseKey = c.Key;

            timerEncypt.Enabled = true;

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
        }

        private void processParams(XmlNodeList paramList)
        {
            if (paramList.Count == 1)
            {
                var state = paramList.Item(0).InnerText;

                if ("1".Equals(state))
                {
                    this.Text = "校验密文错误（无法解密）";
                }
                else if ("2".Equals(state))
                {
                    this.Text = "无效时间戳（逾期）";
                }
                else if ("0".Equals(state))
                {
                    this.Text = "正常";
                }
                else if ("99".Equals(state))
                {
                   // this.Text = "正常";
                    timerClose.Enabled = true;
                }

            }
        }

        private void swf_OnProgress(object sender, AxShockwaveFlashObjects._IShockwaveFlashEvents_OnProgressEvent e)
        {
           /* if (e.percentDone == 100)
            {
                callFunction("pula_key", AES.AesCtr.encrypt(UnixTool.timestamp().ToString(), this.CourseKey, 256));
            }*/
        }

        private void timerEncypt_Tick(object sender, EventArgs e)
        {
            //
            // "c13124f32117de81"

            if (!inited)
            {
                string fp = System.Environment.CurrentDirectory + "\\courses\\" + this.CourseNo + "\\pag2a.swf";

                if (!File.Exists(fp))
                {
                    timerEncypt.Enabled = false;
                    MessageBox.Show("课程文件不存在,请检查:"+fp );
                    return;
                }

                swf.Movie = fp;
             
                inited = true;

                timerEncypt.Interval = 1200;

                return;
            }

            if (!second)
            {
                timerEncypt.Interval = 60000;// 60 secs 
                second = true;
            }

            //callFunction("pula_key", AES.AesCtr.encrypt(UnixTool.timestamp().ToString(), this.CourseKey, 256));

            callFunction("pula_key", AES.AesCtr.encrypt(UnixTool.timestamp().ToString(), this.CourseKey, 256));
           
        }


        private void callFunction(string funName, string arg)
        {
            //C#传给Flash的值
            swf.CallFunction("<invoke name=\"" + funName + "\" returntype=\"xml\"><arguments><string>" + arg + "</string></arguments></invoke>");
        }

        private void btnStart_Click(object sender, EventArgs e)
        {
            callFunction("pula_key", AES.AesCtr.encrypt(UnixTool.timestamp().ToString(), this.CourseKey, 256));
        }

        private void timerClose_Tick(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
