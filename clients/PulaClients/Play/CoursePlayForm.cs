using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Play
{
    public partial class CoursePlayForm : Form
    {
        private FileInfo file;
        public CoursePlayForm(FileInfo tag)
        {
            InitializeComponent();

            file = tag;
        }

        private void btnStart_Click(object sender, EventArgs e)
        {
            swf.LoadMovie(0, "swf/loading1.swf");
        }
    }
}
