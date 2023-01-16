<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webrootCom=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>

<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp"%>
<form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="row form-horizontal">

                    <div class="col-xs-12 col-md-12 panel-group">
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Application Type</label>
                            <div class="col-md-5 col-sm-5 row ">
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio" value="APTY002"
                                               name="applicationType" id="radioNew"
                                               <c:if test="${ calculateFeeConditionDto.applicationType =='APTY002'  }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioNew"><span
                                                class="check-circle"></span>New</label>
                                    </div>
                                </iais:value>
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" value="APTY004"
                                               name="applicationType" id="radioRenewal"
                                               <c:if test="${calculateFeeConditionDto.applicationType == 'APTY004' }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioRenewal"><span
                                                class="check-circle"></span>Renewal</label>
                                    </div>
                                </iais:value>
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" value="APTY005"
                                               name="applicationType" id="radioRfc"
                                               <c:if test="${calculateFeeConditionDto.applicationType == 'APTY005' }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioRfc"><span
                                                class="check-circle"></span>RFC</label>
                                    </div>
                                </iais:value>
                                <div class="col-md-5 col-sm-5 ">
                                    <span id="error_applicationType" style="white-space: nowrap;" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>
                        </iais:row>
                        <iais:row cssClass="amendmentFlow">
                            <label class="col-xs-3 col-md-3 control-label">Amendment Flow?</label>
                            <div class="col-md-5 col-sm-5 row ">
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio" value="Y"
                                               name="radioAmendment" id="radioAmendmentYes"
                                               <c:if test="${ calculateFeeConditionDto.radioAmendment =='Y'  }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioAmendmentYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" value="N"
                                               name="radioAmendment" id="radioAmendmentNo"
                                               <c:if test="${calculateFeeConditionDto.radioAmendment != 'Y' }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioAmendmentNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </div>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Service Name</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <iais:select name="serviceName" options="licSvcTypeOption"
                                             firstOption="Please Select"
                                             cssClass="clearSel"  value="${calculateFeeConditionDto.serviceName}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">MOSD</label>
                            <div class="col-md-8 col-sm-8 row ">
                                <iais:value width="3" cssClass="col-md-3 row">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio" value="PERMANENT"
                                               name="mosdType" id="permanentPremises"
                                               <c:if test="${ calculateFeeConditionDto.mosdType =='PERMANENT'  }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="permanentPremises"><span
                                                class="check-circle"></span>Permanent Premises</label>
                                    </div>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3 row">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="mosdType" id="conveyance" value="CONVEYANCE"
                                               <c:if test="${calculateFeeConditionDto.mosdType == 'CONVEYANCE' }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="conveyance"><span
                                                class="check-circle"></span>Conveyance</label>
                                    </div>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3 row">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="mosdType" id="temporaryPremises" value="MOBILE"
                                               <c:if test="${calculateFeeConditionDto.mosdType == 'MOBILE' }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="temporaryPremises"><span
                                                class="check-circle"></span>Temporary Premises</label>
                                    </div>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3 row">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="mosdType" id="remoteDelivery" value="REMOTE"
                                               <c:if test="${calculateFeeConditionDto.mosdType == 'REMOTE' }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="remoteDelivery"><span
                                                class="check-circle"></span>Remote Delivery</label>
                                    </div>
                                </iais:value>
                                <div class="col-md-5 col-sm-5 ">
                                    <span id="error_mosdType" name="iaisErrorMsg" class="error-msg" style="white-space: nowrap;"></span>
                                </div>
                            </div>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">No. of Simple SS</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <input id="simpleNum" type="text" name="simpleNum" maxlength="2" value="${calculateFeeConditionDto.simpleNum}" />
                                <span id="error_simpleNum" name="iaisErrorMsg" class="error-msg"></span>

                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">No. of Complex SS</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <input id="complexNum" type="text" name="complexNum" maxlength="2" value="${calculateFeeConditionDto.complexNum}" />
                                <span id="error_complexNum" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">No. of Vehicles</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <input id="numVehicles" type="text" name="numVehicles" value="${calculateFeeConditionDto.numVehicles}" />
                                <span id="error_numVehicles" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">No. of Beds</label>
                            <iais:value width="5" cssClass="col-md-5">
                                <input id="numBeds" type="text" name="numBeds"  value="${calculateFeeConditionDto.numBeds}" />
                                <span id="error_numBeds" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:value>
                        </iais:row>

                        <div class="addSvcFormMarkPointInfo newFlow renewalFlow">
                            <c:forEach var="condition" items="${addConditionList}" varStatus="index">
                                <div class="addServiceInfo">
                                    <input type="hidden" name="lengthsInfo" value="${index.index}" />
                                    <iais:row>
                                        <label class="col-xs-3 col-md-3 control-label">Service Name</label>
                                        <iais:value width="5" cssClass="col-md-5">
                                            <iais:select name="serviceName${index.index}" options="licSvcTypeOption"
                                                         firstOption="Please Select"
                                                         cssClass="clearSel"  value="${condition.serviceName}"/>
                                        </iais:value>
                                        <div class="col-xs-2 col-sm-2 col-md-2">
                                            <div class="removeInfoBtn">
                                                <button class=" btn btn-secondary" type="button">-</button>
                                            </div>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-3 col-md-3 control-label">MOSD</label>
                                        <div class="col-md-8 col-sm-8 row ">
                                            <iais:value width="3" cssClass="col-md-3 row">
                                                <div class="form-check">
                                                    <input class="form-check-input"
                                                           type="radio" value="PERMANENT"
                                                           name="mosdType${index.index}" id="permanentPremises${index.index}"
                                                           <c:if test="${ condition.mosdType =='PERMANENT'  }">checked</c:if>
                                                           aria-invalid="false"/>
                                                    <label class="form-check-label"
                                                           for="permanentPremises"><span
                                                            class="check-circle"></span>Permanent Premises</label>
                                                </div>
                                            </iais:value>
                                            <iais:value width="3" cssClass="col-md-3 row">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio"
                                                           name="mosdType${index.index}" id="conveyance${index.index}" value="CONVEYANCE"
                                                           <c:if test="${condition.mosdType == 'CONVEYANCE' }">checked</c:if>
                                                           aria-invalid="false"/>
                                                    <label class="form-check-label"
                                                           for="conveyance"><span
                                                            class="check-circle"></span>Conveyance</label>
                                                </div>
                                            </iais:value>
                                            <iais:value width="3" cssClass="col-md-3 row">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio"
                                                           name="mosdType${index.index}" id="temporaryPremises${index.index}" value="MOBILE"
                                                           <c:if test="${condition.mosdType == 'MOBILE' }">checked</c:if>
                                                           aria-invalid="false"/>
                                                    <label class="form-check-label"
                                                           for="temporaryPremises"><span
                                                            class="check-circle"></span>Temporary Premises</label>
                                                </div>
                                            </iais:value>
                                            <iais:value width="3" cssClass="col-md-3 row">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio"
                                                           name="mosdType${index.index}" id="remoteDelivery${index.index}" value="REMOTE"
                                                           <c:if test="${condition.mosdType == 'REMOTE' }">checked</c:if>
                                                           aria-invalid="false"/>
                                                    <label class="form-check-label"
                                                           for="remoteDelivery"><span
                                                            class="check-circle"></span>Remote Delivery</label>
                                                </div>
                                            </iais:value>
                                            <div class="col-md-5 col-sm-5 ">
                                                <span id="error_mosdType${index.index}" name="iaisErrorMsg" class="error-msg" style="white-space: nowrap;"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-3 col-md-3 control-label">No. of Simple SS</label>
                                        <iais:value width="5" cssClass="col-md-5">
                                            <input id="simpleNum${index.index}" type="text" name="simpleNum${index.index}" maxlength="2" value="${condition.simpleNum}" />
                                            <span id="error_simpleNum${index.index}" name="iaisErrorMsg" class="error-msg"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-3 col-md-3 control-label">No. of Complex SS</label>
                                        <iais:value width="5" cssClass="col-md-5">
                                            <input id="complexNum${index.index}" type="text" name="complexNum${index.index}" maxlength="2" value="${condition.complexNum}" />
                                            <span id="error_complexNum${index.index}" name="iaisErrorMsg" class="error-msg"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-3 col-md-3 control-label">No. of Vehicles</label>
                                        <iais:value width="5" cssClass="col-md-5">
                                            <input id="numVehicles${index.index}" type="text" name="numVehicles${index.index}" value="${condition.numVehicles}" />
                                            <span id="error_numVehicles${index.index}" name="iaisErrorMsg" class="error-msg"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-3 col-md-3 control-label">No. of Beds</label>
                                        <iais:value width="5" cssClass="col-md-5">
                                            <input id="numBeds${index.index}" type="text" name="numBeds${index.index}"  value="${condition.numBeds}" />
                                            <span id="error_numBeds${index.index}" name="iaisErrorMsg" class="error-msg"></span>
                                        </iais:value>
                                    </iais:row>
                                </div>
                            </c:forEach>
                        </div>

                        <iais:row cssClass="newFlow">
                            <label class="col-xs-3 col-md-3 control-label">Align to existing licence?</label>
                            <div class="col-md-5 col-sm-5 row ">
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio" value="Y"
                                               name="radioAlign" id="radioAlignYes"
                                               <c:if test="${ calculateFeeConditionDto.radioAlign =='Y'  }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioAlignYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="radioAlign" id="radioAlignNo" value="N"
                                               <c:if test="${calculateFeeConditionDto.radioAlign != 'Y' }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioAlignNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </div>
                        </iais:row>

                        <iais:row cssClass="newFlow amendmentFlow">
                            <label class="col-xs-3 col-md-3 control-label">Existing licence start and end date</label>
                            <iais:value width="2" cssClass="col-md-2">
                                <iais:datePicker id="licenceDateFrom" name="licenceDateFrom"
                                                 dateVal="${calculateFeeConditionDto.licenceDateFrom}"/>
                            </iais:value>
                            <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                            <iais:value width="2" cssClass="col-md-2">
                                <iais:datePicker id="licenceDateTo" name="licenceDateTo"
                                                 dateVal="${calculateFeeConditionDto.licenceDateTo}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Charitable HCI</label>
                            <div class="col-md-5 col-sm-5 row ">
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio" value="Y"
                                               name="radioCharitable" id="radioCharitableYes"
                                               <c:if test="${ calculateFeeConditionDto.radioCharitable =='Y'  }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioCharitableYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" value="N"
                                               name="radioCharitable" id="radioCharitableNo"
                                               <c:if test="${calculateFeeConditionDto.radioCharitable != 'Y' }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioCharitableNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </div>
                        </iais:row>

                        <iais:row cssClass="renewalFlow">
                            <label class="col-xs-3 col-md-3 control-label">Licence expiry date (Renewal only)</label>
                            <iais:value width="2" cssClass="col-md-2">
                                <iais:datePicker id="licenceExpiryDate" name="licenceExpiryDate"
                                                 dateVal="${calculateFeeConditionDto.licenceExpiryDate}"/>
                            </iais:value>
                        </iais:row>
                        <%--<iais:row>
                            <label class="col-xs-3 col-md-3 control-label">Application is withdrawn/rejected</label>
                            <div class="col-md-5 col-sm-5 row ">
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio" value="Y"
                                               name="radioRejected" id="radioRejectedYes"
                                               <c:if test="${ calculateFeeConditionDto.radioRejected =='Y'  }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioRejectedYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="4" cssClass="col-md-4  row">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" value="N"
                                               name="radioRejected" id="radioRejectedNo"
                                               <c:if test="${calculateFeeConditionDto.radioRejected != 'Y' }">checked</c:if>
                                               aria-invalid="false"/>
                                        <label class="form-check-label"
                                               for="radioRejectedNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </div>
                        </iais:row>--%>
                        <div class="col-xs-12 col-md-12 newFlow renewalFlow">
                            <iais:action >
                                <button type="button" class="addNewSvcInfo btn btn-secondary">Add
                                </button>
                            </iais:action>
                        </div>
                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-primary"
                                        onclick="$('#mainForm').submit();">Submit
                                </button>
                            </iais:action>
                        </div>
                    </div>
                </div>
                <br>
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>

