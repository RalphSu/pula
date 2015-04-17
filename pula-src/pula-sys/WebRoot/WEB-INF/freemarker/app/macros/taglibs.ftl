<#setting url_escaping_charset='utf-8'/><#macro basePath><#if springMacroRequestContext.getContextPath()=="/"><#else>${springMacroRequestContext.getContextPath()}</#if></#macro>
<#macro urlPath>${springMacroRequestContext.getRequestUri()}<#if springMacroRequestContext.getQueryString()?exists>?${springMacroRequestContext.queryString}</#if></#macro>
<#macro text key><#if mls?exists>${mls.t(key,null)?if_exists?html}<#else>${key}</#if></#macro>
<#global base><@basePath/></#global>
<#global url><@urlPath/></#global>
<#global uri>${springMacroRequestContext.getRequestUri()}</#global>
<#macro select id options attributes="">
    <select id="${id}" name="${id}" ${attributes}>
        <#list options as value>
        <option value="${value.id}" <#if value.selected>selected</#if>>${value.name}</option>
        </#list>
    </select>
</#macro>
<#macro radio id options attributes="">
	<#list options as value>
    <label for="${id}_${value.id}"><input type="radio" id="${id}_${value.id}" name="${id}" ${attributes} <#if value.selected>checked</#if> value="${value.id}">${value.name}</label>
    </#list>
</#macro>
<#macro radioclick id options function attributes="">
	<#list options as value>
    <label for="${id}_${value.id}" onClick="${function}"><input onClick="${function}" type="radio" id="${id}_${value.id}" name="${id}" ${attributes} <#if value.selected>checked</#if> value="${value.id}">${value.name}</label>
    </#list>
</#macro>
<#macro boldThat kind equalKind>
<#if kind==equalKind><B><#nested/></b><#else/><#nested/></#if>
</#macro>
<#macro echoOver kind equalKind>
<#if kind==equalKind>Over<#else/></#if>
</#macro>
<#macro color boolValue fontColor>
<#if boolValue><font color="${fontColor}"><#nested/></font><#else/><#nested/></#if>
</#macro>
<#macro submitForm formName checkFunc="check" buttonFunc="disableBtn3" >
$('${formName}').addEvent('submit',function (e){
			e.stop();
			if(!${checkFunc}()) return ;
			var aj = new Request.JSON({
						url:$('${formName}').get('action'),

						onSuccess:function (e){
							
							${buttonFunc}(false);
							if(e.error){
								topHiddenSuccess();
								alert(e.message);
							}else{
								topSuccess();
								<#nested/>
								
							}
						}
					}).send($('${formName}').toQueryString());
			

		});
</#macro>
<#macro pager res>
共有${res.totalCount}个数据，每页:${res.pageSize}个 共有${res.getTotalPageCount()}页，现在是第 ${res.getCurrentPageNo()} 页 ，转到第
<input name="pNo" type="text" style="width:40px" value="" id="pNoTxt" class="pagerEdt" maxPage="${res.getTotalPageCount()}"> 页  
<#assign prevPage=res.getCurrentPageNo()-1/>
<#assign nextPage=res.getCurrentPageNo()+1/>
<#if prevPage&gt;0><a href="javascript:_goPageJs('${prevPage}')">前一页</a></#if>
<#if nextPage&lt;=res.getTotalPageCount()><a href="javascript:_goPageJs('${nextPage}')">后一页</a></#if>
</#macro>
<#macro output html>
${html?html?replace("\n","<BR>")}
</#macro>
<#macro shortFor str len>
<#if str?length&gt;len>
${str?substring(0,len)?html}...
<#else>
${str?html}</#if>
</#macro>
<#macro css bool css>
<#if bool><span class="${css}"><#nested/></span><#else/><#nested/></#if>
</#macro>