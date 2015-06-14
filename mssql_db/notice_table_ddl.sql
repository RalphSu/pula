USE [pula_sys]
GO

/****** Object:  Table [dbo].[notices]    Script Date: 2015/6/7 21:56:23 ******/
DROP TABLE [dbo].[notices]
GO

/****** Object:  Table [dbo].[notices]    Script Date: 2015/6/7 21:56:24 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[notices](
	[noticeId] [bigint] NOT NULL,
	[title] [varchar](200) NOT NULL,
	[summary] [varchar](400) NOT NULL,
	[noticeContent] [text] NOT NULL,
	[DataChange_LastTime] [datetime] NOT NULL,
	[DataChange_CreateTime] [datetime] NOT NULL,
	[publishedBy] [varchar](50) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

