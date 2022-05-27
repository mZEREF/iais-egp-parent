<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="main-content">
  <div class="container">
    <p class="print"><div style="font-size: 16px;text-align: right"><a onclick="printCSPDF()"> <em class="fa fa-print"></em>Print</a></div></p>
    <div class="row">
      <div class="col-xs-12">
        <div class="instruction-content center-content">
          <br/>
          <div class="row">
            <form id="mainForm" class="form-horizontal"
                  enctype="multipart/form-data"
                  action=<%=process.runtime.continueURL()%>>
              <c:forEach items="${confirmDtos}" var="appCess">
              <div class="col-lg-12 col-xs-12 cesform-box">
                <div class="row">
                  <div class="license-info-box">
                    <div class="col-lg-6 col-xs-12">
                      <div class="license-info">
                        <p class="lic-no">Licence Number</p>
                        <h4><c:out value="${appCess.licenceNo}"/></h4>
                      </div>
                    </div>
                    <div class="col-lg-6 col-xs-12">
                      <div class="license-info">
                        <p class="serv-name">Service Name</p>
                        <h4><c:out value="${appCess.svcName}"/></h4>
                      </div>
                    </div>
                  </div>
                  <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci">
                    <div class="col-lg-12 col-xs-12">
                      <div class="table-gp tablebox">
                        <div class="topheader">
                          <c:if test="${appCessHci.hciName==null}">
                            <h4><c:out value="${appCessHci.hciCode}"/></h4>
                          </c:if>
                          <c:if test="${appCessHci.hciName!=null}">
                            <h4><c:out value="${appCessHci.hciName}"/> - <c:out
                                    value="${appCessHci.hciCode}"/></h4>
                          </c:if>
                          <p><c:out value="${appCessHci.hciAddress}"/></p>
                        </div>
                        <input type="hidden" name="sopEngineTabRef"
                               value="<%=process.rtStatus.getTabRef()%>">
                        <input type="hidden" name="crud_action_type" value="">
                        <input type="hidden" name="crud_action_value" value="">
                        <div class="form-group">
                          <label class="col-xs-12 col-md-6 control-label">Effective
                            Date</label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <fmt:formatDate value="${appCessHci.effectiveDate}"
                                            pattern="dd/MM/yyyy"/>
                          </div>
                          <div class="col-xs-8 col-sm-2 col-md-2">
                            <a class="btn-tooltip styleguide-tooltip"
                               data-toggle="tooltip" data-html="true"
                               title="${cess_ack002}">i</a>
                          </div>
                        </div>
                        <div class="form-group">
                          <label class="col-xs-12 col-md-6 control-label">Cessation
                            Reasons </label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <iais:select disabled="true"
                                         id="reasonId"
                                         name="reason"
                                         options="reasonOption"
                                         value="${appCessHci.reason}"
                                         cssClass="nice-select cessationReasons"/>
                          </div>
                        </div>
                        <div class="form-group" id="reason" style="display: none;">
                          <label class="col-xs-12 col-md-6 control-label ">Others</label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <iais:input needDisabled="true" type="text"
                                        name="otherReason"
                                        value="${appCessHci.otherReason}"/>
                          </div>
                        </div>
                        <div class="form-group">
                          <label class="col-xs-12 col-md-6 control-label">Patient's Records
                            will be transferred </label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <div class="form-check-gp">
                              <div class="row">
                                <div class="col-xs-12 col-md-3">
                                  <div class="form-check">
                                    <input class="form-check-input"
                                           id="radioYes"
                                           type="radio"
                                           name="patRadio"
                                           value="yes"
                                           <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                           onchange="javascirpt:changePatSelectCessFe();"
                                           aria-invalid="false" disabled>
                                    <label class="form-check-label"
                                           for="radioYes"><span
                                            class="check-circle <c:if test="${appCessHci.patNeedTrans ==true}">radio-disabled</c:if>"></span>Yes</label>
                                  </div>
                                </div>
                                <div class="col-xs-12 col-md-3">
                                  <div class="form-check">
                                    <input class="form-check-input"
                                           id="radioNo"
                                           type="radio"
                                           name="patRadio"
                                           value="no"
                                           <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                           onchange="javascirpt:changePatSelectCessFe();"
                                           aria-invalid="false" disabled>
                                    <label class="form-check-label"
                                           for="radioNo"><span
                                            class="check-circle <c:if test="${appCessHci.patNeedTrans ==false}">radio-disabled</c:if>"></span>No</label>
                                  </div>
                                </div>
                              </div>
                              <div><span
                                      id="error_patRadio"
                                      name="iaisErrorMsg"
                                      class="error-msg"></span>
                              </div>
                            </div>
                          </div>
                        </div>
                        <%--<div class="form-group" id="patYes" hidden>
                          <label class="col-xs-12 col-md-6 control-label">Who will
                            take over your patients' case records?</label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:select disabled="true"
                                         name="patientSelect"
                                         options="patientsOption"
                                         firstOption="Please Select"
                                         id="patientSelectId"
                                         onchange="javascirpt:changePatientCessFe(this.value);"
                                         value="${appCessHci.patientSelect}"
                                         cssClass="nice-select cessationTransfer"/>
                          </div>
                        </div>--%>
                        <%--<div class="form-group" id="patHciName"
                             hidden>
                          <label class="col-xs-12 col-md-6 control-label">HCI
                            Name / Code</label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input type="text" needDisabled="true"
                                        value="${appCessHci.patHciName}" maxLength="100"
                                        name="patHciName"/>
                          </div>
                        </div>--%>
                        <%--<div class="form-group" id="${num.count}hciNamePat${uid.count}" hidden>
                          <label class="col-xs-12 col-md-6 control-label">HCI
                            Name </label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <span><c:out value="${appCessHci.hciNamePat}"></c:out></span>
                          </div>
                        </div>--%>
                        <%--<div class="form-group" id="${num.count}hciCodePat${uid.count}" hidden>
                          <label class="col-xs-12 col-md-6 control-label">HCI Code </label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <span><c:out value="${appCessHci.hciCodePat}"></c:out></span>
                          </div>
                        </div>--%>
                       <%-- <div class="form-group" id="${num.count}hciAddressPat${uid.count}" hidden>
                          <label class="col-xs-12 col-md-6 control-label">HCI
                            Address </label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <span><c:out value="${appCessHci.hciAddressPat}"></c:out></span>
                          </div>
                        </div>--%>
                        <%--<div class="form-group" id="patRegNo"
                             hidden>
                          <label class="col-xs-12 col-md-6 control-label">Professional
                            Regn. No.</label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input needDisabled="true" cssClass="disabled"
                                        type="text"
                                        name="patRegNo"
                                        value="${appCessHci.patRegNo}"/>
                          </div>
                        </div>--%>
                        <%--<div class="form-group" id="patOthers"
                             hidden>
                          <label class="col-xs-12 col-md-6 control-label">Others</label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input needDisabled="true" type="text"
                                        name="patOthers"
                                        value="${appCessHci.patOthers}"/>
                          </div>
                        </div>--%>
                        <%--<div class="form-group"
                             id="patOthersMobileNo" hidden>
                          <label class="col-xs-12 col-md-6 control-label">Mobile
                            No.</label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input type="text" needDisabled="true"
                                        value="${appCessHci.mobileNo}"
                                        maxLength="8"
                                        name="patOthersMobileNo"/>
                          </div>
                        </div>--%>
                        <%--<div class="form-group"
                             id="patOthersEmailAddress" hidden>
                          <label class="col-xs-12 col-md-6 control-label">Email Address </label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input type="text" value="${appCessHci.emailAddress}"
                                        maxLength="66" needDisabled="true"
                                        name="patOthersEmailAddress"/>
                          </div>
                        </div>--%>
                        <%--<div class="form-group" id="patNo" hidden>
                          <label class="col-xs-12 col-md-6 control-label">Reason for
                            no patients' records transfer</label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input needDisabled="true" type="text"
                                        name="patNoRemarks"
                                        value="${appCessHci.patNoRemarks}"/>
                          </div>
                        </div>--%>
                        <div class="form-group" id="transferDetail" style="display: none;">
                          <label class="col-xs-12 col-md-6">Please provide details of why the transfer could not be done and the reasonable measures that the licensee has taken to ensure continuity of care for the affected patients. </label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <textarea name="transferDetail"  cols="30" rows="2" maxLength="1000" readonly="readonly">${appCessHci.transferDetail}</textarea>
                          </div>
                        </div>
                        <div class="form-group" id="transferredWhere" style="display: none;">
                          <label class="col-xs-12 col-md-6">Please state where the patient's records will be transferred to and where the licensee will store the patients' health records after cessation. </label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <textarea name="transferredWhere"  cols="30" rows="2" maxLength="1000" readonly="readonly">${appCessHci.transferredWhere}</textarea>
                          </div>
                        </div>
                       <%-- <div class="form-group" id="patNoConfirmID"
                             hidden>
                          <div class="col-xs-6 col-sm-4 col-md-6">
                            <div class="form-check">
                              <input disabled class="form-check-input" id="patNoConfirm"
                                     type="checkbox"
                                     name="patNoConfirm"
                                     <c:if test="${appCessHci.patNoConfirm != null}">checked</c:if>>
                              <label class="form-check-label" for="patNoConfirm"><span
                                      class="check-square"></span><iais:message key="CESS_DEC001"/></label>
                            </div>
                          </div>
                        </div>--%>
                        <c:if test="${isGrpLic}">
                          <div>
                            <div class="form-group">
                              <label class="col-xs-12 col-md-6 control-label">To
                                Cease </label>
                              <div class="col-xs-6 col-sm-4 col-md-3">
                                <div class="form-check">
                                  <input class="form-check-input"
                                         id="icon5checkboxSample"
                                         type="checkbox"
                                         name="whichTodo"
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
                        </c:if>
                        <c:if test="${!isGrpLic}">
                          <div style="display: none;">
                            <input class="form-check-input" type="text"
                                   name="whichTodo"
                                   value="${appCessHci.premiseId}">
                          </div>
                        </c:if>
                      </div>
                    </div>
                  </c:forEach>
                </div>
                <c:if test="${specLicInfo !=null}">
                <div class="table-responsive">
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
                    <c:forEach items="${specLicInfo}" var="spec" varStatus="index">
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
                </div>
                </c:if>
              </div>
              </c:forEach>
          </div>
          <div class="row">
            <div class="col-xs-12">
              <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                <%@include file="../common/declarations.jsp"%>
              </div>
            </div>
          </div>
          </form>
          <br/>
          <span style="padding-right: 10%" class="components">
                        <a href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em
                                class="fa fa-angle-left"></em> Back</a>
                    </span>
        </div>
      </div>
    </div>
  </div>
