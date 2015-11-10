
use pula_sys
go
alter table pula_sys.dbo.notice_order add 
	return_code NVARCHAR(128) NOT NULL default '',
	return_msg NVARCHAR(128) NOT NULL default '',
	appid NVARCHAR(128) NOT NULL default '',
	mch_id NVARCHAR(128) NOT NULL default '',
	device_info NVARCHAR(128) NOT NULL default '',
	nonce_str NVARCHAR(128) NOT NULL default '',
	sign NVARCHAR(128) NOT NULL default '',
	result_code NVARCHAR(128) NOT NULL default '',
	err_code NVARCHAR(128) NOT NULL default '',
	err_code_des NVARCHAR(128) NOT NULL default '',
	openid NVARCHAR(128) NOT NULL default '',
	is_subscribe NVARCHAR(128) NOT NULL default '',
	trade_type NVARCHAR(128) NOT NULL default '',
	bank_type NVARCHAR(128) NOT NULL default '',
	total_fee int NOT NULL default 0,
	fee_type NVARCHAR(128) NOT NULL default '',
	cash_fee int NOT NULL default 0,
	cash_fee_type NVARCHAR(128) NOT NULL default '',
	coupon_fee int NOT NULL default 0,
	coupon_count int NOT NULL default 0,
	transaction_id NVARCHAR(128) NOT NULL default '',
	out_trade_no NVARCHAR(128) NOT NULL default '',
	attach NVARCHAR(128) NOT NULL default '',
	time_end NVARCHAR(128) NOT NULL default ''
go