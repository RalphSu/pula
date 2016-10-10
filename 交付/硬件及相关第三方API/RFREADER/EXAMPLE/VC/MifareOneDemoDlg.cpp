// MifareOneDemoDlg.cpp : implementation file
//

#include "stdafx.h"
#include "MifareOneDemo.h"
#include "MifareOneDemoDlg.h"

#include "ExportFunc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

#define BAUD_NUMBER  7
int BaudValue[BAUD_NUMBER] ={ 9600,14400,19200,28800,38400,57600,115200};
/////////////////////////////////////////////////////////////////////////////
// CMifareOneDemoDlg dialog

CMifareOneDemoDlg::CMifareOneDemoDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CMifareOneDemoDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CMifareOneDemoDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
	// Note that LoadIcon does not require a subsequent DestroyIcon in Win32
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	m_hInstMaster = NULL;
}

void CMifareOneDemoDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CMifareOneDemoDlg)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	DDX_Control(pDX, IDC_EDIT_DATA, m_edit_data);
	DDX_Control(pDX, IDC_EDIT_KEY, m_edit_key);
	DDX_Control(pDX, IDC_EDIT_SERIAL, m_edit_serial);
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CMifareOneDemoDlg, CDialog)
	//{{AFX_MSG_MAP(CMifareOneDemoDlg)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_WM_DESTROY()
	ON_BN_CLICKED(IDC_BUTTON_SEARCH, OnButtonSearch)
	ON_BN_CLICKED(IDC_BUTTON_READ, OnButtonRead)
	ON_BN_CLICKED(IDC_BUTTON_WRITE, OnButtonWrite)
	ON_BN_CLICKED(IDC_BUTTON_HALT, OnButtonHalt)
	ON_CBN_SELCHANGE(IDC_COMBO_RAUD, OnSelchangeComboRaud)
	ON_CBN_SELCHANGE(IDC_COMBO_PORT, OnSelchangeComboRaud)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CMifareOneDemoDlg message handlers

