<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>Licensee Details</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type" value="">
    <div class="main-content">
        <div class="tab-gp steps-tab">
            <div class="tab-content">
                <div class="tab-pane active" id="premisesTab" role="tabpanel">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="new-premise-form-conveyance">
                                <div class="form-horizontal">
                                    <%@include file="/WEB-INF/jsp/iais/common/myinfoInstructionsLinks.jsp"%>
                                    <%@include file="/WEB-INF/jsp/iais/LicenseeDetail/licenseeDetailContent.jsp"%>
                                    <div class="application-tab-footer">
                                        <div class="row">
                                            <div class="col-xs-2 col-md-2">
                                                <a   style="padding-left: 90px;" align="left" class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                                            </div>
                                            <div class="text-right col-xs-9 col-md-9">
                                                    <a class="btn btn-primary save" id="reLoadMyInfoSave">Save</a>
                                                <input type="hidden" id="saveDataSolo" name="saveDataSolo" >
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input hidden value="${backtype}" id="backtype">
    <%@ include file="/WEB-INF/jsp/iais/common/myinfoDownRemind.jsp" %>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>
<script type="text/javascript">
    $("#back").click(function () {
        $("[name='crud_action_type']").val('back');
        $('#mainForm').submit();
    })

    $("#reLoadMyInfoSave").click(function () {
        $("[name='crud_action_type']").val('refresh');
        $("#saveDataSolo").val("saveDataSolo");
        $('#mainForm').submit();
    })
    function reLoadMyInfoTodo() {
        if(verifyTaken()){
            $("[name='crud_action_type']").val('refresh');
            $('#mainForm').submit();
        }else {
            // To obtain authorization
            showWaiting();
            callAuthoriseApi();
        }
    }

    $('#addrType').on('change',checkAddressManatory);

    function checkAddressManatory() {
        var addrType = $('#addrType').val();
        if ('ADDTY001' == addrType) {
            $('.blkNoLabel').append('<span class="mandatory">*</span>');
            $('.floorUnitLabel').append('<span class="mandatory">*</span>');
        } else {
            $('.blkNoLabel .mandatory').remove();
            $('.floorUnitLabel .mandatory').remove();
        }
    }

    $(document).ready(function() {
        checkAddressManatory();
    });

</script>