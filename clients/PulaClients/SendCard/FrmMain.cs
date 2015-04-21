using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Security.Cryptography;
using System.IO;
using PulaRfid;
using RemoteService;

namespace SendCard
{
    public partial class FrmMain : Form,IComSupport
    {

        private CardConfig config;
        private PulaCardReader reader = new PulaCardReader();
        private bool opened = false;
        public FrmMain()
        {
            InitializeComponent();
            opened = false;
            config = new CardConfig(Application.StartupPath + "\\config.ini");
        }

        private void BtnSetup_Click(object sender, EventArgs e)
        {
            FrmSetup frm = new FrmSetup();
            frm.Config = this.config;
            frm.ComSupport = this;
            frm.ShowDialog();
            
        }

        public bool OpenComm(int p)
        {
            opened = reader.Open(p);

            UpdateButtons();

            return opened;
        }

        private void UpdateButtons()
        {
            BtnInitCard.Enabled = opened;
            BtnRead.Enabled = opened;
            BtnWrite.Enabled = opened;
        }

        private void FrmMain_Load(object sender, EventArgs e)
        {
            this.ResetLabel();
        }

        private void ResetLabel()
        {
            LblName.Text = "请把卡片放在读卡器上";
            LblNo.Text = "";
            LblType.Text = "";
            TssCardRfid.Text = "";
            TbCardId.Text = "";
        }

        private void FrmMain_Shown(object sender, EventArgs e)
        {

            TssComPort.Text = "COM" + config.ComPort.ToString();

            //打开端口
            if (!OpenComm(config.ComPort))
            {
                MessageBox.Show("无法打开串口,请重新设置");
                BtnSetup_Click(sender, e);
                return;
            }



        }

        private void BtnWrite_Click(object sender, EventArgs e)
        {
            ResetLabel();
            TssCardRfid.Text = "";
            //首先读取看看是否有信息
            /*string cardid;
            int n = reader.SearchCard(out cardid);*/
            //string cardid;

            string cardid = reader.GetCard();



           


            if (!string.IsNullOrEmpty(cardid))
            {
                reader.Buzz(1);
                TssCardRfid.Text = cardid;
                TbCardId.Text = cardid;
            }
            else
            {
                //未读到卡
                return;
            }


            //

            CardMeta rm = null;

            try
            {
                reader.PrepareCheck(cardid);
                rm = reader.ReadMeta();

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                return;
            }


            //从服务器拿资料
            JsonResult jr = RemoteService.RemoteServiceProxy.GetInfo(cardid, config.Username, config.Password);

            if (jr.error)
            {
                MessageBox.Show(jr.message);
                return;
            }


            CardMeta remote = new CardMeta();
            remote.no = jr.data["no"];
            remote.name = jr.data["name"];

            int n = 0 ;
            if (int.TryParse(jr.data["type"], out n))
            {

                remote.type = n;

            }
           

            string no = rm.no.Trim();
            if (string.IsNullOrEmpty(no) || no.Equals("\0\0\0\0\0\0\0\0\0\0\0\0"))
            {
                //空卡
                //申请写入,并要修改密码?

                //修改密码的事情留给初始化卡




            }
            else
            {
                LblName.Text = rm.name;
                LblNo.Text = rm.no;
                LblType.Text = GetTypeName(rm.type);


                if (string.Equals(remote.name, rm.name) && string.Equals(remote.no, rm.no) && (remote.type == rm.type))
                {
                    MessageBox.Show("卡中已写入正确信息,无需重复写入");
                    return;

                }


                if (MessageBox.Show("卡中已经有人员信息,是否覆盖?", "询问", MessageBoxButtons.YesNo, MessageBoxIcon.Question) != DialogResult.Yes)
                {
                    return;
                }

            }


            

            //新的学员,教师信息
            LblName.Text = remote.name;
            LblNo.Text = remote.no;
            LblType.Text = GetTypeName(remote.type);

            if (reader.WriteMeta(remote) != 0)
            {
                MessageBox.Show("写卡失败");
            }
            else
            {
                MessageBox.Show("写卡成功");
                reader.Buzz(4);
            }

            //MessageBox.Show(rm.no+":"+rm.type +":"+rm.name);
        }

        private string GetTypeName(int t)
        {
            if (t == 2)
            {
                return "教师";
            }
            else
            {
                return "学员";
            }
        }

        private void BtnInitCard_Click(object sender, EventArgs e)
        {
            //if (true) return;
            try
            {
                reader.InitCard( );
                MessageBox.Show("初始化卡片完成");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void BtnRead_Click(object sender, EventArgs e)
        {

            ResetLabel();
            string cardid = reader.GetCard();

            

            if (!string.IsNullOrEmpty(cardid))
            {
                reader.Buzz(1);
                TssCardRfid.Text = cardid;
                TbCardId.Text = cardid;
            }
            else
            {
                //未读到卡
                return;
            }


            //

            CardMeta rm = null;

            try
            {
                reader.PrepareCheck(cardid);
                rm = reader.ReadMeta();

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message +" 可能是未初始化,或其他未经认证的卡片");
                return;
            }

            if (string.IsNullOrEmpty(rm.name))
            {
                LblName.Text = "空白卡";
            }
            else
            {
                LblName.Text = rm.name;
                LblNo.Text = rm.no;
                LblType.Text = GetTypeName(rm.type);
            }
        }

       
    }
}
