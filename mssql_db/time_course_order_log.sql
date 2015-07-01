USE [pulasys]
GO

/****** Object:  Table [dbo].[time_course_order_log]    Script Date: 2015/7/1 23:49:56 ******/
DROP TABLE [dbo].[time_course_order_log]
GO

/****** Object:  Table [dbo].[time_course_order_log]    Script Date: 2015/7/1 23:49:56 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[time_course_order_log](
	[logId] [bigint] NULL,
	[opValue] [nvarchar](2000) NULL
) ON [PRIMARY]

GO

