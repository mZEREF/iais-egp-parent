<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<webui:setLayout name="iais-internet"/>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%@include file="../cessation/head.jsp" %>
<div class="main-content">
  <div class="container">
    <p class="print"><div style="font-size: 16px;text-align: right"><a onclick="printWDPDF()"> <em class="fa fa-print"></em>Print</a></div></p>
    <div class="row">
      <div class="col-xs-12">
        <div class="instruction-content center-content">
          <br/>
          <div class="row">
            <form id="mainForm" class="form-horizontal"
                  enctype="multipart/form-data"
                  action=<%=process.runtime.continueURL()%>>
              <c:forEach items="${appCessationDtos}" var="appCess" varStatus="num">
              <div class="col-lg-12 col-xs-12 cesform-box">
                <div class="row">
                  <div class="license-info-box">
                    <div class="col-lg-6 col-xs-12">
                      <div class="license-info">
                        <p class="lic-no">Licence Number</p>
                        <span style="padding-left: 50px;"><c:out value="${appCess.licenceNo}"/></span>
                      </div>
                    </div>
                    <div class="col-lg-6 col-xs-12">
                      <div class="license-info">
                        <p class="serv-name">Service Name</p>
                        <span style="padding-left: 50px;"><c:out value="${appCess.licenceNo}"/></span>
                      </div>
                    </div>
                  </div>
                  <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid">
                    <div class="col-lg-12 col-xs-12">
                      <div class="table-gp tablebox">
                        <div class="topheader">
                          <c:if test="${appCessHci.hciName==null}">
                            <h4>HCI Name: <c:out value="${appCessHci.hciCode}"/></h4>
                          </c:if>
                          <c:if test="${appCessHci.hciName!=null}">
                            <h4>HCI Name: <c:out value="${appCessHci.hciName}"/> - <c:out
                                    value="${appCessHci.hciCode}"/></h4>
                          </c:if>
                          <p>HCI Address: <c:out value="${appCessHci.hciAddress}"/></p>
                        </div>
                        <input type="hidden" name="sopEngineTabRef"
                               value="<%=process.rtStatus.getTabRef()%>">
                        <input type="hidden" name="crud_action_type" value="">
                        <input type="hidden" name="crud_action_value" value="">
                        <div class="form-group">
                          <label class="col-xs-12 col-md-6 control-label">Effective
                            Date <span style="color: red">*</span></label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <fmt:formatDate value="${appCessHci.effectiveDate}"
                                            pattern="dd/MM/yyyy"/>
                          </div>
                          <div class="col-xs-8 col-sm-2 col-md-2">
                            <a class="btn-tooltip styleguide-tooltip"
                               data-toggle="tooltip" data-html="true" style="position: absolute;z-index: 1000"
                               title="${cess_ack002}">i</a>
                          </div>
                        </div>
                        <div class="form-group">
                          <label class="col-xs-12 col-md-6 control-label">Cessation
                            Reasons <span style="color: red">*</span></label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <iais:select disabled="true"
                                         id="${num.count}reasonId${uid.count}"
                                         name="${num.count}reason${uid.count}"
                                         options="reasonOption"
                                         value="${appCessHci.reason}"
                                         cssClass="nice-select cessationReasons"/>
                          </div>
                        </div>
                        <div class="form-group" id="${num.count}reason${uid.count}" style="display: none;">
                          <label class="col-xs-12 col-md-6 control-label ">Others <span
                                  style="color: red">*</span></label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <iais:input needDisabled="true" type="text"
                                        name="${num.count}otherReason${uid.count}"
                                        value="${appCessHci.otherReason}"/>
                          </div>
                        </div>
                        <div class="form-group">
                          <label class="col-xs-12 col-md-6 control-label">Patient's Records will be transferred <span
                                  style="color: red">*</span></label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <div class="form-check-gp">
                              <div class="row">
                                <div class="col-xs-12 col-md-3">
                                  <div class="form-check">
                                    <input class="form-check-input"
                                           id="${num.count}radioYes${uid.count}"
                                           type="radio"
                                           name="${num.count}patRadio${uid.count}"
                                           value="yes"
                                           <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                           onchange="javascirpt:changePatSelectCessFe();"
                                           aria-invalid="false" disabled>
                                    <label class="form-check-label"
                                           for=${num.count}radioYes${uid.count}"><span
                                            class="check-circle <c:if test="${appCessHci.patNeedTrans ==true}">radio-disabled</c:if>"></span>Yes</label>
                                  </div>
                                </div>
                                <div class="col-xs-12 col-md-3">
                                  <div class="form-check">
                                    <input class="form-check-input"
                                           id="${num.count}radioNo${uid.count}"
                                           type="radio"
                                           name="${num.count}patRadio${uid.count}"
                                           value="no"
                                           <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                           onchange="javascirpt:changePatSelectCessFe();"
                                           aria-invalid="false" disabled>
                                    <label class="form-check-label"
                                           for="${num.count}radioNo${uid.count}"><span
                                            class="check-circle <c:if test="${appCessHci.patNeedTrans ==false}">radio-disabled</c:if>"></span>No</label>
                                  </div>
                                </div>
                              </div>
                              <div><span
                                      id="error_${num.count}patRadio${uid.count}"
                                      name="iaisErrorMsg"
                                      class="error-msg"></span>
                              </div>
                            </div>
                          </div>
                        </div>

                        <div class="form-group" id="${num.count}transferDetail${uid.count}" style="display: none;">
                          <label class="col-xs-12 col-md-6">Please provide details of why the transfer could not be done and the reasonable measures that the licensee has taken to ensure continuity of care for the affected patients. </label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <textarea name="${num.count}transferDetail${uid.count}"  style="width: 100%;overflow: auto;word-break: break-all;" maxLength="1000" readonly="readonly">${appCessHci.transferDetail}</textarea>
                          </div>
                        </div>
                        <div class="form-group" id="${num.count}transferredWhere${uid.count}" style="display: none;">
                          <label class="col-xs-12 col-md-6">Please state where the patient's records will be transferred to and where the licensee will store the patients' health records after cessation. </label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <textarea name="${num.count}transferredWhere${uid.count}"  style="width: 100%;overflow: auto;word-break: break-all;" maxLength="1000" readonly="readonly">${appCessHci.transferredWhere}</textarea>
                          </div>
                        </div>

                       <%-- <c:if test="${isGrpLic}">
                          <div>
                            <div class="form-group">
                              <label class="col-xs-12 col-md-6 control-label">To
                                Cease <span style="color: red">*</span></label>
                              <div class="col-xs-6 col-sm-4 col-md-3">
                                <div class="form-check">
                                  <input class="form-check-input"
                                         id="icon5checkboxSample"
                                         type="checkbox"
                                         name="${num.count}whichTodo${uid.count}"
                                         value="${appCessHci.premiseId}"
                                         <c:if test="${appCessHci.premiseIdChecked != null}">checked</c:if>
                                         aria-invalid="false" disabled>
                                  <label class="form-check-label"
                                         for="icon5checkboxSample"><span
                                          class="check-square"></span></label>
                                  <span id="error_whichTodo"
                                        name="iaisErrorMsg"
                                        class="error-msg"></span>
                                </div>
                              </div>
                            </div>
                          </div>
                        </c:if>--%>
                        <c:if test="${!isGrpLic}">
                          <div style="display: none;">
                            <input class="form-check-input" type="text"
                                   name="${num.count}whichTodo${uid.count}"
                                   value="<c:out value="${appCessHci.premiseId}"/>">
                          </div>
                        </c:if>
                      </div>
                    </div>
                  </c:forEach>
                </div>
               <%-- <c:if test="${specLicInfo !=null}">
                <div class="table-responsive">
                  <c:forEach items="${specLicInfo}" var="map">
                    <c:set var="licNo" value="${map.key}"></c:set>
                    <c:if test="${appCess.licenceNo==licNo}">
                      <div><h4>The following specified healthcare services will also be ceased as
                        their
                        underlying <iais:code needLowerCase="true" code="CDN001"/>(s) is/are listed above.</h4>
                      </div>
                      <table aria-describedby="" class="table-gp tablebox">
                        <tr style="text-align:center">
                          <th scope="col" style="text-align:center;width: 0%">S/N</th>
                          <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN003"/>
                            Licence No.
                          </th>
                          <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN003"/>
                            Name
                          </th>
                          <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN001"/> Licence No.
                          </th>
                          <th scope="col" style="text-align:center;width: 25%"><iais:code code="CDN001"/> Name</th>
                        </tr>
                        <c:forEach items="${map.value}" var="spec" varStatus="index">
                          <tr style="text-align:center">
                            <td>
                              <p><c:out value="${index.count}"/></p>
                            </td>
                            <td>
                              <p><c:out value="${spec.specLicNo}"/></p>
                            </td>
                            <td>
                              <p><c:out value="${spec.specSvcName}"/></p>
                            </td>
                            <td>
                              <p><c:out value="${spec.baseLicNo}"/></p>
                            </td>
                            <td>
                              <p><c:out value="${spec.baseSvcName}"/></p>
                            </td>
                          </tr>
                        </c:forEach>
                      </table>
                    </c:if>
                  </c:forEach>
                  <div class="table-responsive">
                    </c:if>--%>
                  </div>
                  </c:forEach>
                </div>
                <div class="row">
                  <div class="col-xs-12">
                    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                      <%@include file="../common/declarations/declarations.jsp"%>
                    </div>
                  </div>
                </div>
                <br/>
              </div>
          </div>
          <div class="application-tab-footer">
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                                <span style="padding-right: 10%" class="components">
                        <a onclick="confirmBack('back')"><em class="fa fa-angle-left"></em> Back</a>
                    </span>
              </div>
              <div class="col-xs-12 col-sm-6">
                                <span style="padding-left: 73%" class="components">
                       <a class="btn btn-primary next" href="javascript:void(0);" onclick="confirmSubmit('submit')">Submit</a>
                    </span>
              </div>
            </div>
          </div>
          </form>
        </div>
      </div>
      <br/>
    </div>
  </div>
  <div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div class="modal-body" >
          <div class="row">
            <div class="col-md-12"><span style="font-size: 2rem;"><%=MessageUtil.getMessageDesc("GENERAL_ERR0048")%></span></div>
          </div>
        </div>
        <div class="row " style="margin-top: 5%;margin-bottom: 5%">
          <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancel()">OK</button>
        </div>
      </div>
    </div>
  </div>
