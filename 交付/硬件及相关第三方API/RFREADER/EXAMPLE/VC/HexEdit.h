#if !defined(AFX_HEXEDIT_H__69C39644_D307_44AE_AEDD_07C051076D32__INCLUDED_)
#define AFX_HEXEDIT_H__69C39644_D307_44AE_AEDD_07C051076D32__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// HexEdit.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CHexEdit window

class CHexEdit : public CEdit
{
// Construction
public:
	CHexEdit();

// Attributes
public:
	BOOL m_bCtrlPressed;

// Operations
public:	
	void SetWindowTextEx(unsigned char *pText,int len);
	void SetWindowTextEx(int nTextNumber);
	
	void GetWindowTextEx(CString& str);
	void SetLimitTextEx(UINT nMax);
	UINT GetLimitTextEx();

	UINT GetTextLenEx();

private:
	//WORD SetHexToAscII(BYTE szHex);
	//BYTE GetHexValue(BYTE ch);
//	BYTE ExchangeStringToChar(BYTE* pBuf,int nLen);

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CHexEdit)
	public:
	virtual BOOL PreTranslateMessage(MSG* pMsg);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CHexEdit();

	// Generated message map functions
protected:
	//{{AFX_MSG(CHexEdit)
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnChar(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnKeyUp(UINT nChar, UINT nRepCnt, UINT nFlags);
	//}}AFX_MSG

	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_HEXEDIT_H__69C39644_D307_44AE_AEDD_07C051076D32__INCLUDED_)
