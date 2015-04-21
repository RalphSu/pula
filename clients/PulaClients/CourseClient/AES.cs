using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace AES
{
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */
    /*  AES counter (CTR) mode implementation in PHP (c) Chris Veness 2005-2010. Right of free use is */
    /*    granted for all commercial or non-commercial use under CC-BY licence. No warranty of any    */
    /*    form is offered.                                                                            */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */
    public class AesCtr
    {
        public static string encrypt(string plaintext, string password, int nBits)
        {
            var blockSize = 16;  // block size fixed at 16 bytes / 128 bits (Nb=4) for AES
            if (!(nBits == 128 || nBits == 192 || nBits == 256)) return "";  // standard allows 128/192/256 bit keys
            // note PHP (5) gives us plaintext and password in UTF8 encoding!

            // use AES itself to encrypt password to get cipher key (using plain password as source for  
            // key expansion) - gives us well encrypted key
            var nBytes = nBits / 8;  // no bytes in key
            var pwBytes = new byte[nBytes];
            //byte[] password_bs  = System.Text.Encoding.UTF8.GetBytes(password);
            for (int i = 0; i < nBytes; i++)
            {
                byte cc = 0;
                if (password.Length > i )
                {
                    cc = (byte) password[i];
                }
                pwBytes[i] = (byte)(cc & 0xff);

            }
            var bs = AES.keyExpansion(pwBytes);
            var key = AES.cipher(pwBytes, bs,false);
            //key = array_merge($key, array_slice($key, 0, $nBytes-16));  // expand key to 16/24/32 bytes long 
            var _tmp = new byte[key.Length + nBytes - 16];
            System.Array.Copy(key, _tmp, key.Length);
            System.Array.Copy(key, 0, _tmp, key.Length, nBytes - 16);
            key = _tmp;


            // initialise counter block (NIST SP800-38A §B.2): millisecond time-stamp for nonce in 
            // 1st 8 bytes, block counter in 2nd 8 bytes
            var counterBlock = new byte[16];
            long nonce =  UnixTool.timestamp();   // timestamp: milliseconds since 1-Jan-1970
            var nonceSec = (long)Math.Floor((decimal)nonce / 1000);
            var nonceMs = nonce % 1000;
            //AESLogger.needLog(true);
            AESLogger.add(nonceMs.ToString());
            // encode nonce with seconds in 1st 4 bytes, and (repeated) ms part filling 2nd 4 bytes
            for (int i = 0; i < 4; i++) counterBlock[i] = ((byte)(AesCtr.urs(nonceSec, (byte)(i * 8)) & 0xff));
            for (int i = 0; i < 4; i++) counterBlock[i + 4] = (byte)(nonceMs & 0xff);
            // and convert it to a string to go on the front of the ciphertext
            //var ctrTxt = "";
            //for (int i=0; i<8; i++) ctrTxt += (char)(counterBlock[i]);
            //var allsb = new StringBuilder();
            MemoryStream ms = new MemoryStream();
            //for (int i = 0; i < 8; i++) allsb.Append((char)(counterBlock[i]));
            for (int i = 0; i < 8; i++) ms.WriteByte((counterBlock[i]));


            AESLogger.traceSingle(counterBlock);

            // generate key schedule - an expansion of the key into distinct Key Rounds for each round
            var keySchedule = AES.keyExpansion(key);
            //print_r($keySchedule);

            var blockCount = (int)Math.Ceiling((decimal)plaintext.Length / blockSize);
            var ciphertxt = new string[blockCount];  // ciphertext as array of strings

            for (var b = 0; b < blockCount; b++)
            {
                // set counter (block #) in last 8 bytes of counter block (leaving nonce in 1st 8 bytes)
                // done in two stages for 32-bit ops: using two words allows us to go past 2^32 blocks (68GB)
                for (var c = 0; c < 4; c++) counterBlock[15 - c] = (byte)(AesCtr.urs(b, (byte)(c * 8)) & 0xff);
                for (var c = 0; c < 4; c++) counterBlock[15 - c - 4] = (byte)(AesCtr.urs((int)(b / 0x100000000), (byte)(c * 8)));

                var cipherCntr = AES.cipher(counterBlock, keySchedule,false);  // -- encrypt counter block --

                // block size is reduced on final block
                var blockLength = b < blockCount - 1 ? blockSize : (plaintext.Length - 1) % blockSize + 1;
                var cipherByte = new byte[blockLength];
                //var sb = new StringBuilder();

                for (var i = 0; i < blockLength; i++)
                {  // -- xor plaintext with ciphered counter byte-by-byte --
                    cipherByte[i] = (byte)(cipherCntr[i] ^ (byte)(plaintext[b * blockSize + i]));
                    //转成字符串
                    //cipherByte[i] = (char)(cipherByte[i]);
                    //sb.Append((char)(cipherByte[i]));
                    ms.WriteByte( cipherByte[i] ) ;
                }
                //$ciphertxt[$b] = implode('', $cipherByte);  // escape troublesome characters in ciphertext
                //ciphertxt[b] = sb.ToString();
                //allsb.Append(sb.ToString());
            }

            // implode is more efficient than repeated string concatenation
            //var ciphertext = ctrTxt . implode('', $ciphertxt);
            //$ciphertext = base64_encode($ciphertext);
            //byte[] byteArray = System.Text.Encoding.Default.GetBytes(allsb.ToString());
            var byteArray = ms.ToArray();
            var ciphertext = Convert.ToBase64String(byteArray);
            return ciphertext;
        }


        /** 
   * Decrypt a text encrypted by AES in counter mode of operation
   *
   * @param ciphertext source text to be decrypted
   * @param password   the password to use to generate a key
   * @param nBits      number of bits to be used in the key (128, 192, or 256)
   * @return           decrypted text
   */
  public static string decrypt(string ciphertext, string password, int nBits) {
    int blockSize = 16;  // block size fixed at 16 bytes / 128 bits (Nb=4) for AES
    if (!(nBits==128 || nBits==192 || nBits==256)) return "";  // standard allows 128/192/256 bit keys

    byte[] bs=   Convert.FromBase64String(ciphertext);

    //$ciphertext = base64_decode($ciphertext);
  
    // use AES to encrypt password (mirroring encrypt routine)
    int nBytes = nBits/8;  // no bytes in key
    var pwBytes = new byte[nBytes];
    //for (int i=0; i<nBytes; i++) pwBytes[i] = ord(substr($password,$i,1)) & 0xff;

       for (int i = 0; i < nBytes; i++)
            {
                byte cc = 0;
                if (password.Length > i )
                {
                    cc = (byte) password[i];
                }
                pwBytes[i] = (byte)(cc & 0xff);

            }

    var key = AES.cipher(pwBytes, AES.keyExpansion(pwBytes),false);
    //$key = array_merge($key, array_slice($key, 0, $nBytes-16));  // expand key to 16/24/32 bytes long
                  var _tmp = new byte[key.Length + nBytes - 16];
            System.Array.Copy(key, _tmp, key.Length);
            System.Array.Copy(key, 0, _tmp, key.Length, nBytes - 16);
            key = _tmp;
    
    // recover nonce from 1st element of ciphertext
    var counterBlock = new byte[16];
    //$ctrTxt = substr($ciphertext, 0, 8);
    var ctrTxt = new byte[8];
    Array.Copy(bs,ctrTxt,8);
    for (int i=0; i<8; i++) counterBlock[i] = ctrTxt[i] ; // ord(substr($ctrTxt,$i,1));
    
    // generate key schedule
    var keySchedule = AES.keyExpansion(key);
  
    // separate ciphertext into blocks (skipping past initial 8 bytes)
    var nBlocks = (int ) Math.Ceiling( (decimal )( ( (double)bs.Length-8) / blockSize));
    var ct = new List<byte[]>();
    //for (var b=0; b<nBlocks; b++) ct[b] = substr($ciphertext, 8+$b*$blockSize, 16);
      for (var b=0; b<nBlocks; b++) {
          var startIndex = 8 + b * blockSize;
          var size = 16;
          //超标
          if (startIndex + 16 > bs.Length)
          {
              size = bs.Length - startIndex ;
          }
          var tmpbs = new byte[size];
           //= substr($ciphertext, 8+$b*$blockSize, 16);
          Array.Copy( bs,startIndex,tmpbs,0,size);
          ct.Add(tmpbs);

      }
    //$ciphertext = $ct;  // ciphertext is now array of block-length strings
  
    // plaintext will get generated block-by-block into array of block-length strings
    //$plaintxt = array();
      
	
	 //StringBuilder sb = new StringBuilder();
      byte[] all_bs = new byte[ bs.Length - 8  ];
      int counter = 0;
    for (int b=0; b<nBlocks; b++) {
      // set counter (block #) in last 8 bytes of counter block (leaving nonce in 1st 8 bytes)
        for (byte c = 0; c < 4; c++) counterBlock[15 - c] = (byte)(AesCtr.urs(b, (byte)(c * 8)) & 0xff);
        for (int c = 0; c < 4; c++) counterBlock[15 - c - 4] =
            //AesCtr::urs(($b+1)/0x100000000-1, $c*8) & 0xff;
            (byte)(AesCtr.urs((int)( (b+1) / 0x100000000), (byte)(c * 8))& 0xff);
            // (byte)(AesCtr.urs((int)((b + 1) / 0x100000000 - 1), (byte)(c * 8)) & 0xff);
  
      var cipherCntr = AES.cipher(counterBlock, keySchedule,false);  // encrypt counter block
      //AESLogger.needLog(true);
      AESLogger.traceSingle(counterBlock);
      AESLogger.add("-----------------" + b.ToString());
      //plaintxtByte = array();
      for (int i=0; i<ct[b].Length; i++) {
        // -- xor plaintext with ciphered counter byte-by-byte --
        //$plaintxtByte[$i] = $cipherCntr[$i] ^ ord(substr($ciphertext[$b],$i,1));
          byte bone = (byte)(cipherCntr[i] ^ ct[b][i]);
        //$plaintxtByte[$i] = chr($plaintxtByte[$i]);
          //sb.Append((char)bone);
          all_bs[counter] = bone;
          counter++;
      }	 
    }

	
  
    // join array of blocks into single plaintext string
    //$plaintext = implode('',$plaintxt);

    //System.Text.UnicodeEncoding converter = new System.Text.UnicodeEncoding();
    return System.Text.Encoding.UTF8.GetString(all_bs);
    

    //return sb.ToString();
  }

private static byte urs(long a,byte b)
{
 	 a = a & 0xffffffff; 
     b =(byte)( b & 0x1f);  // (bounds check)
    if ( (a&0x80000000)!=0 && b>0) {   // if left-most bit set
      a = (a>>1) & 0x7fffffff;   //   right-shift one bit & clear left-most bit
      a = a >> (b-1);           //   remaining right-shifts
    } else {                       // otherwise
      a = (a>>b);               //   use normal right-shift
    } 
    return (byte)a; 
}
    }

    class UnixTool{

            
internal static long timestamp()
{
 	DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1, 0, 0, 0, 0));

DateTime nowTime = DateTime.Now;

long unixTime = (long)Math.Round((nowTime - startTime).TotalMilliseconds, MidpointRounding.AwayFromZero);
    return unixTime;
}

