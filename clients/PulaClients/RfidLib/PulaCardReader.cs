using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace PulaRfid
{
    public class PulaCardReader
    {
        const byte GREEN = 2;
        const byte RED = 1;


        private string password;
        private MasterReader reader;

//private int lastBlock;//最后读写的区块，用来判断是否需要重新发送认证命令

        public PulaCardReader()
        {
            reader = new MasterReader();
            this.password = PulaCardConstants.PASSWORD;
           // lastBlock = -1;
        }

        public bool Open(int com)
        {
            return reader.OpenComm(com, 14400);
        }

        public void PrepareCheck(string cardId)
        {

            string cid;
            int size = reader.SelectCurrent(out cid);

            if (size == 0 && cardId != "" && !cardId.Equals(cid))
            {
                throw new Exception("写入卡号和指定卡号不匹配，请换卡");
            }
            if (size != 0)
            {
                throw new Exception("选卡失败");
            }

            //lastBlock = -1;
        }


        public void InitCard()
        {
            string password = PulaCardConstants.MANUFACTORY_PASSWORD;

            string cid;
            int size = reader.SelectCurrent(out cid);

            /*if (size == 0 && cardId != "" && !cardId.Equals(cid))
            {
                throw new Exception("写入卡号和指定卡号不匹配，请换卡");
            }
            if (size != 0)
            {
                throw new Exception("选卡失败");
            }*/

            if (size != 0)
            {
                throw new Exception("选卡失败");
            }

            byte absblock = reader.GetAbsoluteBlock(0, 1);
            if (reader.Authentication(absblock, password) == 0)
            {
                int n = reader.WritePassword(3, PulaCardConstants.PASSWORD, PulaCardConstants.PASSWORD_B);

                if (n != 0)
                {
                    throw new Exception("初始化卡片失败");
                }

                reader.Light(GREEN);

            }
            else
            {
                reader.Light(RED);
                throw new Exception("卡片认证失败,并非厂方初始卡");

            }
        }

        public CardMeta ReadMeta()
        {
            reader.Light(RED);
            byte absblock = reader.GetAbsoluteBlock(0, 1);

            //头部存储是同一区数据，因此可一次全部读入，无需多次认证
            if (reader.Authentication(absblock, this.password) == 0)
            {
                BlockData bd = reader.ReadBlock(absblock);
                if (bd == null)
                {
                    throw new Exception("读取卡内信息失败");

                }
                absblock++;
                BlockData bd2 = reader.ReadBlock(absblock);
                if (bd == null)
                {
                    throw new Exception("读取卡内信息失败2");
                }
               
                //前12个字节,编号
                //接着4个字节,类型,
                //最后16个字节,姓名

                string no = KillZero(bd.AsString(0, 12));
                int type = bd.AsInteger(12);//-,16


                string name = KillZero(bd2.AsString(0, 16));

                CardMeta cm = new CardMeta();
                cm.no = no;
                cm.name = name;
                cm.type = type;

                reader.Light(GREEN);
                return cm;
            }

            throw new Exception("卡认证失败");

        }

        private string KillZero(string p)
        {
            return p.Replace("\0", "");
        }

        public int WriteMeta(CardMeta cm)
        {
            reader.Light(RED);
            //编号构成前12 ,然后其他构成后4+16
            byte[] no_bs = Encoding.UTF8.GetBytes(cm.no);
            byte[] name_bs = Encoding.UTF8.GetBytes(cm.name);
            

            BlockData bd = BlockData.CreateEmpty();
            bd.WriteBytes(no_bs,12);
            bd.WriteInt( cm.type );

            byte absblock = reader.GetAbsoluteBlock(0, 1);
            if (reader.Authentication(absblock, this.password) == 0)
            {
                int n = reader.WriteBlock(1, bd);

                //最后是个字符串
                if (n == 0)
                {
                    bd = BlockData.CreateEmpty();
                    bd.WriteBytes(name_bs, 16);
                    n = reader.WriteBlock(2, bd);
                    reader.Light(GREEN);
                }
                return n;
            }
            else
            {
                return -1;
            }


        }

        public void Close()
        {
            reader.CloseComm();
        }



        public string GetCard()
        {
            string cardid;
            int n = reader.SearchCard(out cardid);
            return cardid;
        }

        public void Buzz(byte p)
        {
            reader.Beep(p);
        }
    }
}
