using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace CourseClient
{
    class TestCapture
    {

        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main()
        {
            FrmCapture captureForm = new FrmCapture();
            if (captureForm.ShowDialog() == DialogResult.OK)
            {
            }
        }
    }
}
