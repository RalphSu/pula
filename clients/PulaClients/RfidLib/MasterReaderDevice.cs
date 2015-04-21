using System;
using System.Collections.Generic;
using System.Text;
using System.Runtime.InteropServices;

namespace PulaRfid
{
    public class MasterReaderDevice
    {

        [DllImport("MasterRD.dll")]
        public static extern int rf_init_com(int port, System.Int32 baud);

        //int WINAPI rf_beep(unsigned short icdev, unsigned char msec)
        [DllImport("MasterRD.dll")]
        public static extern int rf_beep(System.UInt16 icdev, System.Byte mesc);

        //int WINAPI rf_light(unsigned short icdev, unsigned char color)
        [DllImport("MasterRD.dll")]
        public static extern int rf_light(System.UInt16 icdev, System.Byte color);


        [DllImport("MasterRD.dll")]
        public static extern int rf_ClosePort();

        [DllImport("MasterRD.dll")]
        public static extern int rf_get_model(ushort icdev,
                              ref byte pVersion,
                              ref byte pLen);

        //int WINAPI rf_init_type(unsigned short icdev, unsigned char type)
        [DllImport("MasterRD.dll")]
        public static extern int rf_init_type(ushort icdev, byte type);

        /*
         * int WINAPI rf_request(unsigned short icdev, 
                            unsigned char  model, 
                            unsigned short *pTagType)

         * 
         */

        [DllImport("MasterRD.dll")]
        public static extern int rf_request(ushort icdev, byte model,ref ushort pTagType);

        /**
         * int WINAPI rf_anticoll(unsigned short icdev, 
                             unsigned char  bcnt,
                             unsigned char  *pSnr,
                             unsigned char  *pLen)
*/

        [DllImport("MasterRD.dll")]
        public static extern int rf_anticoll(ushort icdev, byte bcnt, [Out,   MarshalAs(UnmanagedType.LPArray,   SizeConst=180)]   byte[] pSnr ,ref byte pLen);


        /**
         * int WINAPI rf_select(unsigned short icdev,
                           unsigned char  *pSnr, 
                           unsigned char  snrLen,
                           unsigned char  *pSize)

         **/
        [DllImport("MasterRD.dll")]
        public static extern int rf_select(ushort icdev,  [Out, MarshalAs(UnmanagedType.LPArray, SizeConst = 180)] byte[] pSnr, byte snrLen, ref byte pSize);


        /**
         * int WINAPI rf_M1_authentication2(unsigned short icdev, 
                                       unsigned char  model, 
                                       unsigned char  block,
                                       unsigned char  *pKey)
        */
        [DllImport("MasterRD.dll")]
        public static extern int rf_M1_authentication2(ushort icdev, byte model,byte block, [Out, MarshalAs(UnmanagedType.LPArray, SizeConst = 180)]   byte[] pKey);

        /**
         * int WINAPI rf_M1_read (unsigned short icdev, 
                             unsigned char  block, 
                             unsigned char  *pData,
                             unsigned char  *pLen)

         */
        [DllImport("MasterRD.dll")]
        public static extern int rf_M1_read(ushort icdev, byte block, [Out, MarshalAs(UnmanagedType.LPArray, SizeConst = 180)]   byte[] pData, ref byte pLen);

        /**
         * int WINAPI rf_M1_write (unsigned short icdev, 
                              unsigned char  block,
                              unsigned char *pData)**/

        [DllImport("MasterRD.dll")]
        public static extern int rf_M1_write(ushort icdev, byte block, [Out, MarshalAs(UnmanagedType.LPArray, SizeConst = 180)]   byte[] pData);



    }
}
