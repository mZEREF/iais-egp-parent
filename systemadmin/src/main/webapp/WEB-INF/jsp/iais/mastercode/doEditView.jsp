<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content" style="min-height: 73vh;">
    <form class="form-horizontal" method="post" id="MasterCodeForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>Edit Master Code</h2>
                        </div>
                        <div class="form-group">
                            <iais:field value="Master Code Category" required="true"/>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <p>${MasterCodeDto.codeCategory}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="description">Filter Value</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <c:choose>
                                    <c:when test="${empty MasterCodeDto.filterValue}">
                                        <p>-</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p>${ MasterCodeDto.filterValue}</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="description">Version</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <c:choose>
                                    <c:when test="${empty MasterCodeDto.version}">
                                       <p>-</p>
                                    </c:when>
                                    <c:otherwise>
                                    <p>${ MasterCodeDto.version}</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="form-group">
                            <iais:field value="Code Value" required="true"/>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="codeValue" type="text" value="${MasterCodeDto.codeValue}" name="codeValueEd" maxlength="25" >
                                <span id="error_codeValue" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <iais:field value="Code Description" required="true"/>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <form><textarea style="width: 100%;margin-bottom: 15px;" rows="6" id="description" name="codeDescriptionEd"
                                                maxlength="255">${MasterCodeDto.codeDescription}</textarea></form>
                                <span id="error_codeDescription" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <iais:field value="Sequence" required="true"/>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <input id="sequence" type="text" value="<fmt:formatNumber type="number" value="${empty MasterCodeDto.sequence? null : MasterCodeDto.sequence / 1000}" maxFractionDigits="2" pattern="0"/>" name="codeSequenceEd" maxlength="3">
                                <span id="error_sequence" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="description">Remarks</label>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <textarea style="width: 100%" rows="6" id="codeRemarks"  name="codeRemarksEd" maxlength="255">${MasterCodeDto.remarks}</textarea>
                                <span id="error_remarks" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <iais:field value="Status" required="true"/>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <iais:select name="codeStatusEd" id="codeStatus" options="mcStatusSelectList" value="${MasterCodeDto.status}"/>
                                <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group esdDiv">
                            <iais:field value="Effective Start Date" required="true"/>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <iais:datePicker id="esd" name="esdEd" dateVal="${MasterCodeDto.effectiveFrom}" />
                                <span id="error_effectiveFrom" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group eedDiv">
                            <iais:field value="Effective End Date" required="true"/>
                            <div class="col-xs-8 col-sm-6 col-md-5">
                                <iais:datePicker id="eed" name="eedEd" dateVal="${MasterCodeDto.effectiveTo}" />
                                <span id="error_effectiveTo" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="row">
                            <div class="col-xs-2 col-sm-2">
                                <div><a href="/system-admin-web/eservice/INTRANET/MohMasterCode"><em class="fa fa-angle-left"></em> Back</a></div>
                            </div>
                            <div class="col-xs-10 col-sm-10">
                                <div class="text-right text-center-mobile"><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">SAVE</button></div>
                            </div>
                        </div>
                    </div>
                    <iais:confirm msg="Do you confirm the modification ?" needCancel="true" callBack="doEdit('${MasterCodeDto.masterCodeId}')" popupOrder="myModal" yesBtnDesc="Continue" needEscapHtml="false" needFungDuoJi="true"></iais:confirm>
                </div>
            </div>
        </div>
    <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
    </form>
</div>

<script>

    $(document).ready(function () {
        codeStatusChange();
        $('#codeStatus').trigger('change');

    });

    function submit(action){
        $("[name='crud_action_type']").val(action);
        $("#MasterCodeForm").submit();
    }

    function doEdit(mcId){
        showWaiting();
        $("[name='crud_action_value']").val(mcId);
        submit("edit");
    }

    var codeStatusChange = function(){
        $('#codeStatus').change(function () {
            var status = $(this).val();
            if('CMSTAT003' == status){
                $('div.esdDiv').addClass('hidden');
                $('div.eedDiv').addClass('hidden');
            }else{
                $('div.esdDiv').removeClass('hidden');
                $('div.eedDiv').removeClass('hidden');
            }
        });
    }


</script>