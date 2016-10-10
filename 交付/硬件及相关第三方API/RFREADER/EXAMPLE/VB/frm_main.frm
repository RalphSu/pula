VERSION 5.00
Begin VB.Form frm_main 
   Caption         =   "VB DEMO"
   ClientHeight    =   7155
   ClientLeft      =   60
   ClientTop       =   345
   ClientWidth     =   7575
   BeginProperty Font 
      Name            =   "ËÎÌå"
      Size            =   12
      Charset         =   0
      Weight          =   400
      Underline       =   0   'False
      Italic          =   0   'False
      Strikethrough   =   0   'False
   EndProperty
   LinkTopic       =   "Form1"
   ScaleHeight     =   7155
   ScaleWidth      =   7575
   StartUpPosition =   1  'CenterOwner
   Begin VB.ComboBox cb_btl 
      Height          =   360
      ItemData        =   "frm_main.frx":0000
      Left            =   5430
      List            =   "frm_main.frx":0010
      Style           =   2  'Dropdown List
      TabIndex        =   19
      Top             =   1230
      Width           =   1215
   End
   Begin VB.ComboBox cb_ckh 
      Height          =   360
      ItemData        =   "frm_main.frx":0030
      Left            =   2100
      List            =   "frm_main.frx":004F
      Style           =   2  'Dropdown List
      TabIndex        =   18
      Top             =   1230
      Width           =   1275
   End
   Begin VB.CommandButton Command4 
      Caption         =   "Halt"
      Height          =   615
      Left            =   4800
      TabIndex        =   16
      Top             =   5280
      Width           =   1035
   End
   Begin VB.CommandButton Command3 
      Caption         =   "Write"
      Height          =   615
      Left            =   3570
      TabIndex        =   15
      Top             =   5280
      Width           =   1035
   End
   Begin VB.CommandButton Command2 
      Caption         =   "Read"
      Height          =   615
      Left            =   2190
      TabIndex        =   14
      Top             =   5280
      Width           =   1035
   End
   Begin VB.CommandButton Command1 
      Caption         =   "Request"
      Height          =   615
      Left            =   960
      TabIndex        =   13
      Top             =   5280
      Width           =   1035
   End
   Begin VB.TextBox tx_sj 
      Height          =   360
      Left            =   1980
      TabIndex        =   12
      Top             =   4470
      Width           =   4515
   End
   Begin VB.ComboBox cb_kh 
      Height          =   360
      ItemData        =   "frm_main.frx":006E
      Left            =   2580
      List            =   "frm_main.frx":0070
      Style           =   2  'Dropdown List
      TabIndex        =   10
      Top             =   3870
      Width           =   1515
   End
   Begin VB.Frame Frame1 
      Caption         =   "KEY MODE"
      Height          =   975
      Left            =   930
      TabIndex        =   5
      Top             =   2700
      Width           =   5625
      Begin VB.TextBox tx_key 
         Height          =   360
         Left            =   2880
         TabIndex        =   8
         Text            =   "FFFFFFFFFFFF"
         Top             =   360
         Width           =   2445
      End
      Begin VB.OptionButton op_b 
         Caption         =   "KEY B"
         Height          =   375
         Left            =   1590
         TabIndex        =   7
         Top             =   360
         Width           =   1035
      End
      Begin VB.OptionButton op_a 
         Caption         =   "KEY A"
         Height          =   255
         Left            =   210
         TabIndex        =   6
         Top             =   390
         Value           =   -1  'True
         Width           =   1005
      End
   End
   Begin VB.TextBox tx_kh 
      Height          =   360
      Left            =   1980
      TabIndex        =   4
      Top             =   1920
      Width           =   2205
   End
   Begin VB.Label lb_info 
      Height          =   465
      Left            =   840
      TabIndex        =   17
      Top             =   6330
      Width           =   6255
   End
   Begin VB.Label Label6 
      Caption         =   "DATA:"
      Height          =   345
      Left            =   990
      TabIndex        =   11
      Top             =   4500
      Width           =   855
   End
   Begin VB.Label Label5 
      Caption         =   "BLOCK(0-63)"
      Height          =   345
      Left            =   990
      TabIndex        =   9
      Top             =   3900
      Width           =   1395
   End
   Begin VB.Label Label4 
      Caption         =   "Serial Number:"
      Height          =   285
      Left            =   60
      TabIndex        =   3
      Top             =   1950
      Width           =   1815
   End
   Begin VB.Label Label3 
      Caption         =   "MIFARE ONE DEMO"
      BeginProperty Font 
         Name            =   "ËÎÌå"
         Size            =   15
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   465
      Left            =   2040
      TabIndex        =   2
      Top             =   360
      Width           =   3465
   End
   Begin VB.Label Label2 
      Caption         =   "BAUD:"
      Height          =   315
      Left            =   4290
      TabIndex        =   1
      Top             =   1260
      Width           =   1035
   End
   Begin VB.Label Label1 
      Caption         =   "COM PORT:"
      Height          =   315
      Left            =   600
      TabIndex        =   0
      Top             =   1260
      Width           =   1125
   End