internal static DateTime ConvertIntDateTime(long d)
{
    System.DateTime time = System.DateTime.MinValue;
    System.DateTime startTime = TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1));
    time = startTime.AddSeconds(d);
    return time;
}
    }

    class AESLogger
    {
        private static StreamWriter sw = null;
        private static bool _needLog;

        public static void init()
        {
            sw = new StreamWriter("d:/a.txt");
        }

        public static void trace(byte[,] s)
        {
            if (!_needLog) return;
            for (var i = 0; i < s.GetLength(0); i++)
            {
                for (var j = 0; j < s.GetLength(1); j++)
                {
                    add(" " + i + " , " + j + " =" + s[i, j].ToString());
                }
            }
        }

        public static void add(string s)
        {
            if (!_needLog) return;
            sw.WriteLine(s);
        }

        public static void close()
        {
            if (!_needLog) return;
            sw.Close();
            sw = null;
        }

        internal static void needLog(bool needLog)
        {
            _needLog = needLog;
        }

        internal static void traceSingle(byte[] s)
        {
            for (var i = 0; i < s.GetLength(0); i++)
            {
                
                    add(" " + i +" =" + s[i].ToString());
                
            }
        }
    }

    class AES
    {

        public static byte[] cipher(byte[] input, byte[,] w,bool needLog)// main cipher function [§5.1]
        {
            var Nb = 4; // block size (in words): no of columns in state (fixed at 4 for AES)
            var Nr = w.GetLength(0) / Nb - 1;// no of rounds: 10/12/14 for 128/192/256-bit keys


            var state = new byte[4, Nb];// initialise 4xNb byte-array 'state' with input [§3.4]

            for (int i = 0; i < (4 * Nb); i++)
            {
                state[i % 4, (int)Math.Floor(((decimal)i / 4))] = input[i];
            }

            state = AES.addRoundKey(state, w, 0, Nb);
           // AESLogger.needLog(needLog);
            for (int round = 1; round < Nr; round++)
            {  // apply Nr rounds
                state = AES.subBytes(state, Nb);
             //   AESLogger.trace(state);
               // AESLogger.add("------------------" + round);
                state = AES.shiftRows(state, Nb);
                state = AES.mixColumns(state, Nb);
                state = AES.addRoundKey(state, w, round, Nb);
            }

            state = AES.subBytes(state, Nb);
            state = AES.shiftRows(state, Nb);
            state = AES.addRoundKey(state, w, Nr, Nb);

            var output = new byte[4 * Nb];  // convert state to 1-d array before returning [§3.4]
            for (var i = 0; i < 4 * Nb; i++) output[i] = state[i % 4, (int)Math.Floor((decimal)(i / 4))];
            return output;

        }

        private static byte[,] mixColumns(byte[,] s, int Nb)// combine bytes of each col of state S [§5.1.3]
        {
            for (int c = 0; c < 4; c++)
            {
                var a = new byte[4];  // 'a' is a copy of the current column from 's'
                var b = new byte[4];  // 'b' is a•{02} in GF(2^8)
                for (int i = 0; i < 4; i++)
                {
                    a[i] = s[i, c];
                    if ((s[i, c] & 0x80) != 0)
                    {
                        b[i] = (byte)(s[i, c] << 1 ^ 0x011b);
                    }
                    else
                    {
                        b[i] = (byte)(s[i, c] << 1);
                    }
                }
                // a[n] ^ b[n] is a•{03} in GF(2^8)
                s[0, c] = (byte)(b[0] ^ a[1] ^ b[1] ^ a[2] ^ a[3]); // 2*a0 + 3*a1 + a2 + a3
                s[1, c] = (byte)(a[0] ^ b[1] ^ a[2] ^ b[2] ^ a[3]); // a0 * 2*a1 + 3*a2 + a3
                s[2, c] = (byte)(a[0] ^ a[1] ^ b[2] ^ a[3] ^ b[3]); // a0 + a1 + 2*a2 + 3*a3
                s[3, c] = (byte)(a[0] ^ b[0] ^ a[1] ^ a[2] ^ b[3]); // 3*a0 + a1 + a2 + 2*a3
            }
            return s;
        }

        private static byte[,] shiftRows(byte[,] s, int Nb)
        {// shift row r of state S left by r bytes [§5.1.2]
            var t = new byte[4];
            for (int r = 1; r < 4; r++)
            {
                for (int c = 0; c < 4; c++) t[c] = s[r, (c + r) % Nb];  // shift into temp copy
                for (int c = 0; c < 4; c++) s[r, c] = t[c];           // and copy back
            }          // note that this will work for Nb=4,5,6, but not 7,8 (always 4 for AES):
            return s;  // see fp.gladman.plus.com/cryptography_technology/rijndael/aes.spec.311.pdf 
        }

        private static byte[,] subBytes(byte[,] s, int Nb)
        {
            for (int r = 0; r < 4; r++)
            {
                for (int c = 0; c < Nb; c++) s[r, c] = sBox[s[r, c]];
            }
            return s;
        }

        private static byte[,] addRoundKey(byte[,] state, byte[,] w, int rnd, int Nb)// xor Round Key into state S [§5.1.4]
        {
            for (int r = 0; r < 4; r++)
            {
                for (int c = 0; c < Nb; c++) state[r, c] ^= w[rnd * 4 + c, r];
            }
            return state;
        }


         /**
   * Key expansion for Rijndael cipher(): performs key expansion on cipher key
   * to generate a key schedule
   *
   * @param key cipher key byte-array (16 bytes)
   * @return    key schedule as 2D byte-array (Nr+1 x Nb bytes)
   */
  public static byte[,] keyExpansion(byte[] key) {  // generate Key Schedule from Cipher Key [§5.2]
    var Nb = 4;              // block size (in words): no of columns in state (fixed at 4 for AES)
    var Nk = key.Length/4;  // key length (in words): 4/6/8 for 128/192/256-bit keys
    var Nr = Nk + 6;        // no of rounds: 10/12/14 for 128/192/256-bit keys

    var maxLen = (Nb * (Nr + 1));
    var w = new byte[maxLen,4];
    var temp = new byte[4];
  
    for (int i=0; i<Nk; i++) {
      //var r = new byte[]{key[4*i], key[4*i+1], key[4*i+2], key[4*i+3]};
      w[i,0] = key[4*i];
        w[i,1] = key[4*i+1];
        w[i,2] = key[4*i+2];
        w[i,3] = key[4*i+3];
    }
  
    for (int i=Nk; i<maxLen; i++) {
      //w[i] = new byte[Nb];//clear ?? 
      for (int t=0; t<4; t++) temp[t] = w[i-1,t];
      if (i % Nk == 0) {
        temp = AES.subWord(AES.rotWord(temp));
        for (int t=0; t<4; t++) temp[t] ^= AES.rCon[i/Nk,t];
      } else if (Nk > 6 && i%Nk == 4) {
          temp = AES.subWord(temp);
      }
      for (int t = 0; t < 4; t++) w[i, t] = (byte)(w[i - Nk, t] ^ temp[t]);
    }
    return w;
  }

        private static byte[] subWord(byte[] w) {    // apply SBox to 4-byte word w
    for (int i=0; i<4; i++) w[i] = AES.sBox[w[i]];
    return w;
  }
  
  private static byte[] rotWord(byte[] w) {    // rotate 4-byte word w left by one byte
    var tmp = w[0];
    for (int i=0; i<3; i++) w[i] = w[i+1];
    w[3] = tmp;
    return w;
  }

        private static byte[] sBox = new byte[]{
    0x63,0x7c,0x77,0x7b,0xf2,0x6b,0x6f,0xc5,0x30,0x01,0x67,0x2b,0xfe,0xd7,0xab,0x76,
    0xca,0x82,0xc9,0x7d,0xfa,0x59,0x47,0xf0,0xad,0xd4,0xa2,0xaf,0x9c,0xa4,0x72,0xc0,
    0xb7,0xfd,0x93,0x26,0x36,0x3f,0xf7,0xcc,0x34,0xa5,0xe5,0xf1,0x71,0xd8,0x31,0x15,
    0x04,0xc7,0x23,0xc3,0x18,0x96,0x05,0x9a,0x07,0x12,0x80,0xe2,0xeb,0x27,0xb2,0x75,
    0x09,0x83,0x2c,0x1a,0x1b,0x6e,0x5a,0xa0,0x52,0x3b,0xd6,0xb3,0x29,0xe3,0x2f,0x84,
    0x53,0xd1,0x00,0xed,0x20,0xfc,0xb1,0x5b,0x6a,0xcb,0xbe,0x39,0x4a,0x4c,0x58,0xcf,
    0xd0,0xef,0xaa,0xfb,0x43,0x4d,0x33,0x85,0x45,0xf9,0x02,0x7f,0x50,0x3c,0x9f,0xa8,
    0x51,0xa3,0x40,0x8f,0x92,0x9d,0x38,0xf5,0xbc,0xb6,0xda,0x21,0x10,0xff,0xf3,0xd2,
    0xcd,0x0c,0x13,0xec,0x5f,0x97,0x44,0x17,0xc4,0xa7,0x7e,0x3d,0x64,0x5d,0x19,0x73,
    0x60,0x81,0x4f,0xdc,0x22,0x2a,0x90,0x88,0x46,0xee,0xb8,0x14,0xde,0x5e,0x0b,0xdb,
    0xe0,0x32,0x3a,0x0a,0x49,0x06,0x24,0x5c,0xc2,0xd3,0xac,0x62,0x91,0x95,0xe4,0x79,
    0xe7,0xc8,0x37,0x6d,0x8d,0xd5,0x4e,0xa9,0x6c,0x56,0xf4,0xea,0x65,0x7a,0xae,0x08,
    0xba,0x78,0x25,0x2e,0x1c,0xa6,0xb4,0xc6,0xe8,0xdd,0x74,0x1f,0x4b,0xbd,0x8b,0x8a,
    0x70,0x3e,0xb5,0x66,0x48,0x03,0xf6,0x0e,0x61,0x35,0x57,0xb9,0x86,0xc1,0x1d,0x9e,
    0xe1,0xf8,0x98,0x11,0x69,0xd9,0x8e,0x94,0x9b,0x1e,0x87,0xe9,0xce,0x55,0x28,0xdf,
    0x8c,0xa1,0x89,0x0d,0xbf,0xe6,0x42,0x68,0x41,0x99,0x2d,0x0f,0xb0,0x54,0xbb,0x16};

        // rCon is Round Constant used for the Key Expansion [1st col is 2^(r-1) in GF(2^8)] [§5.2]
        private static byte[,] rCon = new byte[,] {
    {0x00, 0x00, 0x00, 0x00},
    {0x01, 0x00, 0x00, 0x00},
    {0x02, 0x00, 0x00, 0x00},
    {0x04, 0x00, 0x00, 0x00},
    {0x08, 0x00, 0x00, 0x00},
    {0x10, 0x00, 0x00, 0x00},
    {0x20, 0x00, 0x00, 0x00},
    {0x40, 0x00, 0x00, 0x00},
    {0x80, 0x00, 0x00, 0x00},
    {0x1b, 0x00, 0x00, 0x00},
    {0x36, 0x00, 0x00, 0x00}};
    }



}
