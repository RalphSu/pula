#ifndef _EXPORT_FUNC_H_
#define _EXPORT_FUNC_H_

//return successful result
#define  LIB_SUCCESS      0  
//return failed result
#define  LIB_FAILED       1

#define MAX_RF_BUFFER     1024

/******** 功能：获取动态库版本号 2字节 *******************/
//  返回: 成功返回0
/*********************************************************/
int (WINAPI* lib_ver)(unsigned int *nVer);


/*****************DES 算法加密函数 ***********************/
//user     : 
//         DES 算法加密函数
//Parameter: 
//         szOut:输出的DES值，长度等于明文长度
//         szIn: 明文
//         inlen:明文长度,8字节的整数倍
//         key:密文
//         keylen: 密文长度,如果大于8字节，是3des,如果小于等于8字节单des.不足补零
//return value: 
//         Success: LIB_SUCCESS ;
//         Failed : LIB_FAILED
/*********************************************************/
int (WINAPI* des_encrypt)(unsigned char *szOut,unsigned char *szIn , unsigned int inlen,unsigned char *key,unsigned int keylen);


/******** 说明：DES 解密算法函数 *************************/
//user     : 
//         DES 算法解密函数
//Parameter: 
//         szOut:输出的DES值，长度等于明文长度
//         szIn: 明文
//         inlen:明文长度,8字节的整数倍
//         key:密文
//         keylen: 密文长度,如果大于8字节，是3des,如果小于等于8字节单des.不足补零
//return value: 
//         Success: LIB_SUCCESS ;
//         Failed : LIB_FAILED
/*********************************************************/
int (WINAPI* des_decrypt)(unsigned char *szOut,unsigned char *szIn , unsigned int inlen,unsigned char *key,unsigned int keylen);


/******** 功能：初始化串口 *******************************/
//  参数：port：串口号，取值为1～4
//        baud：为通讯波特率4800～115200
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_init_com)(int port,long baud);


/******** 功能：指定设备标识 *****************************/
//  参数：icdev：通讯设备标识符，0-65536
//  返回：成功返回0
/*********************************************************/
int (WINAPI* rf_init_device_number)(unsigned short icdev);


/******** 功能：读取设备标识 *****************************/
//  参数：Icdev：通讯设备标识符
//  返回：成功返回0
/*********************************************************/
int (WINAPI* rf_get_device_number)(unsigned short *Icdev);


/******** 功能：取得读写卡器硬件版本号，2 字节 ***********/
//  参数：icdev：  通讯设备标识符
//        Version：存放返回版本信息
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_get_model)(unsigned short icdev,unsigned short *Version);


/******** 功能：取得读写卡器产品序列号，8 字节 ***********/
//  参数：icdev：通讯设备标识符
//        Snr：  存放返回读写卡器产品序列号
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_get_snr)(unsigned short icdev,unsigned char *Snr);


/******** 功能：蜂鸣 *************************************/
//  参数：icdev：通讯设备标识符
//        msec： 蜂鸣时限，单位是10 毫秒
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_beep)(unsigned short icdev,unsigned char msec);


/******** 功能：设置读写卡器sam 卡通讯波特率 *************/
//  参数：icdev：通讯设备标识符
//        bound: sam 卡波特率，取值为9600、38400
//  返回：成功则返回0
//  说明：bound=0:9600
//        bound=1:38400
/*********************************************************/
int (WINAPI* rf_init_sam)(unsigned short icdev,unsigned char bound);


/******* 功能：复位sam 卡 ********************************/
//  参数：icdev： 通讯设备标识符
//        pData：返回的复位信息内容
//        pMsgLg：返回复位信息的长度
//  返回：成功则返回0
//  说明：
/*********************************************************/
int (WINAPI* rf_sam_rst)(unsigned short icdev, unsigned char *pData,unsigned char *pMsgLg);
/*例：
     unsigned int icdev;
     unsigned char pData[MAX_RF_BUFFER];
     unsigned char len;
     status = rf_sam_rst(icdev,pData,&len);
     
*/


/******** 功能：向SAM 卡发送COS 命令 *********************/
//  参数：icdev：  通讯设备标识符
//        command：cos 命令
//        cmdLen:  cos 命令长度
//        pDate： 卡片返回的数据，含SW1、SW2
//        pMsgLg： 返回数据长度
//  返回：成功则返回0
//  说明：
/*********************************************************/
int (WINAPI* rf_sam_cos)(unsigned short icdev, unsigned char *command,unsigned char cmdLen ,unsigned char *pData,unsigned char* Length);
/*例：
     unsigned char icdev;
     unsigned char* cmd;
     unsigned char pData[MAX_RF_BUFFER];
     unsigned char len;
     status = rf_sam_cos(icdev,cmd,sizeof(cmd),pData,&len);
     
*/


