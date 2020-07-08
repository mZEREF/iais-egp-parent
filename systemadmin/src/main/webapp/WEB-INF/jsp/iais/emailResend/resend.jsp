<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="crud_action_type_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Schedule a new date and time</h2>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Message ID</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input type="text" class="disable" name="name" value="${resendBlastedit.messageId}" readonly>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Message Name:</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input type="text" name="name" class="disable"  value="${resendBlastedit.msgName}" readonly>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Send date and time" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <iais:datePicker id="date" name="date"  value="${schedule}" />
                                    <input type="text" value="${hour}" maxlength="2" style="width: 60px" name="HH"/>&nbsp;(HH)
                                    :
                                    <input type="text" value="${minutes}" maxlength="2" style="width: 60px"  name="MM"/>&nbsp;(MM)

                                </div>
                            </iais:value>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-4 col-md-4 control-label">Status</label>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="name" type="text" name="name" class="disable"  value="${resendBlastedit.status}" readonly>
                                </div>
                            </iais:value>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="text-right text-center-mobile">
                                <a id="saveDis" type="button" class="btn btn-primary">Send Message</a>
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
        $("[name='crud_action_type_value']").val('send');
        SOP.Crud.cfxSubmit("mainForm");
    });

    $('#back').click(function () {
        $("[name='crud_action_type_value']").val('back');
        SOP.Crud.cfxSubmit("mainForm");
    });
</script>