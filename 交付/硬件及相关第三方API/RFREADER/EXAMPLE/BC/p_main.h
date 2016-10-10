//---------------------------------------------------------------------------

#ifndef p_mainH
#define p_mainH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>
#include <ExtCtrls.hpp>
//---------------------------------------------------------------------------
class Tfrm_main : public TForm
{
__published:	// IDE-managed Components
        TLabel *Label1;
        TLabeledEdit *le_kh;
        TLabel *Label2;
        TLabel *Label3;
        TComboBox *cb_port;
        TComboBox *cb_baud;
        TRadioGroup *rg_key;
        TEdit *ed_key;
        TLabel *Label4;
        TComboBox *cb_kh;
        TLabeledEdit *le_sj;
        TButton *Button1;
        TButton *Button2;
        TButton *Button3;
        TButton *Button4;
        TLabel *lb_info;
        void __fastcall Button1Click(TObject *Sender);
        void __fastcall Button2Click(TObject *Sender);
        void __fastcall Button4Click(TObject *Sender);
        void __fastcall FormCreate(TObject *Sender);
        void __fastcall Button3Click(TObject *Sender);
private:	// User declarations
public:		// User declarations
        __fastcall Tfrm_main(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE Tfrm_main *frm_main;
//---------------------------------------------------------------------------
#endif