BOOL CMifareOneDemoDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon
	
	// TODO: Add extra initialization here
	TCHAR szBuf[MAX_PATH];	
	GetModuleFileName(NULL, (LPTSTR)szBuf, MAX_PATH);
	*strrchr( szBuf, '\\' ) = 0;    
	strcat(szBuf, _T("\\MasterRD.dll"));

	m_hInstMaster = LoadLibrary(szBuf);	 

	if(m_hInstMaster){
        (FARPROC&)lib_ver               = GetProcAddress(m_hInstMaster,_T("lib_ver"));
		(FARPROC&)des_encrypt           = GetProcAddress(m_hInstMaster,_T("des_encrypt"));
		(FARPROC&)des_decrypt           = GetProcAddress(m_hInstMaster,_T("des_decrypt"));				
		(FARPROC&)rf_init_com           = GetProcAddress(m_hInstMaster,_T("rf_init_com"));
		(FARPROC&)rf_init_device_number = GetProcAddress(m_hInstMaster,_T("rf_init_device_number"));
		(FARPROC&)rf_get_device_number  = GetProcAddress(m_hInstMaster,_T("rf_get_device_number"));
		(FARPROC&)rf_get_model          = GetProcAddress(m_hInstMaster,_T("rf_get_model"));
		(FARPROC&)rf_get_snr            = GetProcAddress(m_hInstMaster,_T("rf_get_snr"));
		(FARPROC&)rf_beep               = GetProcAddress(m_hInstMaster,_T("rf_beep"));
		(FARPROC&)rf_init_sam           = GetProcAddress(m_hInstMaster,_T("rf_init_sam"));
		(FARPROC&)rf_sam_rst            = GetProcAddress(m_hInstMaster,_T("rf_sam_rst"));
		(FARPROC&)rf_sam_cos            = GetProcAddress(m_hInstMaster,_T("rf_sam_cos"));
		(FARPROC&)rf_init_type          = GetProcAddress(m_hInstMaster,_T("rf_init_type"));
		(FARPROC&)rf_antenna_sta        = GetProcAddress(m_hInstMaster,_T("rf_antenna_sta"));
		(FARPROC&)rf_request            = GetProcAddress(m_hInstMaster,_T("rf_request"));
		(FARPROC&)rf_anticoll           = GetProcAddress(m_hInstMaster,_T("rf_anticoll"));
		(FARPROC&)rf_select             = GetProcAddress(m_hInstMaster,_T("rf_select"));
		(FARPROC&)rf_halt               = GetProcAddress(m_hInstMaster,_T("rf_halt"));
		(FARPROC&)rf_download_key       = GetProcAddress(m_hInstMaster,_T("rf_download_key"));
		(FARPROC&)rf_M1_authentication1 = GetProcAddress(m_hInstMaster,_T("rf_M1_authentication1"));
		(FARPROC&)rf_M1_authentication2 = GetProcAddress(m_hInstMaster,_T("rf_M1_authentication2"));
		(FARPROC&)rf_M1_read            = GetProcAddress(m_hInstMaster,_T("rf_M1_read"));
		(FARPROC&)rf_M1_write           = GetProcAddress(m_hInstMaster,_T("rf_M1_write"));
		(FARPROC&)rf_M1_initval         = GetProcAddress(m_hInstMaster,_T("rf_M1_initval"));
		(FARPROC&)rf_M1_readval         = GetProcAddress(m_hInstMaster,_T("rf_M1_readval"));
		(FARPROC&)rf_M1_decrement       = GetProcAddress(m_hInstMaster,_T("rf_M1_decrement"));
		(FARPROC&)rf_M1_increment       = GetProcAddress(m_hInstMaster,_T("rf_M1_increment"));
		(FARPROC&)rf_M1_restore         = GetProcAddress(m_hInstMaster,_T("rf_M1_restore"));
		(FARPROC&)rf_M1_transfer        = GetProcAddress(m_hInstMaster,_T("rf_M1_transfer"));
		(FARPROC&)rf_typea_rst          = GetProcAddress(m_hInstMaster,_T("rf_typea_rst"));
		(FARPROC&)rf_cos_command        = GetProcAddress(m_hInstMaster,_T("rf_cos_command"));
		(FARPROC&)rf_atqb               = GetProcAddress(m_hInstMaster,_T("rf_atqb"));
		(FARPROC&)rf_attrib             = GetProcAddress(m_hInstMaster,_T("rf_attrib"));
		(FARPROC&)rf_typeb_cos          = GetProcAddress(m_hInstMaster,_T("rf_typeb_cos"));
		(FARPROC&)rf_hltb               = GetProcAddress(m_hInstMaster,_T("rf_hltb"));
		(FARPROC&)rf_at020_check        = GetProcAddress(m_hInstMaster,_T("rf_at020_check"));
		(FARPROC&)rf_at020_read         = GetProcAddress(m_hInstMaster,_T("rf_at020_read"));
		(FARPROC&)rf_at020_write        = GetProcAddress(m_hInstMaster,_T("rf_at020_write"));
		(FARPROC&)rf_at020_lock         = GetProcAddress(m_hInstMaster,_T("rf_at020_lock"));
		(FARPROC&)rf_at020_count        = GetProcAddress(m_hInstMaster,_T("rf_at020_count"));
		(FARPROC&)rf_at020_deselect     = GetProcAddress(m_hInstMaster,_T("rf_at020_deselect"));
		(FARPROC&)rf_light              = GetProcAddress(m_hInstMaster,_T("rf_light"));
		(FARPROC&)rf_ClosePort          = GetProcAddress(m_hInstMaster,_T("rf_ClosePort"));
		(FARPROC&)rf_GetErrorMessage    = GetProcAddress(m_hInstMaster,_T("rf_GetErrorMessage"));
		
		
		
		if(NULL == lib_ver                ||
			NULL == des_encrypt    ||
			NULL == des_decrypt    ||			
			NULL == rf_init_com           ||
			NULL == rf_init_device_number ||
			NULL == rf_get_device_number    ||
			NULL == rf_get_model    ||
			NULL == rf_beep    ||
			NULL == rf_init_sam    ||
			NULL == rf_sam_rst    ||
			NULL == rf_sam_cos    ||
			NULL == rf_init_type    ||
			NULL == rf_antenna_sta    ||
			NULL == rf_request    ||
			NULL == rf_anticoll    ||
			NULL == rf_select    ||
			NULL == rf_halt    ||
			NULL == rf_download_key    ||
			NULL == rf_M1_authentication1    ||
			NULL == rf_M1_authentication2    ||
			NULL == rf_M1_read    ||
			NULL == rf_M1_write    ||
			NULL == rf_M1_initval    ||
			NULL == rf_M1_readval    ||
			NULL == rf_M1_decrement    ||
			NULL == rf_M1_increment    ||
			NULL == rf_M1_restore    ||
			NULL == rf_M1_transfer    ||
			NULL == rf_typea_rst    ||
			NULL == rf_cos_command    ||
			NULL == rf_atqb    ||
			NULL == rf_attrib    ||
			NULL == rf_typeb_cos    ||
			NULL == rf_hltb    ||
			NULL == rf_at020_check    ||
			NULL == rf_at020_read    ||
			NULL == rf_at020_write    ||
			NULL == rf_at020_lock    ||
			NULL == rf_at020_count    ||
			NULL == rf_at020_deselect    ||
			NULL == rf_light           ||
			NULL == rf_ClosePort ||
			NULL == rf_GetErrorMessage){			
			AfxMessageBox(_T("Load MasterRD.dll failed !"));
			
		}
	}
	else{
		AfxMessageBox(_T("Load MasterRD.dll failed !"));
	
	}


	//Choose KEY A
	((CButton*)GetDlgItem(IDC_RADIO_KEYA))->SetCheck(TRUE);

	
	CComboBox *pMassCBB = (CComboBox*)GetDlgItem(IDC_COMBO_MASS);
	CString str;

	for( int i = 0 ;i < 64;i++){		
		str.Format(_T("%d"), i);
		pMassCBB->AddString( str);		
		pMassCBB->SetItemData(i, i);		
	} 

	pMassCBB->SetCurSel(0);
	///////////////////////////////
	//set edit control size
	m_edit_key.SetLimitTextEx(6);
	m_edit_data.SetLimitTextEx(16);

	//////////////////////////////
	//initialize 
	CComboBox *pPortCBB = (CComboBox*)GetDlgItem(IDC_COMBO_PORT);
	for( i = 0 ;i < 8;i++){		
		str.Format(_T("%d"), i+1);
		pPortCBB->AddString( str);		
		pPortCBB->SetItemData(i, i+1);		
	} 

	pPortCBB->SetCurSel(0);

	CComboBox *pRaudCBB = (CComboBox*)GetDlgItem(IDC_COMBO_RAUD);
	for( i = 0 ;i < BAUD_NUMBER;i++){		
		str.Format(_T("%d"), BaudValue[i]);
		pRaudCBB->AddString( str);		
		pRaudCBB->SetItemData(i, BaudValue[i]);		
	} 
	pRaudCBB->SetCurSel(2);



	//open port 
	int state = 1;	
	//port : com1
    //baud : 19200
    state = rf_init_com(1,19200);

	if(state != LIB_SUCCESS){
		rf_ClosePort();			
		MessageBox("Open port error!","Error",MB_OK|MB_ICONERROR);
		return FALSE;
	}
	
	return TRUE;  // return TRUE  unless you set the focus to a control
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CMifareOneDemoDlg::OnPaint() 
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, (WPARAM) dc.GetSafeHdc(), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CMifareOneDemoDlg::OnQueryDragIcon()
{
	return (HCURSOR) m_hIcon;
}

