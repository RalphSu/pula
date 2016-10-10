// MifareOneDemoDlg.h : header file
//

#if !defined(AFX_MIFAREONEDEMODLG_H__83B09FF0_5E32_4EF3_9F06_FE1BC72B8E2B__INCLUDED_)
#define AFX_MIFAREONEDEMODLG_H__83B09FF0_5E32_4EF3_9F06_FE1BC72B8E2B__INCLUDED_

#include "HexEdit.h"

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

/////////////////////////////////////////////////////////////////////////////
// CMifareOneDemoDlg dialog

class CMifareOneDemoDlg : public CDialog
{
// Construction
public:
	CMifareOneDemoDlg(CWnd* pParent = NULL);	// standard constructor

	HINSTANCE m_hInstMaster;
// Dialog Data
	//{{AFX_DATA(CMifareOneDemoDlg)
	enum { IDD = IDD_MIFAREONEDEMO_DIALOG };
		// NOTE: the ClassWizard will add data members here
	CHexEdit	m_edit_data;
	CHexEdit	m_edit_key;
	CHexEdit	m_edit_serial;
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CMifareOneDemoDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	//{{AFX_MSG(CMifareOneDemoDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnDestroy();
	afx_msg void OnButtonSearch();
	afx_msg void OnButtonRead();
	afx_msg void OnButtonWrite();
	afx_msg void OnButtonHalt();
	afx_msg void OnSelchangeComboRaud();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_MIFAREONEDEMODLG_H__83B09FF0_5E32_4EF3_9F06_FE1BC72B8E2B__INCLUDED_)