</div>
<style>
  /*.col-md-6 {*/
  /*    width: 35%;*/
  /*}*/

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

        if ($("#reasonId").val() == "CES001") {
            $("#reason").show();
        } else {
            $("#reason").hide();
        }
    }

    /*function changePatientCessFe() {
        if ($("#patientSelectId").val() == "CES004") {
            $("#patOthers").show();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthersMobileNo").show();
            $("#patOthersEmailAddress").show();
            $("#patRegNo").hide();
        } else if ($("#patientSelectId").val() == "CES005"&&$('#radioYes').is(':checked')) {
            $("#patHciName").show();
            $("#hciName").show();
            $("#patOthers").hide();
            $("#patOthersMobileNo").hide();
            $("#patOthersEmailAddress").hide();
            $("#patRegNo").hide();
            $("#hciNamePat").show();
            $("#hciCodePat").show();
            $("#hciAddressPat").show();
        } else if ($("#patientSelectId").val() == "CES006") {
            $("#patRegNo").show();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthers").hide();
            $("#patOthersMobileNo").hide();
            $("#patOthersEmailAddress").hide();

        }
    }*/

    function printCSPDF(){
        window.open("<%=request.getContextPath() %>/eservice/INTERNET/MohAppealPrint?whichPage=cessViewPage",'_blank');
    }

    function changePatSelectCessFe() {
        //changePatientCessFe();
        if ($('#radioYes').is(':checked')) {
            $("#patYes").show();
            $("#patNo").hide();
            $("#transferDetail").hide();
            $("#transferredWhere").show();
        } else if ($('#radioNo').is(':checked')) {
            $("#transferDetail").show();
            $("#transferredWhere").hide();
        }
    }

    $(document).ready(function () {
        changeReasonCessFe();
        //changePatientCessFe();
        changePatSelectCessFe();
    });

  /*  $(document).ready(function () {
        if ($('#radioNo').is(':checked')) {
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthers").hide();
            $("#patRegNo").hide();
            $("#div").hide();
        }
    });*/

    $(document).ready(function () {
        $('input[type="text"]').css('border-color', '#ededed');
        $('input[type="text"]').css('color', '#999');
        /*if ($('#radioNo').is(':checked')) {
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthers").hide();
            $("#patRegNo").hide();
            $("#div").hide();
        }*/
    });

</script>