void CMifareOneDemoDlg::OnDestroy() 
{
	CDialog::OnDestroy();
	
	//close port
    rf_ClosePort();	
	//Release masterRD.dll
	if(m_hInstMaster) FreeLibrary(m_hInstMaster);	
}

//************* search card **************//
void CMifareOneDemoDlg::OnButtonSearch() 
{
	WORD icdev = 0x0000;
	unsigned char mode = 0x52;
	int status;
	unsigned short TagType;
	unsigned char bcnt = 0x04;//mifare card use 0x04
	unsigned char Snr[MAX_RF_BUFFER];
	unsigned char len;
	unsigned char Size;
	
	status = rf_request(icdev,mode,&TagType);//search all card
	if(status) {//error
		m_edit_serial.SetWindowText("");
		return ;
	}
	
	status = rf_anticoll(icdev,bcnt,Snr,&len);//return serial number of card
	if(status || len != 4) { //error
		m_edit_serial.SetWindowText("");
		return ;
	}
	
	status = rf_select(icdev,Snr,len,&Size);//lock ISO14443-3 TYPE_A 
	if(status) {//error
		m_edit_serial.SetWindowText("");
		return ;
	}
	
	m_edit_serial.SetWindowTextEx(Snr,len);	
}
//************* read *************// 
void CMifareOneDemoDlg::OnButtonRead() 
{
	WORD icdev = 0x0000;
	unsigned char mode = 0x60;
	unsigned char secnr = 0x00;
	int state;
	CString strKey;	
	unsigned char pData[MAX_RF_BUFFER];
	unsigned char cLen;

	CButton* pButton = (CButton*)GetDlgItem(IDC_RADIO_KEYA);
	if((pButton->GetCheck())){//key A
		mode = 0x60; //key value
	}
	else{//key B
		mode = 0x61; //key value
	}

	m_edit_key.GetWindowTextEx(strKey);
	if(m_edit_key.GetTextLenEx() != 6){
		MessageBox(_T("Please input 6 bytes in key area"),_T("Error"),MB_OK|MB_ICONERROR);
		return ;
	}

	//choose block
	CComboBox *pCBB = (CComboBox*)GetDlgItem(IDC_COMBO_MASS);
	int nSel = pCBB->GetCurSel();
	secnr = (unsigned char)(pCBB->GetItemData(nSel));	

	
	state = rf_M1_authentication2(icdev,mode,(secnr/4)*4,(unsigned char*)strKey.GetBuffer(strKey.GetLength()));
	if(state){
		MessageBox(_T("authenticate key error"),_T("Error"),MB_OK|MB_ICONERROR);
		m_edit_data.SetWindowText("");
		return;	
	}
	
	state = rf_M1_read(icdev,secnr,pData,&cLen);
	if(state || cLen != 16){
		MessageBox(_T("Read card error"),_T("Error"),MB_OK|MB_ICONERROR);
		m_edit_data.SetWindowText("");
		return;	
	}

	m_edit_data.SetWindowTextEx(pData,16);
}

