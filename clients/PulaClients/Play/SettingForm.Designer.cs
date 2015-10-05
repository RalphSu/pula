namespace Play
{
    partial class SettingForm
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
            this.panel1 = new System.Windows.Forms.Panel();
            this.label4 = new System.Windows.Forms.Label();
            this.localFolderTB = new System.Windows.Forms.TextBox();
            this.settingSaveBtn = new System.Windows.Forms.Button();
            this.passwordText = new System.Windows.Forms.TextBox();
            this.teacherNameText = new System.Windows.Forms.TextBox();
            this.serverUrlText = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.label4);
            this.panel1.Controls.Add(this.localFolderTB);
            this.panel1.Controls.Add(this.settingSaveBtn);
            this.panel1.Controls.Add(this.passwordText);
            this.panel1.Controls.Add(this.teacherNameText);
            this.panel1.Controls.Add(this.serverUrlText);
            this.panel1.Controls.Add(this.label3);
            this.panel1.Controls.Add(this.label2);
            this.panel1.Controls.Add(this.label1);
            this.panel1.Location = new System.Drawing.Point(37, 12);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(629, 372);
            this.panel1.TabIndex = 0;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Font = new System.Drawing.Font("SimSun", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label4.Location = new System.Drawing.Point(36, 28);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(99, 20);
            this.label4.TabIndex = 8;
            this.label4.Text = "本地课件:";
            // 
            // localFolderTB
            // 
            this.localFolderTB.Location = new System.Drawing.Point(142, 23);
            this.localFolderTB.Name = "localFolderTB";
            this.localFolderTB.Size = new System.Drawing.Size(469, 25);
            this.localFolderTB.TabIndex = 7;
            // 
            // settingSaveBtn
            // 
            this.settingSaveBtn.Font = new System.Drawing.Font("SimSun", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.settingSaveBtn.Location = new System.Drawing.Point(142, 54);
            this.settingSaveBtn.Name = "settingSaveBtn";
            this.settingSaveBtn.Size = new System.Drawing.Size(114, 34);
            this.settingSaveBtn.TabIndex = 6;
            this.settingSaveBtn.Text = "保存";
            this.settingSaveBtn.UseVisualStyleBackColor = true;
            this.settingSaveBtn.Click += new System.EventHandler(this.settingSaveBtn_Click);
            // 
            // passwordText
            // 
            this.passwordText.Location = new System.Drawing.Point(142, 332);
            this.passwordText.Name = "passwordText";
            this.passwordText.Size = new System.Drawing.Size(190, 25);
            this.passwordText.TabIndex = 5;
            this.passwordText.Visible = false;
            // 
            // teacherNameText
            // 
            this.teacherNameText.Location = new System.Drawing.Point(143, 284);
            this.teacherNameText.Name = "teacherNameText";
            this.teacherNameText.Size = new System.Drawing.Size(189, 25);
            this.teacherNameText.TabIndex = 4;
            this.teacherNameText.Visible = false;
            this.teacherNameText.TextChanged += new System.EventHandler(this.textBox2_TextChanged);
            // 
            // serverUrlText
            // 
            this.serverUrlText.Location = new System.Drawing.Point(143, 239);
            this.serverUrlText.Name = "serverUrlText";
            this.serverUrlText.Size = new System.Drawing.Size(469, 25);
            this.serverUrlText.TabIndex = 3;
            this.serverUrlText.Visible = false;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("SimSun", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label3.Location = new System.Drawing.Point(37, 337);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(99, 20);
            this.label3.TabIndex = 2;
            this.label3.Text = "老师密码:";
            this.label3.Visible = false;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("SimSun", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label2.Location = new System.Drawing.Point(37, 289);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(99, 20);
            this.label2.TabIndex = 1;
            this.label2.Text = "老师用户:";
            this.label2.Visible = false;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("SimSun", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.label1.Location = new System.Drawing.Point(37, 245);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(79, 20);
            this.label1.TabIndex = 0;
            this.label1.Text = "服务器:";
            this.label1.Visible = false;
            // 
            // SettingForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(684, 117);
            this.Controls.Add(this.panel1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedToolWindow;
            this.Name = "SettingForm";
            this.Text = "设置";
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox passwordText;
        private System.Windows.Forms.TextBox teacherNameText;
        private System.Windows.Forms.TextBox serverUrlText;
        private System.Windows.Forms.Button settingSaveBtn;
        private System.Windows.Forms.TextBox localFolderTB;
        private System.Windows.Forms.Label label4;
    }
}