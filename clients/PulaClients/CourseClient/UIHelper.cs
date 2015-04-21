using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace CourseClient
{
    public  class UIHelper
    {
        internal static void ShowAlert(string p)
        {
            MessageBox.Show(p, "错误", MessageBoxButtons.OK, MessageBoxIcon.Stop);
                
        }

        internal static void ShowInfo(string p)
        {
            MessageBox.Show(p, "信息", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
    }
}
