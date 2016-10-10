Attribute VB_Name = "mo_declare"
'******************通讯命令******************************************************************
'打开串口 int WINAPI rf_init_com(int port,long baud);
Public Declare Function rf_init_com Lib "MasterRD.dll" (ByVal port&, ByVal baud&) As Long
'关闭串口 int WINAPI rf_ClosePort();
Public Declare Function rf_ClosePort Lib "MasterRD.dll" () As Long



'******************************************MF1*****************************************
'int WINAPI rf_request(unsigned short icdev, unsigned char model, unsigned short *TagType);
Public Declare Function rf_request Lib "MasterRD.dll" (ByVal icdev%, ByVal model As Byte, _
               ByRef TagType%) As Long
               
'int WINAPI rf_anticoll(unsigned short icdev, unsigned char bcnt, unsigned char *ppSnr,
'unsigned char* pRLength);
Public Declare Function rf_anticoll Lib "MasterRD.dll" (ByVal icdev%, ByVal bcnt As Byte, _
               ByRef ppsnr As Byte, ByRef pRLength As Byte) As Long
               
'int WINAPI rf_select(unsigned short icdev,unsigned char *pSnr,unsigned char srcLen,
'unsigned char *Size);
Public Declare Function rf_select Lib "MasterRD.dll" (ByVal icdev%, ByRef pSnr As Byte, _
               ByVal srclen As Byte, ByRef size As Byte) As Long

'int WINAPI rf_M1_authentication2(unsigned short icdev,unsigned char model,unsigned char block,
'unsigned char *key);
Public Declare Function rf_M1_authentication2 Lib "MasterRD.dll" (ByVal icdev%, ByVal model As Byte, _
               ByVal block As Byte, ByRef key As Byte) As Long
               
'int WINAPI rf_M1_read(unsigned short icdev, unsigned char block, unsigned char *ppData,
'unsigned char *pLen);
Public Declare Function rf_M1_read Lib "MasterRD.dll" (ByVal icdev%, ByVal block As Byte, _
               ByRef buff As Byte, ByRef pLen As Byte) As Long

'int WINAPI rf_M1_write(unsigned short icdev, unsigned char block, unsigned char *data);
Public Declare Function rf_M1_write Lib "MasterRD.dll" (ByVal icdev%, ByVal block As Byte, _
               ByRef buff As Byte) As Long

'int WINAPI rf_halt(unsigned short icdev);
Public Declare Function rf_halt Lib "MasterRD.dll" (ByVal icdev%) As Long

          
























