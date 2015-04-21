using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using RemoteService;

namespace SendCard
{
    public partial class FrmSetup : Form
    {

        public CardConfig Config { get; set; }
        public IComSupport ComSupport { get; set; } 

        public FrmSetup()
        {
            InitializeComponent();
        }

        private void FrmSetup_Load(object sender, EventArgs e)
        {
            //to ui ;
            
            CbCom.SelectedIndex = Config.ComPort - 1;
            
        }

        private void BtnOK_Click(object sender, EventArgs e)
        {
            //检查并保存

            if (ComSupport != null)
            {
                if (!ComSupport.OpenComm(CbCom.SelectedIndex + 1))
                {
                    MessageBox.Show("串口"+(CbCom.SelectedIndex+1).ToString()+"无法打开");
                    CbCom.Focus();
                    return;
                }
            }

      

            //保存
            Config.ComPort = CbCom.SelectedIndex + 1;

            Config.SaveToFile();

            //return 
            this.DialogResult = System.Windows.Forms.DialogResult.OK;

        }

       
    }
}
