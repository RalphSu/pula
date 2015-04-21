using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using SendCard;

namespace CourseClient
{
    static class Program
    {
        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            SqlLiteHelper.ConnectionString = "Datasource=pula.db;Pooling=true;FailIfMissing=false";


            //先检查是否激活,如果已经激活直接进入Loading界面
            FrmActive frmActive = null;
            var actived = FrmActive.IsActive();
            

            string[] args= Environment.GetCommandLineArgs();

            if (contains(args, "-fa"))
            {
                actived = false;
            }

            if(!actived ){
                if (frmActive == null)
                {
                    frmActive = new FrmActive();
                }
                if (frmActive.ShowDialog() == DialogResult.OK)
                {
                    actived = true;
                }
            }

            frmActive = null;

            if (!actived)
            {
                Application.Exit();
                return;
            }

            var frmMain = new FrmMain();

            if (!frmMain.OpenComm())
            {

                Application.Exit();
                return;
            }

            var frmLoading = new FrmLoading();
            frmLoading.FrmMain = frmMain;

            //(new FrmLoading()).ShowDialog();
           // Application.Run(frmMain);


            Application.Run( frmLoading );//new FrmLand());

           /* var frmCourse = new FrmCourse();
            frmCourse.CourseNo = "123";
            Application.Run(frmCourse);*/


        }

        private static bool contains(string[] args, string p)
        {
            foreach (var a in args)
            {
                if (p.Equals(a))
                {
                    return true;
                }
            }

            return false;
        }
    }
}
