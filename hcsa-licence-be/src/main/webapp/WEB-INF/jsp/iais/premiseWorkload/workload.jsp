<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-intranet"/>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style>
    nav{
        margin-bottom: 55px;
    }
    footer{
        margin-bottom: 0px;
        position: absolute !important;
    }
</style>
<div class="main-content">
    <form id="mainForm1" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="crud_action_type"/>
        <div class="col-xs-12 col-sm-12 col-md-12">
            <div class="center-content">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <h3>
                        ${premiseWorkloadType} Mode of Service Delivery Workload Manhours
                    </h3>

                    <div class="table-gp">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr >
                                <th scope="col" >S/N</th>
                                <th scope="col" >Routing Stages</th>
                                <th scope="col" >Workload Manhours</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${hcsaPrimiseWorkloadDtos}" varStatus="status">
                                <tr>
                                    <td>
                                            ${status.index + 1}
                                    </td>
                                    <td>
                                            ${item.stageDesc}
                                    </td>
                                    <td>
                                        <input name="${item.stageId}" value="${item.manhourCount}" maxlength="2">
                                        <br>
                                        <span class="error-msg" name="errorMsg" id="error_${item.stageId}"></span>

                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <a class="back text-left" href="#" onclick="back()"><em class="fa fa-angle-left"></em> Back</a>
                                <a class="btn btn-primary next " style="float: right"
                                   href="javascript:void(0);"
                                   onclick="javascript: doSubmit();">Submit</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script>

    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $('#mainForm1').submit();
    }

    function doSubmit() {
        submit("save");
    }
    function back() {
        submit("back");
    }
</script>