/*******  功能：设置读写卡器非接触工作方式为 *************/ 
//              ISO14443 TYPE A OR ISO14443 TYPE B
//  参数：icdev：通讯设备标识符
//        type:  读写卡器工作方式
//  返回：成功则返回0
//  说明：type='A':设置为TYPE_A方式
//        type='B':设置为TYPE_B方式
/*********************************************************/
int (WINAPI* rf_init_type)(unsigned short icdev,unsigned char type);


/*******  功能：关闭或启动读写卡器天线发射 ***************/
//  参数：icdev：通讯设备标识符
//        model：天线状态
//  返回：成功则返回0
//  说明：model=0:关闭天线
//        model=1:开启天线
/*********************************************************/
int (WINAPI* rf_antenna_sta)(unsigned short icdev, unsigned char model);


/******** 功能：寻ISO14443-3 TYPE_A 卡 *******************/
//  参数：icdev：  通讯设备标识符
//        model：  寻卡模式
//        TagType：返回卡类型值
//  返回：成功则返回0
//  说明：mode=0x26:寻未进入休眠状态的卡
//        mode=0x52:寻所有状态的卡
/*********************************************************/
int (WINAPI* rf_request)(unsigned short icdev, unsigned char model, unsigned short *TagType);


/********* 功能：ISO14443-3 TYPE_A 卡防冲撞 **************/
//  参数：icdev：  通讯设备标识符
//        bcnt：   卡序列号字节数，取值4、7、10，Mifare 卡取值4
//        pSnr：  返回的卡序列号
//        pRLength:卡序列号长度
//  返回：成功则返回0
//  说明：
/*********************************************************/
int (WINAPI* rf_anticoll)(unsigned short icdev, unsigned char bcnt, unsigned char *pSnr,unsigned char* pRLength);
/*例：int status
      unsigned char icdev;
      unsigned char snr[MAX_RF_BUFFER];
      unsigned char len;
      status = rf_anticoll(icdev,4,snr,&len);      
*/


/******** 功能：锁定一张ISO14443-3 TYPE_A 卡 *************/
//  参数：icdev：通讯设备标识符
//        pSnr： 卡序列号
//        srcLen:卡序列号长度，MifareOne卡该值等于4
//        Size： 返回卡容量
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_select)(unsigned short icdev,unsigned char *pSnr,unsigned char srcLen,unsigned char *Size);


/******* 功能：命令已激活的ISO14443-3 TYPE_A卡进入休眠状态*/
//  参数：icdev：通讯设备标识符
//  返回：成功则返回0
/**********************************************************/
int (WINAPI* rf_halt)(unsigned short icdev);

/****** 功能：向读写卡器下载Mifare One 卡密钥 ******************/
//每6 个字节为1 个密钥，0～15 扇区顺序排列
//  参数：icdev：通讯设备标识符
//        Mode：密钥类型,取值‘A’OR ‘B’
//        key：密钥，96 字节
//  返回：成功则返回0
/***************************************************************/
int (WINAPI* rf_download_key)(WORD icdev, unsigned char mode, unsigned char *key);

/***** 功能：用已下载到读写卡器中的密钥验证Mifare One 卡某一扇区 ****/
//   参数：icdev：通讯设备标识符
//         mode：密码验证模式, 取值0 或1，代表‘A’OR ‘B’
//         secnr：要验证密码的扇区号（0～15）
//   返回：成功则返回0
/********************************************************************/
int (WINAPI* rf_M1_authentication1)(WORD icdev, unsigned char mode, unsigned char secnr);

/***** 功能：用指定的密钥验证Mifare One 卡*****************/
//  参数：icdev：通讯设备标识符
//        model：密码验证模式
//        block：要验证密码的绝对块号（0～63）
//        key：  密钥内容，6 字节
//  返回：成功则返回0
//  说明：model=0x60:验证A密钥
//        model=0x61:验证B密钥
/**********************************************************/
int (WINAPI* rf_M1_authentication2)(unsigned short icdev,unsigned char model,unsigned char block,unsigned char *key);


