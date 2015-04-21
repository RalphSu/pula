using System;
using System.Collections.Generic;
using System.Text;

namespace PulaRfid
{
    class ByteReader
    {

        public static int GetInt(byte[] src, int p)
        {
            byte[] bs = new byte[4];

            bs[0] = src[p];
            bs[1] = src[p + 1];
            bs[2] = src[p + 2];
            bs[3] = src[p + 3];

            return System.BitConverter.ToInt32(bs, 0);

        }

        public static long GetLong(byte[] src, int p)
        {
            byte[] bs = new byte[8];

            bs[0] = src[p];
            bs[1] = src[p + 1];
            bs[2] = src[p + 2];
            bs[3] = src[p + 3];
            bs[4] = src[p + 4];
            bs[5] = src[p + 5];
            bs[6] = src[p + 6];
            bs[7] = src[p + 7];

            return System.BitConverter.ToInt64(bs, 0);

        }
        public static short GetShort(byte[] src, int p)
        {
            byte[] bs = new byte[2];

            bs[0] = src[p];
            bs[1] = src[p + 1];
            return System.BitConverter.ToInt16(bs, 0);

        }

        internal static ushort GetUShort(byte[] src, int p)
        {
            byte[] bs = new byte[2];

            bs[0] = src[p];
            bs[1] = src[p + 1];
            return System.BitConverter.ToUInt16(bs, 0);
        }
    }


    public class ByteData
    {
        private byte[] bytes = null;

        private int pos, lastStep;

        public int Pos
        {
            get { return pos; }
            set { pos = value; }
        }


        public ByteData()
        {
            this.pos = 0;
        }

        public void MoveTo(int n)
        {
            this.pos = n;
        }

        public void Reset()
        {
            this.pos = 0;
        }

        

        public int GetInt()
        {
            lastStep = 4;
            int n = ByteReader.GetInt(this.bytes, pos );
            pos += lastStep;
            return n;
        }

        public long GetLong()
        {
            lastStep = 8;
            long l= ByteReader.GetLong(this.bytes, pos );
            pos += lastStep;
            return l;
        }

        public short GetShort()
        {
            lastStep = 2;
            short s= ByteReader.GetShort(this.bytes, pos );
            pos += lastStep;
            return s; 
        }


        /**
         * ×·¼Óbytes
         * **/

        public void AppendBytes(byte[] bs, int size)
        {

            if(this.bytes==null){
                this.bytes = new byte[size];
                Array.Copy(bs, 0, this.bytes, 0, size);
            }else{
                byte[] alldata = new byte[this.bytes.Length + size];

                this.bytes.CopyTo(alldata,0);
                Array.Copy(bs, 0, alldata, this.bytes.Length, size);
                this.bytes = alldata;
            }
        }


        public bool Eof()
        {
            if (pos >= this.bytes.Length)
            {
                return true; 
            }
            return false;
        }

        /*public Goods GetGoods()
        {
            lastStep = 2;
            ushort id = ByteReader.GetUShort(this.bytes, pos);
            pos += lastStep;

            lastStep = 2;
            short type = ByteReader.GetShort(this.bytes, pos);
            pos += lastStep;

            Goods g = new Goods(id,type);
            return g; 
        }*/
    }
}
