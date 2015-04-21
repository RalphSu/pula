using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using AForge.Video.DirectShow;

namespace CourseClient
{
    public partial class FrmCapture : Form
    {


        // list of video devices
        FilterInfoCollection videoDevices;
        bool captureFlag;
        int studentIndex;

        public long CourseTaskId { get; set; }
        public List<CardInfo> Students { get; set; }
        public IDictionary<string, string> Photos { get; set; }
        

        public FrmCapture()
        {
            InitializeComponent();


            captureFlag = false;

            BtnStop.Enabled = false;
            BtnCapture.Enabled = false;

            BtnStart.Enabled = false;
            TssOpenStatus.Text = "";

            Photos = new Dictionary<string, string>();

            try
            {
                // enumerate video devices
                videoDevices = new FilterInfoCollection(FilterCategory.VideoInputDevice);

                if (videoDevices.Count == 0)
                {
                    TssStatus.Text = "找不到摄像头";
                    return;
                }

                TssStatus.Text = videoDevices[0].Name;

            }
            catch
            {
                BtnStart.Enabled = false;
            }
            
        }

        private void UpdateStudentInfo()
        {
            int c = this.Students.Count;
            //LblStudent.Text = ;
            Text = "(" + (this.studentIndex + 1).ToString() + "/" + c.ToString() + ")" + this.Students[this.studentIndex].Name;

            BtnNext.Enabled = (this.studentIndex + 1 < c);
            BtnPrev.Enabled = this.studentIndex > 0;

        }

        private void FrmCapture_Load(object sender, EventArgs e)
        {
            //清空
            Photos.Clear();
        }

        private void BtnStart_Click(object sender, EventArgs e)
        {
            StartCameras();

            BtnStart.Enabled = false;
            BtnStop.Enabled = true; BtnCapture.Enabled = true;

            TssOpenStatus.Text = "开始";
        }

        private void FrmCapture_FormClosing(object sender, FormClosingEventArgs e)
        {
            StopCameras();
        }

        private void BtnStop_Click(object sender, EventArgs e)
        {
            StopCameras();

            BtnStart.Enabled = true;
            BtnStop.Enabled = false;
            BtnCapture.Enabled = false;

            TssOpenStatus.Text = "停止";
        }

        // Start cameras
        private void StartCameras()
        {
            // create first video source
            VideoCaptureDevice videoSource1 = new VideoCaptureDevice(videoDevices[0].MonikerString);
            videoSource1.DesiredFrameRate = 10;

            videoSourcePlayer1.VideoSource = videoSource1;
            videoSourcePlayer1.Start();

        }

        // Stop cameras
        private void StopCameras()
        {
            //timer.Stop();

            videoSourcePlayer1.SignalToStop();
            //videoSourcePlayer2.SignalToStop();

            videoSourcePlayer1.WaitForStop();
            //videoSourcePlayer2.WaitForStop();
        }

        private void videoSourcePlayer1_NewFrame(object sender, ref Bitmap image)
        {
            if (captureFlag)
            {
                string rootPath = Application.StartupPath + "/photos/" + this.CourseTaskId.ToString();
                System.IO.DirectoryInfo di = new System.IO.DirectoryInfo(rootPath);
                di.Create();
                string imgPath = rootPath+"/" + GetStudentNo() + DateTime.Now.ToString("yyyyMMddhhmmss") + ".jpg";
                image.Save(imgPath);
                captureFlag = false;
                TssOpenStatus.Text = imgPath;

                //start
                string key = GetStudentNo();
                if (Photos.ContainsKey(key))
                {
                    Photos.Remove(key);
                } Photos.Add(key, imgPath);

            }

        }

        private string GetStudentNo()
        {
            return this.Students[this.studentIndex].No ;
        }

        private void BtnCapture_Click(object sender, EventArgs e)
        {
            captureFlag = true;
        }

        private void FrmCapture_Shown(object sender, EventArgs e)
        {
            if (videoDevices.Count > 0)
            {
                BtnStart_Click(null, null);
                studentIndex = 0;
                UpdateStudentInfo();
            }
        }

        private void BtnPrev_Click(object sender, EventArgs e)
        {
            if (studentIndex > 0)
            {
                studentIndex--;
                this.UpdateStudentInfo();
            }
        }

        private void BtnNext_Click(object sender, EventArgs e)
        {
            if (studentIndex < Students.Count -1)
            {
                studentIndex++;
                this.UpdateStudentInfo();
            }
        }

        private void BtnClose_Click(object sender, EventArgs e)
        {
            if (MessageBox.Show("作品拍照结束了吗?", "询问", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
            {
                Close();
            }
        }


    }
}
