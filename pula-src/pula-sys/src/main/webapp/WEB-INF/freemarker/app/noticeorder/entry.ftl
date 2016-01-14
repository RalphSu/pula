<#import "/app/macros/commonBase.ftl" as b><@b.html
title="course.title">

<style>
#workerList A:hover {
	text-decoration: line-through;
}
</style>
<div class="top" id="__top"></div>
<div class="body">
	<!--  search form -->
	<div id="conditionDiv" class="h forList"><#include
		"condition.ftl"/></div>


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
			<div class="l">
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
						<td>活动<span class="redStar">*</span></td>
						<td>
						<select name="course.noticeNo" id="course.noticeNo">
						<#list notices as tp>
						<option value="${tp.no?if_exists?html}">${tp.title?if_exists?html}</option>
						</#list>
						</select>
						</td>
						<!-- <td><input type="text" name="course.noticeNo"
							id="course.noticeNo" size="20" maxlength="40" /></td> -->
						<td>购买次数<span class="redStar">*</span></td>
						<td><input type="text" name="course.count"
							id="course.count" size="20" maxlength="40" /></td>
					</tr>

					<tr>
						<td>学号<span class="redStar">*</span></td>
						<!-- <td>
						<select name="course.studentNo" id="course.studentNo">
						<#list students as tp>
						<option value="${tp.no?if_exists?html}">${tp.name?if_exists?html}</option>
						</#list>
						</select>
						</td> -->
						
						<td><input type="text" name="course.studentNo"
							id="course.studentNo" size="20" maxlength="40" /></td>

						<td>订单状态(0 - 未支付,1 - 完成)</td>
						<td><input type="text" name="course.orderPayStatus"
							id="course.orderPayStatus" size="20" maxlength="40"
							class="numberEdit" /></td>
					</tr>

					<tr>
						<td>支付金额</td>
						<td colspan="1"><input type="text" name="course.paied"
							id="course.paied" size="20" maxlength="40" class="numberEdit" />
						</td>
						<td>微信预支付ID</td>
						<td><input type="text" name="course.prepayId"
							id="course.prepayId" size="20" maxlength="40" /></td>
					</tr>

					<tr>
						<td>备注</td>
						<td colspan="3"><input type="text" name="course.comments"
							id="course.comments" size="60" maxlength="80" /></td>
					</tr>
					<tr>
						<td colspan="4"><input type="submit" value="<@b.text key="submitBtn"/>"
							id="submitBtn"/> 
						<input type="hidden" name="course.id"
							id="course.id" /></td>
					</tr>
				</table>

			</div>

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
	src="${base}/static/app/javascript/notice_order.js"></script>
<script type="text/javascript"
	src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript"
	src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript"
	src="${base}/static/library/puerta/t-selectorloader.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	name:'名称',no:'编号',domain:'课程',expiredTime:'结束日期',publishTime:'开始日期',showInWeb:'显示在网站上',indexNo:'序号',
	status:'状态', branchName: '分部', classRoomName: '教室',
	buyType: '购买类型', paied:'付款金额', paiedCount:'购买次数', courseNo:'课程编号', studentNo:'学号', usedCount:'已使用次数', remainCost:'剩余款',
	updateTime:'最后更新', updator:'操作人',
	orderPayStatus:'订单支付状态', wxPayStatus:'微信支付状态'
}

	function check(){

		var b = true;
		var eel = null ;
		$$(".numberEdit").each ( function (el){
			var v = Number.from( el.value ) ;
			if( b && !v && v!=0  ) {
				eel = el ;
				b= false;
			}
			
		});

		if(!b){
			alert("请正确填写数值");
			eel.select();
			eel.focus();
			return false;
		}

		len = $F("course.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("course.comments").focus();
			return false;
		}

		var pass = true ;
		$$(".dateField").each ( function(el){
			if( !pass ) return ;
			var dv = $F( el );
			if( !isEmpty( dv ) &&  !isDate( dv )){
				alert("请正确填写日期");
				el.focus();
				pass =false; 
				return ;
			}
		});

		if(!pass ) {
			return false ;
		}
		
		return true ;
	}
</script>
</@b.html>
