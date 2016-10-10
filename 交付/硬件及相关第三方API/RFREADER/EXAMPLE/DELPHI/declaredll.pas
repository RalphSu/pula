unit declaredll;

interface
//通讯命令
    //打开串口 int WINAPI rf_init_com(int port,long baud);
    function rf_init_com(port,baud:integer):integer;stdcall;external 'MasterRD.dll';
    //关闭串口  int WINAPI rf_ClosePort();
    function rf_ClosePort():integer;stdcall;external 'MasterRD.dll';
    //释放memory
    procedure rf_free(var pData:byte);stdcall;external 'MasterRD.dll';
//MF 1
    //寻卡 int WINAPI rf_request(unsigned short icdev, unsigned char model, unsigned short *TagType);
    function rf_request(icdev:word;model:byte;var TagType:word):integer;stdcall;external 'MasterRD.dll';
    //防冲撞
    //int WINAPI rf_anticoll(unsigned short icdev, unsigned char bcnt, unsigned char **ppSnr,unsigned char* pRLength);
    function rf_anticoll(icdev:word;bcnt:byte;var ppSnr:byte;var pRLength:byte):integer;stdcall;external 'MasterRD.dll';
    //选卡
    //int WINAPI rf_select(unsigned short icdev,unsigned char *pSnr,unsigned char srcLen,unsigned char *Size)
    function rf_select(icdev:word;var pSnr:byte;srcLen:byte;var Size:byte):integer;stdcall;external 'MasterRD.dll';
    //读块
    //int WINAPI rf_M1_read(unsigned short icdev, unsigned char block, unsigned char **ppData,unsigned char *pLen);
    function rf_M1_read(icdev:word;block:byte;var  ppData:byte;
                   var pLen:byte):integer;stdcall;external 'MasterRD.dll';
    //写块
    //int WINAPI rf_M1_write(unsigned short icdev, unsigned char block, unsigned char *data);
    function rf_M1_write(icdev:word;block:byte;
                   var data:byte):integer;stdcall;external 'MasterRD.dll';
    //卡休眠  int WINAPI rf_halt(unsigned short icdev);
    function rf_halt(icdev:word):integer;stdcall;external 'MasterRD.dll';
    //身份认证int WINAPI rf_M1_authentication2(unsigned short icdev,unsigned char model,
    //unsigned char block,unsigned char *key);
    function  rf_M1_authentication2(icdev:word;model,block:byte;var key:byte):integer;stdcall;external 'MasterRD.dll';

implementation

end.
