<%@page import="com.ecquaria.cloud.entity.vault.VaultFile"%>
<%@page import="com.ecquaria.cloud.mc.api.ConsistencyHelper"%>
<%@page import="com.ecquaria.cloud.mc.base.SearchParam"%>
<%@page import="com.ecquaria.cloud.mc.common.constants.AppConstants"%>
<%@page import="com.ecquaria.cloud.mc.vault.delegator.DocumentVaultDelegator"%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%@page import="sop.i18n.MultiLangUtil"%>
<%@ page import="java.io.Serializable" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<webui:setLayout name="cc"/>

<%
	response.setContentType("text/html;charset=UTF-8");
	String searchPanelStatus = (String)request.getAttribute("searchPanelStatus");
%>

<%
/*
  You can customize this default file:
  /D:/softwares/eclipse/plugins/com.ecquaria.eclipse.sit_6.1.1/WebPage.jsp.default
*/

//handle to the Engine APIs
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<script type="text/javascript">

	var LIST_TABLE_NAME = null;
	var LIST_TABLE_DATA_UNIQUE = null;
	var DEFAULT_VIEW_ACTION = "view";
	
	function bindTableView(tableName, unique, action){
		LIST_TABLE_NAME = tableName;
		LIST_TABLE_DATA_UNIQUE = unique;
		if(action){
			DEFAULT_VIEW_ACTION = action;
		}
	}
	
	function initSearchPanel(){
		if('<%=searchPanelStatus%>'== 'true'){
			$("#slickbox").show();
			$("input[name='searchPanelStatus']").val("true");
		}else{
			$("#slickbox").hide();
			$("input[name='searchPanelStatus']").val("false");
		}	
	}
	
	function bindTableViewHandler(){
		$("table[name='" + LIST_TABLE_NAME + "'] tbody tr").each(function(){
			var unique = $(this).find("input[name='" + LIST_TABLE_DATA_UNIQUE + "']");
			if(unique && unique.length > 0){
				$(this).click(function(){
					SOP.Crud.cfxSubmit('', DEFAULT_VIEW_ACTION, unique.val());
				});
			}
		});
	}
	
	function onDelete(val){
		if(MGOV.Common.checkSelected('auditUids') || val)
			SOP.Common.confirm({message:"<egov-smc:message key='deleterecordqns'>Are you sure you want to delete the selected record(s)</egov-smc:message>", func:function(){SOP.Crud.cfxSubmit('','delete', val);}})
		else
			SOP.Common.alert('<egov-smc:message key="noauditselected">No Audit selected.</egov-smc:message>');
	}
	$(function(){
		EGOV.Common.initSearchAdvance('slick-toggle', 'slickbox');
		//EGOV.Common.holdSearchAdvance('slickbox');
		// $('[name="mainSearch"]').change(initSearchAdvance);
	//	initSearchAdvance();
		
		
		bindTableView("dataTable","uniqueKey");
		bindTableViewHandler();
		
		initSearchPanel();
	});

	function submitMainSearch(){
	   $("input[name='searchPanelStatus']").val("false");
	   SOP.Crud.cfxSubmit('', 'mainSearch');
	}
	
	function submitSearch(){
		$("input[name='searchPanelStatus']").val("true");
		SOP.Crud.cfxSubmit('', 'search');
	}
	
	function parseStr(cs){
		var brace = 0;
		var flag = false;
		var cs1 = '';
		for (var i = 0; i < cs.length; i++) {
			switch(cs[i]){
			case '(':
				brace++;
				if(brace<=1){
					if(i>0 && cs[i-1]==':'){
						cs1 += cs[i];
					}else
						brace--;
				}else
					brace--;
					
				break;
			case ')':
				brace--;
				if(brace==0){
					if(i<cs.length-1 && cs[i+1]==' '){
						cs1 += cs[i];
					}else if(i==cs.length-1)
						cs1 += cs[i];
					else
						brace++;
				}else
					brace++;
					
				//else if(brace>0)
					
				break;
			case ' '://kongge
				if(!flag && brace>0){
					//cs[i] = '_';
					cs1 += '@';
					cs1 += '_';
					cs1 += '@';
				}else
					cs1 += cs[i];
				break;
			default:
				cs1 += cs[i];
			}
		}
		
		return cs1;
	}
	
	function initSearchAdvance(){
		
		var PREFIX_SENDER_FILE = "file:";
		var PREFIX_SENDER_AGENCY = "agency:";
		var PREFIX_SENDER_SUBJECT = "subject:";
		var PREFIX_SENDER_AFTER = "after:";
		var PREFIX_SENDER_BEFORE = "before:";
		var value = $('[name="mainSearch"]').val();
		value = parseStr(value);//.replace(new RegExp("(:\\([^\\s]*)\\s*([^\\)]*\\))","g"), "$1@_@$2");
		var vals = value.split(' ');
		if(vals.length>0){
			$(vals).each(function(index){
				if(index==0){
					$('[name="containKey"]').val('');
					$('[name="file"]').val('');
					$('[name="agency"]').val('');
					$('[name="subject"]').val('');
					$('[name="fromDate"]').val('');
					$('[name="toDate"]').val('');
				}
				
				var obj = this;
				
				//console.log(obj);
				if(obj.StartWith(PREFIX_SENDER_FILE)){
					var o = obj.substring(PREFIX_SENDER_FILE.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					$('[name="file"]').val(o);
				}else if(obj.StartWith(PREFIX_SENDER_AGENCY)){
					var o = obj.substring(PREFIX_SENDER_AGENCY.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					$('[name="agency"]').val(o);
				}else if(obj.StartWith(PREFIX_SENDER_SUBJECT)){
					var o = obj.substring(PREFIX_SENDER_SUBJECT.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					$('[name="subject"]').val(o);
				}else if(obj.StartWith(PREFIX_SENDER_AFTER)){
					var o = obj.substring(PREFIX_SENDER_AFTER.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					$('[name="fromDate"]').val(o);
				}else if(obj.StartWith(PREFIX_SENDER_BEFORE)){
					var o = obj.substring(PREFIX_SENDER_BEFORE.length);
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					$('[name="toDate"]').val(o);
				}else{
					var o = obj;
					if(o.StartWith("(") && o.EndWith(")")){
						o = o.substring(1, o.length-1);
					}
					o = o.replace(new RegExp("@_@","g"), " ");
					
					$('[name="containKey"]').val(($('[name="containKey"]').val().length>0?$('[name="containKey"]').val() + " ": "") + o);
				}
				
			});
		}else{
			$('[name="containKey"]').val('');
			$('[name="file"]').val('');
			$('[name="agency"]').val('');
			$('[name="subject"]').val('');
			$('[name="fromDate"]').val('');
			$('[name="toDate"]').val('');
		}
	}
	
	function openUrl(url) {
		window.location.href = url;
	}
	
	function changeSort_(sort, dir) {
		document.listing.crud_action_type.value = 'sortRecords';
		document.listing.sortBy.value = sort;
		document.listing.sortDir.value=dir;
		$(document.listing).submit();
	}
	
	function changePerPage_(pageSize) {
		document.listing.pageSize.value = pageSize;
		document.listing.pageNumber.value = 1;
		document.listing.crud_action_type.value = 'changePage';
		$(document.listing).submit();
	}
	
	function changePage_(pageNo) {
		document.listing.pageNumber.value = pageNo;
		document.listing.crud_action_type.value = 'changePage';
		$(document.listing).submit();
	}

    function changeJumpPage_() {
        var pageNo=parseInt(document.listing.jumpPageNumber.value);
        var totalPage=parseInt(document.listing.totalPage.value);
        if(pageNo>0&&pageNo<=totalPage) {
            document.listing.pageNumber.value = pageNo;
            document.listing.crud_action_type.value = 'changePage';
            $(document.listing).submit();
        }
    }

</script>

<style type="text/css">
	.entityForm{
	}
	
	input[type="text"].m{
		line-height: 30px\9;
	}
     #nav>:first-child>a{
         color: #f8ca2c;
     }

    .ui-dialog .ui-dialog-content {
        position: relative;
        border: 0;
        padding: .5em 1em;
        background: none;
        overflow: hidden;
        zoom: 1;
    }

    .ui-dialog .ui-dialog-content {
        background-color: #fff;
    }
</style>

<%
	SearchParam param = (SearchParam)request.getSession().getAttribute(DocumentVaultDelegator.SEARCH_PARAM_KEY);
	if(param != null){
		Map<String, Serializable> filters = param.getFilters();
		pageContext.setAttribute("filters",filters);
	}
%>
<webui:setAttribute name="title">
	<c:out value="${title }"/>
</webui:setAttribute>

<br/>

<div>
    <ul class="nav nav-tabs" id="myTab">
      <li class="">
          <a data-toggle="tab" href="#messages" onclick="SOP.Crud.cfxSubmit('', 'myMsg')" title="<egov-smc:commonLabel>My Messages</egov-smc:commonLabel>" name="My Messages">
              <i class="fa fa-envelope-o"></i>
              <span><egov-smc:commonLabel>My Messages</egov-smc:commonLabel></span>
          </a>
      </li>
      <li class="">
          <a data-toggle="tab" href="#applications" onClick="SOP.Crud.cfxSubmit('', 'myApp')" title="<egov-smc:commonLabel>My Applications</egov-smc:commonLabel>" name="My Applications">
            <i class="fa fa-pencil-square-o"></i>
            <span><egov-smc:commonLabel>My Applications</egov-smc:commonLabel></span>
          </a>
      </li>
      <li class="active">
          <a data-toggle="tab" href="#documents" title="<egov-smc:commonLabel>My Documents</egov-smc:commonLabel>" name="My Documents">
              <i class="fa fa-file-text-o"></i>
              <span><egov-smc:commonLabel>My Documents</egov-smc:commonLabel></span>
          </a>
      </li>
    </ul>
    <!-- end Nav Tabs -->
    <form name="listing" class="entityForm" method="post" action=<%=process.runtime.continueURL()%>>
    	<div class="tab-content" id="myTabContent">
    	
			<div id="documents" class="tab-pane fade active in">
				<div class="search-wrap">
           			<div class="input-group">
           				<input type="hidden" name="searchPanelStatus" value="">
						<input type="hidden" name="hiddenMainSearch" class="m" value="<c:out value='${hiddenMainSearch}'/>">
              			<input type="text" name="mainSearch" value="<c:out value='${mainSearch}'/>" class="form-control" placeholder='<egov-smc:commonLabel>I am looking for...</egov-smc:commonLabel>'/>
              			<span class="input-group-btn">
                			<button type="button" class="btn btn-default buttonsearch" onclick="submitMainSearch()"><i class="fa fa-search"></i></button>
                			<a href="#" id="slick-toggle" class="btn btn-default hidden-xs"><egov-smc:commonLabel>Advanced Search</egov-smc:commonLabel>&nbsp;<i class="fa fa-chevron-down"></i></a>
              			</span>
            		</div>
            	</div>
			   		
			   		
		   		<div id="slickbox" class="advanced-search-wrap collapse" style="display: none;">
	              <div class="form-horizontal">
	                <div class="form-group">
	                  <label class="col-sm-3 control-label"><egov-smc:commonLabel>File</egov-smc:commonLabel></label>
	                  <div class="col-sm-9">
	                    <input type="text" class="form-control" name="file" value="<c:out value='${filters.fileName}'/>">
	                  </div>
	                </div>
	
	                <div class="form-group">
	                  <label class="col-sm-3 control-label"><egov-smc:commonLabel>Agency</egov-smc:commonLabel></label>
	                  <div class="col-sm-9">
	                    <input type="text" class="form-control" name="agency" value="<c:out value='${filters.agency}'/>">
	                  </div>
	                </div>
	
	                <div class="form-group">
	                  <label class="col-sm-3 control-label"><egov-smc:commonLabel>Subject</egov-smc:commonLabel></label>
	                  <div class="col-sm-9">
	                    <input type="text" class="form-control" name="subject" value="<c:out value='${filters.subject}'/>">
	                  </div>
	                </div>
	                
	                <div class="form-group ">
	                  <label class="col-sm-3 control-label"><egov-smc:commonLabel>Received Date/Time</egov-smc:commonLabel></label>
	                  <div class="col-sm-9">
	                    <div class="input-group date">
	                    	<label class="input-group-addon"><egov-smc:commonLabel>From</egov-smc:commonLabel></label> <input type="text" class="form-control" name="fromDate" value="<c:out value="${filters.fromDate}"></c:out>"><span class="input-group-addon"><i class="glyphicon glyphicon-th"></i></span>
	                    </div>
	                    <div class="input-group date">
	                    	<label class="input-group-addon"><egov-smc:commonLabel>To</egov-smc:commonLabel></label> <input type="text" name="toDate" class="form-control" value="<c:out value="${filters.toDate}"></c:out>"><span class="input-group-addon"><i class="glyphicon glyphicon-th"></i></span>
	                    </div>
	                  </div>   
	                </div>
	
					<input type="text" style="display:none" name="containKey" value="${containKey }"/>
	                
	                <div class="form-group">
	                  <div class="col-sm-offset-3 col-sm-9">
	                    <button type="button" class="btn btn-round-lg btn-blue2 btn-st" onclick="submitSearch()"><i class="fa fa-search"></i><egov-smc:commonLabel>Search</egov-smc:commonLabel></button>
	                    <button type="button" class="btn btn-round-lg btn-red btn-st" onclick="EGOV.Common.clearForm();"><i class="fa fa-refresh"></i> <egov-smc:commonLabel>Reset</egov-smc:commonLabel></button>
	                  </div>
	                </div>
	              </div>
	            </div>
	            
	            <input type="hidden" name="crud_action_type" value="" >
				<input type="hidden" name="crud_action_value" value="" >
				<input type="hidden" name="crud_action_additional" value="" >
				
				<input type="hidden" name="sortBy" value="" >
				
				<input type="hidden" name="sortDir" value="" >
				<div class="panel panel-darkBlue">
					<input type="hidden" name="pageSize" value="${per_page }" >
					<input type="hidden" name="pageNumber" value="${page }" >
                    <input type="hidden" name="totalPage" value="${totalPage }" >

					<div class="panel-heading">
						<p class="mesg-count"> <egov-smc:message key="page.displaying">Displaying</egov-smc:message>: <c:out value="${count==0?count:per_page*(page-1)+1}"></c:out> -
							<c:out value="${page<totalPage?per_page*page:count}"></c:out>
                            <egov-smc:message key="page.of">of</egov-smc:message> <c:out value="${count}"></c:out> <egov-smc:message key="page.records">Records</egov-smc:message></p>
						<div class="mesg-btn">
                            <div class="pagejump pull-right hidden-xs">
                                <span class="displaypage">Display Per Page:</span>
                                <div class="input-group-display pull-right">
                                    <select id="control--runtime--29--select" onchange="changePerPage_(this.value)" class="dropdownpage form-control control-input control-set-font control-font-normal">
                                        <option value="5" <c:if test="${5==per_page}">selected</c:if> >
                                            5
                                        </option>
                                        <option value="10" <c:if test="${10==per_page}">selected</c:if>>
                                            10
                                        </option>
                                        <option value="20" <c:if test="${20==per_page}">selected</c:if>>
                                            20
                                        </option>
                                        <option value="50" <c:if test="${50==per_page}">selected</c:if>>
                                            50
                                        </option>
                                        <option value="100" <c:if test="${100==per_page}">selected</c:if>>
                                            100
                                        </option>
                                        <option value="150" <c:if test="${150==per_page}">selected</c:if>>
                                            150
                                        </option>
                                    </select>
                                </div>
                            </div>
                            &nbsp;&nbsp; &nbsp;&nbsp;
						</div>
					</div>
		    	
			    	<div class="panel-body">
			    		<div class="table-responsive-wrap">
					    	<table width="100%" class="noti_datatable table-striped table-bordered table-hover" name="dataTable">
					    	<script type="text/javascript">
					    		sopCrud1= new SOP.Crud.SOPCrud('entity', "");
					    		sopCrud1.entityUidsVar = 'entityUids';
					    		sopCrud1.allEntityUidsVar = 'allEntityUids1';
					    		sopCrud1.allEntitiesCbVar = 'allEntitiesCb1';
					    	</script>
					    		<thead>
					    			<tr>
						    			<th>
						    				<c:choose>
												<c:when test="${sort.equals('-fileName')}">
                                                    <egov-smc:commonLabel>File</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('fileName', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png"></a>
												</c:when>
												<c:otherwise>
                                                    <egov-smc:commonLabel>File</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('fileName', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png"></a>
												</c:otherwise>
											</c:choose>
						    			</th>
						    			
						    			<th class="hidden-xs">
						    				<c:choose>
												<c:when test="${sort.equals('-agency')}">
                                                    <egov-smc:commonLabel>Agency</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('agency', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png"></a>
												</c:when>
												<c:otherwise>
                                                    <egov-smc:commonLabel>Agency</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('agency', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png"></a>
												</c:otherwise>
											</c:choose>
						    			</th>
						    			<th class="hidden-xs">
						    				<c:choose>
												<c:when test="${sort.equals('-subject')}">
                                                    <egov-smc:commonLabel>Subject</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('subject', 'ascending')" ><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png"></a>
												</c:when>
												<c:otherwise>
                                                    <egov-smc:commonLabel>Subject</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('subject', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png"></a>
												</c:otherwise>
											</c:choose>
						    			</th>
						    			<th>
						    				<c:choose>
												<c:when test="${sort.equals('-fileSize')}">
                                                    <egov-smc:commonLabel>Size</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('fileSize', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png"></a>
												</c:when>
												<c:otherwise>
                                                    <egov-smc:commonLabel>Size</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('fileSize', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png"></a>
												</c:otherwise>
											</c:choose>
						    			</th>
						    			<th class="hidden-xs">
						    				<c:choose>
												<c:when test="${sort.equals('-fileDate')}">
                                                    <egov-smc:commonLabel>Date</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('fileDate', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png"></a>
												</c:when>
												<c:otherwise>
                                                    <egov-smc:commonLabel>Date</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('fileDate', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png"></a>
												</c:otherwise>
											</c:choose>
						    			</th>
						    			<th><egov-smc:commonLabel>Download</egov-smc:commonLabel>
                                            <div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/tblock.jpg">
                                        </th>
					    			</tr>
					    		</thead>
					    		<tbody>
					    		
					    			<c:forEach var="document" items="${searchResult.rows}">
										<tr>
											<% VaultFile vaultFile = (VaultFile) pageContext.getAttribute("document"); %>
											<td><span class='view-file' docId='${document.fileId }' docTitle='${document.subject }'><c:out value="${document.fileName}"></c:out></span></td>
											<td class="hidden-xs"><span class='view-file' docId='${document.fileId }' docTitle='${document.subject }'><c:out value="${document.agency}"></c:out></span></td>
											<td class="hidden-xs"><span class='view-file' docId='${document.fileId }' docTitle='${document.subject }'><c:out value="${document.subject}"></c:out></span></td>
											<td><span class='view-file' docId='${document.fileId }' docTitle='${document.subject }'><c:out value="${document.fileSize} byte"></c:out></span></td>
											<%
												Locale locale= MultiLangUtil.getSiteLocale();
												if (locale==null)
													locale=AppConstants.DEFAULT_LOCAL;
												String dateTime = ConsistencyHelper.formatDateTime(vaultFile.getFileDate());
											%>

											<td class="hidden-xs">
												<div style="text-align:left;"><span class='view-file' docId='${document.fileId }' docTitle='${document.subject }'><%=dateTime%></span></div>
											</td>
                                            <td><button type="button" class="btn btn-green" onclick="openUrl('<%=request.getContextPath()%>/documentVault/<c:out value="${document.fileId }"/>?download=true')"><i class="fa fa-download"></i><span class="hidden-xs">&nbsp;&nbsp;<egov-smc:commonLabel>Download</egov-smc:commonLabel></span></button></td>
										</tr>
									</c:forEach>
									<c:if test="${searchResult.rowCount==0}">
										<tr><td align="center" colspan="6"><blod><center><b><egov-smc:commonLabel>No records found</egov-smc:commonLabel></b></center></blod></td></tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
					<div class="blank">
					</div>
					<div class="panel-footer">
						<div class="form-horizontal control-set-alignment formgap pull-left hidden-xs">
						</div>
						<nav>
                            <div class="row" id="paginationrow">
                                <div class="col-md-12 text-right" id="pagination">
							<ul class="pagination">
											
								<c:choose>
									<c:when test="${page==1 }">
										<li class="disabled">
											<a class="active previous" href="javascript:void(0);">
												<span aria-hidden="true">&lt;</span>
												<span class="sr-only">Previous</span>
											</a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
											<a class="previous" href="javascript:changePage_(${page-1 });">
												<span aria-hidden="true">&lt;</span>
												<span class="sr-only">Previous</span>
											</a>
										</li>
									</c:otherwise>
								</c:choose>
								<% 
									Integer totalPage =(Integer) request.getAttribute("totalPage");
									Integer currentPage = (Integer)request.getAttribute("page");
                                    if(totalPage<6){
                                        if(currentPage<3){
                                            for(int i = 1; i <= totalPage; i++ ){
                                                request.setAttribute("pages", i);
                                                if(currentPage==i){
                                                    %>
                                                    <li class="active">
                                                        <a class="active" href="javascript:void(0);"><c:out value="${page}"></c:out>
                                                            <span class="sr-only">(current)</span>
                                                        </a>
                                                    </li>
                                                    <%
                                                }else{
                                                    if(i>3){
                                                        %>
                                                        <li class="hidden-xs">
                                                            <a  class="hidden-xs" href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }else{
                                                        %>
                                                        <li>
                                                            <a href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }
                                                }
                                            }
                                        }else if(currentPage==3 || currentPage==4){
                                            for(int i=1; i<=totalPage; i++){
                                                request.setAttribute("pages", i);
                                                if(i<currentPage-1 || i>currentPage+1){
                                                    %>
                                                    <li class="hidden-xs">
                                                        <a class="hidden-xs" href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                    </li>
                                                    <%
                                                }else{
                                                    if(currentPage==i){
                                                        %>
                                                        <li class="active">
                                                            <a class="active" href="javascript:void(0);"><c:out value="${page}"></c:out>
                                                                <span class="sr-only">(current)</span>
                                                            </a>
                                                        </li>
                                                        <%
                                                    }else{
                                                        %>
                                                        <li>
                                                            <a href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }
                                                }
                                            }
                                        }else{
                                            for(int i = 1; i <= totalPage; i++ ){
                                                request.setAttribute("pages", i);
                                                if(i<3){
                                                    %>
                                                    <li class="hidden-xs">
                                                        <a class="hidden-xs" href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                    </li>
                                                    <%
                                                }else{
                                                    if(currentPage==i){
                                                        %>
                                                        <li class="active">
                                                            <a class="active" href="javascript:void(0);"><c:out value="${page}"></c:out>
                                                                <span class="sr-only">(current)</span>
                                                            </a>
                                                        </li>
                                                        <%
                                                    }else{
                                                        %>
                                                        <li>
                                                            <a href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if(totalPage>=6 && currentPage<4){
                                        if(currentPage<3){
                                            for(int i = 1; i <= 5; i++ ){
                                                request.setAttribute("pages", i);
                                                if(currentPage==i){
                                                    %>
                                                    <li class="active">
                                                        <a class="active" href="javascript:void(0);"><c:out value="${page}"></c:out>
                                                            <span class="sr-only">(current)</span>
                                                        </a>
                                                    </li>
                                                    <%
                                                }else{
                                                    if(i>3){
                                                        %>
                                                        <li class="hidden-xs">
                                                            <a  class="hidden-xs" href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }else{
                                                        %>
                                                        <li>
                                                            <a href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }
                                                }
                                            }
                                        }else if(currentPage==3){
                                            for(int i=1; i<=5; i++){
                                                request.setAttribute("pages", i);
                                                if(i<currentPage-1 || i>currentPage+1){
                                                    %>
                                                    <li class="hidden-xs">
                                                        <a class="hidden-xs" href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                    </li>
                                                    <%
                                                }else{
                                                    if(currentPage==i){
                                                        %>
                                                        <li class="active">
                                                            <a class="active" href="javascript:void(0);"><c:out value="${page}"></c:out>
                                                                <span class="sr-only">(current)</span>
                                                            </a>
                                                        </li>
                                                        <%
                                                    }else{
                                                        %>
                                                        <li>
                                                            <a href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }
                                                }
                                            }
                                        }
                                    }


                                    if(totalPage>=6 && currentPage>=4 && currentPage+2 <= totalPage){
                                        for(int i=currentPage-2; i <= currentPage+2; i++ ){
                                            request.setAttribute("pages", i);
                                            if(i<currentPage-1 || i>currentPage+1){
                                                %>
                                                <li class="hidden-xs">
                                                    <a class="hidden-xs" href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                </li>
                                                <%
                                            }else{
                                                if(currentPage==i){
                                                    %>
                                                    <li class="active">
                                                        <a class="active" href="javascript:void(0);"><c:out value="${page}"></c:out>
                                                            <span class="sr-only">(current)</span>
                                                        </a>
                                                    </li>
                                                    <%
                                                }else{
                                                    %>
                                                    <li>
                                                        <a href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                    </li>
                                                    <%
                                                }
                                            }
                                        }
                                    }
                                    if(totalPage>=6 && currentPage>=4 && currentPage+2 > totalPage){
                                        if(currentPage<totalPage){
                                            for(int i=totalPage-4; i <= totalPage; i++ ) {
                                                request.setAttribute("pages", i);
                                                if(currentPage==i){
                                                    %>
                                                    <li class="active">
                                                        <a class="active" href="javascript:void(0);"><c:out value="${page}"></c:out>
                                                            <span class="sr-only">(current)</span>
                                                        </a>
                                                    </li>
                                                    <%
                                                }else{
                                                    if(i<currentPage-1 || i>currentPage+1){
                                                        %>
                                                        <li class="hidden-xs">
                                                            <a class="hidden-xs" href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }else{
                                                        %>
                                                        <li>
                                                            <a href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }
                                                }
                                            }
                                        }else{
                                            for(int i=totalPage-4; i <= totalPage; i++ ){
                                                request.setAttribute("pages", i);
                                                if(currentPage==i){
                                                    %>
                                                    <li class="active">
                                                        <a class="active" href="javascript:void(0);"><c:out value="${page}"></c:out>
                                                            <span class="sr-only">(current)</span>
                                                        </a>
                                                    </li>
                                                    <%
                                                }else{
                                                    if(i<currentPage-2){
                                                        %>
                                                        <li class="hidden-xs">
                                                            <a class="hidden-xs" href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }else{
                                                        %>
                                                        <li>
                                                            <a href="javascript:changePage_(${pages})" ><c:out value="${pages}"></c:out></a>
                                                        </li>
                                                        <%
                                                    }
                                                }
                                            }
                                        }
									}
								%>	
								<c:choose>
									<c:when test="${totalPage==page }">
										<li class="disabled">
											<a class="active next" href="javascript:void(0);">
												<span aria-hidden="true">&gt;</span>
												<span class="sr-only">next</span>
											</a>
										</li>
									</c:when>
									<c:otherwise>
										<li>
											<a class="next" href="javascript:changePage_(${page+1 })" >
												<span aria-hidden="true">&gt;</span>
												<span class="sr-only">Next</span>
											</a>
										</li>
									</c:otherwise>
								</c:choose>
                                <div class="pagejump hidden-xs">
                                    <span class="gotopage" style="display:inline-block;">Go to page</span><span class="input-group-jump jump-page-icon pull-right" title="Go to page"><a href="javascript:changeJumpPage_()" >Go</a></span>
                                    <input type="text" class="jump-page-input pull-right" name="jumpPageNumber">
                                </div>
							</ul>
                                </div>
                            </div>
						</nav>
						<div class="clearfix">
						</div>
					</div>
    			</div>
    		</div>
     	</div>
     	<br class='clear'/>
    </form>
</div>






<script type="text/javascript">
jQuery(document).ready(function() {

    jQuery("body").append("<div id='dialog' title='Form Title' style='display:none'></div>");

    jQuery(".view-file").parent().click(function() {
		var docId = $(".view-file",this).attr('docId');
		var docTitle = $(".view-file",this).attr('docTitle');

		//console.log($(window).height());

		var dialogHeight = $(window).height() * .75;
		var contentHeight = dialogHeight-20;

		var dialogWidth = $(window).width() * .75;
		var contentWidth = dialogWidth-20;
	
        $( "#dialog" ).dialog({
        	width: dialogWidth,
        	height: dialogHeight,
			modal: true,
        	resizable: false,
        	dialogClass: "form-viewer",
        	title: docTitle
        });

        $("#dialog").html("<object height='" + contentHeight + "' width='" + contentWidth + "' type='application/pdf' data='<%=request.getContextPath()%>/documentVault/" + docId + "'><param name='src' value='<%=request.getContextPath()%>/documentVault/" + docId + "' /><p>PDF 	cannot be displayed</p></object>");

	    $(window).resize(function() {
	    	resizeBody();
	   	});

		function resizeBody(){
			if($("#dialog").is(":visible")) {
				var windowHeight = $(window).height();
				var windowWidth = $(window).width();

				$("#dialog").height(windowHeight*.75);
				$("#dialog object").attr("height", windowHeight*.75);


				$("#dialog").parent().width(windowWidth*.75);
				$("#dialog object").attr("width", windowWidth*.75);

			    $("#dialog").dialog("option", "position", "center");

			}

    	}

    });

    
    
});
</script>