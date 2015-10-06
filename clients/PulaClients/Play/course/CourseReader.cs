using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Play.course
{
    public class CourseReader
    {
        private readonly PlayConfig playConfig;
        private FileInfo fileInfo;

        private readonly DirectoryInfo tempDirectory;

        public CourseReader(PlayConfig playConfig, FileInfo courseFile)
        {
            this.playConfig = playConfig;
            this.fileInfo = courseFile;

            string temp = Environment.GetEnvironmentVariable("TEMP");
            tempDirectory = new DirectoryInfo(temp);
        }

        public string readCourse()
        {
            if (fileInfo.Exists)
            {
                try
                {
                    long ticks = DateTime.Now.Ticks;
                    string tmpName = tempDirectory.FullName + "\\" + ticks + ".swf";
                    // byte[] allBytes = File.ReadAllBytes(fileInfo.FullName);
                    // long unEncryptedLength = allBytes.Length - 2;

                    // MemoryStream ms = new MemoryStream(allBytes, 2, allBytes.Length - 2);
                    using (FileStream input = new FileStream(fileInfo.FullName, FileMode.Open, FileAccess.Read))
                    {
                        // long unEncryptedLength = input.Length - 2;
                        using (var br = new BinaryReader(input))
                        {
                            // MemoryStream ms = new MemoryStream();
                            var bw = new BinaryWriter(new FileStream(tmpName, FileMode.Create, FileAccess.Write));

                            byte majorVersion = br.ReadByte();
                            byte minorVersion = br.ReadByte();
                            
                            // magic here! Check stackoverflow for the reason.
                            // http://stackoverflow.com/questions/1874077/loading-a-flash-movie-from-a-memory-stream-or-a-byte-array
                            // http://stackoverflow.com/questions/6323389/manually-creating-a-flash-axhost-ocxstate
                            // http://stackoverflow.com/questions/19767664/how-to-play-encrypted-swf-in-c-sharp
                            ///* Write length of stream for AxHost.State */
                            //bw.Write(8 + unEncryptedLength);
                            ///* Write Flash magic 'fUfU' */
                            //bw.Write(0x55665566);
                            ///* Length of swf file */
                            //bw.Write(unEncryptedLength);

                            bool end = false;
                            while (!end)
                            {
                                byte[] read = br.ReadBytes(CourseMaker.BlockCount);
                                bw.Write(read);
                                if (read.Length < CourseMaker.BlockCount)
                                {
                                    end = true;
                                }
                            }
                            bw.Flush();

                            // ms.Seek(0, SeekOrigin.Begin);

                            // test
                            //BinaryWriter writer = new BinaryWriter(
                            //    new FileStream(fileInfo.FullName + "test.swf", FileMode.OpenOrCreate, FileAccess.Write));
                            //writer.Write(ms.GetBuffer());
                            //writer.Flush();
                            //writer.Close();
                            // return ms;
                        }
                    }
                    return tmpName;
                }
                catch (Exception e)
                {
                    return null;
                }
            }
            return null;
        }
    }
}
