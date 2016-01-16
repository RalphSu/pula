namespace Play
{
    partial class CoursePlayForm
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(CoursePlayForm));
            this.swf = new AxShockwaveFlashObjects.AxShockwaveFlash();
            this.btnStart = new System.Windows.Forms.Button();
            this.timerPlay = new System.Windows.Forms.Timer(this.components);
            ((System.ComponentModel.ISupportInitialize)(this.swf)).BeginInit();
            this.SuspendLayout();
            // 
            // swf
            // 
            this.swf.Dock = System.Windows.Forms.DockStyle.Fill;
            this.swf.Enabled = true;
            this.swf.Location = new System.Drawing.Point(0, 0);
            this.swf.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.swf.Name = "swf";
            this.swf.OcxState = ((System.Windows.Forms.AxHost.State)(resources.GetObject("swf.OcxState")));
            this.swf.Size = new System.Drawing.Size(1249, 785);
            this.swf.TabIndex = 2;
            this.swf.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.swf_PreviewKeyDown);
            // 
            // btnStart
            // 
            this.btnStart.Location = new System.Drawing.Point(572, 516);
            this.btnStart.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.btnStart.Name = "btnStart";
            this.btnStart.Size = new System.Drawing.Size(112, 32);
            this.btnStart.TabIndex = 3;
            this.btnStart.Text = "btnStart";
            this.btnStart.UseVisualStyleBackColor = true;
            this.btnStart.Visible = false;
            this.btnStart.Click += new System.EventHandler(this.btnStart_Click);
            // 
            // CoursePlayForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 18F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSize = true;
            this.ClientSize = new System.Drawing.Size(1249, 785);
            this.Controls.Add(this.btnStart);
            this.Controls.Add(this.swf);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.Margin = new System.Windows.Forms.Padding(3, 4, 3, 4);
            this.Name = "CoursePlayForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "课件播放";
            this.WindowState = System.Windows.Forms.FormWindowState.Maximized;
            this.DoubleClick += new System.EventHandler(this.CoursePlayForm_DoubleClick);
            this.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.CoursePlayForm_KeyPress);
            this.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.CoursePlayForm_MouseDoubleClick);
            ((System.ComponentModel.ISupportInitialize)(this.swf)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private AxShockwaveFlashObjects.AxShockwaveFlash swf;
        private System.Windows.Forms.Button btnStart;
        private System.Windows.Forms.Timer timerPlay;
    }
}