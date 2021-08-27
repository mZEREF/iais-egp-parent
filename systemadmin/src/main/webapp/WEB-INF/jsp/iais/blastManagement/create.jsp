<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style type="text/css">
    .flowDisplay{
        display: flex;
    }
    .flowHidden{
        display: none;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" id="crud_action_type_value" name="crud_action_type_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                            <c:choose>
                                <c:when test="${empty edit.getMode() || 'Email'.equals(edit.getMode())}">
                                    <ul class="progress-tracker" id="emailFlow" >
                                        <li class="tracker-item active">Fill in Message Details</li>
                                        <li class="tracker-item ">Write Message</li>
                                        <li class="tracker-item ">Select Recipients to send</li>
                                    </ul>
                                    <ul class="progress-tracker flowHidden" id="smsFlow">
                                        <li class="tracker-item active">Fill in Message Details</li>
                                        <li class="tracker-item ">Write Message</li>
                                    </ul>
                                </c:when>
                                <c:otherwise>
                                    <ul class="progress-tracker flowHidden" id="emailFlow">
                                        <li class="tracker-item active">Fill in Message Details</li>
                                        <li class="tracker-item ">Write Message</li>
                                        <li class="tracker-item ">Select Recipients to send</li>
                                    </ul>
                                    <ul class="progress-tracker" id="smsFlow">
                                        <li class="tracker-item active">Fill in Message Details</li>
                                        <li class="tracker-item ">Write Message</li>
                                    </ul>
                                </c:otherwise>
                            </c:choose>
                        <div class="form-group">
                            <iais:field value="Message ID"/>
                            <iais:value>
                                <div class="col-xs-4 col-sm-4 col-md-4">
                                    <input id="msgId" type="text" name="msgId" value="${edit.messageId}" readonly>
                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Message Name" required="true"/>
                            <iais:value>
                                <div class="col-xs-4 col-sm-4 col-md-4">
                                    <input id="msgName" type="text" name="msgName" maxlength="500" value="${edit.getMsgName()}">
                                    <span id="error_msgName" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Mode of Delivery" required="true"/>
                            <iais:value>
                                <div class="col-xs-4 col-sm-4 col-md-4">
                                    <iais:select name="mode" options="mode" firstOption="Please Select" onchange="changeFlow(this.value)" value="${edit.mode}"></iais:select>
                            </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Send date and time" required="true"/>
                            <iais:value>
                                <div class="col-xs-4 col-sm-4 col-md-4">
                                    <iais:datePicker id="date" name="date"   value="${schedule}" ></iais:datePicker>
                                </div>
                                <div style="width: 10%;float: left">
                                    <iais:select name="HH" options="HHselect" value="${edit.HH}"></iais:select>
                                </div>
                                <div style="width: 5%;float: left;line-height: 50px;height: 80px;">&nbsp;(HH):</div>
                                <div style="width: 10%;float: left">
                                    <iais:select name="MM" options="MMselect"  value="${edit.MM}"></iais:select>
                                </div>
                                <div style="width: 5%;float: left;line-height: 50px;height: 80px;">&nbsp;(MM)</div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Status" required="true"/>
                            <div class="col-xs-4 col-sm-4 col-md-4">
                                    <iais:select name="status" options="status" firstOption="Please Select" value="${edit.status}"></iais:select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a href="#" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="text-right text-center-mobile">
                                <button id="saveDis" type="button" class="btn btn-primary">Write Message</button>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</div>


<script type="text/javascript">
    $('#saveDis').click(function () {
        $("#crud_action_type_value").val("next")
        SOP.Crud.cfxSubmit("mainForm");
    });

    $("#back").click(function () {
        $("#crud_action_type_value").val("back")
        SOP.Crud.cfxSubmit("mainForm");
    })

    function changeFlow(mode) {
        if(mode == "SMS"){
            $("#smsFlow").addClass("flowDisplay");
            $("#smsFlow").removeClass("flowHidden");
            $("#emailFlow").addClass("flowHidden");
            $("#emailFlow").removeClass("flowDisplay");
        }else{
            $("#smsFlow").addClass("flowHidden");
            $("#smsFlow").removeClass("flowDisplay");
            $("#emailFlow").addClass("flowDisplay");
            $("#emailFlow").removeClass("flowHidden");
        }
    }

</script>