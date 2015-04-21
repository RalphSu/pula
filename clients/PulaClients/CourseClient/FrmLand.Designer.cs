namespace CourseClient
{
    partial class FrmLand
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FrmLand));
            this.swf = new AxShockwaveFlashObjects.AxShockwaveFlash();
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
            this.swf.Size = new System.Drawing.Size(502, 337);
            this.swf.TabIndex = 0;
            // 
            // FrmLand
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(502, 337);
            this.Controls.Add(this.swf);
            this.Name = "FrmLand";
            this.Text = "FrmLand";
            this.Shown += new System.EventHandler(this.FrmLand_Shown);
            ((System.ComponentModel.ISupportInitialize)(this.swf)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private AxShockwaveFlashObjects.AxShockwaveFlash swf;
    }
}