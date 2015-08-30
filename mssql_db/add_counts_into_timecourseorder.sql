
use pula_sys
go
alter table pula_sys.dbo.time_course_order add 
	gongfangCount int NOT NULL default 0,
	usedGongFangCount int NOT NULL default 0,
	huodongCount int NOT NULL default 0,
	usedHuodongCount int NOT NULL default 0
go