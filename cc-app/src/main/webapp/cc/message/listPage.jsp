<%@page import="com.ecquaria.cloud.mc.api.ConsistencyHelper" %>
<%@page import="com.ecquaria.cloud.mc.api.MessageConstants" %>
<%@page import="com.ecquaria.cloud.mc.api.MessageHelper" %>
<%@page import="com.ecquaria.cloud.mc.base.SearchParam" %>
<%@page import="com.ecquaria.cloud.mc.common.constants.AppConstants" %>
<%@page import="com.ecquaria.cloud.mc.message.Message" %>
<%@page import="com.ecquaria.cloud.mc.message.delegator.MyMessageDelegator" %>
<%@page import="ecq.commons.helper.StringHelper" %>
<%@page import="sop.rbac.user.User" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Map" %>
<%@ page import="com.ecquaria.cloud.message.source.TransUtil" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.springframework.web.servlet.i18n.SessionLocaleResolver" %>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="ecquaria/sop/sop-smc" prefix="sop-smc" %>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core" %>
<%@taglib uri="ecquaria/sop/sop-htmlform" prefix="sop-htmlform" %>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%@ page import="java.io.Serializable" %>
<webui:setLayout name="cc"/>
<%
    response.setContentType("text/html;charset=UTF-8");
    String searchPanelStatus = (String) request.getAttribute("searchPanelStatus");
    searchPanelStatus = StringHelper.escapeHtmlChars(searchPanelStatus);

    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    Locale locale= MultiLangUtil.getSiteLocale();
    if (locale==null)
        locale=AppConstants.DEFAULT_LOCAL;
    %>
