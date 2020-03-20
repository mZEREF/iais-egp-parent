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
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="bg-title">
                        <h2>Public Holiday</h2>
                    </div>
                    <div class="row">
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">Description</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="descriptionSwitch" name="descriptionSwitch" type="text"
                                           value="${descriptionSwitch}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-12 col-md-4 control-label">Year</label>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="yearSwitch" name="yearSwitch" type="text" value="${yearSwitch}">
                                </div>
                            </div>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-11 col-md-11">
                                    <div class="text-right">
                                        <a class="btn btn-secondary" id="clear">Clear</a>
                                        <a class="btn btn-primary" id="search">Search</a>
                                    </div>
                                </div>
                            </div>
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
        <div class="application-tab-footer">
            <div class="row">
                <div class="col-xs-11 col-md-11">
                    <div class="text-right">
                        <a class="btn btn-primary" id="createholiday">Create</a>
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