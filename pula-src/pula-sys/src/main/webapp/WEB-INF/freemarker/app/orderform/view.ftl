<#import "/app/macros/commonBase.ftl" as b><@b.html title="订单">
<style>
.top .text{
	line-height:25px;
	height:25px;
	margin-top:3px;
	color:#5f5f5f;
}
.even{
	background:#f7ffff;
}

.ready-for-student{
}
.t-simple-no input{ width: 100px}
.t-simple-no select{ width:100px}


.body input{
	border:0px;
}
</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
	<!-- edit form -->
	
	<table border="0" class="grid" width="800">
<colgroup>
        <col style="width:120px;"></col>
        <col ></col> 
        <col style="width:80px;"></col>
        <col ></col> 
        <col style="width:140px;"></col>
</colgroup>
  <tr class="title"> 
    <td colspan="5">订单学员信息</td>
  </tr>
    <tr> 
    <td >学员编号 </td>
    <td colspan="3">
	
	<span id="spanStudentNo"></span>
	<span id="spanStudentName"></span>
	
	</td>
   
	<td rowspan="5" valign="attop" class="photo">
	<A id="uploadPic"><img src="/pula-sys/static/app/images/nophoto.jpg"/></A>
	
	</td>
	</tr>
   <tr> 
	<td>性别</td>
	<td>
		<span id="spnGender" class="ready-for-student"></span></td>
	<td>年龄</td>
	<td><span id="spnAge" class="ready-for-student"></span></td>
   </tr><tr class="title"> 
    <td colspan="4">产品信息</td>
  </tr><tr>
		<td>购买产品</td>
		<td>
		 ${orderForm.courseProduct.name!?html}</td>
	
		<td>价格</td>
		<td>${orderForm.totalAmount?string("#.##")}</td>
	</tr><tr>
		<td>课程数量</td>
		<td colspan="3"><span class="ready-for-product" id="spanCourseCount">${orderForm.courseCount}</span>
		
		已上课程数:
		<span class="ready-for-product" id="spanConsumeCourseCount">${orderForm.consumeCourseCount}</span>
		</td>
	
	</tr><tr class="title"> 
    <td colspan="5">销售信息</td>
  </tr>		
	
	<tr>
		<td>主销售人员</td>
		<td><span id="spanMasterNo"></span> <span id="spanMasterName"></span></td>
	
		<td>辅销售人员</td>
		<td colspan="2"><span id="spanSlaveNo"></span> <span id="spanSlaveName"></span></td>
	</tr>
	<tr>
		<td>佣金类型</td>
		<td >

		${orderForm.commissionTypeName!?html}
		
		</td><td>支付状态</td>
		<td >
		<div class="l">
		${orderForm.payStatusName!?html}
		</div>
		<div class="r prepay-ui"style="line-height:21px;margin-top:2px;">
		预付金额
		</div>
		<div class="c"></div>		
		</td>
		<td><INPUT type="text" name="form.prepay" id="form.prepay" class="numberEdit prepay-ui" style="width:80px" value="0"></td>
	</tr>
	<tr><td>辅助教师</td>
		<td colspan="1"><span id="spanTeacherNo"></span> <span id="spanTeacherName"></span></td><td >赠分</td>
		<td colspan="2"><input type="text" name="form.points" id="form.points" size="10" class="numberEdit" value='0' /> </td>
	</tr>
	<tr>
		<td>备注</td>
		<td colspan="3">
		<div class="l"><input type="text" name="form.comments" id="form.comments" size="60" maxlength="100">
		</div><div class="r prepay-ui" >余额</div>
		</td>
		<td>
		<div class="prepay-ui numberEdit" id="divLeftPrice" style="width:80px"></div>
		</td>
	</tr>
	<tr>
	</tr>	
	
	 
	</table>
</div>


<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/orderform_view4chargeback.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-datagrid.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/UUID.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}',
	imagePath:'${base}/static/laputa/images/icons/',
	updateMode:${updateMode?string("true","false")},
	noPhoto:'${base}/static/app/images/nophoto.jpg'
}

var pageConsts= {
	PS_PREPAY : 1 
}

var lang = {
	name:'名称',no:'编号',domain:'查看订单',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',picNo:'图号',msgSelectFirst:'请选择...',uploadPic:'上传照片',uploadFile:'上传附件',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消',relationShip:'关系',phone:'电话',address:'地址',
	name:'姓名',attachmentItem:'<span class="fileName"><a href="#" data="{fileId}" dataId="{id}" class="download" title="下载">{fileName}</a><a href="#" data="{fileId}" dataId="{id}" class="remove" title="删除"></a></span>',tipsOfTSN:'输入名称加载',
	noneAttachment:"<span class='gray'>暂无附件</span>"
}





	var pes = null ;
	window.addEvent('domready',function(){
		pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

	<#if updateMode>
		//set value 
		<#assign student=orderForm.student/>
		$('spanStudentNo').set('html',  '${student.no!?html}');
		$('spanStudentName').set('html','${student.name!?html}' );
		<#if orderForm.teacher??>
		$('spanTeacherNo').set('html', '${orderForm.teacher.no!?html}');
		$('spanTeacherName').set('html','${orderForm.teacher.name!?html}');
		</#if>
		$('spanMasterNo').set('html','${orderForm.masterSalesman.no!?html}');
		$('spanMasterName').set('html','${orderForm.masterSalesman.name!?html}');
		<#if orderForm.slaveSalesman??>$('spanSlaveNo').set('html','${orderForm.slaveSalesman.no!?html}');$('spanSlaveName').set('html','${orderForm.slaveSalesman.name!?html}');</#if>
			
		
		$('form.comments').value = "${orderForm.comments!?js_string}";
		pageVars.id = ${orderForm.id} ;


		$$('.rbCommType').each ( function(el ) {
			if( el.value == ${orderForm.commissionType} ) {
				el.checked = true ;
			}
		});
		$$('.rbPayStatus').each ( function(el ) {
			if( el.value == ${orderForm.payStatus} ) {
				el.checked = true ;
				el.fireEvent('click');
			}
		});
		<#if orderForm.payStatus!=1>
			$$(".prepay-ui").addClass('h');
		</#if>

		$('form.points').value = "${orderForm.points}";
		$('form.prepay').value = "${orderForm.prepay?string("#.##")}";
		$$("input").set('readonly',true);
		pes.vars.price = "${orderForm.totalAmount?string("#.##")}";

		pes.calcLeftPrice();
		
		//student
		var data = { genderName:'${student.genderName!?html}' <#if student.birthday??>,birthday: ${student.birthday.timeInMillis}</#if>
					<#if icon??>,iconId:"${icon.id!?js_string}",iconFileId:"${icon.fileId!?js_string}"</#if>
					};
		pes.showStudent( data );
		

		var div= new Element("div",{'class':'text l',html:'${orderForm.id!?js_string} ${orderForm.no!?js_string} <#if orderForm.updater??>最后修改人:${orderForm.updater.loginId!?html}(${orderForm.updater.name!?html}) ${orderForm.updatedTime.time?datetime}</#if>'});
		var obj = $$("#__top .t-head .t-title");
		div.inject(obj[0],'after');
	<#else>
		pes.mock();
	</#if>

	

});



</script>
</@b.html>