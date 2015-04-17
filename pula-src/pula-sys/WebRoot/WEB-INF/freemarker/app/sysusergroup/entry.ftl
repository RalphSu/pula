<#import "/app/macros/commonBase.ftl" as b>
<@b.html title="用户权限组"> <#include "/calendar.ftl"/>
  <div class="top" id="__top"> </div>
  <div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
      <form action="${uri}" method="get" id="searchForm">
        <table border="0" class="grid" width="600" >
          <colgroup>
          <col style="width:80px"/>
          <col/>
          <col style="width:60px"/>
          <col/>
          </colgroup>
          <tr class="title">
            <td colspan="4">查询条件</td></tr><tr>
         
            <td width="100">编号</td>
            <td ><input type="text" name="condition.loginId" value="${condition.loginId?if_exists?html}" class="inputText"/></td>
         
            <td>名称</td>
            <td ><input type="text" name="condition.name" value="${condition.name?if_exists?html}" class="inputText"/></td>
            </tr>
          <tr>
            <td colspan="4"><input type="submit" value="查询" id="searchBtn"/>
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
        <table border="0" class="grid" width="600">
          <colgroup>
          <col style="width:100px"/>
		  <col/>
          </colgroup>
          <tr class="title">
            <td colspan="2"><div class="l">请填写下列信息 <span id="pageMode" style="color:blue"></span></div>             
              <div class="backToList r" id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
          </tr>
          <tr height="30">
            <td width="100">编号 <span class="redStar">*</span></td>
            <td ><input type="text" name="sysUserGroup.no" value="" id="sysUserGroup.no" maxlength="40"/></td>
          </tr>
          
          <tr>
            <td>名称 <span class="redStar">*</span></td>
            <td ><input type="text" name="sysUserGroup.name" id="sysUserGroup.name" size="20" maxlength="10" value=""/></td>
          </tr>
		 
          <tr>
            <td colspan="2">
			  <div class="l"><input type="submit" value="确定" id="submitBtn"/>
				<input type="hidden" name="sysUserGroup.id" id="sysUserGroup.id"/>
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
<script type="text/javascript" src="${base}/static/app/javascript/sysusergroup.js"></script> 
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	no :'编号',
	name:'名称',
	status:'<@b.text key="enabled"/>',
	purview:'权限',
	domain:'用户权限组',purview:'权限',setup:'设置'
}


	function check(){
		if(isEmpty($F("sysUserGroup.no"))){
			alert("请输入编号");
			$("sysUserGroup.no").focus();
			return false;
		}

		if(!isKeyStr($F("sysUserGroup.no"))){
			alert("请正确填写编号，英文数字组合");
			$("sysUserGroup.no").focus();
			return false;
		}		

		if(isEmpty($F("sysUserGroup.name"))){
			alert("请输入名称");
			$("sysUserGroup.name").focus();
			return false;
		}

		return true ;
	}
</script> 
</@b.html>
