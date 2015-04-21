using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace SendCard
{
    public class CardConfig
    {
        public int ComPort { get; set; }
        public string URL { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }


        private IniFiles iniFile;

        public CardConfig(string fn)
        {
            iniFile = new IniFiles(fn);
            LoadFromFile();
            //tempFolder = Application.StartupPath + "\\temp";
            //ClearFolder(this.tempFolder);
            //this.tempFolder += "\\";

        }

        private void ClearFolder(string p)
        {
            if (Directory.Exists(p))
            {
                DirectoryInfo di = new DirectoryInfo(p);
                foreach (FileInfo f in di.GetFiles())
                {
                    f.Delete();
                }

            }
            ForceFolder(p);

        }
        private void ForceFolder(string ff)
        {
            if (!Directory.Exists(ff))
            {
                Directory.CreateDirectory(ff);
            }
        }

       

        public void LoadFromFile()
        {
            /*url = iniFile.ReadString("main", "url", "http://localhost:8081/et/");
            //no = iniFile.ReadString("main", "no", "001");
            //password = iniFile.ReadString("main", "password", "001");
            mode = iniFile.ReadInteger("main", "mode", 0);*/
            ComPort = iniFile.ReadInteger("main", "comPort", 1);
            //baud = iniFile.ReadInteger("main", "baud", 14400);
            //openPath = iniFile.ReadString("main", "openPath", "");

           

            //antennaNo = iniFile.ReadString("main", "antennaNo", "1,2");
            //newVersion = iniFile.ReadBool("main", "newVersion", false);
           // List<int> ans = GetAntennaNoList();
            //MessageBox.Show(ans.Count.ToString());
        }

        public void SaveToFile()
        {

            //iniFile.WriteString("main", "url", url.Trim());
            //iniFile.WriteString("main", "no", no.Trim());
            //iniFile.WriteString("main", "password", password.Trim());
            //iniFile.WriteInteger("main", "mode", mode);
            iniFile.WriteInteger("main", "comPort", ComPort);

            //iniFile.WriteInteger("main", "baud", baud);
            //iniFile.WriteString("main", "openPath", openPath);
            //iniFile.WriteBool("main", "newVersion", newVersion);
        }

        
    }
}