/*******  功能：读取Mifare One 卡一块数据 ****************/
//  参数：icdev： 通讯设备标识符
//        block： M1卡绝对块号（0～63）
//        pData：读出数据
//        pLen:   读出数据的长度
//  返回：成功则返回0
//  说明：
/*********************************************************/
int (WINAPI* rf_M1_read)(unsigned short icdev, unsigned char block, unsigned char *pData,unsigned char *pLen);
/*例：int status
      unsigned short icdev
      unsigned char pData[MAX_RF_BUFFER];
      unsigned char len;
      status = rf_M1_read(icdev,0,pData,&len);
      
*/


/*******  功能：向Mifare One 卡中写入一块数据 ************/
//  参数：icdev：通讯设备标识符
//        block：M1卡绝对块号（0～63)
//        data： 写入的数据，16 字节
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_M1_write)(unsigned short icdev, unsigned char block, unsigned char *data);


/*******  功能：将Mifare One 卡某一扇区初始化为钱包 *******/
//  参数：icdev：通讯设备标识符
//        block：M1 卡块地址（0～63）
//        value：初始值
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_M1_initval)(unsigned short icdev, unsigned char block, long value);


/*******  功能：读Mifare One 钱包值 **********************/
//  参数：icdev： 通讯设备标识符
//        block： M1 卡块地址（0～63）
//        pValue：返回的值
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_M1_readval)(unsigned short icdev, unsigned char block,long* pValue);


/*******  功能：Mifare One 扣款 **************************/
//  参数：icdev：通讯设备标识符
//        block：M1 卡块地址（0～63）
//        value：要扣的值
//  返回：成功则返回0
//  说明：此函数执行成功后，结果保存在卡片的BUFFER 内，
//        尚未改写相应块的内容，若要将结果保存到卡片
//        相应块中需紧跟执行rf_M1_restore 函数
/*********************************************************/
int (WINAPI* rf_M1_decrement)(unsigned short icdev, unsigned char block,long value);


/******** 功能：Mifare One 充值 **************************/
//  参数：icdev：通讯设备标识符
//        block：M1 卡块地址（0～63）
//        value：要增加的值
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_M1_increment)(unsigned short icdev, unsigned char block,long value);


/******** 功能：Mifare One 卡值回传 **********************/
//  参数：icdev：通讯设备标识符
//        block：M1 卡块地址（0～63）
//  返回：成功则返回0
//  说明：用此函数将指定的块内容传入卡的buffer，然后可用
//        rf_M1transfer()函数将buffer 中数据再传送到另一块中去
/*********************************************************/
int (WINAPI* rf_M1_restore)(unsigned short icdev, unsigned char block);


/****** 功能：将Mifare One数据传送 ***********************/
//  参数：icdev：通讯设备标识符
//        block：M1 卡块地址（0～63）
//  返回：成功则返回0
//  说明：该函数仅在increment、decrement和restore 命令之后调用。
/*********************************************************/
int (WINAPI* rf_M1_transfer)(unsigned short icdev, unsigned char block);


/******** 功能：复位符合ISO14443-A 标准的CPU 卡 **********/
//  参数：icdev： 通讯设备标识符
//        model:  寻卡方式
//        pDate：返回的复位信息内容
//        pMsgLg：返回复位信息长度
//  返回：成功则返回0
//  说明：
/*********************************************************/
int (WINAPI* rf_typea_rst)(unsigned short icdev,unsigned char model,unsigned char *pData,unsigned char *pMsgLg);
/*例：int status
      unsigned short icdev
      unsigned char pData[MAX_RF_BUFFER];
      unsigned char len;
      status = rf_typea_rst(icdev,0,pData,&len);      
*/


/******** 功能：向符合ISO14443-4标准的CPU卡发送COS 命令***/
//  参数：icdev：  通讯设备标识符
//        command：cos 命令
//        cmdLen:  cos 命令长度
//        pDate： 卡片返回的数据，含SW1、SW2
//        pMsgLg： 返回数据长度
//  返回：成功则返回0
//  说明：
/*********************************************************/
int (WINAPI* rf_cos_command)(unsigned short icdev,unsigned char *command,unsigned char cmdLen,unsigned char *pData,unsigned char* pMsgLg);
/*例：int status
      unsigned short icdev
      unsigned char* cmd;
      unsigned char pData[MAX_RF_BUFFER];
      unsigned char len;
      status = rf_typea_cos(icdev,cmd,sizeof(cmd),pData,&len);      
*/


