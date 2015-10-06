using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms.VisualStyles;

namespace Play
{
    public class CourseDownloader
    {
        private readonly PlayConfig playConfig;
        public CourseDownloader(PlayConfig playConfig)
        {
            this.playConfig = playConfig;
        }

        public void Download(object sender, EventArgs eventArgs, Action<object, EventArgs, CourseDownloader> downloadCallback)
        {
            throw new NotImplementedException();

            downloadCallback(sender, eventArgs, this);
        }
    }
}
