// HexEdit.cpp : implementation file
//

#include "stdafx.h"

#include "HexEdit.h"



#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif
////////////////////////////////////////////////////////////////////////////
WORD SetHexToAscII(BYTE szHex)
{
	WORD wAscII;
	BYTE loBits = szHex & 0x0f;
	BYTE hiBits = (szHex & 0xf0) >> 4;

	BYTE loByte,hiByte;

	if(loBits <= 9) loByte = loBits + 0x30;
	else loByte = loBits + 0x37;

	if(hiBits <= 9) hiByte = hiBits + 0x30;
	else hiByte = hiBits + 0x37;

	wAscII = MAKEWORD(hiByte,loByte);
	return wAscII;
}

BYTE GetHexValue(BYTE ch)
{
	BYTE sz;
	if(ch <= '9' && ch >= '0')
		sz = ch - 0x30;
	if(ch <= 'F' && ch >= 'A')
		sz = ch - 0x37;
	if(ch <= 'f' && ch >= 'a')
		sz = ch - 0x57;

	return sz;
}

CString ExchangeHexToString(CString strHex)
{
	CString str("");
	int nLen = strHex.GetLength();

	BYTE*pData = (BYTE*)(strHex.GetBuffer(strHex.GetLength()));

	for(int i = 0 ; i < nLen ;i++){        
		WORD wAscII = SetHexToAscII(pData[i]);
		str += LOBYTE(wAscII);
		str += HIBYTE(wAscII);
	}

	return str;
}


CString ExchangeHexToString(BYTE* pData,int nLen)
{
	CString str("");

	for(int i = 0 ; i < nLen ;i++){        
		WORD wAscII = SetHexToAscII(pData[i]);
		str += LOBYTE(wAscII);
		str += HIBYTE(wAscII);
	}

	return str;
}

BYTE ExchangeStringToChar(BYTE* pBuf,int nLen)
{
	BYTE chHex;
	if(nLen > 2||nLen == 0) return 0x00;

	if(nLen == 2)
	chHex = (GetHexValue(pBuf[0]) << 4) + GetHexValue(pBuf[1]);
	else
		chHex = GetHexValue(pBuf[0]);

	return chHex;
}

CString ExchangeStringToHex(BYTE* pBuff,int nTextLength)
{
	CString strHex("");
	if(nTextLength == 0)return strHex;

	int nBitsNumber = nTextLength / 2;
	int nBitsRes    = nTextLength % 2;
	
	BYTE cHex;
	for(int i = 0 ;i  < nBitsNumber ;i++){
		cHex = ExchangeStringToChar(&pBuff[i * 2],2);
		strHex += cHex;
	}
	if(nBitsRes){
		cHex = ExchangeStringToChar(&pBuff[nBitsNumber * 2],1);
		strHex += cHex;
	}

	return strHex;
}
/////////////////////////////////////////////////////////////////////////////
// CHexEdit

CHexEdit::CHexEdit()
{
	m_bCtrlPressed = FALSE;
}

CHexEdit::~CHexEdit()
{
}


BEGIN_MESSAGE_MAP(CHexEdit, CEdit)
	//{{AFX_MSG_MAP(CHexEdit)
	ON_WM_KEYDOWN()
	ON_WM_CHAR()
	ON_WM_KEYUP()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CHexEdit message handlers

void CHexEdit::SetWindowTextEx(unsigned char *pText,int nLen)
{
	if(pText == NULL) return;
	CString strEdit("");

	for(int i = 0 ; i < nLen ;i++){
		WORD wAscII = SetHexToAscII(pText[i]);
		strEdit += LOBYTE(wAscII);
		strEdit += HIBYTE(wAscII);
	}
    
	SetWindowText(strEdit);
}

void CHexEdit::SetWindowTextEx(int nTextNumber)
{
	BYTE szNumberText[4];	

	szNumberText[0] = LOBYTE(LOWORD(nTextNumber));
	szNumberText[1] = HIBYTE(LOWORD(nTextNumber));

	szNumberText[2] = LOBYTE(HIWORD(nTextNumber));
	szNumberText[3] = HIBYTE(HIWORD(nTextNumber));

	SetWindowTextEx(szNumberText,4);
}

void CHexEdit::GetWindowTextEx(CString& str)
{
	CString strEdit;
	GetWindowText(strEdit);
	if(strEdit.GetLength() == 0) {		
		str.Empty();
		return;
	}

	str = _T("");

	int nTextLength = strEdit.GetLength();

	int nBitsNumber = nTextLength / 2;
	int nBitsRes    = nTextLength % 2;

	BYTE* pBuff = (BYTE*)(strEdit.GetBuffer(nTextLength));
	BYTE cHex;
	for(int i = 0 ;i  < nBitsNumber ;i++){
		cHex = ExchangeStringToChar(&pBuff[i * 2],2);
		str += cHex;
	}
	if(nBitsRes){
		cHex = ExchangeStringToChar(&pBuff[nBitsNumber * 2],1);
		str += cHex;
	}
}

void CHexEdit::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags) 
{
	// TODO: Add your message handler code here and/or call default
	if(nChar == VK_CONTROL){
		m_bCtrlPressed = TRUE;
		CEdit::OnKeyDown(nChar, nRepCnt, nFlags);
		return;
	}

	if(m_bCtrlPressed) {
		CEdit::OnKeyDown(nChar, nRepCnt, nFlags);	
		return;
	}

    
	if(nChar >= 'A' && nChar <= 'F'){
		nChar += 0x20;		
		CEdit::OnKeyDown(nChar, nRepCnt, nFlags);	
		return;
	}

	if((nChar >= '0' && nChar <= '9') ||
		(nChar >= 'a' && nChar <= 'f') ||		
		(VK_DELETE == nChar)) 	
	CEdit::OnKeyDown(nChar, nRepCnt, nFlags);			
}

void CHexEdit::OnKeyUp(UINT nChar, UINT nRepCnt, UINT nFlags) 
{
	// TODO: Add your message handler code here and/or call default
	if(nChar == VK_CONTROL){
		m_bCtrlPressed = FALSE;	
	}

	CEdit::OnKeyUp(nChar, nRepCnt, nFlags);
}

void CHexEdit::OnChar(UINT nChar, UINT nRepCnt, UINT nFlags) 
{
	// TODO: Add your message handler code here and/or call default
	if(m_bCtrlPressed){
		CEdit::OnChar(nChar, nRepCnt, nFlags);
		return;
	}

	if(nChar >= 'A' && nChar <= 'F'){
		nChar += 0x20;
		PostMessage(WM_CHAR,nChar,nFlags);
		return;
	}
	
	if((nChar >= '0' && nChar <= '9') ||
		(nChar >= 'a' && nChar <= 'f') ||		
		(VK_BACK == nChar    ) )	
	CEdit::OnChar(nChar, nRepCnt, nFlags);
}

void CHexEdit::SetLimitTextEx(UINT nMax)
{
	SetLimitText(nMax * 2);
}

UINT CHexEdit::GetLimitTextEx()
{
	UINT nText = GetLimitText();

	return (nText / 2 + nText % 2);	
}

UINT CHexEdit::GetTextLenEx()
{
	CString str;
	GetWindowText(str);
	return (str.GetLength() / 2);
}


BOOL CHexEdit::PreTranslateMessage(MSG* pMsg) 
{
	return CEdit::PreTranslateMessage(pMsg);
}


