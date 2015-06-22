namespace CourseClient
{
    partial class FrmActive
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FrmActive));
            this.lblMachineCode = new System.Windows.Forms.Label();
            this.tbMachineCode = new System.Windows.Forms.TextBox();
            this.btnRequestActive = new System.Windows.Forms.Button();
            this.btnSyncActive = new System.Windows.Forms.Button();
            this.lblActived = new System.Windows.Forms.Label();
            this.lblVersion = new System.Windows.Forms.Label();
            this.lblExpired = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // lblMachineCode
            // 
            this.lblMachineCode.AutoSize = true;
            this.lblMachineCode.Location = new System.Drawing.Point(31, 42);
            this.lblMachineCode.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.lblMachineCode.Name = "lblMachineCode";
            this.lblMachineCode.Size = new System.Drawing.Size(52, 15);
            this.lblMachineCode.TabIndex = 0;
            this.lblMachineCode.Text = "机器码";
            // 
            // tbMachineCode
            // 
            this.tbMachineCode.Location = new System.Drawing.Point(96, 38);
            this.tbMachineCode.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.tbMachineCode.Name = "tbMachineCode";
            this.tbMachineCode.ReadOnly = true;
            this.tbMachineCode.Size = new System.Drawing.Size(352, 25);
            this.tbMachineCode.TabIndex = 1;
            // 
            // btnRequestActive
            // 
            this.btnRequestActive.Location = new System.Drawing.Point(96, 85);
            this.btnRequestActive.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.btnRequestActive.Name = "btnRequestActive";
            this.btnRequestActive.Size = new System.Drawing.Size(100, 27);
            this.btnRequestActive.TabIndex = 2;
            this.btnRequestActive.Text = "申请激活";
            this.btnRequestActive.UseVisualStyleBackColor = true;
            this.btnRequestActive.Click += new System.EventHandler(this.btnRequestActive_Click);
            // 
            // btnSyncActive
            // 
            this.btnSyncActive.Location = new System.Drawing.Point(223, 85);
            this.btnSyncActive.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.btnSyncActive.Name = "btnSyncActive";
            this.btnSyncActive.Size = new System.Drawing.Size(128, 27);
            this.btnSyncActive.TabIndex = 3;
            this.btnSyncActive.Text = "同步激活信息";
            this.btnSyncActive.UseVisualStyleBackColor = true;
            this.btnSyncActive.Click += new System.EventHandler(this.btnSyncActive_Click);
            // 
            // lblActived
            // 
            this.lblActived.AutoSize = true;
            this.lblActived.Location = new System.Drawing.Point(384, 95);
            this.lblActived.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.lblActived.Name = "lblActived";
            this.lblActived.Size = new System.Drawing.Size(75, 15);
            this.lblActived.TabIndex = 4;
            this.lblActived.Text = "已经激活!";
            this.lblActived.Visible = false;
            // 
            // lblVersion
            // 
            this.lblVersion.AutoSize = true;
            this.lblVersion.Location = new System.Drawing.Point(435, 129);
            this.lblVersion.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.lblVersion.Name = "lblVersion";
            this.lblVersion.Size = new System.Drawing.Size(55, 15);
            this.lblVersion.TabIndex = 5;
            this.lblVersion.Text = "label1";
            // 
            // lblExpired
            // 
            this.lblExpired.AutoSize = true;
            this.lblExpired.ForeColor = System.Drawing.Color.DarkRed;
            this.lblExpired.Location = new System.Drawing.Point(92, 129);
            this.lblExpired.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.lblExpired.Name = "lblExpired";
            this.lblExpired.Size = new System.Drawing.Size(240, 15);
            this.lblExpired.TabIndex = 6;
            this.lblExpired.Text = "当前注册信息已经失效,请申请激活";
            // 
            // FrmActive
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(543, 179);
            this.Controls.Add(this.lblExpired);
            this.Controls.Add(this.lblVersion);
            this.Controls.Add(this.lblActived);
            this.Controls.Add(this.btnSyncActive);
            this.Controls.Add(this.btnRequestActive);
            this.Controls.Add(this.tbMachineCode);
            this.Controls.Add(this.lblMachineCode);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedToolWindow;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "FrmActive";
            this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "激活";
            this.Load += new System.EventHandler(this.FrmActive_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label lblMachineCode;
        private System.Windows.Forms.TextBox tbMachineCode;
        private System.Windows.Forms.Button btnRequestActive;
        private System.Windows.Forms.Button btnSyncActive;
        private System.Windows.Forms.Label lblActived;
        private System.Windows.Forms.Label lblVersion;
        private System.Windows.Forms.Label lblExpired;
    }
}