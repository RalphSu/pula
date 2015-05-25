# pula
pula system for fun

##安装数据库
sqlserver 去下一个express的，带sqlserver managementtool的。安装好，确认能从本机的1433端口连上。用户名和密码设置好，见代码中的配置。然后把mssqldb目录下数据文件使用sqlmangementtool导入即可。

##服务端
服务端的代码在pula-src下，要先安装java和maven。进入pula-src目录，mvn jetty:run即可，服务器就会起在8080端口了。

##客户端
都clients目录下，用visualstudio2013 express版打开项目，有两个可运行的项目

SendCard（发卡的程序，即给学生和老师录入卡片），这个要接口服务端的配置。先在服务端加入卡片，然后使用这个程序把信息写入卡片。
CourseClient 客户端上课用的
