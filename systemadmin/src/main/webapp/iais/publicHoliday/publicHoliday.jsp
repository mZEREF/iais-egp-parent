<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="col-xs-12 col-sm-12 col-md-12">
            <div class="center-content">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <h3>
                        <span>Public Holiday</span>
                    </h3>
                    <div class="form-group">
                        <iais:value>
                            <label class="col-xs-2 col-md-2 control-label">Description</label>
                            <div class="col-xs-4 col-sm-4 col-md-4">
                                <input id="descriptionSwitch" name="descriptionSwitch" type="text"
                                       value="${descriptionSwitch}">
                            </div>
                        </iais:value>
                    </div>
                    <div class="form-group">
                        <iais:value>
                            <label class="col-xs-2 col-md-2 control-label">Year</label>
                            <div class="col-xs-4 col-sm-4 col-md-4">
                                <input id="yearSwitch" name="yearSwitch" type="text" value="${yearSwitch}">
                            </div>
                        </iais:value>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-12">
                            <div class="text-right"><a class="btn btn-primary" id="search">Search</a></div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                        <div class="button-group">
                            <a class="btn btn-file-upload btn-secondary" id="createholiday">Create</a>
                        </div>
                    </div>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <th>Date</th>
                                <th>Week</th>
                                <th>Description</th>
                                <th>Edit</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${holidayList}" varStatus="status">
                                <tr>
                                    <td><c:out value="${item.date}"/></td>
                                    <td><c:out value="${item.week}"/></td>
                                    <td><c:out value="${item.description}"/></td>
                                    <td>
                                        <a onclick="edit('${item.id}','${item.sub_date}','${item.to_date}','${item.description}')">edit</a>
                                        <a onclick="deleteItem('${item.id}')">delete</a></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>

        <input type="hidden" name="holidayId" id="holidayId" value="">
        <input type="hidden" name="sub_date" id="sub_date" value="">
        <input type="hidden" name="to_date" id="to_date" value="">
        <input type="hidden" name="des" id="des" value="">
    </form>
</div>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    $('#createholiday').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "create");
    });

    function edit(id, sub_date, to_date, des) {
        $("#holidayId").val(id);
        $("#sub_date").val(sub_date);
        $("#to_date").val(to_date);
        $("#des").val(des);
        SOP.Crud.cfxSubmit("mainForm", "edit");
    };

    function deleteItem(id) {
        $("#holidayId").val(id);
        SOP.Crud.cfxSubmit("mainForm", "delete");
    }

    $('#search').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "search");
    });
</script>