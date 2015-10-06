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
    public partial class CourseBrowserPlayForm : Form
    {
        private const string html = @"
<html>
<body>
<div class=""player"" id=""player"">
<object type=""application/x-shockwave-flas"" data=""{0}"" width=""100%"" height=""100%"" id=""movie_player"">
<param name=""allowFullScreen"" value=""true"">
<param name=""allowscriptaccess"" value=""always"">
<param name=""allowFullScreenInteractive"" value=""true"">
<param name=""flashvars"" value=""{0}"">
<param name=""movie"" value=""{0}"">
</object>
</div>
</body>
</html>
";

        public CourseBrowserPlayForm(FileInfo tag)
        {
            InitializeComponent();
            string file = tag.FullName;
            file = Path.GetFullPath("swf/loading1.swf");
            string contents = string.Format(html, "file:///" + file);
            // webBrowser1.Url = new Uri(tag.FullName, UriKind.RelativeOrAbsolute);
            webBrowser1.DocumentText = contents;
            webBrowser1.Show();
        }
    }
}
