<%@page import="com.ecquaria.cloud.helper.ConfigHelper"%>
<%@page import="com.ecquaria.cloud.mc.api.ConsistencyHelper"%>
<%@page import="com.ecquaria.cloud.mc.application.Application"%>
<%@page import="com.ecquaria.cloud.mc.application.delegator.ApplicationDelegator"%>
<%@page import="com.ecquaria.cloud.mc.base.SearchParam"%>
<%@page import="com.ecquaria.cloud.mc.common.constants.AppConstants"%>
<%@page import="org.springframework.web.servlet.i18n.SessionLocaleResolver"%>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.Map" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%--<%@ taglib uri="ecquaria/sop/egov-mc" prefix="egov"%>--%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="ecquaria/sop/sop-smc" prefix="sop-smc"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%@ page import="java.io.Serializable" %>
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
	
	$(function(){
		EGOV.Common.initSearchAdvance('slick-toggle', 'slickbox');
		bindTableView("dataTable", "uniqueKey");
		bindTableViewHandler();
		
		initSearchPanel();
		
		SOP.Common.setupTooltip($('.status-info'),{
			position: "center right",
			offset:[-80, -90],
			effect: "fade"
		});
	});
	
	
	
	function submitMainSearch(){
		$("input[name='searchPanelStatus']").val("false");
		SOP.Crud.cfxSubmit('', 'mainSearch');
	}
	
	function submitSearch(){
		$("input[name='searchPanelStatus']").val("true");
		SOP.Crud.cfxSubmit('', 'search');
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
</style>

<%
	SearchParam param = (SearchParam)request.getSession().getAttribute(ApplicationDelegator.KEY_SEARCH_FORM);
	if(param != null){
		Map<String,Serializable> filters = param.getFilters();
		pageContext.setAttribute("filters",filters);
	}
%>
<webui:setAttribute name="title">
	<c:out value="${title }"/>
</webui:setAttribute>

<script type="text/javascript">

	function changeSort_(sort, dir) {
		document.listing.crud_action_type.value = 'sortRecords';
		document.listing.sortBy.value = sort;
		document.listing.sortDir.value=dir;
		$(document.listing).submit();
	}
	
	</script>
	
	
	<script type="text/javascript">
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
	
	<script type="text/javascript">
		sopCrud1= new SOP.Crud.SOPCrud('entity', "");
		sopCrud1.entityUidsVar = 'entityUids';
		sopCrud1.allEntityUidsVar = 'allEntityUids1';
		sopCrud1.allEntitiesCbVar = 'allEntitiesCb1';
	</script>

<br/>

<div >
	<!-- begin Nav Tabs -->
    <ul class="nav nav-tabs" id="myTab">
	      <li class="">
	          <a data-toggle="tab" href="#messages" onclick="SOP.Crud.cfxSubmit('', 'myMsg')" title="<egov-smc:commonLabel>My Messages</egov-smc:commonLabel>" name="My Messages">
	              <i class="fa fa-envelope-o"></i>
	              <span><egov-smc:commonLabel>My Messages</egov-smc:commonLabel></span>
	          </a>
	      </li>
	      <li class="active">
	          <a data-toggle="tab" href="#applications" title="<egov-smc:commonLabel>My Applications</egov-smc:commonLabel>" name="My Applications">
	            <i class="fa fa-pencil-square-o"></i>
	            <span><egov-smc:commonLabel>My Applications</egov-smc:commonLabel></span>
	          </a>
	      </li>
	      <li class="">
	          <a data-toggle="tab" href="#documents" onClick="SOP.Crud.cfxSubmit('', 'myDoc')" title="<egov-smc:commonLabel>My Documents</egov-smc:commonLabel>" name="My Documents">
	              <i class="fa fa-file-text-o"></i>
	              <span><egov-smc:commonLabel>My Documents</egov-smc:commonLabel></span>
	          </a>
	      </li>
	  
    </ul>
    <!-- end Nav Tabs -->
    
    
	<form name="listing" class="entityForm" method="post" action=<%=process.runtime.continueURL()%>>
		<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
	    <div class="tab-content" id="myTabContent">
     
    		<!-- Applications -->
     		<div id="applications" class="tab-pane fade active in">
     			<!-- SearchBox -->
		        <div class="search-wrap">
		            <div class="input-group">
		          		<input type="hidden" name="searchPanelStatus" value="">
						<input type="hidden" name="hiddenMainSearch" class="m" value="<c:out value='${hiddenMainSearch}'/>">
		              	<input type="text" class="form-control" placeholder="<egov-smc:commonLabel>I am looking for...</egov-smc:commonLabel>" name="mainSearch" value="<c:out value='${mainSearch}'/>">
		             	<span class="input-group-btn">
			                <button type="button" class="btn btn-default buttonsearch" onclick="submitMainSearch()"><i class="fa fa-search"></i></button>
			                <a href="#" id="slick-toggle" class="btn btn-default hidden-xs"><egov-smc:commonLabel>Advanced Search</egov-smc:commonLabel>&nbsp;<i class="fa fa-chevron-down"></i></a>
		              	</span>
		            </div>
		        </div>
		        
		        <div id="slickbox" class="advanced-search-wrap collapse" style="display: none;">
	              <div class="form-horizontal">
	                <div class="form-group">
	                  <label class="col-sm-3 control-label"><egov-smc:commonLabel>Application No.</egov-smc:commonLabel></label>
	                  <div class="col-sm-9">
	                    <input type="text" class="form-control" name="no" value="<c:out value='${filters.no}'/>">
	                  </div>
	                </div>
	
	                <div class="form-group">
	                  <label class="col-sm-3 control-label"><egov-smc:commonLabel>Service</egov-smc:commonLabel></label>
	                  <div class="col-sm-9">
	                    <input type="text" class="form-control" name="serviceName" value="<c:out value='${filters.serviceName}'/>">
	                  </div>
	                </div>
	
	                <div class="form-group">
	                  <label class="col-sm-3 control-label"><egov-smc:commonLabel>Agency</egov-smc:commonLabel></label>
	                  <div class="col-sm-9">
	                    <input type="text" class="form-control" name="agencyName" value="<c:out value='${filters.agencyName}'/>">
	                  </div>
	                </div>
	                
	                <div class="form-group">
	                  <label class="col-sm-3 control-label"><egov-smc:commonLabel>Status</egov-smc:commonLabel></label>
	                  <div class="col-sm-9">
	                    <input type="text" class="form-control" name="status" value="<c:out value='${filters.status}'/>">
	                  </div>
	                </div>
	
	                <div class="form-group ">
	                  <label class="col-sm-3 control-label"><egov-smc:commonLabel>Submitted Date/Time</egov-smc:commonLabel></label>
	                  <div class="col-sm-9">
	                    <div class="input-group date">
	                    	<label class="input-group-addon"><egov-smc:commonLabel>From</egov-smc:commonLabel></label> <input type="text" class="form-control" name="From_submittedDate" value="<c:out value="${filters.From_submittedDate}"></c:out>"><span class="input-group-addon"><i class="glyphicon glyphicon-th"></i></span>
	                    </div>
	                    <div class="input-group date">
	                    	<label class="input-group-addon"><egov-smc:commonLabel>To</egov-smc:commonLabel></label> <input type="text" name="To_submittedDate" class="form-control" value="<c:out value="${filters.To_submittedDate}"></c:out>"><span class="input-group-addon"><i class="glyphicon glyphicon-th"></i></span>
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
								
								<thead>
									<tr>
										<th>
											<c:choose>
												<c:when test="${sort.equals('-no')}">
													<egov-smc:commonLabel>Application No.</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('no', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png"></a>
												</c:when>
												<c:otherwise>
													<egov-smc:commonLabel>Application No.</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('no', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png"></a>
												</c:otherwise>
											</c:choose>
										</th>
										<th>
											<c:choose>
												<c:when test="${sort.equals('-serviceName')}">
													<egov-smc:commonLabel>Service</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('serviceName', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png"></a>
												</c:when>
												<c:otherwise>
													<egov-smc:commonLabel>Service</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('serviceName', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png"></a>
												</c:otherwise>
											</c:choose>
											
										</th>
										<th class="hidden-xs">
											<c:choose>
												<c:when test="${sort.equals('-agencyName')}">
													<egov-smc:commonLabel>Agency</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('agencyName', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png"></a>
												</c:when>
												<c:otherwise>
													<egov-smc:commonLabel>Agency</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('agencyName', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png"></a>
												</c:otherwise>
											</c:choose>
											
										</th>
										<th>
											<c:choose>
												<c:when test="${sort.equals('-status')}">
													<egov-smc:commonLabel>Status</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('status', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png"></a>
												</c:when>
												<c:otherwise>
													<egov-smc:commonLabel>Status</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('status', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png"></a>
												</c:otherwise>
											</c:choose>
											
										</th>
										<th class="hidden-xs">
											<c:choose>
												<c:when test="${sort.equals('-submittedDate')}">
													<egov-smc:commonLabel>Submitted Date/Time</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('submittedDate', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png"></a>
												</c:when>
												<c:otherwise>
													<egov-smc:commonLabel>Submitted Date/Time</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('submittedDate', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png"></a>
												</c:otherwise>
											</c:choose>
											
										</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="application" items="${application_search_result.rows}">
										<!--<tr onclick="javascript:viewSubmit(${application.id})">-->
										<tr title="${application.statusMessage}">
											<% Application app = (Application) pageContext.getAttribute("application"); %>
											<td class="break-xxs"><c:out value="${application.no}"></c:out><input name="uniqueKey" type="hidden" value="${application.id }"></input></td>
											<td><c:out value="${application.serviceName}"></c:out></td>
											<td class="hidden-xs"><c:out value="${application.agencyName}"></c:out></td>
											<td><span class="status-info"><c:out value="${application.status}"></c:out></span>
												<div class="tooltip-empty">
												</div>
											</td>
											<td class="hidden-xs">
												<%
													Locale locale= MultiLangUtil.getSiteLocale();
													if (locale==null)
														locale=AppConstants.DEFAULT_LOCAL;
												    String receivedDate = ConsistencyHelper.formatDateTime(app.getSubmittedDate());
												%>
												
												<div style="text-align:left;"><%=receivedDate%></div>
											</td>
										</tr>
									</c:forEach>
									<c:if test="${application_search_result.rowCount==0}">
										<tr><td align="center" colspan="5"><blod><center><b><egov-smc:commonLabel>No records found</egov-smc:commonLabel></b></center></blod></td></tr>
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
									int totalPage =(int) request.getAttribute("totalPage");
									int currentPage = (int)request.getAttribute("page");
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


