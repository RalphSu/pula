USE [pula_sys]
GO

/****** Object:  Table [dbo].[time_course_work]    Script Date: 2015/10/2 13:06:04 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[time_course_work](
	[id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
	[courseNo] [varchar](255) NULL,
	[studentNo] [varchar](255) NULL,
	[branchNo] [varchar](255) NULL,
	[imagePath] [varchar](255) NULL,
	[workEffectDate] [datetime] NULL,
	[updator] [varchar](255) NULL,
	[comments] [varchar](255) NULL,
	[attachmentKey] [varchar](255) NULL,
	[rate] [int] NULL,
	[createTime] [datetime] NULL,
	[updateTime] [datetime] NULL,
	[enabled] [tinyint] NULL,
	[removed] [tinyint] NULL,
	[no] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO


