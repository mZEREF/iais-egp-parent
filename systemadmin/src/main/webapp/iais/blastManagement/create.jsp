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
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>New Blast Management List</h2>
                        </div>
                            <c:choose>
                                <c:when test="${empty edit.getMode() || 'email'.equals(edit.getMode())}">
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
                            <iais:field value="Message Name" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="name" type="text" name="name" value="${edit.getMsgName()}">
                                    <span id="error_msgName" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Mode of Delivery" required="true"/>
                            <iais:value>
                                <iais:value width="10">
                                    <iais:select name="mode" options="mode" onchange="changeFlow(this.value)" value=""></iais:select>
                                </iais:value>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Send date and time" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="date" name="date"  value="${schedule}" ></iais:datePicker>
                                    <input type="text" value="${edit.getHH()}" maxlength="2" style="width: 60px" name="HH"/>&nbsp;(HH)
                                    :
                                    <input type="text" value="${edit.getMM()}" maxlength="2" style="width: 60px"  name="MM"/>&nbsp;(MM)
                                </div>
                                <span id="error_date" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <iais:field value="Status" required="true"/>
                                <iais:value width="10">
                                    <iais:select name="status" options="status" value=""></iais:select>
                                </iais:value>
                            <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-11 col-sm-11">
                                <div class="text-right text-center-mobile">
                                    <button id="saveDis" type="button" class="btn btn-primary">Write Message</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/include/validation.jsp" %>
</div>


<script type="text/javascript">
    $('#saveDis').click(function () {
        SOP.Crud.cfxSubmit("mainForm");
    });

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