//************* write *************//
void CMifareOneDemoDlg::OnButtonWrite() 
{
	WORD icdev = 0x0000;
	unsigned char mode = 0x60;
	unsigned char secnr = 0x00;
	int state;
	CString strKey,strEdit;	
	unsigned char Data[16];	

	CButton* pButton = (CButton*)GetDlgItem(IDC_RADIO_KEYA);
	if((pButton->GetCheck())){//KEY A
		mode = 0x60; //KEY value		
	}
	else{//key B
		mode = 0x61; //key value
	}

	m_edit_key.GetWindowTextEx(strKey);
	if(m_edit_key.GetTextLenEx() != 6){
		MessageBox(_T("Please input 6 bytes in key area"),_T("Error"),MB_OK|MB_ICONERROR);
		return ;
	}

	//choose block
	CComboBox *pCBB = (CComboBox*)GetDlgItem(IDC_COMBO_MASS);
	int nSel = pCBB->GetCurSel();
	secnr = (unsigned char)(pCBB->GetItemData(nSel));	
	
	
	//======================	
	state = rf_M1_authentication2(icdev,mode,(secnr/4)*4,(unsigned char*)strKey.GetBuffer(strKey.GetLength()));
	if(state){
		MessageBox(_T("authenticate key error"),_T("Error"),MB_OK|MB_ICONERROR);
		return;	
	}

	
	m_edit_data.GetWindowTextEx(strEdit);			
	memcpy(Data,strEdit.GetBuffer(strEdit.GetLength()),strEdit.GetLength());
	//write mifare one
	state = rf_M1_write(icdev,secnr,Data);
	if(state){
		MessageBox(_T("write card error"),_T("Error"),MB_OK|MB_ICONERROR);
		return;	
	}
}
//************* halt *************//
void CMifareOneDemoDlg::OnButtonHalt() 
{
	WORD icdev = 0x0000;
	int state;

	state = rf_halt(icdev);
	if(state){
		MessageBox(_T("HALT error"),_T("Error"),MB_OK|MB_ICONERROR);
	}	
}



void CMifareOneDemoDlg::OnSelchangeComboRaud() 
{
	CComboBox *pPortCBB = (CComboBox*)GetDlgItem(IDC_COMBO_PORT);
	CComboBox *pBaudCBB = (CComboBox*)GetDlgItem(IDC_COMBO_RAUD);
	
	int nPort = pPortCBB->GetItemData(pPortCBB->GetCurSel());
	int nBaud = pBaudCBB->GetItemData(pBaudCBB->GetCurSel());

	//close port
    rf_ClosePort();	

	//open
	int state = 1;	
	
    state = rf_init_com(nPort,nBaud);

	if(state != LIB_SUCCESS){
		rf_ClosePort();			
		MessageBox("Open error!","Error",MB_OK|MB_ICONERROR);
		return ;
	}	
}
