<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div class="row form-horizontal">
                                    <iais:row>
                                        <iais:field value="Type"/>
                                        <iais:value width="50">
                                            <select id="selectType">
                                                <option value="1">Notification of Consumption</option>
                                                <option value="2">Notification of Complete Destruction and/or Disposal
                                                    Stock Inventory
                                                </option>
                                                <option value="3">Notification of Export</option>
                                                <option value="4">Notification of Receipt</option>
                                                <option value="5">Request for Transfer</option>
                                                <option value="6">Notification of Transfer</option>
                                                <option value="7">Acknowledgement of Receipt of Transfer</option>
                                            </select>
                                        </iais:value>
                                    </iais:row>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="container">
            <div class="col-xs-12 col-md-6 text-right">
                <button class="btn btn-primary save" id="nextBtn">NEXT</button>
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script>
    $(function () {
        $("#nextBtn").click(function () {
            // showWaiting();
            var optionValue = $("#selectType option:selected").val();
            if (optionValue == "1") {
                $("[name='action_type']").val("doConsume");
            }else if (optionValue == "2") {
                $("[name='action_type']").val("doDisposal");
            }else if (optionValue == "3") {
                $("[name='action_type']").val("doExport");
            }else if (optionValue == "4") {
                $("[name='action_type']").val("doReceipt");
            }else if (optionValue == "5") {
                $("[name='action_type']").val("doRequestTransfer");
            }else if (optionValue == "6") {
                $("[name='action_type']").val("doTransferNotification");
            }else if (optionValue == "7") {
                $("[name='action_type']").val("doAckOfReceiptOfTransfer");
            }
            $("#mainForm").submit();
        })
    });
</script>