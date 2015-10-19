namespace SendCard
{
    partial class FrmMain
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FrmMain));
            this.BtnWrite = new System.Windows.Forms.Button();
            this.BtnRead = new System.Windows.Forms.Button();
            this.BtnSetup = new System.Windows.Forms.Button();
            this.statusStrip1 = new System.Windows.Forms.StatusStrip();
            this.TssComPort = new System.Windows.Forms.ToolStripStatusLabel();
            this.toolStripStatusLabel1 = new System.Windows.Forms.ToolStripStatusLabel();
            this.TssCardRfid = new System.Windows.Forms.ToolStripStatusLabel();
            this.LblNo = new System.Windows.Forms.Label();
            this.LblName = new System.Windows.Forms.Label();
            this.LblType = new System.Windows.Forms.Label();
            this.BtnInitCard = new System.Windows.Forms.Button();
            this.TbCardId = new System.Windows.Forms.TextBox();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.baseInfoLB = new System.Windows.Forms.Label();
            this.coursesGridView = new System.Windows.Forms.DataGridView();
            this.button1 = new System.Windows.Forms.Button();
            this.addUsageBtn = new System.Windows.Forms.Button();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.panel1 = new System.Windows.Forms.Panel();
            this.courseRB = new System.Windows.Forms.RadioButton();
            this.gongfangRB = new System.Windows.Forms.RadioButton();
            this.specialCourseRB = new System.Windows.Forms.RadioButton();
            this.huodongUsageRB = new System.Windows.Forms.RadioButton();
            this.usageStatusLb = new System.Windows.Forms.TextBox();
            this.statusStrip1.SuspendLayout();
            this.groupBox1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.coursesGridView)).BeginInit();
            this.groupBox2.SuspendLayout();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // BtnWrite
            // 
            this.BtnWrite.Location = new System.Drawing.Point(25, 23);
            this.BtnWrite.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.BtnWrite.Name = "BtnWrite";
            this.BtnWrite.Size = new System.Drawing.Size(310, 28);
            this.BtnWrite.TabIndex = 0;
            this.BtnWrite.Text = "写卡信息";
            this.BtnWrite.UseVisualStyleBackColor = true;
            this.BtnWrite.Click += new System.EventHandler(this.BtnWrite_Click);
            // 
            // BtnRead
            // 
            this.BtnRead.Font = new System.Drawing.Font("SimSun", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.BtnRead.Location = new System.Drawing.Point(25, 57);
            this.BtnRead.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.BtnRead.Name = "BtnRead";
            this.BtnRead.Size = new System.Drawing.Size(309, 56);
            this.BtnRead.TabIndex = 0;
            this.BtnRead.Text = "读卡信息";
            this.BtnRead.UseVisualStyleBackColor = true;
            this.BtnRead.Click += new System.EventHandler(this.BtnRead_Click);
            // 
            // BtnSetup
            // 
            this.BtnSetup.Location = new System.Drawing.Point(53, 12);
            this.BtnSetup.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.BtnSetup.Name = "BtnSetup";
            this.BtnSetup.Size = new System.Drawing.Size(310, 33);
            this.BtnSetup.TabIndex = 0;
            this.BtnSetup.Text = "读卡器和用户配置";
            this.BtnSetup.UseVisualStyleBackColor = true;
            this.BtnSetup.Click += new System.EventHandler(this.BtnSetup_Click);
            // 
            // statusStrip1
            // 
            this.statusStrip1.ImageScalingSize = new System.Drawing.Size(20, 20);
            this.statusStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.TssComPort,
            this.toolStripStatusLabel1,
            this.TssCardRfid});
            this.statusStrip1.Location = new System.Drawing.Point(0, 667);
            this.statusStrip1.Name = "statusStrip1";
            this.statusStrip1.Padding = new System.Windows.Forms.Padding(1, 0, 19, 0);
            this.statusStrip1.Size = new System.Drawing.Size(1754, 25);
            this.statusStrip1.SizingGrip = false;
            this.statusStrip1.TabIndex = 1;
            this.statusStrip1.Text = "statusStrip1";
            // 
            // TssComPort
            // 
            this.TssComPort.Name = "TssComPort";
            this.TssComPort.Size = new System.Drawing.Size(167, 20);
            this.TssComPort.Text = "toolStripStatusLabel1";
            // 
            // toolStripStatusLabel1
            // 
            this.toolStripStatusLabel1.Name = "toolStripStatusLabel1";
            this.toolStripStatusLabel1.Size = new System.Drawing.Size(54, 20);
            this.toolStripStatusLabel1.Text = "卡标识";
            // 
            // TssCardRfid
            // 
            this.TssCardRfid.Name = "TssCardRfid";
            this.TssCardRfid.Size = new System.Drawing.Size(167, 20);
            this.TssCardRfid.Text = "toolStripStatusLabel2";
            // 
            // LblNo
            // 
            this.LblNo.AutoSize = true;
            this.LblNo.Location = new System.Drawing.Point(16, 14);
            this.LblNo.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.LblNo.Name = "LblNo";
            this.LblNo.Size = new System.Drawing.Size(55, 15);
            this.LblNo.TabIndex = 2;
            this.LblNo.Text = "label1";
            // 
            // LblName
            // 
            this.LblName.AutoSize = true;
            this.LblName.Location = new System.Drawing.Point(16, 43);
            this.LblName.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.LblName.Name = "LblName";
            this.LblName.Size = new System.Drawing.Size(55, 15);
            this.LblName.TabIndex = 3;
            this.LblName.Text = "label2";
            // 
            // LblType
            // 
            this.LblType.AutoSize = true;
            this.LblType.Location = new System.Drawing.Point(16, 78);
            this.LblType.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.LblType.Name = "LblType";
            this.LblType.Size = new System.Drawing.Size(55, 15);
            this.LblType.TabIndex = 4;
            this.LblType.Text = "label3";
            // 
            // BtnInitCard
            // 
            this.BtnInitCard.Location = new System.Drawing.Point(25, 119);
            this.BtnInitCard.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.BtnInitCard.Name = "BtnInitCard";
            this.BtnInitCard.Size = new System.Drawing.Size(310, 27);
            this.BtnInitCard.TabIndex = 5;
            this.BtnInitCard.Text = "初始化卡片(&I)";
            this.BtnInitCard.UseVisualStyleBackColor = true;
            this.BtnInitCard.Click += new System.EventHandler(this.BtnInitCard_Click);
            // 
            // TbCardId
            // 
            this.TbCardId.Location = new System.Drawing.Point(25, 168);
            this.TbCardId.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.TbCardId.Name = "TbCardId";
            this.TbCardId.ReadOnly = true;
            this.TbCardId.Size = new System.Drawing.Size(308, 25);
            this.TbCardId.TabIndex = 6;
            this.TbCardId.TextChanged += new System.EventHandler(this.TbCardId_TextChanged);
            // 
            // groupBox1
            // 
            this.groupBox1.AutoSize = true;
            this.groupBox1.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.groupBox1.Controls.Add(this.huodongUsageRB);
            this.groupBox1.Controls.Add(this.specialCourseRB);
            this.groupBox1.Controls.Add(this.gongfangRB);
            this.groupBox1.Controls.Add(this.courseRB);
            this.groupBox1.Controls.Add(this.baseInfoLB);
            this.groupBox1.Controls.Add(this.coursesGridView);
            this.groupBox1.Controls.Add(this.button1);
            this.groupBox1.Controls.Add(this.addUsageBtn);
            this.groupBox1.Location = new System.Drawing.Point(420, 12);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(1328, 650);
            this.groupBox1.TabIndex = 7;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "上课登记";
            // 
            // baseInfoLB
            // 
            this.baseInfoLB.AutoSize = true;
            this.baseInfoLB.Location = new System.Drawing.Point(25, 139);
            this.baseInfoLB.Name = "baseInfoLB";
            this.baseInfoLB.Size = new System.Drawing.Size(55, 15);
            this.baseInfoLB.TabIndex = 8;
            this.baseInfoLB.Text = "label1";
            // 
            // coursesGridView
            // 
            this.coursesGridView.AllowUserToAddRows = false;
            this.coursesGridView.AllowUserToDeleteRows = false;
            this.coursesGridView.AllowUserToOrderColumns = true;
            this.coursesGridView.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.AllCells;
            this.coursesGridView.AutoSizeRowsMode = System.Windows.Forms.DataGridViewAutoSizeRowsMode.AllCells;
            this.coursesGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.coursesGridView.Location = new System.Drawing.Point(25, 164);
            this.coursesGridView.MultiSelect = false;
            this.coursesGridView.Name = "coursesGridView";
            this.coursesGridView.RowTemplate.Height = 27;
            this.coursesGridView.Size = new System.Drawing.Size(1297, 462);
            this.coursesGridView.TabIndex = 7;
            // 
            // button1
            // 
            this.button1.Font = new System.Drawing.Font("SimSun", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.button1.Location = new System.Drawing.Point(25, 98);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(147, 34);
            this.button1.TabIndex = 5;
            this.button1.Text = "查看剩余课数";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.ListCoursesButton_Click);
            // 
            // addUsageBtn
            // 
            this.addUsageBtn.Font = new System.Drawing.Font("SimSun", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.addUsageBtn.Location = new System.Drawing.Point(354, 98);
            this.addUsageBtn.Name = "addUsageBtn";
            this.addUsageBtn.Size = new System.Drawing.Size(126, 35);
            this.addUsageBtn.TabIndex = 3;
            this.addUsageBtn.Text = "消费";
            this.addUsageBtn.UseVisualStyleBackColor = true;
            this.addUsageBtn.Click += new System.EventHandler(this.addUsageBtn_Click);
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.panel1);
            this.groupBox2.Controls.Add(this.BtnRead);
            this.groupBox2.Controls.Add(this.TbCardId);
            this.groupBox2.Controls.Add(this.BtnWrite);
            this.groupBox2.Controls.Add(this.BtnInitCard);
            this.groupBox2.Location = new System.Drawing.Point(28, 67);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(371, 577);
            this.groupBox2.TabIndex = 8;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "卡片管理";
            // 
            // panel1
            // 
            this.panel1.AutoScroll = true;
            this.panel1.Controls.Add(this.usageStatusLb);
            this.panel1.Controls.Add(this.LblNo);
            this.panel1.Controls.Add(this.LblName);
            this.panel1.Controls.Add(this.LblType);
            this.panel1.Location = new System.Drawing.Point(25, 211);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(310, 360);
            this.panel1.TabIndex = 7;
            // 
            // courseRB
            // 
            this.courseRB.AutoSize = true;
            this.courseRB.Location = new System.Drawing.Point(27, 45);
            this.courseRB.Name = "courseRB";
            this.courseRB.Size = new System.Drawing.Size(88, 19);
            this.courseRB.TabIndex = 9;
            this.courseRB.TabStop = true;
            this.courseRB.Text = "系统课程";
            this.courseRB.UseVisualStyleBackColor = true;
            // 
            // gongfangRB
            // 
            this.gongfangRB.AutoSize = true;
            this.gongfangRB.Location = new System.Drawing.Point(181, 45);
            this.gongfangRB.Name = "gongfangRB";
            this.gongfangRB.Size = new System.Drawing.Size(88, 19);
            this.gongfangRB.TabIndex = 10;
            this.gongfangRB.TabStop = true;
            this.gongfangRB.Text = "工坊课程";
            this.gongfangRB.UseVisualStyleBackColor = true;
            // 
            // specialCourseRB
            // 
            this.specialCourseRB.AutoSize = true;
            this.specialCourseRB.Location = new System.Drawing.Point(336, 45);
            this.specialCourseRB.Name = "specialCourseRB";
            this.specialCourseRB.Size = new System.Drawing.Size(88, 19);
            this.specialCourseRB.TabIndex = 11;
            this.specialCourseRB.TabStop = true;
            this.specialCourseRB.Text = "特殊课程";
            this.specialCourseRB.UseVisualStyleBackColor = true;
            // 
            // huodongUsageRB
            // 
            this.huodongUsageRB.AutoSize = true;
            this.huodongUsageRB.Location = new System.Drawing.Point(487, 44);
            this.huodongUsageRB.Name = "huodongUsageRB";
            this.huodongUsageRB.Size = new System.Drawing.Size(58, 19);
            this.huodongUsageRB.TabIndex = 12;
            this.huodongUsageRB.TabStop = true;
            this.huodongUsageRB.Text = "活动";
            this.huodongUsageRB.UseVisualStyleBackColor = true;
            // 
            // usageStatusLb
            // 
            this.usageStatusLb.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.usageStatusLb.Location = new System.Drawing.Point(19, 133);
            this.usageStatusLb.Multiline = true;
            this.usageStatusLb.Name = "usageStatusLb";
            this.usageStatusLb.ReadOnly = true;
            this.usageStatusLb.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.usageStatusLb.Size = new System.Drawing.Size(288, 224);
            this.usageStatusLb.TabIndex = 5;
            // 
            // FrmMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSize = true;
            this.ClientSize = new System.Drawing.Size(1754, 692);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.statusStrip1);
            this.Controls.Add(this.BtnSetup);
            this.Controls.Add(this.groupBox2);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.SizableToolWindow;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.MinimizeBox = false;
            this.Name = "FrmMain";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "普拉星球创造力系统学生卡管理系统";
            this.Load += new System.EventHandler(this.FrmMain_Load);
            this.Shown += new System.EventHandler(this.FrmMain_Shown);
            this.statusStrip1.ResumeLayout(false);
            this.statusStrip1.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.coursesGridView)).EndInit();
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button BtnWrite;
        private System.Windows.Forms.Button BtnRead;
        private System.Windows.Forms.Button BtnSetup;
        private System.Windows.Forms.StatusStrip statusStrip1;
        private System.Windows.Forms.ToolStripStatusLabel TssComPort;
        private System.Windows.Forms.Label LblNo;
        private System.Windows.Forms.Label LblName;
        private System.Windows.Forms.Label LblType;
        private System.Windows.Forms.ToolStripStatusLabel toolStripStatusLabel1;
        private System.Windows.Forms.ToolStripStatusLabel TssCardRfid;
        private System.Windows.Forms.Button BtnInitCard;
        private System.Windows.Forms.TextBox TbCardId;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Button addUsageBtn;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.DataGridView coursesGridView;
        private System.Windows.Forms.Label baseInfoLB;
        private System.Windows.Forms.RadioButton huodongUsageRB;
        private System.Windows.Forms.RadioButton specialCourseRB;
        private System.Windows.Forms.RadioButton gongfangRB;
        private System.Windows.Forms.RadioButton courseRB;
        private System.Windows.Forms.TextBox usageStatusLb;
    }
}

