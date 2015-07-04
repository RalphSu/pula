USE [pulasys]
GO

/****** Object:  Table [dbo].[time_course_work]    Script Date: 2015/7/1 23:50:12 ******/
DROP TABLE [dbo].[time_course_work]
GO

/****** Object:  Table [dbo].[time_course_work]    Script Date: 2015/7/1 23:50:12 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[time_course_work](
	[courseId] [int] NOT NULL,
	[no] [varchar(40)] NULL,
	[filePath] [nvarchar](400) NULL,
	[suffix] [nvarchar](10) NULL,
	[author] [nvarchar](50) NULL,
	[uploadBy] [nvarchar](50) NULL,
	[createTime] [datetime] NULL,
	[updateTime] [datetime] NULL,
	[comments] nvarchar(400) NULL,
	[removed] [int] NULL,
	[enabled] [int] NULL
) ON [PRIMARY]

GO

