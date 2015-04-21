namespace SendCard
{
    partial class FrmSetup
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
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.TbUsername = new System.Windows.Forms.TextBox();
            this.TbPassword = new System.Windows.Forms.TextBox();
            this.BtnOK = new System.Windows.Forms.Button();
            this.CbCom = new System.Windows.Forms.ComboBox();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 20);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(31, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "串口";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(12, 60);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(46, 13);
            this.label2.TabIndex = 1;
            this.label2.Text = "用户名:";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(12, 92);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(34, 13);
            this.label3.TabIndex = 2;
            this.label3.Text = "密码:";
            // 
            // TbUsername
            // 
            this.TbUsername.Location = new System.Drawing.Point(80, 57);
            this.TbUsername.Name = "TbUsername";
            this.TbUsername.Size = new System.Drawing.Size(175, 20);
            this.TbUsername.TabIndex = 4;
            // 
            // TbPassword
            // 
            this.TbPassword.Location = new System.Drawing.Point(80, 92);
            this.TbPassword.Name = "TbPassword";
            this.TbPassword.PasswordChar = '*';
            this.TbPassword.Size = new System.Drawing.Size(175, 20);
            this.TbPassword.TabIndex = 5;
            // 
            // BtnOK
            // 
            this.BtnOK.Location = new System.Drawing.Point(80, 129);
            this.BtnOK.Name = "BtnOK";
            this.BtnOK.Size = new System.Drawing.Size(75, 23);
            this.BtnOK.TabIndex = 6;
            this.BtnOK.Text = "确定(&S)";
            this.BtnOK.UseVisualStyleBackColor = true;
            this.BtnOK.Click += new System.EventHandler(this.BtnOK_Click);
            // 
            // CbCom
            // 
            this.CbCom.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.CbCom.FormattingEnabled = true;
            this.CbCom.Items.AddRange(new object[] {
            "COM1",
            "COM2",
            "COM3",
            "COM4",
            "COM5",
            "COM6",
            "COM7",
            "COM8"});
            this.CbCom.Location = new System.Drawing.Point(80, 17);
            this.CbCom.Name = "CbCom";
            this.CbCom.Size = new System.Drawing.Size(121, 21);
            this.CbCom.TabIndex = 7;
            // 
            // FrmSetup
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(284, 164);
            this.Controls.Add(this.CbCom);
            this.Controls.Add(this.BtnOK);
            this.Controls.Add(this.TbPassword);
            this.Controls.Add(this.TbUsername);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "FrmSetup";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "设置";
            this.Load += new System.EventHandler(this.FrmSetup_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox TbUsername;
        private System.Windows.Forms.TextBox TbPassword;
        private System.Windows.Forms.Button BtnOK;
        private System.Windows.Forms.ComboBox CbCom;
    }
}