</div>
<style>
  #effectiveDate {
    margin-bottom: 0px;
  }

  input[type='text'] {
    margin-bottom: 0px;
  }

  .radio-disabled::before{
    background-color: #999999 !important;

  }
  .radio-disabled{
    border-color: #999999 !important;
  }
</style>
<script type="text/javascript">

    function confirmSubmit(action) {
        showWaiting();
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }

    function confirmBack(action) {
        $("[name='crud_action_type']").val(action);
        $("#mainForm").submit();
    }

    function changeReasonCessFe() {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($("#" + i + "reasonId" + j).val() == "CES001") {
                    $("#" + i + "reason" + j).show();
                } else {
                    $("#" + i + "reason" + j).hide();
                }
            }
        }
    }

    /*function changePatientCessFe() {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($("#" + i + "patientSelectId" + j).val() == "CES004") {
                    $("#" + i + "patOthersTakeOver" + j).show();
                    $("#" + i + "patOthersMobileNo" + j).show();
                    $("#" + i + "patOthersEmailAddress" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "hciName" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES005" && $('#' + i + 'radioYes' + j).is(':checked')) {
                    $("#" + i + "patHciName" + j).show();
                    $("#" + i + "hciName" + j).show();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patOthersMobileNo" + j).hide();
                    $("#" + i + "patOthersEmailAddress" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "hciNamePat" + j).show();
                    $("#" + i + "hciCodePat" + j).show();
                    $("#" + i + "hciAddressPat" + j).show();
                } else if ($("#" + i + "patientSelectId" + j).val() == "CES006") {
                    $("#" + i + "patRegNo" + j).show();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "hciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patOthersMobileNo" + j).hide();
                    $("#" + i + "patOthersEmailAddress" + j).hide();
                }
            }
        }
    }*/

    function changePatSelectCessFe() {
        //changePatientCessFe();
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioYes' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).show();
                    $("#" + i + "patNo" + j).hide();
                    // $("#" + i + "patNoConfirmID" + j).hide();
                    $("#" + i + "transferDetail" + j).hide();
                    $("#" + i + "transferredWhere" + j).show();
                } else if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "transferDetail" + j).show();
                    $("#" + i + "transferredWhere" + j).hide();
                   /* $("#" + i + "patNo" + j).show();
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "hciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "patOthersMobileNo" + j).hide();
                    $("#" + i + "patOthersEmailAddress" + j).hide();
                    $("#" + i + "patNoConfirmID" + j).show();*/
                }
            }
        }
    }

    $(document).ready(function () {
        changeReasonCessFe();
        //changePatientCessFe();
        changePatSelectCessFe();
    });

    $(document).ready(function () {
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "hciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "div" + j).hide();
                }
            }
        }
    });

    function printWDPDF(){
        window.open("<%=request.getContextPath() %>/eservice/INTERNET/MohAppealPrint?whichPage=cessPage",'_blank');
    }

    $(document).ready(function () {
        $('input[type="text"]').css('border-color', '#ededed');
        $('input[type="text"]').css('color', '#999');
        for (var i = 1; i < 8; i++) {
            for (var j = 1; j < 8; j++) {
                if ($('#' + i + 'radioNo' + j).is(':checked')) {
                    $("#" + i + "patYes" + j).hide();
                    $("#" + i + "patHciName" + j).hide();
                    $("#" + i + "hciName" + j).hide();
                    $("#" + i + "patOthersTakeOver" + j).hide();
                    $("#" + i + "patRegNo" + j).hide();
                    $("#" + i + "div" + j).hide();
                }
            }
        }
    });

</script>