<script type="text/javascript">
    $(document).ready(function() {
        var applicationType='${calculateFeeConditionDto.applicationType}';
        if(applicationType == "APTY005"){
            $('.newFlow').attr("style" ,"display: none");
            $('.renewalFlow').attr("style" ,"display: none");
            $('.amendmentFlow').attr("style" ,"display: block");
        } else
        if(applicationType == "APTY002"){
            $('.renewalFlow').attr("style" ,"display: none");
            $('.amendmentFlow').attr("style" ,"display: none");
            $('.newFlow').attr("style" ,"display: block");
        }else
        if(applicationType == "APTY004"){
            $('.newFlow').attr("style" ,"display: none");
            $('.amendmentFlow').attr("style" ,"display: none");
            $('.renewalFlow').attr("style" ,"display: block");

        }else {
            $('.newFlow').attr("style" ,"display: none");
            $('.amendmentFlow').attr("style" ,"display: none");
            $('.renewalFlow').attr("style" ,"display: none");

        }

        addSvcInfo();
        removeSvcInfo();
        selectApplicationType();
    })

    var addServiceInfoLength=$('div.addServiceInfo').length;
    var lengthInfo =0+addServiceInfoLength;

    var selectApplicationType = function () {
        $('input[name ="applicationType"]').click(function () {
            var applicationType=$(this).val();
            if(applicationType == "APTY005"){
                $('.newFlow').attr("style" ,"display: none");
                $('.renewalFlow').attr("style" ,"display: none");
                $('.amendmentFlow').attr("style" ,"display: block");
            }
            if(applicationType == "APTY002"){
                $('.renewalFlow').attr("style" ,"display: none");
                $('.amendmentFlow').attr("style" ,"display: none");
                $('.newFlow').attr("style" ,"display: block");
            }
            if(applicationType == "APTY004"){
                $('.newFlow').attr("style" ,"display: none");
                $('.amendmentFlow').attr("style" ,"display: none");
                $('.renewalFlow').attr("style" ,"display: block");

            }

        });
    }

    var addSvcInfo = function () {
        $('.addNewSvcInfo').click(function () {

            var $contentDivEle = $(this).closest('div.panel-group');

            var jsonData={
                'Length': lengthInfo
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}/fee-jsp-add-condition-html',
                'dataType':'text',
                'data':jsonData,
                'type':'GET',
                'success':function (data) {
                    if(data == null){
                        return;
                    }
                    <!--use ph mark point -->
                    $contentDivEle.find('div.addSvcFormMarkPointInfo').addClass('rfiContentInfo');
                    <!--add html -->
                    $contentDivEle.find('div.rfiContentInfo').before(data);
                    <!--init ph mark point -->
                    $contentDivEle.find('div.addSvcFormMarkPointInfo').removeClass('rfiContentInfo');
                    <!--change hidden length value -->
                    //Prevent duplicate binding
                    $('.removeInfoBtn').unbind('click');
                    removeSvcInfo();
                    lengthInfo=lengthInfo+1;
                },
                'error':function () {
                }
            });


        });
    }

    var removeSvcInfo = function () {
        $('.removeInfoBtn').click(function () {
            var $rfiContentEle = $(this).closest('div.addServiceInfo');
            $rfiContentEle.remove();
            lengthInfo=lengthInfo-1;

        });
    }
</script>
