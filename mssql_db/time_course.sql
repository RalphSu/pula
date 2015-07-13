USE [pula_sys]
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
<<<<<<< HEAD
	[courseId] [int] NOT NULL,
	[no] [varchar](40) NULL,
=======
	[id] [int] NOT NULL PRIMARY KEY IDENTITY,
	[no] [nvarchar](40) NULL,
>>>>>>> bdd6f0d72594d6e59a2a8ae4d944983c7678cbb9
	[courseType] [int] NULL,
	[name] [nvarchar](200) NULL,
	[branchName] [nvarchar](200) NULL,
	[classRoomName] [nvarchar](200) NULL,
	[price] [decimal](18, 0) NULL,
	[maxStudentNum] [int] NULL,
	[startTime] [datetime] NULL,
	[endTime] [datetime] NULL,
	[durationMinute] [int] NULL,
	[startHour] [int] NULL,
	[startMinute] [int] NULL,
	[startWeekDay] [varchar](50) NULL,
	[createTime] [datetime] NULL,
	[updateTime] [datetime] NULL,
	[comments] nvarchar(400) NULL,
	[removed] [int] NULL,
	[enabled] [int] NULL,
	[creator] nvarchar(400) NULL,
	[updator] nvarchar(400) NULL
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

