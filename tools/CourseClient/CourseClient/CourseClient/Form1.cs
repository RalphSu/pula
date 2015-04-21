using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Security.Cryptography;
using System.Data.SQLite;
using System.IO;
using System.Net;
using CourseClient.Remote;

namespace CourseClient
{
    public partial class Form1 : Form
    {
        private string code = null;
        public Form1()
        {
            InitializeComponent();

            
            SqlLiteHelper.ConnectionString = "Datasource=pula.db;Pooling=true;FailIfMissing=false";

            string code = HardwareInfo.GetHardDiskID();// +":" + HardwareInfo.GetCpuID();

            string pwd = this.Encrypt(code);
            this.code = pwd; AddLog("CODE=" + this.code);
        }

        private void AddLog(string msg){
            txtLog.Text += msg +"\r\n";
        }

        private void BtnMachineNo_Click(object sender, EventArgs e)
        {
 
            //txtLog.Text += "CODE=" + pwd +"\n\r";
            

            
            //SqlLiteHelper.Password = "pula_db_secret_hidden";
            //看看是否存在一行
            DataSet ds = SqlLiteHelper.ExecuteQuery("select machine_code,node_name from system");
            AddLog("Count=" + ds.Tables.Count.ToString());
            if(ds.Tables[0].Rows.Count>0){
                string machine_code = ds.Tables[0].Rows[0]["machine_code"].ToString();
                string name = ds.Tables[0].Rows[0]["node_name"].ToString();
                if (String.Equals(machine_code, this.code))
                {
                    AddLog("machine code match!");
                }
                else
                {
                    //need update
                    AddLog("machine code not match ,old =" + machine_code);
                    SqlLiteHelper.ExecuteNonQuery("update system set machine_code=? ", this.code);
                    AddLog("updated");
                }
                AddLog("machine name=" + name);
            }else{
                AddLog("don't have machine code yet ");
                int n = SqlLiteHelper.ExecuteNonQuery("insert into system( machine_code) values(?)", this.code);
                AddLog("inserted :"+n.ToString());
            }

            
        }

        public string Encrypt(string strPwd)
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

        private void button1_Click(object sender, EventArgs e)
        {
            JsonResult jr = RemoteService.RequestActive(this.code,"我要申请激活");
            if (jr.error)
            {
                AddLog("error=" + jr.message);
            }
            else
            {
                AddLog("success=" + jr.message);
            }
        }

        private void BtnSyncActive_Click(object sender, EventArgs e)
        {
            JsonResult jr = RemoteService.SyncActive(this.code);
            if (jr.error)
            {
                AddLog("error=" + jr.message);
            }
            else
            {
               // AddLog("success=" + jr.message);
                //save
                int n = SqlLiteHelper.ExecuteNonQuery("update system set active_code=?,node_name=?,active_time=datetime('now','localtime'),node_id=?",jr.data["licenseKey"],jr.data["name"],jr.data["classroomId"]);
               // int n = SqlLiteHelper.ExecuteNonQuery("update system set node_id=?", jr.data["classroomId"]);
                AddLog("result=" + n.ToString());
            }

        }

        private void BtnChangePassword_Click(object sender, EventArgs e)
        {
           // SqlLiteHelper.ConnectionString = "Datasource=pula.db;Pooling=true;FailIfMissing=false";
            //SqlLiteHelper.ChangePassword("pula_db_secret_hidden");
        }

      
    }
}