<script type="text/javascript">
    $('#type').val("L");
    $('#importance').val("H");

    var LIST_TABLE_NAME = null;
    var LIST_TABLE_DATA_UNIQUE = null;
    var DEFAULT_VIEW_ACTION = "view";

    function bindTableView(tableName, unique, action) {
        LIST_TABLE_NAME = tableName;
        LIST_TABLE_DATA_UNIQUE = unique;
        if (action) {
            DEFAULT_VIEW_ACTION = action;
        }
    }

    function initSearchPanel() {
        if ('<%=searchPanelStatus%>' == 'true') {
            $("#slickbox").show();
            $("input[name='searchPanelStatus']").val("true");
        } else {
            $("#slickbox").hide();
            $("input[name='searchPanelStatus']").val("false");
        }
    }

    function bindTableViewHandler() {
        $("table[name='" + LIST_TABLE_NAME + "'] tbody tr").each(function () {
            var unique = $(this).find("input[name='" + LIST_TABLE_DATA_UNIQUE + "']");
            console.log(unique.val());
            if (unique && unique.length > 0) {
                $(this).click(function () {
                    SOP.Crud.cfxSubmit('', DEFAULT_VIEW_ACTION, unique.val());
                });
            }
        });
    }

    function onDelete(val) {
        if (MGOV.Common.checkSelected('auditUids') || val)
            SOP.Common.confirm({
                message: "<egov-smc:message key='deleterecordqns'>Are you sure you want to delete the selected record(s)</egov-smc:message>", func: function () {
                    SOP.Crud.cfxSubmit('', 'delete', val);
                }
            })
        else
            SOP.Common.alert('<egov-smc:message key="noauditselected">No Audit selected.</egov-smc:message>');
    }
    $(function () {
        EGOV.Common.initSearchAdvance('slick-toggle', 'slickbox');
        $('[name="mainSearch"]').change(initSearchAdvance);
        initMainSearch();
        bindTableView("dataTable", "uniqueKey");
        bindTableViewHandler();
        initSearchPanel();
    });

    function submitMainSearch() {
        initSearchAdvance();
        $("input[name='searchPanelStatus']").val("false");
        SOP.Crud.cfxSubmit('', 'mainSearch');
    }

    function submitSearch() {
        $("input[name='searchPanelStatus']").val("true");
        SOP.Crud.cfxSubmit('', 'search');
    }

    function parseStr(cs) {
        var brace = 0;
        var flag = false;
        var cs1 = '';
        for (var i = 0; i < cs.length; i++) {
            switch (cs.charAt(i)) {
                case '(':
                    brace++;
                    if (brace <= 1) {
                        if (i > 0 && cs.charAt(i - 1) == ':') {
                            cs1 += cs.charAt(i);
                        } else
                            brace--;
                    } else
                        brace--;

                    break;
                case ')':
                    brace--;
                    if (brace == 0) {
                        if (i < cs.length - 1 && cs.charAt(i + 1) == ' ') {
                            cs1 += cs.charAt(i);
                        } else if (i == cs.length - 1)
                            cs1 += cs.charAt(i);
                        else
                            brace++;
                    } else
                        brace++;

                    //else if(brace>0)

                    break;
                case ' '://kongge
                    if (!flag && brace > 0) {
                        //cs.charAt(i) = '_';
                        cs1 += '@';
                        cs1 += '_';
                        cs1 += '@';
                    } else
                        cs1 += cs.charAt(i);
                    break;
                default:
                    cs1 += cs.charAt(i);
            }
        }

        return cs1;
    }

    function initSearchAdvance() {
        var PREFIX_SENDER_NAME = "from:";
        var PREFIX_SENDER_AGENCY = "agency:";
        var PREFIX_SENDER_SUBJECT = "subject:";
        var PREFIX_SENDER_AFTER = "after:";
        var PREFIX_SENDER_BEFORE = "before:";
        var PREFIX_SENDER_MODE = "mode:";
        var PREFIX_SENDER_IMPORTANT = "priority:";
        var value = $('[name="mainSearch"]').val();
        value = parseStr(value);//.replace(new RegExp("(:\\([^\\s]*)\\s*([^\\)]*\\))","g"), "$1@_@$2");
        var vals = value.split(' ');
        if (vals.length > 0) {
            $(vals).each(function (index) {
                if (index == 0) {
                    $('[name="containKey"]').val('');
                    $('[name="senderName"]').val('');
                    $('[name="senderAgency"]').val('');
                    $('[name="subject"]').val('');
                    $('[name="fromDate"]').val('');
                    $('[name="toDate"]').val('');
                    $('[name="mode"]').val('');
                    $('[name="important"]').val('');
                }

                var obj = this;
                if (obj.StartWith(PREFIX_SENDER_NAME)) {
                    var o = obj.substring(PREFIX_SENDER_NAME.length);
                    if (o.StartWith("(") && o.EndWith(")")) {
                        o = o.substring(1, o.length - 1);
                    }
                    o = o.replace(new RegExp("@_@", "g"), " ");
                    $('[name="senderName"]').val(o);
                } else if (obj.StartWith(PREFIX_SENDER_AGENCY)) {
                    var o = obj.substring(PREFIX_SENDER_AGENCY.length);
                    if (o.StartWith("(") && o.EndWith(")")) {
                        o = o.substring(1, o.length - 1);
                    }
                    o = o.replace(new RegExp("@_@", "g"), " ");
                    $('[name="senderAgency"]').val(o);
                } else if (obj.StartWith(PREFIX_SENDER_SUBJECT)) {
                    var o = obj.substring(PREFIX_SENDER_SUBJECT.length);
                    if (o.StartWith("(") && o.EndWith(")")) {
                        o = o.substring(1, o.length - 1);
                    }
                    o = o.replace(new RegExp("@_@", "g"), " ");
                    $('[name="subject"]').val(o);
                } else if (obj.StartWith(PREFIX_SENDER_AFTER)) {
                    var o = obj.substring(PREFIX_SENDER_AFTER.length);
                    if (o.StartWith("(") && o.EndWith(")")) {
                        o = o.substring(1, o.length - 1);
                    }
                    o = o.replace(new RegExp("@_@", "g"), " ");
                    $('[name="fromDate"]').val(o);
                } else if (obj.StartWith(PREFIX_SENDER_BEFORE)) {
                    var o = obj.substring(PREFIX_SENDER_BEFORE.length);
                    if (o.StartWith("(") && o.EndWith(")")) {
                        o = o.substring(1, o.length - 1);
                    }
                    o = o.replace(new RegExp("@_@", "g"), " ");
                    $('[name="toDate"]').val(o);
                } else if (obj.StartWith(PREFIX_SENDER_MODE)) {
                    var o = obj.substring(PREFIX_SENDER_MODE.length);
                    if (o.StartWith("(") && o.EndWith(")")) {
                        o = o.substring(1, o.length - 1);
                    }
                    o = o.replace(new RegExp("@_@", "g"), " ");
                    if ($.trim(o))
                        $('[name="mode"] :containsIgnoreCase("' + o + '")').attr("selected", "");
                } else if (obj.StartWith(PREFIX_SENDER_IMPORTANT)) {
                    var o = obj.substring(PREFIX_SENDER_IMPORTANT.length);
                    if (o.StartWith("(") && o.EndWith(")")) {
                        o = o.substring(1, o.length - 1);
                    }
                    o = o.replace(new RegExp("@_@", "g"), " ");
                    if ($.trim(o))
                        $('[name="important"] :containsIgnoreCase("' + o + '")').attr("selected", "");
                } else {
                    var o = obj;
                    if (o.StartWith("(") && o.EndWith(")")) {
                        o = o.substring(1, o.length - 1);
                    }
                    o = o.replace(new RegExp("@_@", "g"), " ");

                    $('[name="containKey"]').val(($('[name="containKey"]').val().length > 0 ? $('[name="containKey"]').val() + " " : "") + o);
                }

            });
        } else {
            $('[name="containKey"]').val('');
            $('[name="senderName"]').val('');
            $('[name="senderAgency"]').val('');
            $('[name="subject"]').val('');
            $('[name="fromDate"]').val('');
            $('[name="toDate"]').val('');
            $('[name="mode"]').val('');
            $('[name="important"]').val('');
        }
    }

    function initMainSearch() {
        var $mode = $("div.form select[name='mode']");
        var $important = $("div.form select[name='important']");
        if ($mode.val()) {
            var modeLabel = $mode.find("[value='" + $mode.val() + "']").text()
            changeMainSesrch("mode:", modeLabel);
        }
        if ($important.val()) {
            var importantLabel = $important.find("[value='" + $important.val() + "']").text()
            changeMainSesrch("priority:", importantLabel);
        }
    }

    function changeMainSesrch(key, value) {
        var mainValue = $("input[name='mainSearch']").val();
        var startIndex = mainValue.indexOf(key);
        if (value.indexOf(" ") != "-1") {
            value = "(" + value + ")";
        }
        if (startIndex == -1) {
            return;
        } else {
            startIndex += key.length;
            var beforevalue = mainValue.substring(0, startIndex);
            var tmpAfterValue = mainValue.substring(startIndex);
            if (tmpAfterValue.charAt(0) == "(") {
                tmpEndIndex = tmpAfterValue.indexOf(")");
                tmpAfterValue = tmpAfterValue.substring(tmpEndIndex);
            }
            var endIndex = tmpAfterValue.indexOf(" ");
            if (endIndex == -1) {
                endIndex = tmpAfterValue.length;
            }
            var afterValue = tmpAfterValue.substring(endIndex);
            $("input[name='mainSearch']").val(beforevalue + value + afterValue);
        }

    }
