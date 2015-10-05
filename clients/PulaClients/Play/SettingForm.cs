using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Play
{
    public partial class SettingForm : Form
    {
        private readonly PlayConfig config;

        public SettingForm(PlayConfig playConfig)
        {
            InitializeComponent();

            config = playConfig;

            serverUrlText.Text = config.Server;
            teacherNameText.Text = config.UserName;
            passwordText.Text = config.Password;
            localFolderTB.Text = config.LocalFolder;
        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {
            // nothing
        }

        private void settingSaveBtn_Click(object sender, EventArgs e)
        {
            string url = serverUrlText.Text;
            string username = teacherNameText.Text;
            string password = passwordText.Text;
            string localFolder = localFolderTB.Text;
            if (config.EnforceUserCheck)
            {
                if (HttpHelper.validate(url, username, password))
                {
                    UpdateConfig(url, username, password, localFolder);
                }
            }
            else
            {
                UpdateConfig(url, username, password, localFolder);
            }
                
        }

        private void UpdateConfig(string url, string username, string password, string localFolder)
        {
            config.Server = url;
            config.UserName = username;
            config.Password = password;
            config.LocalFolder = localFolder;
        }
    }
}
