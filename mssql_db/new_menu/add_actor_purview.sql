/****** Script for SelectTopNRows command from SSMS  ******/
SELECT TOP 1000 [id]
      ,[purview]
      ,[actorId]
      ,[dataFrom]
  FROM [pula_sys].[dbo].[actorpurview] where [purview] = '5f751d12-24e8-4c9a-be9b-4beaed185d1f'

  SELECT count(1)
  FROM [pula_sys].[dbo].[actorpurview] 

--次课查询
insert into [pula_sys].[dbo].[actorpurview]
([id],[purview],[actorId] ,[dataFrom]) 
	  values(
	  'c78cd590-7090-433b-9551-066798301a12', '5f751d10-24e8-4c9a-be9b-4beaed185d1f', '70d6b57a-4f37-4711-aa45-66da7b9d47c5', 1)

insert into [pula_sys].[dbo].[actorpurview]
([id],[purview],[actorId] ,[dataFrom]) 
	  values(
	  'c78cd591-7090-433b-9551-066798301a13', '5f751d10-24e8-4c9a-be9b-4beaed185d1f', '3be92173-9091-40cc-be40-d3550259e70c', 1)


--次课订单查询
insert into [pula_sys].[dbo].[actorpurview]
([id],[purview],[actorId] ,[dataFrom]) 
	  values(
	  'c78cd590-7090-433b-9551-066798301a14', '5f751d11-24e8-4c9a-be9b-4beaed185d1f', '70d6b57a-4f37-4711-aa45-66da7b9d47c5', 1)

insert into [pula_sys].[dbo].[actorpurview]
([id],[purview],[actorId] ,[dataFrom]) 
	  values(
	  'c78cd591-7090-433b-9551-066798301a15', '5f751d11-24e8-4c9a-be9b-4beaed185d1f', '3be92173-9091-40cc-be40-d3550259e70c', 1)

--次课活动查询
insert into [pula_sys].[dbo].[actorpurview]
([id],[purview],[actorId] ,[dataFrom]) 
	  values(
	  'c78cd590-7090-433b-9551-066798301a16', '5f751d12-24e8-4c9a-be9b-4beaed185d1f', '70d6b57a-4f37-4711-aa45-66da7b9d47c5', 1)
	   -- delete from [pula_sys].[dbo].[actorpurview] where id='c78cd590-7090-433b-9551-066798301a16'

insert into [pula_sys].[dbo].[actorpurview]
([id],[purview],[actorId] ,[dataFrom]) 
	  values(
	  'c78cd591-7090-433b-9551-066798301a17', '5f751d12-24e8-4c9a-be9b-4beaed185d1f', '3be92173-9091-40cc-be40-d3550259e70c', 1)
	  -- delete from [pula_sys].[dbo].[actorpurview] where id='c78cd591-7090-433b-9551-066798301a17'