</script>
<style>
    #nav>:first-child>a{
        color: #f8ca2c;
    }
</style>
<%
    SearchParam param = (SearchParam) request.getSession().getAttribute(MyMessageDelegator.SEARCH_PARAM_KEY);
    if (param != null) {
        Map<String, Serializable> filters = param.getFilters();
        pageContext.setAttribute("filters", filters);
    }
    User user = sop.iwe.SessionManager.getInstance(request).getCurrentUser();
%>
<webui:setAttribute name="title">
    <c:out value="${title }"/>
</webui:setAttribute>


<%
    String isHideWelcomInfo = (String) session.getAttribute("KEY_HIDE_WELCOME_INFO");

    if (!StringHelper.equals(isHideWelcomInfo, "true")) {
        session.setAttribute("KEY_HIDE_WELCOME_INFO", "true");
%>
<div role="alert" class="alert alert-info fade in alert-dismissable">
    <a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">×</a>
    <strong><egov-smc:commonLabel>Welcome Back</egov-smc:commonLabel>!&nbsp;<a><%=user != null ? user.getDisplayName()+".":""%>
</a></strong>
</div>
<%
    }
%>

<br/>

<div>
    <ul class="nav nav-tabs" id="myTab">
        <li class="active">
            <a data-toggle="tab" href="#messages" title="<egov-smc:commonLabel>My Messages</egov-smc:commonLabel>">
                <em class="fa fa-envelope-o"></em>
                <span><egov-smc:commonLabel>My Messages</egov-smc:commonLabel></span>
            </a>
        </li>
        <li class="">
            <a data-toggle="tab" href="#applications" onClick="SOP.Crud.cfxSubmit('', 'myApp')" title="<egov-smc:commonLabel>My Applications</egov-smc:commonLabel>">
                <em class="fa fa-pencil-square-o"></em>
                <span><egov-smc:commonLabel>My Applications</egov-smc:commonLabel></span>
            </a>
        </li>
        <li class="">
            <a data-toggle="tab" href="#documents" onClick="SOP.Crud.cfxSubmit('', 'myDoc')" title="<egov-smc:commonLabel>My Documents</egov-smc:commonLabel>">
                <em class="fa fa-file-text-o"></em>
                <span><egov-smc:commonLabel>My Documents</egov-smc:commonLabel></span>
            </a>
        </li>
    </ul>


    <form action=<%=process.runtime.continueURL()%> class="entityForm" method="post" name="listing">
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="pageId" value="1600447467"/>
        <div class="tab-content" id="myTabContent">
            <div id="messages" class="tab-pane fade active in">
                <div class="search-wrap">
                    <div class="input-group">
                        <input type="hidden" name="searchPanelStatus" value="">
                        <input type="hidden" name="hiddenMainSearch" class="m"
                               value="<c:out value='${hiddenMainSearch}'/>">
                        <input type="text" class="form-control" placeholder='<egov-smc:commonLabel>I am looking for...</egov-smc:commonLabel>' name="mainSearch"
                               value="<c:out value='${displayMainSearch}'/>">
                        <span class="input-group-btn">
			                <button type="button" class="btn btn-default buttonsearch" onclick="submitMainSearch()"><em
                                    class="fa fa-search"></em></button>
			                <a href="#" id="slick-toggle" class="btn btn-default hidden-xs"><egov-smc:commonLabel>Advanced Search</egov-smc:commonLabel>&nbsp;<em
                                    class="fa fa-chevron-down"></em></a>
		              	</span>
                    </div>
                </div>

                <div id="slickbox" class="advanced-search-wrap collapse" style="display: none;">
                    <div class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-3 control-label"><egov-smc:commonLabel>Sender (by Name)</egov-smc:commonLabel></label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="senderName"
                                       value="<c:out value='${filters.sender}'/>">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label"><egov-smc:commonLabel>Sender (by Agency)</egov-smc:commonLabel></label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="senderAgency"
                                       value="<c:out value='${filters.senderAgy}'/>">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label"><egov-smc:commonLabel>Subject</egov-smc:commonLabel></label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="subject"
                                       value="<c:out value='${filters.subject}'/>">
                            </div>
                        </div>

                        <div class="form-group ">
                            <label class="col-sm-3 control-label"><egov-smc:commonLabel>Received Date/Time</egov-smc:commonLabel></label>
                            <div class="col-sm-9">
                                <div class="input-group date">
                                    <label class="input-group-addon"><egov-smc:commonLabel>From</egov-smc:commonLabel></label> <input type="text"
                                                                                                                                      class="form-control"
                                                                                                                                      name="fromDate"
                                                                                                                                      value="<c:out value="${filters.fromDate}"></c:out>"><span
                                        class="input-group-addon"><em class="glyphicon glyphicon-th"></em></span>
                                </div>
                                <div class="input-group date">
                                    <label class="input-group-addon"><egov-smc:commonLabel>To</egov-smc:commonLabel></label> <input type="text" name="toDate"
                                                                                                                                    class="form-control"
                                                                                                                                    value="<c:out value="${filters.toDate}"></c:out>"><span
                                        class="input-group-addon"><em class="glyphicon glyphicon-th"></em></span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label"><egov-smc:commonLabel>Mode</egov-smc:commonLabel></label>
                            <div class="col-sm-9">
                                <select id="type" name="mode" class="form-control">
                                    <option value=""></option>
                                    <option value="S"><egov-smc:message key="mode.sms">SMS</egov-smc:message></option>
                                    <option value="N"><egov-smc:message key="mode.mail">Normal Mail</egov-smc:message></option>
                                    <option value="E"><egov-smc:message key="mode.email">eMail</egov-smc:message></option>
                                    <option value="P"><egov-smc:message key="mode.phone">Phone Call</egov-smc:message></option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="importance" class="col-sm-3 control-label"><egov-smc:commonLabel>Importance</egov-smc:commonLabel></label>
                            <div class="col-sm-9">
                                <select id="importance" name="important" class="form-control">
                                    <option value=""></option>
                                    <option value="L"><egov-smc:commonLabel>Low</egov-smc:commonLabel></option>
                                    <option value="N"><egov-smc:commonLabel>Normal</egov-smc:commonLabel></option>
                                    <option value="H"><egov-smc:commonLabel>High</egov-smc:commonLabel></option>
                                </select>
                            </div>
                        </div>
                        <script type="text/javascript">
                            var type = '${filters.type}';
                            var importance = '${filters.importance}';
                            if (type != null)
                                $('#type').val(type);
                            if (importance != null)
                                $('#importance').val(importance);
                        </script>
                        <input type="text" style="display:none" name="containKey" value="${containKey }"/>

                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-9">
                                <button type="button" class="btn btn-round-lg btn-blue2 btn-st" onclick="submitSearch()"><em
                                        class="fa fa-search"></em><egov-smc:commonLabel>Search</egov-smc:commonLabel></button>
                                <button type="button" class="btn btn-round-lg btn-red btn-st" onclick="EGOV.Common.clearForm();"><em
                                        class="fa fa-refresh"></em> <egov-smc:commonLabel>Reset</egov-smc:commonLabel></button>
                            </div>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="crud_action_type" value="">
                <input type="hidden" name="crud_action_value" value="">
                <input type="hidden" name="crud_action_additional" value="">

                <input type="hidden" name="sortBy" value="">

                <input type="hidden" name="sortDir" value="">

                <script type="text/javascript">
                    function changeSort_(sort, dir) {
                        document.listing.crud_action_type.value = 'sortRecords';
                        document.listing.sortBy.value = sort;
                        document.listing.sortDir.value = dir;
                        $(document.listing).submit();
                    }
                </script>
                <div class="panel panel-darkBlue">
                    <input type="hidden" name="pageSize" value="${per_page }">
                    <input type="hidden" name="pageNumber" value="${page}">
                    <input type="hidden" name="totalPage" value="${totalPage }" >
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
                    <div class="panel-heading">
                        <p class="mesg-count"> <egov-smc:message key="page.displaying">Displaying</egov-smc:message>: <c:out value="${count==0?0:(page-1)*per_page+1 }"/> - <c:out
                                value="${page*per_page>count?count:page*per_page }"/> <egov-smc:message key="page.of">of</egov-smc:message> <c:out value="${count}"/> <egov-smc:message key="page.records">Records</egov-smc:message></p>


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
                            <table class="noti_datatable table-striped table-bordered table-hover"
                                   name="dataTable">
                                <script type="text/javascript">
                                    sopCrud1 = new SOP.Crud.SOPCrud('entity', "");
                                    sopCrud1.entityUidsVar = 'entityUids';
                                    sopCrud1.allEntityUidsVar = 'allEntityUids1';
                                    sopCrud1.allEntitiesCbVar = 'allEntitiesCb1';
                                </script>
                                <thead>
                                <tr>
                                    <th><egov-smc:commonLabel>Mode</egov-smc:commonLabel>
                                        <div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/tblock.jpg" alt="">
                                    </th>
                                    <th class="hidden-xs">
                                        <c:choose>
                                            <c:when test="${sort.equals('-sender')}">
                                                <egov-smc:commonLabel>Sender</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('sender', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png" alt=""></a>
                                            </c:when>
                                            <c:otherwise>
                                                <egov-smc:commonLabel>Sender</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('sender', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png" alt=""></a>
                                            </c:otherwise>
                                        </c:choose>
                                    </th>
                                    <th class="hidden-xs">
                                        <c:choose>
                                            <c:when test="${sort.equals('-senderAgy')}">
                                                <egov-smc:commonLabel>Agency</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('senderAgy', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png" alt=""></a>
                                            </c:when>
                                            <c:otherwise>
                                                <egov-smc:commonLabel>Agency</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('senderAgy', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png" alt=""></a>
                                            </c:otherwise>
                                        </c:choose>
                                    </th>
                                    <th>
                                        <c:choose>
                                            <c:when test="${sort.equals('-subject')}">
                                                <egov-smc:commonLabel>Subject</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('subject', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png" alt=""></a>
                                            </c:when>
                                            <c:otherwise>
                                                <egov-smc:commonLabel>Subject</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('subject', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png" alt=""></a>
                                            </c:otherwise>
                                        </c:choose>
                                    </th>
                                    <th class="hidden-xs">
                                        <c:choose>
                                            <c:when test="${sort.equals('-importance')}">
                                                <egov-smc:commonLabel>Importance</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('importance', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png" alt=""></a>
                                            </c:when>
                                            <c:otherwise>
                                                <egov-smc:commonLabel>Importance</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('importance', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png" alt=""></a>
                                            </c:otherwise>
                                        </c:choose>
                                    </th>
                                    <th>
                                        <c:choose>
                                            <c:when test="${sort.equals('-receivedDate')}">
                                                <egov-smc:commonLabel>Date</egov-smc:commonLabel><a class="sort-up" href="javascript:changeSort_('receivedDate', 'ascending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-uparrow.png" alt=""></a>
                                            </c:when>
                                            <c:otherwise>
                                                <egov-smc:commonLabel>Date</egov-smc:commonLabel><a class="sort-down" href="javascript:changeSort_('receivedDate', 'descending')" ><div class="hidden-sm hidden-md hidden-lg"></div><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/images/sort-downarrow.png" alt=""></a>
                                            </c:otherwise>
                                        </c:choose>
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="message" items="${searchResult.rows}">
                                    <tr class="${message.status=='U'?'unread':'read' }">
                                        <td>
                                            <%
                                                Message msg = (Message) pageContext.getAttribute("message");

                                            %>

                                            <c:if test="${message.type=='S' }">
												<%--<span class="badge badge-success">
													<i class="fa fa-comment"></i>
												</span>--%>
                                                <em class="fa fa-comment circle-icon"></em>
                                            </c:if>

                                            <c:if test="${message.type=='E' }">
													<%--<span class="badge badge-info">
														<i class="fa fa-envelope-o"></i>
													</span>--%>
                                                <em class="fa fa-envelope-o circle-icon"></em>
                                            </c:if>
                                        </td>
                                        <td class="hidden-xs">${message.sender}</td>
                                        <td class="hidden-xs">${message.senderAgy}</td>
                                        <td class="break-xxs">
                                            <%
                                                String subject = msg.getSubject();
                                                if (StringHelper.equals(msg.getType(), MessageConstants.MODE_TYPE_SMS)) {
                                                    subject = MessageConstants.MODE_SUBJECT_LABEL_SMS;
                                                }
                                            %>
                                            <%=subject %>
                                            <input name="uniqueKey" type="hidden" value="${message.id }">
                                        </td>
                                        <td class="hidden-xs">
                                            <%
                                                String displayLabel="";
                                                String styleClass="";
                                                switch (msg.getImportance()){
                                                    case "L":
                                                        displayLabel="Low";
                                                        styleClass="label-success";
                                                        break;
                                                    case "H":
                                                        displayLabel="High";
                                                        styleClass="label-danger";
                                                        break;
                                                    case "N":
                                                        displayLabel="Normal";
                                                        styleClass="label-success";
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            %>
                                            <label><span class="label <%=styleClass%>"><egov-smc:commonLabel><%=displayLabel%></egov-smc:commonLabel></span>
                                            </label>
                                        </td>
                                        <td>
                                            <%
                                                String receivedDate = ConsistencyHelper.formatDateTime(msg.getReceivedDate());
                                            %>

                                            <%=receivedDate%>
                                        </td>
                                    </tr>

                                </c:forEach>
                                <c:if test="${count==0 }">
                                    <tr>
                                        <td colspan="6">
                                            <blod>
                                                <strong><egov-smc:commonLabel>No records found</egov-smc:commonLabel></strong>
                                            </blod>
                                        </td>
                                    </tr>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="blank"></div>
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
                                    int totalPage = (int) request.getAttribute("totalPage");
                                    int currentPage = (int) request.getAttribute("page");
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
                                            <a class="next" href="javascript:changePage_(${page+1 })">
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
                        <div class="clearfix"></div>
                    </div>
                </div>
            </div>
        </div>
        <br class="clear"/></form>


</div>