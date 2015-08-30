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

            serviceEndpointTb.Text = RemoteServiceProxy.ServiceUrl;
        }

        private void FrmSetup_Load(object sender, EventArgs e)
        {
            //to ui ;
            
            CbCom.SelectedIndex = Config.ComPort - 1;
            TbUsername.Text = Config.Username;
            TbPassword.Text = Config.Password;
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

            RemoteServiceProxy.ServiceUrl = serviceEndpointTb.Text;

            string uid = TbUsername.Text.Trim();
            string pwd = TbPassword.Text.Trim();
            //检查用户名和密码
            JsonResult jr = RemoteService.RemoteServiceProxy.CheckLogin(uid,pwd);

            if (jr.error)
            {
                MessageBox.Show("用户身份验证失败,请重新输入用户名和密码");
                TbUsername.Focus();
                return;
            }

            //保存
            Config.ComPort = CbCom.SelectedIndex + 1;
            Config.Username = uid;
            Config.Password = pwd;
            Config.SaveToFile();

            //return 
            this.DialogResult = System.Windows.Forms.DialogResult.OK;

        }

       
    }
}
