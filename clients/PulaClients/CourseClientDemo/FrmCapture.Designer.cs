namespace CourseClient
{
    partial class FrmCapture
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
            this.statusStrip1 = new System.Windows.Forms.StatusStrip();
            this.TssStatus = new System.Windows.Forms.ToolStripStatusLabel();
            this.TssOpenStatus = new System.Windows.Forms.ToolStripStatusLabel();
            this.BtnStart = new System.Windows.Forms.Button();
            this.BtnStop = new System.Windows.Forms.Button();
            this.videoSourcePlayer1 = new AForge.Controls.VideoSourcePlayer();
            this.BtnCapture = new System.Windows.Forms.Button();
            this.BtnPrev = new System.Windows.Forms.Button();
            this.BtnNext = new System.Windows.Forms.Button();
            this.BtnClose = new System.Windows.Forms.Button();
            this.statusStrip1.SuspendLayout();
            this.SuspendLayout();
            // 
            // statusStrip1
            // 
            this.statusStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.TssStatus,
            this.TssOpenStatus});
            this.statusStrip1.Location = new System.Drawing.Point(0, 381);
            this.statusStrip1.Name = "statusStrip1";
            this.statusStrip1.Size = new System.Drawing.Size(655, 22);
            this.statusStrip1.SizingGrip = false;
            this.statusStrip1.TabIndex = 0;
            this.statusStrip1.Text = "statusStrip1";
            // 
            // TssStatus
            // 
            this.TssStatus.Name = "TssStatus";
            this.TssStatus.Size = new System.Drawing.Size(131, 17);
            this.TssStatus.Text = "toolStripStatusLabel1";
            // 
            // TssOpenStatus
            // 
            this.TssOpenStatus.Name = "TssOpenStatus";
            this.TssOpenStatus.Size = new System.Drawing.Size(131, 17);
            this.TssOpenStatus.Text = "toolStripStatusLabel1";
            // 
            // BtnStart
            // 
            this.BtnStart.Location = new System.Drawing.Point(11, 343);
            this.BtnStart.Name = "BtnStart";
            this.BtnStart.Size = new System.Drawing.Size(75, 23);
            this.BtnStart.TabIndex = 1;
            this.BtnStart.Text = "开始";
            this.BtnStart.UseVisualStyleBackColor = true;
            this.BtnStart.Click += new System.EventHandler(this.BtnStart_Click);
            // 
            // BtnStop
            // 
            this.BtnStop.Location = new System.Drawing.Point(92, 343);
            this.BtnStop.Name = "BtnStop";
            this.BtnStop.Size = new System.Drawing.Size(75, 23);
            this.BtnStop.TabIndex = 2;
            this.BtnStop.Text = "停止";
            this.BtnStop.UseVisualStyleBackColor = true;
            this.BtnStop.Click += new System.EventHandler(this.BtnStop_Click);
            // 
            // videoSourcePlayer1
            // 
            this.videoSourcePlayer1.Location = new System.Drawing.Point(12, 12);
            this.videoSourcePlayer1.Name = "videoSourcePlayer1";
            this.videoSourcePlayer1.Size = new System.Drawing.Size(623, 314);
            this.videoSourcePlayer1.TabIndex = 3;
            this.videoSourcePlayer1.Text = "svideoSourcePlayer1";
            this.videoSourcePlayer1.VideoSource = null;
            this.videoSourcePlayer1.NewFrame += new AForge.Controls.VideoSourcePlayer.NewFrameHandler(this.videoSourcePlayer1_NewFrame);
            // 
            // BtnCapture
            // 
            this.BtnCapture.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.BtnCapture.ForeColor = System.Drawing.SystemColors.MenuHighlight;
            this.BtnCapture.Location = new System.Drawing.Point(411, 343);
            this.BtnCapture.Name = "BtnCapture";
            this.BtnCapture.Size = new System.Drawing.Size(75, 23);
            this.BtnCapture.TabIndex = 4;
            this.BtnCapture.Text = "拍照";
            this.BtnCapture.UseVisualStyleBackColor = true;
            this.BtnCapture.Click += new System.EventHandler(this.BtnCapture_Click);
            // 
            // BtnPrev
            // 
            this.BtnPrev.Location = new System.Drawing.Point(224, 343);
            this.BtnPrev.Name = "BtnPrev";
            this.BtnPrev.Size = new System.Drawing.Size(75, 23);
            this.BtnPrev.TabIndex = 5;
            this.BtnPrev.Text = "上一位学员";
            this.BtnPrev.UseVisualStyleBackColor = true;
            this.BtnPrev.Click += new System.EventHandler(this.BtnPrev_Click);
            // 
            // BtnNext
            // 
            this.BtnNext.Location = new System.Drawing.Point(316, 343);
            this.BtnNext.Name = "BtnNext";
            this.BtnNext.Size = new System.Drawing.Size(75, 23);
            this.BtnNext.TabIndex = 6;
            this.BtnNext.Text = "下一位学员";
            this.BtnNext.UseVisualStyleBackColor = true;
            this.BtnNext.Click += new System.EventHandler(this.BtnNext_Click);
            // 
            // BtnClose
            // 
            this.BtnClose.Location = new System.Drawing.Point(560, 343);
            this.BtnClose.Name = "BtnClose";
            this.BtnClose.Size = new System.Drawing.Size(75, 23);
            this.BtnClose.TabIndex = 8;
            this.BtnClose.Text = "结束(&E)";
            this.BtnClose.UseVisualStyleBackColor = true;
            this.BtnClose.Click += new System.EventHandler(this.BtnClose_Click);
            // 
            // FrmCapture
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(655, 403);
            this.Controls.Add(this.BtnClose);
            this.Controls.Add(this.BtnNext);
            this.Controls.Add(this.BtnPrev);
            this.Controls.Add(this.BtnCapture);
            this.Controls.Add(this.videoSourcePlayer1);
            this.Controls.Add(this.BtnStop);
            this.Controls.Add(this.BtnStart);
            this.Controls.Add(this.statusStrip1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "FrmCapture";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.FrmCapture_FormClosing);
            this.Load += new System.EventHandler(this.FrmCapture_Load);
            this.Shown += new System.EventHandler(this.FrmCapture_Shown);
            this.statusStrip1.ResumeLayout(false);
            this.statusStrip1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.StatusStrip statusStrip1;
        private System.Windows.Forms.ToolStripStatusLabel TssStatus;
        private System.Windows.Forms.Button BtnStart;
        private System.Windows.Forms.Button BtnStop;
        private System.Windows.Forms.ToolStripStatusLabel TssOpenStatus;
        private AForge.Controls.VideoSourcePlayer videoSourcePlayer1;
        private System.Windows.Forms.Button BtnCapture;
        private System.Windows.Forms.Button BtnPrev;
        private System.Windows.Forms.Button BtnNext;
        private System.Windows.Forms.Button BtnClose;
    }
}