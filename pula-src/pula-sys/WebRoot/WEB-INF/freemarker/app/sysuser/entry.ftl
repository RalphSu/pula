<#import "/app/macros/commonBase.ftl" as b>
<@b.html title="sysUser"> <#include "/calendar.ftl"/>
  <div class="top" id="__top"> </div>
  <div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
      <form action="${uri}" method="get" id="searchForm">
        <table border="0" class="grid" width="1000" >
          <colgroup>
          <col style="width:80px"/>
          <col/>
          <col style="width:60px"/>
          <col/>
          <col style="width:60px"/>
          <col style="width:120px"/>
          <col style="width:60px"/>
          <col style="width:120px"/>
          <col style="width:80px"/>
          <col style="width:140px"/>
          </colgroup>
          <tr class="title">
            <td colspan="10">查询条件</td></tr><tr>
         
            <td width="100">登录账号</td>
            <td ><input type="text" name="condition.loginId" value="${condition.loginId?if_exists?html}" size="15"/></td>
         
            <td>姓名</td>
            <td ><input type="text" name="condition.name" value="${condition.name?if_exists?html}"  size="15"/></td>
       
            <td>用户组</td>
            <td ><select name="condition.groupId">
                <option value="">(全部)</option>
                <#list groups as r>
                <option value="${r.id?if_exists?html}">${r.name?if_exists?html}</option>
                </#list>
              </select></td>
         <td>角色</td>
            <td ><select name="condition.roleId">
                <option value="">(全部)</option>
                <#list roles as r>
                <option value="${r.id?if_exists?html}">${r.name?if_exists?html}</option>
                </#list>
              </select></td>
			  <td>分支机构</td>
		<td>
		<select name="condition.branchId" id="condition.branchId">
		<option value="">(全部)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no?if_exists?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
            </tr>
          
          <tr>
            <td colspan="10"><input type="submit" value="查询" id="searchBtn"/>
              <input type="reset" value="重填" id="resetBtn"/></td>
          </tr>
        </table>
      </form>
    </div>
	
    <!-- listview -->
    <div class="l forList" id="listview">
      <form id="batchForm">
        <div id="dt"> </div>
      </form>
      <!-- left ends --> 
    </div>
    
    <!-- edit form -->
    <div id="inputPanel" class="l h">
      <form action="${uri}" method="post" id="addForm">
        <table border="0" class="grid l" width="450" >
          <colgroup>
          <col style="width:100px"/>
          <col style="width:350px"/>
          </colgroup>
          <tr class="title">
            <td colspan="2"><div class="l">请填写下列信息 <span id="pageMode" style="color:blue"></span></div>
             <div class="backToList r" id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div>
              </td>
			 
          </tr>
          <tr height="30">
            <td width="100">登录账号 <span class="redStar">*</span></td>
            <td ><input type="text" name="sysUser.loginId" value="" id="sysUser.loginId" maxlength="40"/>
              <span id="spanLoginId" class="h"></span></td>
          </tr>
          <tr>
            <td>密码 <span class="redStar">*</span></td>
            <td ><input type="password" name="sysUser.password" value="" id="sysUser.password" maxlength="40"/>
              <label for="cb" id="lblChangePassword">
                <input type="checkbox" name="sysUser.changePassword" value="true" id="cb"/>
                <@b.text key="edit"/>
              </label></td>
          </tr>
          <tr>
            <td>姓名 <span class="redStar">*</span></td>
            <td ><input type="text" name="sysUser.name" id="sysUser.name" size="20" maxlength="10" value=""/></td>
          </tr>
          <tr>
		   <td>用户权限组 <span class="redStar no_sales_men">*</span></td>
            <td ><select name="sysUser.groupId" id="sysUser.groupId" class="no_sales_men">
                <option value="">请选择...</option>
                <#list groups as r>
                <option value="${r.id?if_exists?html}">${r.name?if_exists?html}</option>
                </#list>
              </select>
			  <span class="show_sales_men">销售人员组</span>
			  </td></tr>
			  <tr>
			<td>角色 <span class="redStar no_sales_men">*</span></td>
            <td ><select name="sysUser.roleId" id="sysUser.roleId" class="no_sales_men">
                <option value="">请选择...</option>
                <#list roles as r>
				<#if r.no=='R020_SALES'>
				<#else>
                <option value="${r.id?if_exists?html}" no="${r.no!?html}">${r.name?if_exists?html}</option>
				</#if>
                </#list>
              </select>
			  <span class="show_sales_men">销售人员</span>
			  
			  </td>
            </tr>
			<tr>
		   <td>分支机构 <span class="redStar no_sales_men">*</span></td>
            <td ><select name="sysUser.branchId" id="sysUser.branchId" class="no_sales_men_lock">
                <option value="">请选择...</option>
                <#list branches as r>
                <option value="${r.id?if_exists?html}">${r.name?if_exists?html}</option>
                </#list>
              </select>
			  
			  </td></tr>
			  <tr>
          <tr>
            <td colspan="2">
			  <div class="l"><input type="submit" value="保存" id="submitBtn"/>
				<input type="hidden" name="sysUser.id" id="sysUser.id"/>
			  </div>
              <div class="r">                
                <input type="button" value="权限" id="purviewBtn" onclick="pes.purviewThis()" style="color:green"/>
              </div>
			<div class="c"></div>	  
		</td>
          </tr>
        </table>
		
		


      </form>


    </div>
  </div>
  <!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/sysuser.js"></script> 
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}',admin:${admin?string("true","false")},salesman_mode:false
}

var pageConsts= {
	ROLE_SALES :'R020_SALES'
}

var lang = {
	loginId :'登录账号',
	name:'姓名',
	status:'<@b.text key="enabled"/>',
	groupName:'用户权限组',
	purview:'权限',
	domain:'用户',purview:'权限',setup:'设置',roleName:'角色',positionName:'位置',beHim:'切入',department:'管理部门'
}


	function check(){
		if(isEmpty($F("sysUser.loginId"))){
			alert("请输入登录账号");
			$("sysUser.loginId").focus();
			return false;
		}

		if(!isKeyStr($F("sysUser.loginId"))){
			alert("请正确填写管理员登录账号，英文数字组合");
			$("sysUser.loginId").focus();
			return false;
		}		

		if(isEmpty($F("sysUser.name"))){
			alert("请输入名称");
			$("sysUser.name").focus();
			return false;
		}

		if(!pageVars.salesman_mode){

			if(isEmpty($F("sysUser.groupId"))){
				alert("请选取所属组");
				$("sysUser.groupId").focus();
				return false;
			}
			if(isEmpty($F("sysUser.roleId"))){
				alert("请选取角色");
				$("sysUser.roleId").focus();
				return false;
			}		

			if(isEmpty($F("sysUser.branchId"))){
				alert("请选取分支机构");
				$("sysUser.branchId").focus();
				return false;
			}		
		}else{

		}

		return true ;
	}
</script> 
</@b.html>
