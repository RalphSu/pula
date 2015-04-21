namespace CourseClient
{
    partial class FrmDemo
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.BtnMachineNo = new System.Windows.Forms.Button();
            this.txtLog = new System.Windows.Forms.TextBox();
            this.button1 = new System.Windows.Forms.Button();
            this.BtnSyncActive = new System.Windows.Forms.Button();
            this.BtnChangePassword = new System.Windows.Forms.Button();
            this.BtnStart = new System.Windows.Forms.Button();
            this.BtnSyncCourse = new System.Windows.Forms.Button();
            this.BtnCourseAll = new System.Windows.Forms.Button();
            this.BtnStartCourseTask = new System.Windows.Forms.Button();
            this.BtnCloseCourseTask = new System.Windows.Forms.Button();
            this.BtnCapture = new System.Windows.Forms.Button();
            this.btnTestJson = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // BtnMachineNo
            // 
            this.BtnMachineNo.Location = new System.Drawing.Point(12, 12);
            this.BtnMachineNo.Name = "BtnMachineNo";
            this.BtnMachineNo.Size = new System.Drawing.Size(75, 23);
            this.BtnMachineNo.TabIndex = 0;
            this.BtnMachineNo.Text = "生成机器码";
            this.BtnMachineNo.UseVisualStyleBackColor = true;
            this.BtnMachineNo.Click += new System.EventHandler(this.BtnMachineNo_Click);
            // 
            // txtLog
            // 
            this.txtLog.Location = new System.Drawing.Point(12, 138);
            this.txtLog.Multiline = true;
            this.txtLog.Name = "txtLog";
            this.txtLog.ReadOnly = true;
            this.txtLog.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.txtLog.Size = new System.Drawing.Size(652, 208);
            this.txtLog.TabIndex = 1;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(102, 12);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 2;
            this.button1.Text = "申请激活(&A)";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // BtnSyncActive
            // 
            this.BtnSyncActive.Location = new System.Drawing.Point(192, 12);
            this.BtnSyncActive.Name = "BtnSyncActive";
            this.BtnSyncActive.Size = new System.Drawing.Size(89, 23);
            this.BtnSyncActive.TabIndex = 3;
            this.BtnSyncActive.Text = "同步激活信息";
            this.BtnSyncActive.UseVisualStyleBackColor = true;
            this.BtnSyncActive.Click += new System.EventHandler(this.BtnSyncActive_Click);
            // 
            // BtnChangePassword
            // 
            this.BtnChangePassword.Location = new System.Drawing.Point(558, 12);
            this.BtnChangePassword.Name = "BtnChangePassword";
            this.BtnChangePassword.Size = new System.Drawing.Size(106, 23);
            this.BtnChangePassword.TabIndex = 4;
            this.BtnChangePassword.Text = "修改数据库密码";
            this.BtnChangePassword.UseVisualStyleBackColor = true;
            this.BtnChangePassword.Click += new System.EventHandler(this.BtnChangePassword_Click);
            // 
            // BtnStart
            // 
            this.BtnStart.Location = new System.Drawing.Point(463, 12);
            this.BtnStart.Name = "BtnStart";
            this.BtnStart.Size = new System.Drawing.Size(89, 23);
            this.BtnStart.TabIndex = 5;
            this.BtnStart.Text = "开始课程";
            this.BtnStart.UseVisualStyleBackColor = true;
            // 
            // BtnSyncCourse
            // 
            this.BtnSyncCourse.Location = new System.Drawing.Point(298, 12);
            this.BtnSyncCourse.Name = "BtnSyncCourse";
            this.BtnSyncCourse.Size = new System.Drawing.Size(89, 23);
            this.BtnSyncCourse.TabIndex = 6;
            this.BtnSyncCourse.Text = "同步课程";
            this.BtnSyncCourse.UseVisualStyleBackColor = true;
            this.BtnSyncCourse.Click += new System.EventHandler(this.BtnSyncCourse_Click);
            // 
            // BtnCourseAll
            // 
            this.BtnCourseAll.Location = new System.Drawing.Point(192, 42);
            this.BtnCourseAll.Name = "BtnCourseAll";
            this.BtnCourseAll.Size = new System.Drawing.Size(89, 23);
            this.BtnCourseAll.TabIndex = 7;
            this.BtnCourseAll.Text = "显示所有课程";
            this.BtnCourseAll.UseVisualStyleBackColor = true;
            this.BtnCourseAll.Click += new System.EventHandler(this.BtnCourseAll_Click);
            // 
            // BtnStartCourseTask
            // 
            this.BtnStartCourseTask.Location = new System.Drawing.Point(298, 42);
            this.BtnStartCourseTask.Name = "BtnStartCourseTask";
            this.BtnStartCourseTask.Size = new System.Drawing.Size(89, 23);
            this.BtnStartCourseTask.TabIndex = 8;
            this.BtnStartCourseTask.Text = "开始课程任务";
            this.BtnStartCourseTask.UseVisualStyleBackColor = true;
            this.BtnStartCourseTask.Click += new System.EventHandler(this.button2_Click);
            // 
            // BtnCloseCourseTask
            // 
            this.BtnCloseCourseTask.Location = new System.Drawing.Point(402, 42);
            this.BtnCloseCourseTask.Name = "BtnCloseCourseTask";
            this.BtnCloseCourseTask.Size = new System.Drawing.Size(89, 23);
            this.BtnCloseCourseTask.TabIndex = 9;
            this.BtnCloseCourseTask.Text = "结束课程任务";
            this.BtnCloseCourseTask.UseVisualStyleBackColor = true;
            this.BtnCloseCourseTask.Click += new System.EventHandler(this.BtnCloseCourseTask_Click);
            // 
            // BtnCapture
            // 
            this.BtnCapture.Location = new System.Drawing.Point(558, 42);
            this.BtnCapture.Name = "BtnCapture";
            this.BtnCapture.Size = new System.Drawing.Size(106, 23);
            this.BtnCapture.TabIndex = 10;
            this.BtnCapture.Text = "保存照片";
            this.BtnCapture.UseVisualStyleBackColor = true;
            this.BtnCapture.Click += new System.EventHandler(this.BtnCapture_Click);
            // 
            // btnTestJson
            // 
            this.btnTestJson.Location = new System.Drawing.Point(12, 68);
            this.btnTestJson.Name = "btnTestJson";
            this.btnTestJson.Size = new System.Drawing.Size(75, 23);
            this.btnTestJson.TabIndex = 11;
            this.btnTestJson.Text = "Json测试";
            this.btnTestJson.UseVisualStyleBackColor = true;
            this.btnTestJson.Click += new System.EventHandler(this.btnTestJson_Click);
            // 
            // FrmDemo
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(684, 358);
            this.Controls.Add(this.btnTestJson);
            this.Controls.Add(this.BtnCapture);
            this.Controls.Add(this.BtnCloseCourseTask);
            this.Controls.Add(this.BtnStartCourseTask);
            this.Controls.Add(this.BtnCourseAll);
            this.Controls.Add(this.BtnSyncCourse);
            this.Controls.Add(this.BtnStart);
            this.Controls.Add(this.BtnChangePassword);
            this.Controls.Add(this.BtnSyncActive);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.txtLog);
            this.Controls.Add(this.BtnMachineNo);
            this.Name = "FrmDemo";
            this.Text = "DemoForm";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button BtnMachineNo;
        private System.Windows.Forms.TextBox txtLog;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button BtnSyncActive;
        private System.Windows.Forms.Button BtnChangePassword;
        private System.Windows.Forms.Button BtnStart;
        private System.Windows.Forms.Button BtnSyncCourse;
        private System.Windows.Forms.Button BtnCourseAll;
        private System.Windows.Forms.Button BtnStartCourseTask;
        private System.Windows.Forms.Button BtnCloseCourseTask;
        private System.Windows.Forms.Button BtnCapture;
        private System.Windows.Forms.Button btnTestJson;

    }
}

