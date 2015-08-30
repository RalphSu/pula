
use pula_sys
go
alter table pula_sys.dbo.notice add 
	noticePrice int NOT NULL default 0,
	noticeCount int NOT NULL default 0,
	noticeCourseNo nvarchar(40) NOT NULL default ''
go