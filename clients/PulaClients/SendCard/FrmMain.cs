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

            coursesGridView.Columns.Clear();
            DataGridViewCheckBoxColumn selectColumn = new DataGridViewCheckBoxColumn(false);
            selectColumn.HeaderText = "选择";
            coursesGridView.Columns.Add(selectColumn);
            DataGridViewTextBoxColumn commentColumn = new DataGridViewTextBoxColumn();
            commentColumn.AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill;
            commentColumn.HeaderText = "订单详情";
            coursesGridView.Columns.Add(commentColumn);

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

            addUsageBtn.Enabled = opened;
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

        private void TbCardId_TextChanged(object sender, EventArgs e)
        {

        }

        private void addUsageBtn_Click(object sender, EventArgs e)
        {
            #region 判断用户选择是否满足要求
            int courseCout = courseUsageBtn.Checked ? 1 : 0;
            int gongfangCount = gongfangUsageBtn.Checked ? 1 : 0;
            int huodongCount = huodongUsageBtn.Checked ? 1 : 0;
            int specialCourseCount = specialCourseCB.Checked ? 1 : 0;
            if (courseCout == 0 && gongfangCount == 0 && huodongCount == 0 && specialCourseCount == 0)
            {
                MessageBox.Show("请选择消费类型然后消费!");
                return;
            }

            DataGridViewRow selectedRow = null;
            foreach (DataGridViewRow row in coursesGridView.Rows)
            {
                DataGridViewCheckBoxCell selectCell = row.Cells[0] as DataGridViewCheckBoxCell;
                if (selectCell != null && selectCell.Value != null && (bool)selectCell.Value)
                {
                    if (selectedRow != null)
                    {
                        MessageBox.Show("只能选择一个订单!");
                        return;
                    }
                    selectedRow = row;
                }
            }

            if (selectedRow == null)
            {
                MessageBox.Show("请选择一个订单然后消费!");
                return;
            }
            #endregion

            TimeCourseOrder order = (TimeCourseOrder)selectedRow.Tag;

            string orderNo = order.no;
            string msg;
            string cardid;
            CardMeta rm;
            if (ReadCard(out cardid, out rm))
            {
                return;
            }

            if (rm.no != order.studentNo)
            {
                MessageBox.Show("订单用户和当前卡用户不匹配，请先点击查看 剩余课程 按钮!");
                return;
            }

            try
            {
                var result = RemoteServiceProxy.AddTimeCourseUsage(courseCout, gongfangCount, huodongCount,
                    specialCourseCount,
                    cardid, rm.no, rm.name, 
                    orderNo,
                    config.Username, config.Password);
                if (result.error)
                {
                    msg = "Error: " + result.message;
                }
                else
                {
                    msg = "成功: " + result.message;
                }
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
            this.usageStatusLb.Text = msg;
        }

        private bool ReadCard(out string cardid, out CardMeta rm)
        {
            rm = null;
            this.usageStatusLb.Text = "";

            cardid = reader.GetCard();
            if (!string.IsNullOrEmpty(cardid))
            {
                reader.Buzz(1);
                TssCardRfid.Text = cardid;
                TbCardId.Text = cardid;
            }
            else
            {
                //未读到卡
                this.usageStatusLb.Text = "没读到卡信息";
                return true;
            }

            rm = null;
            try
            {
                reader.PrepareCheck(cardid);
                rm = reader.ReadMeta();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                return true;
            }

            string msg = "";
            return false;
        }

        private void ListCoursesButton_Click(object sender, EventArgs e)
        {
            // rows cleared
            coursesGridView.Rows.Clear();
            baseInfoLB.Text = "";

            string msg;
            string cardid;
            CardMeta rm;
            if (ReadCard(out cardid, out rm))
            {
                return;
            }

            try
            {
                var result = RemoteServiceProxy.GetUserCourseOrders(rm.no);
                if (result == null || result.records == null || result.records.Count == 0)
                {
                    msg = "Error: " + "没找到用户的订单，请登录系统页面查看！";
                }
                else
                {
                    var sb = new StringBuilder();
                    sb.Append("用户：").Append(rm.name).Append("用户编号：").Append(rm.no).AppendLine();
                    baseInfoLB.Text = sb.ToString();
                    foreach (var timeCourseOrder in result.records)
                    {
                        sb = new StringBuilder();
                        sb.Append("订单号：").Append(timeCourseOrder.no).AppendLine();
                        sb.Append("课程次数：")
                            .Append(timeCourseOrder.paiedCount)
                            .Append(" 已使用：")
                            .Append(timeCourseOrder.usedCount)
                            .Append("次；")
                            .AppendLine();
                        sb.Append("工坊课次数：")
                            .Append(timeCourseOrder.gongfangCount)
                            .Append(" 已使用：")
                            .Append(timeCourseOrder.usedGongFangCount)
                            .Append("次；")
                            .AppendLine();
                        sb.Append("活动次数：")
                            .Append(timeCourseOrder.huodongCount)
                            .Append(" 已使用：")
                            .Append(timeCourseOrder.usedHuodongCount)
                            .Append("次；")
                            .AppendLine();
                        sb.Append("特殊课程次数：")
                            .Append(timeCourseOrder.specialCourseCount)
                            .Append(" 已使用：").Append(timeCourseOrder.usedSpecialCourseCount)
                            .Append("次；")
                            .AppendLine();

                        var row = new DataGridViewRow();
                        row.CreateCells(coursesGridView);
                        ((DataGridViewCheckBoxCell) row.Cells[0]).TrueValue = true;
                        ((DataGridViewCheckBoxCell) row.Cells[0]).FalseValue = false;

                        row.Cells[1].Value = sb.ToString();
                        row.Height = 35;
                        row.Cells[1].ToolTipText = sb.ToString();
                        row.Tag = timeCourseOrder;

                        coursesGridView.Rows.Add(row);
                    }
                    msg = sb.ToString();
                }
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
            this.usageStatusLb.Text = msg;
        }


    }
}
