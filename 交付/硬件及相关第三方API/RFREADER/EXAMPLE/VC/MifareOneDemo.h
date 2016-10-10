// MifareOneDemo.h : main header file for the MIFAREONEDEMO application
//

#if !defined(AFX_MIFAREONEDEMO_H__228367D0_DCD8_4C51_B9DF_BD1032FDC75F__INCLUDED_)
#define AFX_MIFAREONEDEMO_H__228367D0_DCD8_4C51_B9DF_BD1032FDC75F__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

/////////////////////////////////////////////////////////////////////////////
// CMifareOneDemoApp:
// See MifareOneDemo.cpp for the implementation of this class
//

class CMifareOneDemoApp : public CWinApp
{
public:
	CMifareOneDemoApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CMifareOneDemoApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation

	//{{AFX_MSG(CMifareOneDemoApp)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_MIFAREONEDEMO_H__228367D0_DCD8_4C51_B9DF_BD1032FDC75F__INCLUDED_)
