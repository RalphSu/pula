namespace CourseClient
{
    partial class FrmMain
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.pb = new System.Windows.Forms.ProgressBar();
            this.tbLog = new System.Windows.Forms.TextBox();
            this.btnSyncCourse = new System.Windows.Forms.Button();
            this.btnSyncCourseTask = new System.Windows.Forms.Button();
            this.BtnCheckCourse = new System.Windows.Forms.Button();
            this.lblSyncLeft = new System.Windows.Forms.Label();
            this.btnCourseStart = new System.Windows.Forms.Button();
            this.btnCourseEnd = new System.Windows.Forms.Button();
            this.BtnSetup = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // pb
            // 
            this.pb.Location = new System.Drawing.Point(12, 232);
            this.pb.Name = "pb";
            this.pb.Size = new System.Drawing.Size(696, 23);
            this.pb.TabIndex = 0;
            // 
            // tbLog
            // 
            this.tbLog.Location = new System.Drawing.Point(12, 57);
            this.tbLog.Multiline = true;
            this.tbLog.Name = "tbLog";
            this.tbLog.ReadOnly = true;
            this.tbLog.Size = new System.Drawing.Size(696, 160);
            this.tbLog.TabIndex = 1;
            // 
            // btnSyncCourse
            // 
            this.btnSyncCourse.Location = new System.Drawing.Point(279, 12);
            this.btnSyncCourse.Name = "btnSyncCourse";
            this.btnSyncCourse.Size = new System.Drawing.Size(75, 23);
            this.btnSyncCourse.TabIndex = 2;
            this.btnSyncCourse.Text = "同步课程(&C)";
            this.btnSyncCourse.UseVisualStyleBackColor = true;
            this.btnSyncCourse.Click += new System.EventHandler(this.btnSyncCourse_Click);
            // 
            // btnSyncCourseTask
            // 
            this.btnSyncCourseTask.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.btnSyncCourseTask.ForeColor = System.Drawing.Color.Green;
            this.btnSyncCourseTask.Location = new System.Drawing.Point(12, 12);
            this.btnSyncCourseTask.Name = "btnSyncCourseTask";
            this.btnSyncCourseTask.Size = new System.Drawing.Size(94, 23);
            this.btnSyncCourseTask.TabIndex = 3;
            this.btnSyncCourseTask.Text = "同步课程任务(&C)";
            this.btnSyncCourseTask.UseVisualStyleBackColor = true;
            this.btnSyncCourseTask.Click += new System.EventHandler(this.btnSyncCourseTask_Click);
            // 
            // BtnCheckCourse
            // 
            this.BtnCheckCourse.Location = new System.Drawing.Point(384, 12);
            this.BtnCheckCourse.Name = "BtnCheckCourse";
            this.BtnCheckCourse.Size = new System.Drawing.Size(125, 23);
            this.BtnCheckCourse.TabIndex = 4;
            this.BtnCheckCourse.Text = "检查课程资源(&R)";
            this.BtnCheckCourse.UseVisualStyleBackColor = true;
            this.BtnCheckCourse.Click += new System.EventHandler(this.BtnCheckCourse_Click);
            // 
            // lblSyncLeft
            // 
            this.lblSyncLeft.AutoSize = true;
            this.lblSyncLeft.Location = new System.Drawing.Point(119, 19);
            this.lblSyncLeft.Name = "lblSyncLeft";
            this.lblSyncLeft.Size = new System.Drawing.Size(35, 13);
            this.lblSyncLeft.TabIndex = 5;
            this.lblSyncLeft.Text = "label1";
            // 
            // btnCourseStart
            // 
            this.btnCourseStart.Location = new System.Drawing.Point(542, 179);
            this.btnCourseStart.Name = "btnCourseStart";
            this.btnCourseStart.Size = new System.Drawing.Size(69, 23);
            this.btnCourseStart.TabIndex = 6;
            this.btnCourseStart.Text = "开始[模拟]";
            this.btnCourseStart.UseVisualStyleBackColor = true;
            this.btnCourseStart.Click += new System.EventHandler(this.btnCourseStart_Click);
            // 
            // btnCourseEnd
            // 
            this.btnCourseEnd.Location = new System.Drawing.Point(629, 179);
            this.btnCourseEnd.Name = "btnCourseEnd";
            this.btnCourseEnd.Size = new System.Drawing.Size(69, 23);
            this.btnCourseEnd.TabIndex = 7;
            this.btnCourseEnd.Text = "结束[模拟]";
            this.btnCourseEnd.UseVisualStyleBackColor = true;
            this.btnCourseEnd.Click += new System.EventHandler(this.btnCourseEnd_Click);
            // 
            // BtnSetup
            // 
            this.BtnSetup.Location = new System.Drawing.Point(633, 12);
            this.BtnSetup.Name = "BtnSetup";
            this.BtnSetup.Size = new System.Drawing.Size(75, 23);
            this.BtnSetup.TabIndex = 8;
            this.BtnSetup.Text = "设置(&S)";
            this.BtnSetup.UseVisualStyleBackColor = true;
            this.BtnSetup.Click += new System.EventHandler(this.BtnSetup_Click);
            // 
            // FrmMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(720, 267);
            this.Controls.Add(this.BtnSetup);
            this.Controls.Add(this.btnCourseEnd);
            this.Controls.Add(this.btnCourseStart);
            this.Controls.Add(this.lblSyncLeft);
            this.Controls.Add(this.BtnCheckCourse);
            this.Controls.Add(this.btnSyncCourseTask);
            this.Controls.Add(this.btnSyncCourse);
            this.Controls.Add(this.tbLog);
            this.Controls.Add(this.pb);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedToolWindow;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "FrmMain";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "同步数据";
            this.Load += new System.EventHandler(this.FrmMain_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ProgressBar pb;
        private System.Windows.Forms.TextBox tbLog;
        private System.Windows.Forms.Button btnSyncCourse;
        private System.Windows.Forms.Button btnSyncCourseTask;
        private System.Windows.Forms.Button BtnCheckCourse;
        private System.Windows.Forms.Label lblSyncLeft;
        private System.Windows.Forms.Button btnCourseStart;
        private System.Windows.Forms.Button btnCourseEnd;
        private System.Windows.Forms.Button BtnSetup;
    }
}