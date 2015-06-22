namespace CourseClient
{
    partial class FrmLoading
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
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FrmLoading));
            this.swf = new AxShockwaveFlashObjects.AxShockwaveFlash();
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.timerFinish = new System.Windows.Forms.Timer(this.components);
            this.timerRead = new System.Windows.Forms.Timer(this.components);
            ((System.ComponentModel.ISupportInitialize)(this.swf)).BeginInit();
            this.SuspendLayout();
            // 
            // swf
            // 
            this.swf.Dock = System.Windows.Forms.DockStyle.Fill;
            this.swf.Enabled = true;
            this.swf.Location = new System.Drawing.Point(0, 0);
            this.swf.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.swf.Name = "swf";
            this.swf.OcxState = ((System.Windows.Forms.AxHost.State)(resources.GetObject("swf.OcxState")));
            this.swf.Size = new System.Drawing.Size(1040, 563);
            this.swf.TabIndex = 0;
            this.swf.OnProgress += new AxShockwaveFlashObjects._IShockwaveFlashEvents_OnProgressEventHandler(this.swf_OnProgress);
            this.swf.FlashCall += new AxShockwaveFlashObjects._IShockwaveFlashEvents_FlashCallEventHandler(this.swf_FlashCall);
            // 
            // timer1
            // 
            this.timer1.Enabled = true;
            this.timer1.Interval = 500;
            this.timer1.Tick += new System.EventHandler(this.timer1_Tick);
            // 
            // timerFinish
            // 
            this.timerFinish.Tick += new System.EventHandler(this.timerFinish_Tick);
            // 
            // timerRead
            // 
            this.timerRead.Interval = 500;
            this.timerRead.Tick += new System.EventHandler(this.timerRead_Tick);
            // 
            // FrmLoading
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1040, 563);
            this.Controls.Add(this.swf);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.KeyPreview = true;
            this.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.Name = "FrmLoading";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Loading...";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.FrmLoading_FormClosed);
            this.Load += new System.EventHandler(this.FrmLoading_Load);
            this.Shown += new System.EventHandler(this.FrmLoading_Shown);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.FrmLoading_KeyDown);
            ((System.ComponentModel.ISupportInitialize)(this.swf)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private AxShockwaveFlashObjects.AxShockwaveFlash swf;
        private System.Windows.Forms.Timer timer1;
        private System.Windows.Forms.Timer timerFinish;
        private System.Windows.Forms.Timer timerRead;
    }
}

