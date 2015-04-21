using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Security.Cryptography;
using RemoteService;
using AES;

namespace CourseClient
{
    public partial class FrmActive : Form
    {
        public FrmActive()
        {
            InitializeComponent();
        }

        public static string machineCode = null;
        public static string activeCode = null;
        public static string clientVersion = "20140318";
        public static bool expired = false;
        public static DateTime? expireTime;
        static FrmActive(){
            string code = HardwareInfo.GetHardDiskID();// +":" + HardwareInfo.GetCpuID();
            machineCode = Encrypt(code);
        }



        

        private void FrmActive_Load(object sender, EventArgs e)
        {
            

            tbMachineCode.Text = machineCode;
            lblVersion.Text = clientVersion;

            ShowSuccess(false);

            lblExpired.Visible = expired;
        }

        public static string Encrypt(string strPwd)
        {
            MD5 md5 = new MD5CryptoServiceProvider();
            byte[] data = System.Text.Encoding.Default.GetBytes(strPwd);//将字符编码为一个字节序列 
            byte[] md5data = md5.ComputeHash(data);//计算data字节数组的哈希值 
            md5.Clear();
            string str = "";
            for (int i = 0; i < md5data.Length - 1; i++)
            {
                str += md5data[i].ToString("x").PadLeft(2, '0');
            }
            return str;
        }

        public static bool IsActive()
        {
            DataSet ds = SqlLiteHelper.ExecuteQuery("select machine_code,node_name,active_code,expire_time from system");
            //AddLog("Count=" + ds.Tables.Count.ToString());
            if (ds.Tables[0].Rows.Count > 0)
            {
                string machine_code = ds.Tables[0].Rows[0]["machine_code"].ToString();
                string name = ds.Tables[0].Rows[0]["node_name"].ToString();
                activeCode = ds.Tables[0].Rows[0]["active_code"].ToString();

                if(ds.Tables[0].Rows[0].IsNull("expire_time")){
                    expireTime = null;
                }else{
                    expireTime = (DateTime?)ds.Tables[0].Rows[0]["expire_time"];
                }

                if (expireTime !=null && expireTime.Value.CompareTo(System.DateTime.Now) <= 0)
                {
                    //到期了
                    expired = true;
                }

                if (String.Equals(machine_code, machineCode))
                {
                    //AddLog("machine code match!");
                }
                else
                {
                    //need update
                    //AddLog("machine code not match ,old =" + machine_code);
                    SqlLiteHelper.ExecuteNonQuery("update system set machine_code=?,active_code=null ", machineCode);
                    //机器码,不同,强制设为无效
                    activeCode = null;
                    //AddLog("updated");
                }
                //AddLog("machine name=" + name);
            }
            else
            {
                //AddLog("don't have machine code yet ");
                int n = SqlLiteHelper.ExecuteNonQuery("insert into system( machine_code) values(?)", machineCode);
                //AddLog("inserted :" + n.ToString());
            }


            if (string.IsNullOrEmpty(activeCode) || expired)
            {
                return false;
            }
            return true;

        }

        private void btnRequestActive_Click(object sender, EventArgs e)
        {
            JsonResult jr = RemoteServiceProxy.RequestActive(machineCode, "我要申请激活");
            if (jr.error)
            {
                //AddLog("error=" + jr.message);
                UIHelper.ShowAlert(jr.message);
            }
            else
            {
                //AddLog("success=" + jr.message);
                UIHelper.ShowInfo(jr.message);
            }
        }

        private void btnSyncActive_Click(object sender, EventArgs e)
        {
            JsonResult jr = RemoteServiceProxy.SyncActive(machineCode);
            if (jr.error)
            {
                UIHelper.ShowAlert(jr.message);
            }
            else
            {
                long tick = 0;
                long.TryParse(jr.data["expiredTime"], out tick);
                tick = tick / 1000;

                

                // AddLog("success=" + jr.message);
                //save
                int n =0;
                if(tick==0){
                    SqlLiteHelper.ExecuteNonQuery("update system set active_code=?,node_name=?,active_time=datetime('now','localtime'),node_id=?,expire_time=null",
                    jr.data["licenseKey"], jr.data["name"], jr.data["classroomId"]);
                }else{

                    DateTime expire_time = UnixTool.ConvertIntDateTime(tick);
                    if (expire_time.CompareTo(System.DateTime.Now) <= 0)
                    {
                        //到期了
                        expired = true;
                        UIHelper.ShowAlert("激活信息已经失效,请与总部联系");
                        return;
                    }


                    n = SqlLiteHelper.ExecuteNonQuery("update system set active_code=?,node_name=?,active_time=datetime('now','localtime'),node_id=?,expire_time=datetime(?, 'unixepoch', 'localtime')",
                    jr.data["licenseKey"], jr.data["name"], jr.data["classroomId"], tick );
                }
                // int n = SqlLiteHelper.ExecuteNonQuery("update system set node_id=?", jr.data["classroomId"]);
                UIHelper.ShowInfo("成功同步激活信息");
                ShowSuccess(true);

                DialogResult = DialogResult.OK;
            }
        }

        private void ShowSuccess(bool b)
        {
            btnRequestActive.Visible = !b;
            btnSyncActive.Visible = !b;
            lblActived.Visible = b;
            lblActived.Location = btnRequestActive.Location;

        }
    }
}
