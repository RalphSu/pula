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

CREATE TABLE [dbo].[time_course_order](
	[courseId] [int] NOT NULL,
	[no] [varchar(40)] NULL,
	[studentId] [int] NOT NULL,
	[buyType] [int] NULL,
	[paied] [decimal](18, 0) NULL,
	[paiedCount] [int] NULL,
	[usedCount] [int] NULL,
	[usedCost] [decimal](18, 0) NULL,
	[createTime] [datetime] NULL,
	[updateTime] [datetime] NULL,
	[comments] nvarchar(400) NULL,
	[removed] [int] NULL,
	[enabled] [int] NULL
) ON [PRIMARY]

GO

