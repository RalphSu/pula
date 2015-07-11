USE [pulasys]
GO

/****** Object:  Table [dbo].[time_course_order]    Script Date: 2015/7/1 23:49:45 ******/
DROP TABLE [dbo].[time_course_order]
GO

/****** Object:  Table [dbo].[time_course_order]    Script Date: 2015/7/1 23:49:45 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [pula_sys].[dbo].[time_course_order](
	[id] [int] NOT NULL,
	[courseId] [int] NOT NULL,
	[no] nvarchar(40) NULL,
	[studentId] [int] NOT NULL,
	[buyType] [int] NULL,
	[paied] [int] NULL,
	[paiedCount] [int] NULL,
	[remainCount] [int] NULL,
	[remainCost] [int] NULL,
	[createTime] [datetime] NULL,
	[updateTime] [datetime] NULL,
	[comments] nvarchar(400) NULL,
	[removed] [int] NULL,
	[enabled] [int] NULL
) ON [PRIMARY]


CREATE TABLE [dbo].[time_course_usage](
	[id] [int] NOT NULL,
	[courseId] [int] NOT NULL,
	[no] [varchar(40)] NULL,
	[studentId] [int] NOT NULL,
	[usedCount] [int] NULL,
	[usedCost] [decimal](18, 0) NULL,
	[createTime] [datetime] NULL,
	[updateTime] [datetime] NULL,
	[comments] nvarchar(400) NULL,
	[removed] [int] NULL,
	[enabled] [int] NULL
) ON [PRIMARY]


GO


