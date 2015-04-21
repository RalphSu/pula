using System;
using System.Collections.Generic;
using System.Text;
namespace PulaRfid
{
    public class MasterReader
    {

        public bool OpenComm(int n, int bd)
        {
            if (MasterReaderDevice.rf_init_com(n, bd) != 0)
            {
                return false;
            }

            byte v = Convert.ToByte('A');
            int c = MasterReaderDevice.rf_init_type(0, v);
            if (c != 0)
            {
                return false;
            }

            return true;
        }

        public int SearchCard(out string cardId)
        {
            ushort tag = 0;
            int n = MasterReaderDevice.rf_request(0, 0x26, ref tag);
            if (n != 0)
            {
                cardId = "";
                return -1; //request failed
            }

            byte[] byteCard = new byte[180];
            byte cardIdLen = 0;
            n = MasterReaderDevice.rf_anticoll(0, 4, byteCard, ref cardIdLen);
            //abel3.Text = "ref_anticoll : " + n.ToString() + " len=" + b.ToString();
            if (n == 0)
            {
                string hex = BitConverter.ToString(byteCard, 0, cardIdLen);
                hex = hex.Replace("-", "");
                cardId = hex;
                return 0;
            }
            else
            {
                cardId = "";
                return -2;
            }

        }

        public bool Beep(byte len)
        {
            return MasterReaderDevice.rf_beep(0, len) == 0;
        }
        public bool Light(byte l)
        {
            return MasterReaderDevice.rf_light(0, l) == 0;
        }
        public bool CloseComm()
        {
            return MasterReaderDevice.rf_ClosePort() == 0;
        }


        public int SelectCurrent(out string cardId)
        {

            ushort tag = 0;

            int n = MasterReaderDevice.rf_request(0, 0x26, ref tag);
            if (n != 0)
            {
                cardId = "";
                //return -1; //request failed
            }

            byte[] byteCard = new byte[180];
            byte b = 0;
            n = MasterReaderDevice.rf_anticoll(0, 4, byteCard, ref b);
            //abel3.Text = "ref_anticoll : " + n.ToString() + " len=" + b.ToString();
            if (n == 0)
            {
                string hex = BitConverter.ToString(byteCard, 0, b);
                hex = hex.Replace("-", "");
                cardId = hex;
                //return 0;
            }
            else
            {
                cardId = "";
                return -2;
            }

            //拿到卡了，开始选卡
            byte size = 0;
            if (MasterReaderDevice.rf_select(0, byteCard, b, ref size) == 0)
            {
                return 0;
            }
            return -3;

        }

        public int ReadBlock(byte block, byte[] bs, ref  byte size)
        {
            return MasterReaderDevice.rf_M1_read(0, block, bs, ref size);
        }

        public int Authentication(byte block, string password)
        {
            byte m = 0x60; //model=0x61:验证B密钥 60 ,a
            byte[] bs = new byte[6];
            for (int i = 0; i < 6; i++)
            {
                bs[i] = Convert.ToByte(password.Substring(i * 2, 2), 16);

            }
            return MasterReaderDevice.rf_M1_authentication2(0, m, block, bs);
        }


        public int WritePassword(byte block, string key_a,string key_b)
        {
            //key -a
            byte[] bs = new byte[6];
            for (int i = 0; i < 6; i++)
            {
                bs[i] = Convert.ToByte(key_a.Substring(i * 2, 2), 16);

            }
            //key -b
            byte[] bsb = new byte[6];
            for (int i = 0; i < 6; i++)
            {
                bsb[i] = Convert.ToByte(key_b.Substring(i * 2, 2), 16);

            }

            BlockData bd = new BlockData();

            byte[] block_bs = new byte[16];

            Array.Copy(bs, 0, block_bs, 0, 6);
            Array.Copy(new byte[] { 0xff, 0x07, 0x80, 0x69 }, 0, block_bs, 6,4);
            Array.Copy(bsb, 0, block_bs, 10,6);

            string hex = BitConverter.ToString(block_bs);
           // return 0;
            return MasterReaderDevice.rf_M1_write(0, block, block_bs);
        }

        public int WriteBlock(byte block, BlockData bd)
        {



            //符合末行范围
            //如果是小区内的
            int small = 32 * 4 - 1;
            if (block < small )
            {
                //直接报警
                if ((block+1) % 4 == 0)
                {
                    throw new BlockException(block, "尝试写密码段");
                }
            }
            else
            {

                int bblock = (byte)(block - small);
                if (bblock % 16 == 0)
                {
                    throw new BlockException(block, "尝试写密码段");
                }
            }

            byte[] bs = bd.Bytes;
            return MasterReaderDevice.rf_M1_write(0, block, bs);
        }



        public BlockData ReadBlock(byte block)
        {
            byte[] buf = new byte[100];
            byte size = 0;
            List<int> ret = new List<int>();
            if (MasterReaderDevice.rf_M1_read(0, block, buf, ref size) == 0)
            {
                //取四个字节生成一个int
                return BlockData.BuildFromBytes(buf, size);
            }

            return null;
        }



        public byte GetAbsoluteBlock(int section, int block)
        {
            byte n = 0;

            if (section < 32)
            {
                n = (byte)(section * 4 + block);
            }
            else
            {
                n += (byte)(((section - 32) * 16) + (32 * 4 + block));


            }

            return n;
        }



      
    }
}
