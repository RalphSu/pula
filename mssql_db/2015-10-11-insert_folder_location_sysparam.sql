select * from dbo.parameter  where name='教师附件路径'


insert into dbo.parameter(id, paramType, name, magicNo, folder, appField, removed, page, indexNo, value, no)
values('b717cd1c-1d6f-43f2-9588-4f885c716e27',	1001,	'活动附件路径',	0,	'f503ac04-e76e-461d-9cd6-46a859359a38',	'bd5cab3d-453f-4bf2-ad21-47dce6dfbc68',	0,	'ce2416ab-5346-49cc-82bf-46c63c78417a',	60,	'D:\server\files\notices',	'P_NOTICE_ICON_DIR')

insert into dbo.parameter(id, paramType, name, magicNo, folder, appField, removed, page, indexNo, value, no)
values
('b717cd1c-1d6f-43f2-9588-4f885c716e28',	1001,	'次课附件路径',	0,	'f503ac04-e76e-461d-9cd6-46a859359a38',	'bd5cab3d-453f-4bf2-ad21-47dce6dfbc68',	0,	'ce2416ab-5346-49cc-82bf-46c63c78417a',	60,	'D:\server\files\timecourses',	'P_TIMECOURSE_ICON_DIR')


select * from dbo.parameterfolder

select * from dbo.parameterpage


