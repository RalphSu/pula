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
</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
	<!-- edit form -->
	
	<form action="${uri}" method="post" id="addForm">
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
    <td ><div class="l">学员编号 <font color=red>*</font></div><div class="r"><img src="${base}/static/laputa/images/16X16/add.png" width="16" height="16" style="cursor:pointer" id="imgNew" align="absmiddle" alt="新增一名学员"/></div> </td>
    <td colspan="3">
	<div id="sStudent"></div>
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
		<td>购买产品 <font color=red>*</font></td>
		<td>
		  <select name="form.courseProductId" id="form.courseProductId">
		    <option value="0">请选择...</option>
			<#list productList as cp>
		    <option value="${cp.id}" price="${cp.price!?string("#.00")}" courseCount="${cp.courseCount}">${cp.name!?html}</option>
			</#list>
        </select></td>
	
		<td>价格</td>
		<td><span class="ready-for-product" id="spanPrice"></span></td>
	</tr><tr>
		<td>课程数量</td>
		<td colspan="3"><span class="ready-for-product" id="spanCourseCount"></span></td>
	
	</tr><tr class="title"> 
    <td colspan="5">销售信息</td>
  </tr>		
	
	<tr>
		<td>主销售人员 <font color=red>*</font></td>
		<td><div id="smsm"></div></td>
	
		<td>辅销售人员</td>
		<td colspan="2"><div id="sssm"></div></td>
	</tr>
	<tr>
		<td>佣金类型 <font color=red>*</font></td>
		<td >

		<#list commisionTypeList as ps>
		<label for="ct${ps.id}"><input name="form.commissionType" type="radio" value="${ps.id!?html}" id="ct${ps.id}" class="rbCommType" />${ps.name!?html}</label>
		</#list>
		
		</td><td>支付状态 <font color=red>*</font></td>
		<td >
		<div class="l"><#list payStatusList as ps>
		<label for="ps${ps.id}"><input name="form.payStatus" type="radio" value="${ps.id!?html}" id="ps${ps.id}" class="rbPayStatus" />${ps.name!?html}</label>
		</#list>
		</div>
		<div class="r prepay-ui"style="line-height:21px;margin-top:2px;">
		预付金额
		</div>
		<div class="c"></div>		
		</td>
		<td><INPUT type="text" name="form.prepay" id="form.prepay" class="numberEdit prepay-ui" style="width:80px" value="0"></td>
	</tr>
	<tr><td>辅助教师</td>
		<td colspan="1"><div id="sTeacherNo"></div></td><td >赠分</td>
		<td colspan="2"><input type="text" name="form.points" id="form.points" size="10" class="numberEdit" value='0'/> </td>
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
		<td colspan="5">
		
		<div class="l">
		<input type="button" value="保存" id="saveBtn" accesskey="s"/>
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn" class="h"/>
		<input type="hidden" name="form.id" id="form.id" value="0"/>
		</div>
		<div class="r">
			ALT+S 保存
		</div>
		
		</td>
	  </tr> 
	</table>
	</form>
</div>
<div id="divStudent" class="h">
<form id="frmStudent">
<table border="0" class="grid" width="700">
	<colgroup><col style="width:60px"/>
	<col /><col style="width:60px"/>
	<col /><col style="width:60px"/>
	<col />
	</colgroup>
	  <tr> 
		<td >姓名 <font color=red>*</font></td>
		<td ><input type="text" name="student.name" maxlength="20" id="student.name" /></td>
		<td >生日</td>
		<td ><input type="text" name="student.birthdayText" maxlength="10" size="12" id="student.birthdayText" class="dateField" /></td>
		<td>性别 <font color=red>*</font></td>
	<td>
		<#list genders as tp>
		<label><input type="radio" name="student.gender" value="${tp.id?if_exists?html}" class="rGender"/>${tp.name?if_exists?html}</label>
		</#list>
	</td>
	  </tr> 
	  <tr> 
		<td >备注</td>
		<td colspan="5"><input type="text" name="student.comments" id="student.comments" size="60" maxlength="80"/> </td>
	  </tr> 
	  <tr> 
		<td colspan="6"><span class="l">
		  <input type="submit" value="保存" id="saveStudentBtn" accesskey="t"/>
		</span></td>
	  </tr> 
	</table>
</form>
</div>

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/orderform_create.js"></script>
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
	PS_PREPAY : 2 ,
	S_INPUT : 1
}

