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
using Play.course;

namespace Play
{
    public partial class CoursePlayForm : Form
    {
        private readonly FileInfo file;
        private readonly PlayConfig playConfig;
        private readonly string tmpFile;

        public CoursePlayForm(FileInfo tag, PlayConfig playConfig)
        {
            InitializeComponent();

            file = tag;
            this.playConfig = playConfig;
            var reader = new CourseReader(this.playConfig, file);
            tmpFile = reader.readCourse();
            btnStart_Click(null, null);
        }

        public void finish()
        {
            if (tmpFile != null)
            {
                try
                {
                    File.Delete(tmpFile);
                }
                catch (Exception e)
                {
                }
            }
        }

        private void btnStart_Click(object sender, EventArgs e)
        {
            if (tmpFile != null)
            {
                timerPlay.Enabled = true;
                timerPlay.Interval = 1200;
                swf.Movie = tmpFile;
                swf.Play();
            }
        }
    }
}
