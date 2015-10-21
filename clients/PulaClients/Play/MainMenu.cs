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
    public partial class MainMenu : Form
    {
        private readonly PlayConfig playConfig;

        public MainMenu()
        {
            InitializeComponent();

            playConfig = new PlayConfig();

            if (!playConfig.CourseMaker)
            {
                downloadCourseBtn.Enabled = false;
            }
            else
            {
                downloadCourseBtn.Enabled = true;
            }
        }

        private void serverSettingBtn_Click(object sender, EventArgs e)
        {
            var setting = new SettingForm(playConfig);
            setting.ShowDialog();
        }

        private void dataGridView1_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= dataGridView1.RowCount)
            {
                return;
            }
            DataGridViewRow row = dataGridView1.Rows[e.RowIndex];
            var playForm = new CoursePlayForm((FileInfo)row.Tag, playConfig);
            playForm.ShowDialog();
            playForm.finish();
        }

        private void localCourseBtn_Click(object sender, EventArgs e)
        {
            dataGridView1.Rows.Clear();
            var fi = new DirectoryInfo(Path.GetFullPath(playConfig.LocalFolder));
            foreach (var fileIt in fi.EnumerateFiles("*.course"))
            {
                var row = new DataGridViewRow();
                row.CreateCells(dataGridView1);
                row.Cells[0].Value = fileIt.Name;
                row.Cells[1].Value = fileIt.LastWriteTime;
                row.Tag = fileIt;
                row.Height = 35;
                dataGridView1.Rows.Add(row);
            }
        }

        private void downloadCourseBtn_Click(object sender, EventArgs e)
        {
            if (!playConfig.CourseMaker)
            {
                return;
            }

            var dialog = new OpenFileDialog();
            dialog.Filter = @"Flash课件(*.swf)|*.swf";
            dialog.Multiselect = true;
            if (DialogResult.OK == dialog.ShowDialog())
            {
                string[] fileNames = dialog.FileNames;

                var maker = new CourseMaker(fileNames, playConfig);
                maker.Make();
                string result = maker.GetResult();
                MessageBox.Show(result);
            }

        }

        private void aboutUsBtn_Click(object sender, EventArgs e)
        {
            var aboutUs = new AboutUs(playConfig);
            aboutUs.ShowDialog();
        }
    }
}