var lang = {
	name:'名称',no:'编号',domain:'<#if updateMode>修改<#else>新增</#if>订单',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',picNo:'图号',msgSelectFirst:'请选择...',uploadPic:'上传照片',uploadFile:'上传附件',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消',relationShip:'关系',phone:'电话',address:'地址',
	name:'姓名',attachmentItem:'<span class="fileName"><a href="#" data="{fileId}" dataId="{id}" class="download" title="下载">{fileName}</a><a href="#" data="{fileId}" dataId="{id}" class="remove" title="删除"></a></span>',tipsOfTSN:'输入名称加载',
	noneAttachment:"<span class='gray'>暂无附件</span>"
}




function checkStudent(){
		if(isEmpty($F("student.name"))){
			alert("请填写姓名");
			$("student.name").focus();
			return false; 
		}
		
		var len = $$(".rGender:checked").length ;
		if(len==0){
			alert('请选取性别');
			$$(".rGender")[0].focus();
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

		len = $F("student.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("student.comments").focus();
			return false;
		}

	return true ;
}


	function check(){
	

		if(isEmpty($F("cStudentNo"))){
			alert("请填写学员编号");
			$("cStudentNo").focus();
			return false; 
		}
		
		if(isEmpty($F("form.courseProductId"))){
			alert("请选取购买产品");
			$("form.courseProductId").focus();
			return false; 
		}
		
		if(isEmpty($F("cmSalesmanNo"))){
			alert("请正确填写主销售人员");
			$("cmSalesmanNo").focus();
			return false; 
		}

		var cmN = $F("cmSalesmanNo");
		var csN = $F("csSalesmanNo");
		if(cmN== csN){
			alert("主辅销售人员的编号相同，请修改");
			$("csSalesmanNo").focus();
			return false;
		}
		
		var len = $$(".rbCommType:checked").length ;
		if(len==0){
			alert('请选取佣金类型');
			$$(".rbCommType")[0].focus();
			return false;
		}
		
		var len = $$(".rbPayStatus:checked").length ;
		if(len==0){
			alert('请选取支付状态');
			$$(".rbPayStatus")[0].focus();
			return false;
		}
		var checkNum =false;
		if($$(".rbPayStatus")[1].checked){
			//预付金额
			checkNum= true; 
		}
		var ptz = $('form.points').value;
		if(!isInteger( ptz ) || eval(ptz) < 0 ){
			alert('请正确填写赠分');
			$('form.points').focus();
			return false;
		}

		if(checkNum){
			var v = Number.from( $F("form.prepay") );
			if(  !v || v<=0  ) {
				alert( '请正确填写预付款');
				return false;
			}
		}else{
			$("form.prepay").value = 0 ;
		}

		len = $F("form.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("form.comments").focus();
			return false;
		}


		
		return true ;
	}

	var pes = null ;
	window.addEvent('domready',function(){
		pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

	<#if updateMode>
		//set value 
		<#assign student=orderForm.student/>
		pes.edtStudent.setValue( '${student.no!?html}','${student.name!?html}' );
		<#if orderForm.teacher??>
		pes.edtTeacher.setValue( '${orderForm.teacher.no!?html}','${orderForm.teacher.name!?html}' );
		</#if>
		pes.master.setValue('${orderForm.masterSalesman.no!?html}','${orderForm.masterSalesman.name!?html}');
		<#if orderForm.slaveSalesman??>pes.slave.setValue('${orderForm.slaveSalesman.no!?html}','${orderForm.slaveSalesman.name!?html}');</#if>
		$('form.courseProductId').set('value', "${orderForm.courseProduct.id}").fireEvent('change');
		
		
		$('form.comments').value = "${orderForm.comments!?js_string}";
		$('form.id').value = "${orderForm.id}";
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

		$('form.points').value = "${orderForm.points}";
		$('form.prepay').value = "${orderForm.prepay?string("#.##")}";
		pes.vars.price = "${orderForm.totalAmount?string("#.##")}";

		pes.calcLeftPrice();
		
		//student
		var data = { genderName:'${student.genderName!?html}' <#if student.birthday??>,birthday: ${student.birthday.timeInMillis}</#if>
					<#if icon??>,iconId:"${icon.id!?js_string}",iconFileId:"${icon.fileId!?js_string}"</#if>
					};
		pes.showStudent( data );
		

		var div= new Element("div",{'class':'text l',html:'${orderForm.id!?js_string} <#if orderForm.updater??>最后修改人:${orderForm.updater.loginId!?html}(${orderForm.updater.name!?html}) ${orderForm.updatedTime.time?datetime}</#if>'});
		var obj = $$("#__top .t-head .t-title");
		div.inject(obj[0],'after');
	<#else>
		pes.mock();
	</#if>

	

});



</script>
</@b.html>