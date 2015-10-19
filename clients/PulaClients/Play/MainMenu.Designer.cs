namespace Play
{
    partial class MainMenu
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
            this.downloadCourseBtn = new System.Windows.Forms.Button();
            this.serverSettingBtn = new System.Windows.Forms.Button();
            this.localCourseBtn = new System.Windows.Forms.Button();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.dataGridView1 = new System.Windows.Forms.DataGridView();
            this.CourseName = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.UpdateTime = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.aboutUsBtn = new System.Windows.Forms.Button();
            this.groupBox1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
            this.SuspendLayout();
            // 
            // downloadCourseBtn
            // 
            this.downloadCourseBtn.Font = new System.Drawing.Font("SimSun", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.downloadCourseBtn.Location = new System.Drawing.Point(201, 12);
            this.downloadCourseBtn.Name = "downloadCourseBtn";
            this.downloadCourseBtn.Size = new System.Drawing.Size(147, 42);
            this.downloadCourseBtn.TabIndex = 0;
            this.downloadCourseBtn.Text = "制作课程";
            this.downloadCourseBtn.UseVisualStyleBackColor = true;
            this.downloadCourseBtn.Click += new System.EventHandler(this.downloadCourseBtn_Click);
            // 
            // serverSettingBtn
            // 
            this.serverSettingBtn.Font = new System.Drawing.Font("SimSun", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.serverSettingBtn.Location = new System.Drawing.Point(29, 12);
            this.serverSettingBtn.Name = "serverSettingBtn";
            this.serverSettingBtn.Size = new System.Drawing.Size(147, 42);
            this.serverSettingBtn.TabIndex = 1;
            this.serverSettingBtn.Text = "设置";
            this.serverSettingBtn.UseVisualStyleBackColor = true;
            this.serverSettingBtn.Click += new System.EventHandler(this.serverSettingBtn_Click);
            // 
            // localCourseBtn
            // 
            this.localCourseBtn.Font = new System.Drawing.Font("SimSun", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.localCourseBtn.Location = new System.Drawing.Point(20, 24);
            this.localCourseBtn.Name = "localCourseBtn";
            this.localCourseBtn.Size = new System.Drawing.Size(141, 41);
            this.localCourseBtn.TabIndex = 2;
            this.localCourseBtn.Text = "查看本地课程";
            this.localCourseBtn.UseVisualStyleBackColor = true;
            this.localCourseBtn.Click += new System.EventHandler(this.localCourseBtn_Click);
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.dataGridView1);
            this.groupBox1.Controls.Add(this.localCourseBtn);
            this.groupBox1.Location = new System.Drawing.Point(29, 71);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(819, 571);
            this.groupBox1.TabIndex = 3;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "课程播放";
            // 
            // dataGridView1
            // 
            this.dataGridView1.BackgroundColor = System.Drawing.SystemColors.Control;
            this.dataGridView1.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.dataGridView1.ColumnHeadersHeight = 35;
            this.dataGridView1.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.CourseName,
            this.UpdateTime});
            this.dataGridView1.Location = new System.Drawing.Point(20, 87);
            this.dataGridView1.MultiSelect = false;
            this.dataGridView1.Name = "dataGridView1";
            this.dataGridView1.ReadOnly = true;
            this.dataGridView1.RowTemplate.Height = 35;
            this.dataGridView1.RowTemplate.ReadOnly = true;
            this.dataGridView1.Size = new System.Drawing.Size(783, 478);
            this.dataGridView1.TabIndex = 3;
            this.dataGridView1.CellDoubleClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.dataGridView1_CellDoubleClick);
            // 
            // CourseName
            // 
            this.CourseName.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.CourseName.HeaderText = "课程名字";
            this.CourseName.Name = "CourseName";
            this.CourseName.ReadOnly = true;
            // 
            // UpdateTime
            // 
            this.UpdateTime.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.UpdateTime.HeaderText = "添加时间";
            this.UpdateTime.Name = "UpdateTime";
            this.UpdateTime.ReadOnly = true;
            // 
            // aboutUsBtn
            // 
            this.aboutUsBtn.Location = new System.Drawing.Point(369, 13);
            this.aboutUsBtn.Name = "aboutUsBtn";
            this.aboutUsBtn.Size = new System.Drawing.Size(132, 41);
            this.aboutUsBtn.TabIndex = 4;
            this.aboutUsBtn.Text = "关于...";
            this.aboutUsBtn.UseVisualStyleBackColor = true;
            this.aboutUsBtn.Click += new System.EventHandler(this.aboutUsBtn_Click);
            // 
            // MainMenu
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(875, 670);
            this.Controls.Add(this.aboutUsBtn);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.downloadCourseBtn);
            this.Controls.Add(this.serverSettingBtn);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedToolWindow;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "MainMenu";
            this.Text = "普拉星球开课客户端";
            this.groupBox1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button downloadCourseBtn;
        private System.Windows.Forms.Button serverSettingBtn;
        private System.Windows.Forms.Button localCourseBtn;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.DataGridView dataGridView1;
        private System.Windows.Forms.DataGridViewTextBoxColumn CourseName;
        private System.Windows.Forms.DataGridViewTextBoxColumn UpdateTime;
        private System.Windows.Forms.Button aboutUsBtn;
    }
}

