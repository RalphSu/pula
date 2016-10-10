#ifndef USINT
 #define USINT  unsigned short int   //2字节无符号
#endif
#ifndef UCHAR
 #define UCHAR  unsigned char        //1字节无符号
#endif
#ifndef UINT
 #define UINT   unsigned int         //4字节无符号
#endif

//#define UCHAR  unsigned char        //1字节无符号
//#define USINT  unsigned short int   //2字节无符号
//#define UINT   unsigned int         //4字节无符号





//将一字节数组转成16进制文本字符串
extern AnsiString f_bytetohex(UCHAR *,int);

//计算1字节数组的异或值
extern UCHAR f_getxor(UCHAR *buff,int ilen);

//根据16进制形式的字符串将其转化为字节数组
extern int f_stringtobin(AnsiString s1,int str_len,UCHAR *buff);

//判断1字符串是否全是数字,是返回0，不是非0
extern int f_string_is_number(AnsiString s1,int ilen);

//返回16字节格式的当前日期时间 "20030602224623"
extern AnsiString f_get_datetime(void);
