var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'course.no'});
			this.initToolBar({add:true,condition:true});
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			

			loadCalCss('.dateField');

			var $this = this;

			var obj = new TSelectLoader( {'leader':'categoryId','fireChangeAfterLoad':true,
					'uri':pageVars.base+'/app/course/listByCategory',
					'params':function(v){
						return "categoryId="+v;
					}}
			);

			if($("cBranchId")){
				var obj = new TSelectLoader( {'leader':'cBranchId','fireChangeAfterLoad':true,
						'uri':pageVars.base+'/app/classroom/listByBranch',
						'params':function(v){
							return "branchId="+v;
						}}
				);
			}
			
			this.edtStudent = new TSimpleNo({
				field:'condition.studentNo',
				field_id:'cStudentNo',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../student/find',
				container:'sStudent'
			});	
			this.teacherNo = new TSimpleNo({
				field:'form.teacherNo',
				field_id:'cTeacherNo',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../teacher/find',
				container:'sTeacher'
			});	
			this.reload();

			//c();
		},//init ends


		initViewTable : function(){
			var getRequestParam = function(){
				return this.vars.requestParam;
			}
			var onSelectRow = function(i,tr){
				if(i==-1){
					this.navigateHistory("id", 'create');
				}else{
					var data = this.dt.rows[i] ;
					//this.showData(data);
					if(!data.allowEdit){
						this.view(data.id);
					}else{
						this.manage(data.id);
					}
				}
			}

			var showLabel = function(d){
				if(d.allowEdit) return "管理";
				return "查看";
			}
			var showFunc = function(d){
				if(d.allowEdit) return "pes.manage";
				return "pes.view";
			}
			var cfgs = {
				id :'dt',
				container:'dt',
				height:PGlobals.minusHeight.bind(['conditionDiv','__top']),
				url:"list",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns: [
					//{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:'开始时间',width:160,key:'startTime',formatter:TTable.formatDateTime},
					{label:'结束时间',width:160,key:'endTime',formatter:TTable.formatDateTime},
					{label:'学生数',key:'studentCount',width:45,align:'right'},
					{label:'主教师',key:'masterName',width:100},
					{label:'助教1',key:'assistant1Name',width:100},
					{label:'助教2',key:'assistant2Name',width:100},
					{label:'课程分类',key:'courseCategoryName',width:100,align:'center'},
					{label:'课程',key:'courseName'},
					{label:'分校',key:'branchName',width:140},
					{label:'教室',key:'classroomName',width:140},
					{label:'来源',key:'submitTypeName',width:60,align:'center'},
					{label:'管理',width:40,key:'id',formatter:TTable.formatLinkJs.bind({func:showFunc,label:showLabel}),align:'center'}
				]
			};

			if(PGlobals.smallScreen()){
				cfgs.selectRow = null ;
				cfgs.intoRow = onSelectRow.bind(this) ;
			}

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();
			
		},
		manage : function(id ){
		var url = encodeURIComponent('../coursetaskresult/update?id='+id);
		        centerWindow('../my/window?u=' + url,950, 500);
		}	,
		view : function(id ){
		var url = encodeURIComponent('../coursetaskresult/view?id='+id);
		        centerWindow('../my/window?u=' + url,950, 480);
		}	,
		createPage : function(id ){
		var url = encodeURIComponent('../coursetaskresult/create');
		        centerWindow('../my/window?u=' + url,950, 580);
		}	,

		showData:function (d){

			
		},updateMode:function(){

		}
		
		
});

var WORKER_TPL = "<a href='javascript:pes.eraseWorker(\"{no}\")'>{no} {name}<input type='hidden' name='course.courseNos' value='{no}'/></a>";
var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


