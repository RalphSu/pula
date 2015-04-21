namespace CourseClient
{
    partial class FrmCourse
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
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FrmCourse));
            this.swf = new AxShockwaveFlashObjects.AxShockwaveFlash();
            this.timerEncypt = new System.Windows.Forms.Timer(this.components);
            this.btnStart = new System.Windows.Forms.Button();
            this.timerClose = new System.Windows.Forms.Timer(this.components);
            ((System.ComponentModel.ISupportInitialize)(this.swf)).BeginInit();
            this.SuspendLayout();
            // 
            // swf
            // 
            this.swf.Dock = System.Windows.Forms.DockStyle.Fill;
            this.swf.Enabled = true;
            this.swf.Location = new System.Drawing.Point(0, 0);
            this.swf.Name = "swf";
            this.swf.OcxState = ((System.Windows.Forms.AxHost.State)(resources.GetObject("swf.OcxState")));
            this.swf.Size = new System.Drawing.Size(467, 375);
            this.swf.TabIndex = 1;
            this.swf.OnProgress += new AxShockwaveFlashObjects._IShockwaveFlashEvents_OnProgressEventHandler(this.swf_OnProgress);
            this.swf.FlashCall += new AxShockwaveFlashObjects._IShockwaveFlashEvents_FlashCallEventHandler(this.swf_FlashCall);
            // 
            // timerEncypt
            // 
            this.timerEncypt.Tick += new System.EventHandler(this.timerEncypt_Tick);
            // 
            // btnStart
            // 
            this.btnStart.Location = new System.Drawing.Point(297, 60);
            this.btnStart.Name = "btnStart";
            this.btnStart.Size = new System.Drawing.Size(75, 23);
            this.btnStart.TabIndex = 2;
            this.btnStart.Text = "btnStart";
            this.btnStart.UseVisualStyleBackColor = true;
            this.btnStart.Visible = false;
            this.btnStart.Click += new System.EventHandler(this.btnStart_Click);
            // 
            // timerClose
            // 
            this.timerClose.Tick += new System.EventHandler(this.timerClose_Tick);
            // 
            // FrmCourse
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(467, 375);
            this.Controls.Add(this.btnStart);
            this.Controls.Add(this.swf);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.Name = "FrmCourse";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "FrmCourse";
            this.WindowState = System.Windows.Forms.FormWindowState.Maximized;
            this.Load += new System.EventHandler(this.FrmCourse_Load);
            ((System.ComponentModel.ISupportInitialize)(this.swf)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private AxShockwaveFlashObjects.AxShockwaveFlash swf;
        private System.Windows.Forms.Timer timerEncypt;
        private System.Windows.Forms.Button btnStart;
        private System.Windows.Forms.Timer timerClose;
    }
}