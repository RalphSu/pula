USE [pulasys]
GO

/****** Object:  Table [dbo].[time_course]    Script Date: 2015/7/1 23:49:35 ******/
DROP TABLE [dbo].[time_course]
GO

/****** Object:  Table [dbo].[time_course]    Script Date: 2015/7/1 23:49:35 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[time_course](
	[courseId] [int] NOT NULL,
	[courseType] [int] NULL,
	[courseName] [nvarchar](200) NULL,
	[branchName] [nvarchar](200) NULL,
	[price] [decimal](18, 0) NULL,
	[maxStudentNum] [int] NULL,
	[startTime] [datetime] NULL,
	[endTime] [datetime] NULL,
	[startHour] [int] NULL,
	[startMinute] [int] NULL,
	[startWeekDay] [varchar](50) NULL,
	[createTime] [datetime] NULL,
	[updateTime] [datetime] NULL
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

