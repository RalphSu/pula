using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Play
{
    public partial class AboutUs : Form
    {
        public AboutUs(PlayConfig playConfig)
        {
            InitializeComponent();

            versionLabel.Text = string.Format("{0}.{1}", playConfig.MajorVersion, playConfig.MinorVersion);
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}
