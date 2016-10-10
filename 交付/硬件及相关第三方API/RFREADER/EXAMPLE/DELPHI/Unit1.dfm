object Form1: TForm1
  Left = 93
  Top = 165
  Width = 559
  Height = 393
  Caption = 'Form1'
  Color = clBtnFace
  Font.Charset = ANSI_CHARSET
  Font.Color = clWindowText
  Font.Height = -16
  Font.Name = #23435#20307
  Font.Style = []
  OldCreateOrder = False
  Position = poDesktopCenter
  PixelsPerInch = 96
  TextHeight = 16
  object Label1: TLabel
    Left = 122
    Top = 14
    Width = 302
    Height = 32
    Caption = 'MIFARE ONE'#35835#20889#31243#24207
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -32
    Font.Name = #23435#20307
    Font.Style = [fsBold]
    ParentFont = False
  end
  object Label2: TLabel
    Left = 72
    Top = 167
    Width = 80
    Height = 16
    Caption = #32477#23545#22359#21495#65306
  end
  object lb_info: TLabel
    Left = 86
    Top = 326
    Width = 8
    Height = 16
  end
  object Label3: TLabel
    Left = 76
    Top = 75
    Width = 64
    Height = 16
    Caption = #20018#21475#21495#65306
  end
  object Label4: TLabel
    Left = 284
    Top = 77
    Width = 64
    Height = 16
    Caption = #27874#29305#29575#65306
  end
  object le_xlh: TLabeledEdit
    Left = 154
    Top = 119
    Width = 109
    Height = 24
    EditLabel.Width = 64
    EditLabel.Height = 16
    EditLabel.Caption = #24207#21015#21495#65306
    LabelPosition = lpLeft
    LabelSpacing = 3
    TabOrder = 0
  end
  object le_akey: TLabeledEdit
    Left = 328
    Top = 161
    Width = 121
    Height = 24
    EditLabel.Width = 48
    EditLabel.Height = 16
    EditLabel.Caption = #23494#38053#65306
    LabelPosition = lpLeft
    LabelSpacing = 3
    TabOrder = 1
    Text = 'FFFFFFFFFFFF'
  end
  object cb_kh: TComboBox
    Left = 154
    Top = 163
    Width = 109
    Height = 24
    Style = csDropDownList
    ItemHeight = 16
    ItemIndex = 1
    TabOrder = 2
    Text = '1'
    Items.Strings = (
      '0'
      '1'
      '2'
      '3'
      '4'
      '5'
      '6'
      '7'
      '8'
      '9'
      '10'
      '11'
      '12'
      '13'
      '14'
      '15'
      '16'
      '17'
      '18'
      '19'
      '20'
      '21'
      '22'
      '23'
      '24'
      '25'
      '26'
      '27'
      '28'
      '29'
      '30'
      '31'
      '32'
      '33'
      '34'
      '35'
      '36'
      '37'
      '38'
      '39'
      '40'
      '41'
      '42'
      '43'
      '44'
      '45'
      '46'
      '47'
      '48'
      '49'
      '50'
      '51'
      '52'
      '53'
      '54'
      '55'
      '56'
      '57'
      '58'
      '59'
      '60'
      '61'
      '62'
      '63')
  end
  object le_sj: TLabeledEdit
    Left = 154
    Top = 209
    Width = 297
    Height = 24
    EditLabel.Width = 64
    EditLabel.Height = 16
    EditLabel.Caption = #22359#25968#25454#65306
    LabelPosition = lpLeft
    LabelSpacing = 3
    TabOrder = 3
  end
  object Button1: TButton
    Left = 86
    Top = 264
    Width = 63
    Height = 41
    Caption = #23547#21345
    TabOrder = 4
    OnClick = Button1Click
  end
  object Button2: TButton
    Left = 184
    Top = 264
    Width = 63
    Height = 41
    Caption = #35835#21345
    TabOrder = 5
    OnClick = Button2Click
  end
  object Button3: TButton
    Left = 292
    Top = 264
    Width = 63
    Height = 41
    Caption = #20889#21345
    TabOrder = 6
    OnClick = Button3Click
  end
  object Button4: TButton
    Left = 386
    Top = 264
    Width = 63
    Height = 41
    Caption = #20241#30496
    TabOrder = 7
    OnClick = Button4Click
  end
  object cb_port: TComboBox
    Left = 154
    Top = 71
    Width = 109
    Height = 24
    Style = csDropDownList
    ItemHeight = 16
    ItemIndex = 1
    TabOrder = 8
    Text = '2'
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
    Left = 370
    Top = 73
    Width = 81
    Height = 24
    Style = csDropDownList
    ItemHeight = 16
    ItemIndex = 1
    TabOrder = 9
    Text = '19200'
    Items.Strings = (
      '9600'
      '19200'
      '57600'
      '115200')
  end
  object rg_key: TRadioGroup
    Left = 278
    Top = 116
    Width = 171
    Height = 37
    Caption = #23494#38053#36873#25321
    Columns = 2
    ItemIndex = 0
    Items.Strings = (
      'A'#23494#38053
      'B'#23494#38053)
    TabOrder = 10
  end
end
