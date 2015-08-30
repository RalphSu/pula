
use pula_sys
go
alter table pula_sys.dbo.time_course_usage add 
	usedGongFangCount int NOT NULL default 0,
	usedHuodongCount int NOT NULL default 0
go