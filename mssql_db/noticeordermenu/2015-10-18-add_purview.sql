/****** Script for SelectTopNRows command from SSMS  ******/
SELECT *
  FROM [pula_sys].[dbo].[purview] where module = 'c78cd590-7090-433b-9551-066798301a10'

insert into [pula_sys].[dbo].[purview](
id,menuItem,defaultURL,parentPurview,level,name,module,visible,appField,treePath,removed,leaf,indexNo,no)
values
(
'5f751d15-24e8-4c9a-be9b-4beaed185d1f',1, 'noticeorder/entry',NULL,0,'活动订单查询', 
'c78cd590-7090-433b-9551-066798301a10', 1, 'bd5cab3d-453f-4bf2-ad21-47dce6dfbc68', 'M070-P050',0,1,10,'P_NOTICE_ORDER'
)
-- delete from [pula_sys].[dbo].[purview] where id='5f751d15-24e8-4c9a-be9b-4beaed185d1f'