/******** 功能：激活符合ISO14443 TYPE_B 标准的卡 *********/
//  参数：icdev： 通讯设备标识符
//        model： 寻卡方式0＝REQB,1=WUPB
//        pDate：卡片返回的数据
//        pMsgLg：返回数据的长度
//  返回：成功则返回0
//  说明：
/*********************************************************/
int (WINAPI* rf_atqb)(unsigned short icdev,unsigned char model,unsigned char *pData,unsigned char *pMsgLg);
/*例：int status
      int icdev
      unsigned char msglg
      unsigned char pDate[MAX_RF_BUFFER];
      status = rf_atqb(icdev,0,pDate,&msglg);      
*/

/*********  功能：激活已寻到的符合ISO14443-B 标准的卡 ************/
//  参数：icdev：通讯设备标识符
//        PUPI ： 卡片唯一标识符
//        CID  ： 指定该卡片使用的逻辑地址,取值0～14 且小于slotmax
//  返回：成功则返回0
/*****************************************************************/
int (WINAPI* rf_attrib)(WORD icdev, unsigned long PUPI, unsigned char CID);

/*********  功能：向符合ISO14443-B 标准的CPU 卡发送COS 命令 ******/
//  参数：icdev：通讯设备标识符
//        CID  ：卡片逻辑地址
//        command：cos 命令
//        MsgLg：返回数据长度
//        Date： 卡片返回的数据，含SW1、SW2
//  返回：成功则返回0
/*****************************************************************/

int (WINAPI* rf_typeb_cos)(WORD icdev, unsigned char CID,unsigned char *command, unsigned char cmdLen,unsigned char *pData,unsigned char *pMsgLg);
/*
例：int status
int icdev
unsigned char *command;
unsigned char msglg;
unsigned char pData[MAX_RF_BUFFER];
status = rf_typeb_cos(icdev,0,command,sizeof(command),pData,&msglg);
*/
/******** 功能：命令一选中的TYPE_B卡进入HALT 状态*********/
//  参数：icdev：通讯设备标识符
//        PUPI： 卡片唯一标识符
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_hltb)(unsigned short icdev,unsigned long PUPI);
//??

/******** 功能：验证AT88RF020 卡密码 *********************/
//  参数：icdev：通讯设备标识符
//        key：  密码，8 字节
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_at020_check)(unsigned short icdev, unsigned char *key);


//******* 功能：读AT88RF020 卡一页数据 *******************/
//  参数：icdev： 通讯设备标识符
//        page：  页地址，（0～31）
//        pDate：返回的数据
//        pMsgLen:返回数据的长度
//  返回：成功则返回0
//  说明：
/*********************************************************/
int (WINAPI* rf_at020_read)(unsigned short icdev, unsigned char page, unsigned char *pData,unsigned char* pMsgLen); 
/*
例：int status
    int icdev
    unsigned char pData[MAX_RF_BUFFER];
    unsigned char len;
    status = rf_at020_read(icdev,0,pData,&len);    
*/


/******** 功能：写AT88RF020 卡一页数据 *******************/
//  参数：icdev：通讯设备标识符
//        page： 页地址，（0～31）
//        Date： 要写入的数据，8 字节
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_at020_write)(unsigned short icdev, unsigned char page, unsigned char *data);
 

/******** 功能：LOCK AT88RF020卡**************************/
//  参数：icdev：通讯设备标识符
//        date： 数据，4 字节
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_at020_lock)(unsigned short icdev,unsigned char *data);


/******** 功能：AT88RF020卡计数函数 **********************/
//  参数：icdev：通讯设备标识符
//        date： 数据，6 字节
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_at020_count)(unsigned short icdev,unsigned char *data);


/******** 功能：命令AT88RF020 卡进入HALT 状态 ************/
//  参数：icdev：通讯设备标识符
//  返回：成功则返回0
/*********************************************************/
int (WINAPI* rf_at020_deselect)(unsigned short icdev);


/******** 功能：控制灯的颜色 *****************************/
//  参数：icdev：通讯设备标识符
//        color: 0 ,熄灯
//               1 ,红灯
//               2 ,绿灯
//               3 ,黄灯
//  返回：成功返回0
/*********************************************************/
int (WINAPI* rf_light)(unsigned short icdev,unsigned char color);



/******** 功能：关闭Com端口 ******************************/
//  返回：成功返回0
/*********************************************************/
int (WINAPI* rf_ClosePort)();


/******** 功能：返回状态字 *******************************/
//  返回：错误代码
/*********************************************************/
int (WINAPI* rf_GetErrorMessage)();


#endif