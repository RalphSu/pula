using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SQLLiteTest
{
    static class Program
    {
        static string ConnectionString = "Data Source=pula.db;Pooling=true;FailIfMissing=false";
        // const string NewConnection = "Data Source=test.db;Pooling=true;FailIfMissing=false";

        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            string message = "abc";
            //AddLog("don't have machine code yet ");
            // string cmdText = "insert into system(machine_code) values('abc')";
            // create table


            string cmdText = "select machine_code,node_name,active_code,expire_time from system";
            try
            {
                using (SQLiteConnection conn = new SQLiteConnection(ConnectionString))
                {
                    conn.Open();
                    using (SQLiteCommand cmd = new SQLiteCommand())
                    {
                        cmd.Parameters.Clear();
                        cmd.Connection = conn;
                        cmd.CommandText = cmdText;// "create table system(machine_code varchar(200))";
                        cmd.CommandType = CommandType.Text;
                        cmd.CommandTimeout = 30;
                        cmd.ExecuteReader();
                    }
                }
            }
            catch (Exception e)
            {
                message = e.Message + "\n" +
                          e.StackTrace;
            }

            //AddLog("inserted :" + n.ToString());

            //string cmdText = "select machine_code,node_name,active_code,expire_time from system";
            //using (SQLiteConnection conn = new SQLiteConnection(ConnectionString))
            //{
            //    using (SQLiteCommand command = new SQLiteCommand())
            //    {
            //        DataSet ds = new DataSet();
            //        PrepareCommand(command, conn, cmdText);
            //        SQLiteDataAdapter da = new SQLiteDataAdapter(command);
            //        da.Fill(ds);
            //    }
            //}


            Application.Run(new Form1(message));
        }


        public static int ExecuteNonQuery(string cmdText, params object[] p)
        {
            using (SQLiteConnection conn = new SQLiteConnection(ConnectionString))
            {
                using (SQLiteCommand command = new SQLiteCommand())
                {
                    PrepareCommand(command, conn, cmdText, p);
                    return command.ExecuteNonQuery();
                }
            }
        }

        private static void PrepareCommand(SQLiteCommand cmd, SQLiteConnection conn, string cmdText, params object[] p)
        {
            if (conn.State != ConnectionState.Open)
            {
                //if (!String.IsNullOrEmpty(Password))
                //{
                //    conn.SetPassword(Password);
                //}
                conn.Open();

            }
            cmd.Parameters.Clear();
            cmd.Connection = conn;
            cmd.CommandText = cmdText;
            cmd.CommandType = CommandType.Text;
            cmd.CommandTimeout = 30;
            if (p != null)
            {
                foreach (object parm in p)
                    cmd.Parameters.AddWithValue(string.Empty, parm);
            }
        }
    }
}
