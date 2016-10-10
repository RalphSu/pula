object frm_main: Tfrm_main
  Left = 388
  Top = 123
  Width = 446
  Height = 461
  Caption = 'frm_main'
  Color = clBtnFace
  Font.Charset = GB2312_CHARSET
  Font.Color = clWindowText
  Font.Height = -16
  Font.Name = #23435#20307
  Font.Style = []
  OldCreateOrder = False
  OnCreate = FormCreate
  PixelsPerInch = 96
  TextHeight = 16
  object Label1: TLabel
    Left = 102
    Top = 20
    Width = 180
    Height = 29
    Caption = 'MIFARE1 DEMO'
    Font.Charset = GB2312_CHARSET
    Font.Color = clWindowText
    Font.Height = -29
    Font.Name = #23435#20307
    Font.Style = []
    ParentFont = False
  end
  object Label2: TLabel
    Left = 42
    Top = 62
    Width = 40
    Height = 16
    Caption = 'COMM:'
  end
  object Label3: TLabel
    Left = 182
    Top = 62
    Width = 40
    Height = 16
    Caption = 'BAUD:'
  end
  object Label4: TLabel
    Left = 42
    Top = 216
    Width = 48
    Height = 16
    Caption = 'BLOCK:'
  end
  object lb_info: TLabel
    Left = 42
    Top = 382
    Width = 8
    Height = 16
  end
  object le_kh: TLabeledEdit
    Left = 110
    Top = 106
    Width = 201
    Height = 24
    EditLabel.Width = 32
    EditLabel.Height = 16
    EditLabel.Caption = 'UID:'
    LabelPosition = lpLeft
    LabelSpacing = 3
    TabOrder = 0
  end
  object cb_port: TComboBox
    Left = 112
    Top = 60
    Width = 65
    Height = 24
    Style = csDropDownList
    ItemHeight = 16
    ItemIndex = 2
    TabOrder = 1
    Text = '3'
    Items.Strings = (
      '1'
      '2'
      '3'
      '4'
      '5'
      '6'
      '7'
      '8'
      '9')
  end
  object cb_baud: TComboBox
    Left = 252
    Top = 60
    Width = 79
    Height = 24
    Style = csDropDownList
    ItemHeight = 16
    ItemIndex = 1
    TabOrder = 2
    Text = '19200'
    Items.Strings = (
      '9600'
      '19200'
      '57600'
      '115200')
  end
  object rg_key: TRadioGroup
    Left = 42
    Top = 140
    Width = 99
    Height = 57
    Caption = 'KEY'
    ItemIndex = 0
    Items.Strings = (
      'KEY A'
      'KEY B')
    TabOrder = 3
  end
  object ed_key: TEdit
    Left = 154
    Top = 160
    Width = 157
    Height = 24
    TabOrder = 4
    Text = 'FFFFFFFFFFFF'
  end
  object cb_kh: TComboBox
    Left = 108
    Top = 212
    Width = 93
    Height = 24
    ItemHeight = 16
    ItemIndex = 0
    TabOrder = 5
    Text = '0'
    Items.Strings = (
      '0'
      '1')
  end
  object le_sj: TLabeledEdit
    Left = 42
    Top = 266
    Width = 347
    Height = 24
    EditLabel.Width = 40
    EditLabel.Height = 16
    EditLabel.Caption = 'DATA:'
    LabelPosition = lpAbove
    LabelSpacing = 3
    TabOrder = 6
  end
  object Button1: TButton
    Left = 52
    Top = 318
    Width = 71
    Height = 43
    Caption = 'REQUEST'
    TabOrder = 7
    OnClick = Button1Click
  end
  object Button2: TButton
    Left = 312
    Top = 318
    Width = 71
    Height = 43
    Caption = 'HALT'
    TabOrder = 8
    OnClick = Button2Click
  end
  object Button3: TButton
    Left = 228
    Top = 318
    Width = 71
    Height = 43
    Caption = 'WRITE'
    TabOrder = 9
    OnClick = Button3Click
  end
  object Button4: TButton
    Left = 138
    Top = 318
    Width = 71
    Height = 43
    Caption = 'READ'
    TabOrder = 10
    OnClick = Button4Click
  end
end
