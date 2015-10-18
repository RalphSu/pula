/****** Script for SelectTopNRows command from SSMS  ******/
SELECT TOP 1000 [id]
      ,[purview]
      ,[actorId]
      ,[dataFrom]
  FROM [pula_sys].[dbo].[actorpurview] where [purview] = '5f751d15-24e8-4c9a-be9b-4beaed185d1f'

  SELECT count(1)
  FROM [pula_sys].[dbo].[actorpurview] 

-- 活动订单查询
insert into [pula_sys].[dbo].[actorpurview]
([id],[purview],[actorId] ,[dataFrom]) 
	  values(
	  'c78cd591-7090-433b-9551-066798301a22', '5f751d15-24e8-4c9a-be9b-4beaed185d1f', '70d6b57a-4f37-4711-aa45-66da7b9d47c5', 1)
	  -- delete from [pula_sys].[dbo].[actorpurview] where id='c78cd590-7090-433b-9551-066798301a19'

insert into [pula_sys].[dbo].[actorpurview]
([id],[purview],[actorId] ,[dataFrom]) 
	  values(
	  'c78cd591-7090-433b-9551-066798301a23', '5f751d15-24e8-4c9a-be9b-4beaed185d1f', '3be92173-9091-40cc-be40-d3550259e70c', 1)