using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Play
{
    public class CourseMaker
    {
        public const int BlockCount = 1024*1024;
        private readonly string[] sourceFileNames;
        private readonly PlayConfig playConfig;

        private StringBuilder results = new StringBuilder();

        public CourseMaker(string[] sourceFileNames, PlayConfig playConfig)
        {
            this.sourceFileNames = sourceFileNames;
            this.playConfig = playConfig;
        }

        public void Make()
        {
            string baseFolder = Path.GetFullPath(playConfig.LocalFolder);
            foreach (var sourceFileName in sourceFileNames)
            {
                string withoutExtension = Path.GetFileNameWithoutExtension(sourceFileName);
                string targetFullName = baseFolder + "\\" + withoutExtension + "_encrypt.course";

                try
                {
                    using (var br = new BinaryReader(
                        new FileStream(sourceFileName, FileMode.Open, FileAccess.Read)))
                    {
                        using (
                            var bw =
                                new BinaryWriter(new FileStream(targetFullName, FileMode.OpenOrCreate, FileAccess.Write))
                            )
                        {
                            byte major = Convert.ToByte(playConfig.MajorVersion);
                            byte minor = Convert.ToByte(playConfig.MinorVersion);
                            // write leading version bytes
                            bw.Write(new byte[] {major, minor});
                            bool end = false;
                            while (!end)
                            {
                                byte[] read = br.ReadBytes(BlockCount);
                                bw.Write(read);
                                if (read.Length < BlockCount)
                                {
                                    end = true;
                                }
                            }

                            bw.Flush();
                        }
                    }

                    results.Append(withoutExtension)
                        .Append(" 已转换为")
                        .Append(withoutExtension)
                        .Append("_encrypt.course")
                        .AppendLine();
                }
                catch (Exception e)
                {
                    results.Append(withoutExtension).Append(" 转换失败:").Append(e.Message).AppendLine();
                }
            }
        }


        public string GetResult()
        {
            return results.ToString();
        }
    }
}
