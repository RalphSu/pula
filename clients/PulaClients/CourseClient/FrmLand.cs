using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace CourseClient
{
    public partial class FrmLand : Form
    {
        public FrmLand()
        {
            InitializeComponent();
        }

        private void FrmLand_Shown(object sender, EventArgs e)
        {
            string fp = System.Environment.CurrentDirectory + "\\swf\\land.swf";
            swf.Movie = fp;
            this.FormBorderStyle = FormBorderStyle.None;
            this.WindowState = FormWindowState.Maximized; 
        }
    }
}
