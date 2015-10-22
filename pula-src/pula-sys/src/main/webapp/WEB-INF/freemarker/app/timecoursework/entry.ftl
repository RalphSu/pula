<#import "/app/macros/commonBase.ftl" as b><@b.html
title="course.title">
<div class="top" id="__top"></div>
<div class="body">
	<!-- condition -->
	<div id="conditionDiv" class="h forList">
		<form action="${uri}" method="get" id="searchForm">
			<table border="0" class="grid" width="1000" id="queryForm">
				<colgroup>
					<col style="width: 70px" />
					<col />
					<col style="width: 70px" />
					<col />
					<col style="width: 80px" />
					<col />
					<col style="width: 80px" />
					<col style="width: 60px" />
					<col style="width: 80px" />
				</colgroup>
				<tr class="title">
					<td colspan="8">查询条件</td>
				</tr>
				<tr>
					<td>课程编号</td>
					<td><input type="text" name="condition.courseNo"
						value="${condition.courseNo?if_exists?html}" /></td>
					<td >学员编号</td>
					<td><input type="text" name="condition.studentNo"
						value="${condition.studentNo?if_exists?html}" /></td>
					<td >上课时间</td>
					<td><input type="text" name="condition.workEffectDate"
						value="${condition.workEffectDate?if_exists?html}" class="dateField" /></td>
				</tr>
				<tr>
					<td colspan="8"><input type="submit" value="查询" id="searchBtn" />
					</td>
				</tr>
			</table>
		</form>
	</div>


	<!-- listview -->
	<div class="l forList" id="listview">
		<form id="batchForm">
			<div id="dt"></div>
		</form>
		<!-- left ends -->
	</div>

	<!-- edit form -->
	<div id="inputPanel" class="l h">
		<form action="${uri}" method="post" id="addForm">
			<table border="0" class="grid" width="700">
				<colgroup>
					<col style="width: 100px" />
					<col />
					<col style="width: 100px" />
					<col />
				</colgroup>
				<tr class="title">
					<td colspan="4"><div class="l">
							请填写下列信息 <span id="pageMode" style="color: blue"></span>
						</div>
						<div class="r">
							<div class="backToList " id="backToList">
								<A href="javascript:pes.backToList()">返回至列表</A>
							</div></td>
				</tr>
				<tr>
					<!-- <td>编号</td>
					<td><input type="hiddern" name="course.no" id="course.no"
						size="20" maxlength="40" /></td> -->

					<td>课程编号<span class="redStar">*</span></td>
					<td>
					<!-- <input type="text" name="course.courseNo" id="course.courseNo"
						size="20" maxlength="40" /> -->
						<select name="course.courseNo" id="course.courseNo">
						<#list courses as tp>
						<option value="${tp.no?if_exists?html}">${tp.name?if_exists?html}</option>
						</#list>
						</select>
					</td>

					<td>学生编号<span class="redStar">*</span></td>
					<td><input type="text" name="course.studentNo" id="course.studentNo"
						size="20" maxlength="40" /></td>
				</tr>
				
				<tr>
					<td>作品时间<span class="redStar">*</span></td>
					<td><input type="text" name="course.workEffectDate" id="course.workEffectDate"
						size="20" maxlength="40" class="dateField"/></td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td>注释 </td>
					<td><input type="text" name="course.comments" id="course.comments"
						size="20" maxlength="80" /></td>
					<td colspan="2">
					</td>
				</tr>

				<tr>
					<td>作品:<span class="redStar">*</span></td>
					<td colspan="3">
						<input type="text" name="course.imagePath" id="course.imagePath"
							size="60" maxlength="120" disabled />
					</td>
				</tr>
				<tr>
				<td colspan="4">
						<A href="#" id="uploadPic"><img src="${base}/static/app/images/nophoto.jpg"/></A>
					</td>
				</tr>

				<tr>
					<td colspan="4">
					<input type="submit" value="<@b.text key=" submitBtn "/>" id="submitBtn"/> 
						<input type="hidden" name="course.id"
						id="course.id" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css"
	href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript"
	src="${base}/static/library/puerta/t-table.js"></script>
<script type="text/javascript"
	src="${base}/static/app/javascript/timecoursework.js"></script>
<script type="text/javascript"
	src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript"
	src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	name:'名称',no:'编号',domain:'作品管理',gender:'性别',status:'状态',print:'打印条码',printOK:'选中项已成功输出打印',file:'档案'
	,view:'查看',birthday:'生日',branchName:'所在分支机构',assign:'指派',levelName:'级别',barcode:'卡号'
	, studentName: '学员名字', courseName:'课程名', studentNo:'学员编号', courseNo:'课程号'
	, workEffectDate: '上课时间', rate:'评级', image: '作品', comments:'备注',
	uploadPic:'上传照片',uploadFile:'上传附件'
	
}

	function checkAssign(){
		return true ;
	}

	function check(){
		return true ;
	}
</script>
</@b.html>