End
Attribute VB_Name = "frm_main"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit

Private Sub Command1_Click()
Dim i&, j%, port&, baud&, buf1(200) As Byte, b1 As Byte, s1$
port = cb_ckh.ListIndex + 1
If (port = 0) Then
    lb_info.Caption = "Please select COM Port!"
End If
baud = CLng(cb_btl.Text)
If (baud = 0) Then
    lb_info.Caption = "Please select Baud rate!"
End If
'Open Port
i = rf_init_com(port, baud)
If (i <> 0) Then
    lb_info.Caption = "Open Port Fail!"
    Exit Sub
End If
'Request
i = rf_request(0, &H52, j)
If (i <> 0) Then
    lb_info.Caption = "Request Fail!"
    Exit Sub
End If
'Anticollision
i = rf_anticoll(0, 4, buf1(0), b1)
If (i <> 0) Then
    lb_info.Caption = "Anticollision Fail!"
    Exit Sub
End If
s1 = ""
For i = 0 To b1 - 1
    s1 = s1 & Right("00" & Hex(buf1(i)), 2)
Next i
tx_kh.Text = s1
'Select card
i = rf_select(0, buf1(0), 4, b1)
If (i <> 0) Then
    lb_info.Caption = "Select card fail!"
    Exit Sub
End If
lb_info.Caption = "Select card succeed!"
End Sub

Private Sub Command2_Click()
Dim i&, m&, buf1(200) As Byte, buf2(200) As Byte, s1$, b1 As Byte, b2 As Byte, b3 As Byte
s1 = Trim(tx_key.Text)
If (Len(s1) <> 12) Then
    lb_info.Caption = "Wrong Key Length!"
    tx_key.SetFocus
    Exit Sub
End If
For i = 0 To 5
    buf1(i) = Val("&H" & Mid(s1, i * 2 + 1, 2))
Next i
m = cb_kh.ListIndex
If (m = -1) Then
    lb_info.Caption = "Select Block Please!"
    Exit Sub
End If
If (op_a.Value) Then
   b1 = &H60
End If
If (op_b.Value) Then
   b1 = &H61
End If
b3 = CByte(m)
'Authentication
i = rf_M1_authentication2(0, b1, b3, buf1(0))
If (i <> 0) Then
    lb_info.Caption = "Authentication Fail£¡"
    Exit Sub
End If
'Read card
i = rf_M1_read(0, b3, buf2(0), b2)
If (i <> 0) Then
    lb_info.Caption = "Read Card Fail!"
    Exit Sub
End If
s1 = ""
For i = 0 To b2 - 1
    s1 = s1 & Right("00" & Hex(buf2(i)), 2)
Next i
tx_sj.Text = s1
lb_info.Caption = "Read Succeed!"
End Sub

Private Sub Command3_Click()
Dim i&, m&, buf1(200) As Byte, buf2(200) As Byte, s1$, b1 As Byte, b2 As Byte, b3 As Byte
s1 = Trim(tx_key.Text)
If (Len(s1) <> 12) Then
    lb_info.Caption = "Wrong Key Length!"
    tx_key.SetFocus
    Exit Sub
End If
For i = 0 To 5
    buf1(i) = Val("&H" & Mid(s1, i * 2 + 1, 2))
Next i
m = cb_kh.ListIndex
If (m = -1) Then
    lb_info.Caption = "Select Block Please!"
    Exit Sub
End If
If (op_a.Value) Then
   b1 = &H60
End If
If (op_b.Value) Then
   b1 = &H61
End If

s1 = Trim(tx_sj.Text)
If (Len(s1) <> 32) Then
    lb_info.Caption = "Wrong Data length "
    tx_sj.SetFocus
    Exit Sub
End If
For i = 0 To 15
    buf2(i) = Val("&H" & Mid(s1, i * 2 + 1, 2))
Next i
'Authentication
b3 = CByte(m)
i = rf_M1_authentication2(0, b1, b3, buf1(0))
If (i <> 0) Then
    lb_info.Caption = "Authentication Fail!"
    Exit Sub
End If
'Write card
i = rf_M1_write(0, b3, buf2(0))
If (i <> 0) Then
    lb_info.Caption = "Write Card Fail£¡"
    Exit Sub
End If

lb_info.Caption = "Write Succeed!"
End Sub

Private Sub Command4_Click()
Dim i&
i = rf_halt(0)
If (i <> 0) Then
    lb_info.Caption = "Halt Fail!"
    Exit Sub
End If
lb_info.Caption = "Halt Succeed"
End Sub

Private Sub Form_Load()
Dim i&
For i = 0 To 63
    cb_kh.AddItem CStr(i), i
Next i
cb_ckh.ListIndex = 0: cb_btl.ListIndex = 1: cb_kh.ListIndex = 4
End Sub
