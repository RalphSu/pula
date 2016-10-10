#include <vcl.h>
#include "myfunc.h"
#include <strutils.hpp>
//返回16字节格式的当前日期时间 "20030602224623"
AnsiString f_get_datetime(void)
{
 AnsiString s1;
 DateTimeToString(s1,"yyyymmddhhnnss",Now());
 return s1;
}
//判断1字符串是否全是数字,是返回0，不是非0
int f_string_is_number(AnsiString s1,int ilen)
{
 bool b1=false;int i;
 for(i=1;i<=ilen;i++)
 {
  if((MidStr(s1,i,1)>'\x39')||(MidStr(s1,i,1)<'\x30'))
  {
   b1=true;
   break;
  }
 }
 if(b1) return 1;
 else   return 0;

}
//将1字节数组转化为对应16进制字符串
AnsiString f_bytetohex(UCHAR *buff,int ilen)
{
 AnsiString s1;
 int i;
 s1="";
 for(i=0;i<ilen;i++)
 {
   s1=s1+IntToHex(buff[i],2);
 };
 return s1;
}
//计算1字节数组的异或值
UCHAR f_getxor(UCHAR *buff,int ilen)
{
 UCHAR b1;int i;
 for(i=0,b1=0;i<ilen;i++)
 {
   b1=b1 ^ buff[i];
 };
 return b1;
}
//根据16进制形式的字符串将其转化为字节数组
int f_stringtobin(AnsiString s1,int str_len,UCHAR *buff)
{
 int i;AnsiString s2;
 for(i=0;i<str_len/2;i++)
 {
 s2= "0x" +MidStr(s1,i*2+1,2);
 buff[i]=StrToInt(s2);
 };
 return 0;
}
