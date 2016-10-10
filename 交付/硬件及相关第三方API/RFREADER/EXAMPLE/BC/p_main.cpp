//---------------------------------------------------------------------------

#include <vcl.h>
#pragma hdrstop

#include "p_main.h"
#include "declaredll.h"
#include "myfunc.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
Tfrm_main *frm_main;
//---------------------------------------------------------------------------
__fastcall Tfrm_main::Tfrm_main(TComponent* Owner)
        : TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall Tfrm_main::Button1Click(TObject *Sender)
{
 int port,baud,i;AnsiString s1;UCHAR b1; UCHAR buf1[100];USINT sint1;
 HINSTANCE g=NULL;
 F_RF_INIT_COM rf_init_com;
 F_RF_REQUEST  rf_request;
 F_RF_ANTICOLL rf_anticoll;
 F_RF_SELECT   rf_select;
 g=LoadLibrary("MasterRD.dll");
 if(g==NULL)
 {
  lb_info->Caption ="Load dll failure!";
  return;
 }
 rf_init_com=(F_RF_INIT_COM)GetProcAddress(g,"rf_init_com");
 if(rf_init_com==NULL)
 {
  lb_info->Caption ="function rf_init_com GetProcAddress failure!";
  return;
 }
 rf_request=(F_RF_REQUEST)GetProcAddress(g,"rf_request");
 if(rf_request==NULL)
 {
  lb_info->Caption ="function rf_request GetProcAddress failure!";
  return;
 }
 rf_anticoll=(F_RF_ANTICOLL)GetProcAddress(g,"rf_anticoll");
 if(rf_anticoll==NULL)
 {
  lb_info->Caption ="function rf_anticoll GetProcAddress failure!";
  return;
 }
 rf_select=(F_RF_SELECT)GetProcAddress(g,"rf_select");
 port=cb_port->ItemIndex+1;
 baud=StrToInt(cb_baud->Text );
 i=rf_init_com(port,baud);
 if(i!=0)
 {
  lb_info->Caption ="open the comm failure!";
  return;
 }
 i=rf_request(0,0x52,&sint1);
 if(i!=0)
 {
  lb_info->Caption ="Request failure!";
  return;
 }
 i=rf_anticoll(0,4,buf1,&b1);
 if(i!=0)
 {
  lb_info->Caption ="Anticoll failure!";
  return;
 }
 s1=f_bytetohex(buf1,b1);
 le_kh->Text =s1;
 i=rf_select(0,buf1,4,&b1);
 if(i!=0)
 {
  lb_info->Caption ="Select failure!";
  return;
 }
 FreeLibrary(g);
 lb_info->Caption ="Select card!";
}
//---------------------------------------------------------------------------
void __fastcall Tfrm_main::Button2Click(TObject *Sender)
{
 int i;
 HINSTANCE g=NULL;
 F_RF_HALT rf_halt;

 g=LoadLibrary("MasterRD.dll");
 if(g==NULL)
 {
  lb_info->Caption ="Load dll failure!";
  return;
 }
 rf_halt=(F_RF_HALT)GetProcAddress(g,"rf_halt");
 i=rf_halt(0);
 if(i!=0)
 {
  lb_info->Caption ="Halt error!";
  return;
 }
 lb_info->Caption ="Halt ok!";
 FreeLibrary(g);
}
//---------------------------------------------------------------------------
void __fastcall Tfrm_main::Button4Click(TObject *Sender)
{
 int port,baud,i,j;AnsiString s1;UCHAR b1,b2; UCHAR buf1[100],buf2[100];USINT sint1;
 HINSTANCE g=NULL;
 F_RF_M1_AUTHENTICATION2 rf_M1_authentication2;
 F_RF_M1_READ rf_M1_read;
 g=LoadLibrary("MasterRD.dll");
 if(g==NULL)
 {
  lb_info->Caption ="Load dll failure!";
  return;
 }
 rf_M1_authentication2=(F_RF_M1_AUTHENTICATION2)GetProcAddress(g,"rf_M1_authentication2");
 rf_M1_read=(F_RF_M1_READ)GetProcAddress(g,"rf_M1_read");
 s1=ed_key->Text;
 if(s1.Length() !=12)
 {
   lb_info->Caption ="The length of key is error!"; ed_key->SetFocus();
   return;
 }
 i=f_stringtobin(s1,12,buf2);
 if(rg_key->ItemIndex ==0)
    b1=0x60;
 else
    b1=0x61;
 b2=cb_kh->ItemIndex;
 i=rf_M1_authentication2(0,b1,b2,buf2);
 if(i!=0)
 {
  lb_info->Caption ="Verify the key failure!";
  return;
 }
 i=rf_M1_read(0,b2,buf1,&b1);
 if(i!=0)
 {
  lb_info->Caption ="Read card error!";
  return;
 }
 j=b1;
 le_sj->Text =f_bytetohex(buf1,j);
 lb_info->Caption ="Read card ok!";
 FreeLibrary(g);
}
//---------------------------------------------------------------------------
void __fastcall Tfrm_main::FormCreate(TObject *Sender)
{
 int i;
 for(i=2;i<64;i++)
     cb_kh->AddItem(StrToInt(i),NULL);
}
//---------------------------------------------------------------------------
void __fastcall Tfrm_main::Button3Click(TObject *Sender)
{
 int port,baud,i,j;AnsiString s1;UCHAR b1,b2; UCHAR buf1[100],buf2[100];USINT sint1;
 HINSTANCE g=NULL;
 F_RF_M1_AUTHENTICATION2 rf_M1_authentication2;
 F_RF_M1_WRITE rf_M1_write;
 g=LoadLibrary("MasterRD.dll");
 if(g==NULL)
 {
  lb_info->Caption ="Load dll failure!";
  return;
 }
 rf_M1_authentication2=(F_RF_M1_AUTHENTICATION2)GetProcAddress(g,"rf_M1_authentication2");
 rf_M1_write=(F_RF_M1_WRITE)GetProcAddress(g,"rf_M1_write");
 s1=ed_key->Text;
 if(s1.Length() !=12)
 {
   lb_info->Caption ="The length of key is error!"; ed_key->SetFocus();
   return;
 }
 i=f_stringtobin(s1,12,buf2);

 s1=le_sj->Text;
 if(s1.Length() !=32)
 {
   lb_info->Caption ="The length of data is error!"; le_sj->SetFocus();
   return;
 }
 i=f_stringtobin(s1,32,buf1);

 if(rg_key->ItemIndex ==0)
    b1=0x60;
 else
    b1=0x61;
 b2=cb_kh->ItemIndex;
 i=rf_M1_authentication2(0,b1,b2,buf2);
 if(i!=0)
 {
  lb_info->Caption ="Verify the key is error!";
  return;
 }
 i=rf_M1_write(0,b2,buf1);
 if(i!=0)
 {
  lb_info->Caption ="Write card error!";
  return;
 }
 lb_info->Caption ="Write card ok!";
 FreeLibrary(g);
}
//---------------------------------------------------------------------------
