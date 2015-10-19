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
            this.specialCourseCB = new System.Windows.Forms.CheckBox();
            this.button1 = new System.Windows.Forms.Button();
            this.addUsageBtn = new System.Windows.Forms.Button();
            this.huodongUsageBtn = new System.Windows.Forms.CheckBox();
            this.gongfangUsageBtn = new System.Windows.Forms.CheckBox();
            this.courseUsageBtn = new System.Windows.Forms.CheckBox();
            this.usageStatusLb = new System.Windows.Forms.Label();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.panel1 = new System.Windows.Forms.Panel();
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
            this.groupBox1.Controls.Add(this.baseInfoLB);
            this.groupBox1.Controls.Add(this.coursesGridView);
            this.groupBox1.Controls.Add(this.specialCourseCB);
            this.groupBox1.Controls.Add(this.button1);
            this.groupBox1.Controls.Add(this.addUsageBtn);
            this.groupBox1.Controls.Add(this.huodongUsageBtn);
            this.groupBox1.Controls.Add(this.gongfangUsageBtn);
            this.groupBox1.Controls.Add(this.courseUsageBtn);
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
            this.coursesGridView.Name = "coursesGridView";
            this.coursesGridView.RowTemplate.Height = 27;
            this.coursesGridView.Size = new System.Drawing.Size(1297, 462);
            this.coursesGridView.TabIndex = 7;
            // 
            // specialCourseCB
            // 
            this.specialCourseCB.AutoSize = true;
            this.specialCourseCB.Location = new System.Drawing.Point(336, 51);
            this.specialCourseCB.Name = "specialCourseCB";
            this.specialCourseCB.Size = new System.Drawing.Size(89, 19);
            this.specialCourseCB.TabIndex = 6;
            this.specialCourseCB.Text = "特殊课程";
            this.specialCourseCB.UseVisualStyleBackColor = true;
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
            // huodongUsageBtn
            // 
            this.huodongUsageBtn.AutoSize = true;
            this.huodongUsageBtn.Location = new System.Drawing.Point(488, 51);
            this.huodongUsageBtn.Name = "huodongUsageBtn";
            this.huodongUsageBtn.Size = new System.Drawing.Size(59, 19);
            this.huodongUsageBtn.TabIndex = 2;
            this.huodongUsageBtn.Text = "活动";
            this.huodongUsageBtn.UseVisualStyleBackColor = true;
            // 
            // gongfangUsageBtn
            // 
            this.gongfangUsageBtn.AutoSize = true;
            this.gongfangUsageBtn.Location = new System.Drawing.Point(182, 51);
            this.gongfangUsageBtn.Name = "gongfangUsageBtn";
            this.gongfangUsageBtn.Size = new System.Drawing.Size(89, 19);
            this.gongfangUsageBtn.TabIndex = 1;
            this.gongfangUsageBtn.Text = "工坊课程";
            this.gongfangUsageBtn.UseVisualStyleBackColor = true;
            // 
            // courseUsageBtn
            // 
            this.courseUsageBtn.AutoSize = true;
            this.courseUsageBtn.Location = new System.Drawing.Point(25, 51);
            this.courseUsageBtn.Name = "courseUsageBtn";
            this.courseUsageBtn.Size = new System.Drawing.Size(89, 19);
            this.courseUsageBtn.TabIndex = 0;
            this.courseUsageBtn.Text = "系统课程";
            this.courseUsageBtn.UseVisualStyleBackColor = true;
            // 
            // usageStatusLb
            // 
            this.usageStatusLb.AutoSize = true;
            this.usageStatusLb.Cursor = System.Windows.Forms.Cursors.PanSW;
            this.usageStatusLb.Location = new System.Drawing.Point(16, 114);
            this.usageStatusLb.Name = "usageStatusLb";
            this.usageStatusLb.Size = new System.Drawing.Size(87, 15);
            this.usageStatusLb.TabIndex = 4;
            this.usageStatusLb.Text = "          ";
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
        private System.Windows.Forms.CheckBox huodongUsageBtn;
        private System.Windows.Forms.CheckBox gongfangUsageBtn;
        private System.Windows.Forms.CheckBox courseUsageBtn;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.Label usageStatusLb;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.CheckBox specialCourseCB;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.DataGridView coursesGridView;
        private System.Windows.Forms.Label baseInfoLB;
    }
}

