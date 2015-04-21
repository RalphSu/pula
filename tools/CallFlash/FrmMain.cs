using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using AES;
using System.Xml;

namespace CallFlash
{
    public partial class FrmMain : Form
    {
        public FrmMain()
        {
            InitializeComponent();
            lblStatus.Text = "";

            swf.FlashCall += new AxShockwaveFlashObjects._IShockwaveFlashEvents_FlashCallEventHandler(swf_FlashCall);
        }

        private void btnStartCall_Click(object sender, EventArgs e)
        {
            AESLogger.init();
            callFunction("pula_key", AES.AesCtr.encrypt(UnixTool.timestamp().ToString(), "c13124f32117de81", 256));
            AESLogger.close();

         
        }

        private void swf_FlashCall(object sender, AxShockwaveFlashObjects._IShockwaveFlashEvents_FlashCallEvent e)//向as发送请求
        {
            //没有任何代码也可以；

            XmlDocument document = new XmlDocument();
            document.LoadXml(e.request);
            // get attributes to see which command flash is trying to call
            XmlAttributeCollection attributes = document.FirstChild.Attributes;
            // get function
            String command = attributes.Item(0).InnerText;
            // get parameters
            XmlNodeList list = document.GetElementsByTagName("arguments");

            tbLog.Text = e.request ;



            lblStatus.Text = command + " status=" + list.Item(0).InnerText; 

        }

         private void callFunction(string funName, string arg)
        {
            //C#传给Flash的值
            swf.CallFunction("<invoke name=\"" + funName + "\" returntype=\"xml\"><arguments><string>" + arg + "</string></arguments></invoke>");
        }

        private void FrmMain_Load(object sender, EventArgs e)
        {
           // swf.Movie = @"E:\work\pula\demo\interface\PulaInfsDemo.swf";
            swf.Movie = Application.StartupPath + "\\PulaInfsDemo.swf ";

        }

        private void swf_OnReadyStateChange(object sender, AxShockwaveFlashObjects._IShockwaveFlashEvents_OnReadyStateChangeEvent e)
        {
            //AESLogger.init();
            //callFunction("pula_key", AES.AesCtr.encrypt(UnixTool.timestamp().ToString(), "c13124f32117de81", 256));
            //AESLogger.close();
        }

        private void button1_Click(object sender, EventArgs e)
        {

            AESLogger.init();
            callFunction("pula_key", AES.AesCtr.encrypt(UnixTool.timestamp().ToString(), "c13124f32117de8d", 256));
            AESLogger.close();
        }
    }
}
