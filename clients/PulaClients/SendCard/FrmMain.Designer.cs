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
            this.statusStrip1.SuspendLayout();
            this.SuspendLayout();
            // 
            // BtnWrite
            // 
            this.BtnWrite.Location = new System.Drawing.Point(28, 31);
            this.BtnWrite.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.BtnWrite.Name = "BtnWrite";
            this.BtnWrite.Size = new System.Drawing.Size(309, 28);
            this.BtnWrite.TabIndex = 0;
            this.BtnWrite.Text = "写卡信息";
            this.BtnWrite.UseVisualStyleBackColor = true;
            this.BtnWrite.Click += new System.EventHandler(this.BtnWrite_Click);
            // 
            // BtnRead
            // 
            this.BtnRead.Location = new System.Drawing.Point(28, 82);
            this.BtnRead.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.BtnRead.Name = "BtnRead";
            this.BtnRead.Size = new System.Drawing.Size(309, 28);
            this.BtnRead.TabIndex = 0;
            this.BtnRead.Text = "读卡信息";
            this.BtnRead.UseVisualStyleBackColor = true;
            this.BtnRead.Click += new System.EventHandler(this.BtnRead_Click);
            // 
            // BtnSetup
            // 
            this.BtnSetup.Location = new System.Drawing.Point(28, 134);
            this.BtnSetup.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.BtnSetup.Name = "BtnSetup";
            this.BtnSetup.Size = new System.Drawing.Size(309, 28);
            this.BtnSetup.TabIndex = 0;
            this.BtnSetup.Text = "配置";
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
            this.statusStrip1.Location = new System.Drawing.Point(0, 283);
            this.statusStrip1.Name = "statusStrip1";
            this.statusStrip1.Padding = new System.Windows.Forms.Padding(1, 0, 19, 0);
            this.statusStrip1.Size = new System.Drawing.Size(867, 25);
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
            this.LblNo.Location = new System.Drawing.Point(440, 31);
            this.LblNo.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.LblNo.Name = "LblNo";
            this.LblNo.Size = new System.Drawing.Size(55, 15);
            this.LblNo.TabIndex = 2;
            this.LblNo.Text = "label1";
            // 
            // LblName
            // 
            this.LblName.AutoSize = true;
            this.LblName.Location = new System.Drawing.Point(440, 61);
            this.LblName.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.LblName.Name = "LblName";
            this.LblName.Size = new System.Drawing.Size(55, 15);
            this.LblName.TabIndex = 3;
            this.LblName.Text = "label2";
            // 
            // LblType
            // 
            this.LblType.AutoSize = true;
            this.LblType.Location = new System.Drawing.Point(440, 95);
            this.LblType.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.LblType.Name = "LblType";
            this.LblType.Size = new System.Drawing.Size(55, 15);
            this.LblType.TabIndex = 4;
            this.LblType.Text = "label3";
            // 
            // BtnInitCard
            // 
            this.BtnInitCard.Location = new System.Drawing.Point(28, 187);
            this.BtnInitCard.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.BtnInitCard.Name = "BtnInitCard";
            this.BtnInitCard.Size = new System.Drawing.Size(309, 27);
            this.BtnInitCard.TabIndex = 5;
            this.BtnInitCard.Text = "初始化卡片(&I)";
            this.BtnInitCard.UseVisualStyleBackColor = true;
            this.BtnInitCard.Click += new System.EventHandler(this.BtnInitCard_Click);
            // 
            // TbCardId
            // 
            this.TbCardId.Location = new System.Drawing.Point(28, 235);
            this.TbCardId.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.TbCardId.Name = "TbCardId";
            this.TbCardId.ReadOnly = true;
            this.TbCardId.Size = new System.Drawing.Size(308, 25);
            this.TbCardId.TabIndex = 6;
            // 
            // FrmMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(867, 308);
            this.Controls.Add(this.TbCardId);
            this.Controls.Add(this.BtnInitCard);
            this.Controls.Add(this.LblType);
            this.Controls.Add(this.LblName);
            this.Controls.Add(this.LblNo);
            this.Controls.Add(this.statusStrip1);
            this.Controls.Add(this.BtnSetup);
            this.Controls.Add(this.BtnRead);
            this.Controls.Add(this.BtnWrite);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Margin = new System.Windows.Forms.Padding(4, 3, 4, 3);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "FrmMain";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "PULA SYSTEM 卡信息";
            this.Load += new System.EventHandler(this.FrmMain_Load);
            this.Shown += new System.EventHandler(this.FrmMain_Shown);
            this.statusStrip1.ResumeLayout(false);
            this.statusStrip1.PerformLayout();
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
    }
}

