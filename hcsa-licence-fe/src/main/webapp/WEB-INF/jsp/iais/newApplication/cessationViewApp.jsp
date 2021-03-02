<div class="main-content">
  <div class="container">
    <div class="row">
      <div class="col-xs-12">
        <div class="instruction-content center-content">
          <br/>
          <div class="row">
            <form id="mainForm" class="form-horizontal"
                  enctype="multipart/form-data"
                  action=<%=process.runtime.continueURL()%>>
              <c:forEach items="${confirmDtos}" var="appCess" varStatus="num">
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
                  <c:forEach items="${appCess.appCessHciDtos}" var="appCessHci" varStatus="uid">
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
                            Date <span style="color: red">*</span></label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <fmt:formatDate value="${appCessHci.effectiveDate}"
                                            pattern="dd/MM/yyyy"/>
                          </div>
                          <div class="col-xs-8 col-sm-2 col-md-2">
                            <a class="btn-tooltip styleguide-tooltip"
                               data-toggle="tooltip" data-html="true"
                               title="&lt;p&gt;The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.&lt;/p&gt;">i</a>
                          </div>
                        </div>
                        <div class="form-group">
                          <label class="col-xs-12 col-md-6 control-label">Cession
                            Reasons <span style="color: red">*</span></label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <iais:select disabled="true"
                                         id="reasonId"
                                         name="reason"
                                         options="reasonOption"
                                         value="${appCessHci.reason}"
                                         cssClass="nice-select cessationReasons"/>
                          </div>
                        </div>
                        <div class="form-group" id="reason" hidden>
                          <label class="col-xs-12 col-md-6 control-label ">Others <span
                                  style="color: red">*</span></label>
                          <div class="col-xs-12 col-sm-4 col-md-3">
                            <iais:input needDisabled="true" type="text"
                                        name="otherReason"
                                        value="${appCessHci.otherReason}"/>
                          </div>
                        </div>
                        <div class="form-group">
                          <label class="col-xs-12 col-md-6 control-label">Ratient's Record
                            will be transferred <span style="color: red">*</span></label>
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
                                           onchange="javascirpt:changePatSelectCessFe(this.value);"
                                           aria-invalid="false" disabled>
                                    <label class="form-check-label"
                                           for="radioYes"><span
                                            class="check-circle"></span>Yes</label>
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
                                           onchange="javascirpt:changePatSelectCessFe(this.value);"
                                           aria-invalid="false" disabled>
                                    <label class="form-check-label"
                                           for="radioNo"><span
                                            class="check-circle"></span>No</label>
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
                        <div class="form-group" id="patYes" hidden>
                          <label class="col-xs-12 col-md-6 control-label">Who will
                            take over your patients' case records?<span
                                    style="color: red">*</span></label>
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
                        </div>
                        <div class="form-group" id="patHciName"
                             hidden>
                          <label class="col-xs-12 col-md-6 control-label">HCI
                            Name <span style="color: #ff0000">*</span></label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input type="text" needDisabled="true"
                                        value="${appCessHci.patHciName}" maxLength="100"
                                        name="patHciName"/>
                          </div>
                        </div>
                        <div class="form-group" id="hciName">
                          <label class="col-xs-12 col-md-6 control-label"></label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <span><c:out value="${appCessHci.hciNamePat}"></c:out></span>
                            <span><c:out value="${appCessHci.hciAddressPat}"></c:out></span>
                          </div>
                        </div>
                        <div class="form-group" id="patRegNo"
                             hidden>
                          <label class="col-xs-12 col-md-6 control-label">Professional
                            Regn. No. <span style="color: #ff0000">*</span></label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input needDisabled="true" cssClass="disabled"
                                        type="text"
                                        name="patRegNo"
                                        value="${appCessHci.patRegNo}"/>
                          </div>
                        </div>
                        <div class="form-group" id="patOthers"
                             hidden>
                          <label class="col-xs-12 col-md-6 control-label">Others <span
                                  style="color: red">*</span></label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input needDisabled="true" type="text"
                                        name="patOthers"
                                        value="${appCessHci.patOthers}"/>
                          </div>
                        </div>
                        <div class="form-group"
                             id="patOthersMobileNo" hidden>
                          <label class="col-xs-12 col-md-6 control-label">Mobile
                            No. <span style="color: red">*</span></label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input type="text" needDisabled="true"
                                        value="${appCessHci.mobileNo}"
                                        maxLength="8"
                                        name="patOthersMobileNo"/>
                          </div>
                        </div>
                        <div class="form-group"
                             id="patOthersEmailAddress" hidden>
                          <label class="col-xs-12 col-md-6 control-label">Email Address <span style="color: red">*</span></label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input type="text" value="${appCessHci.emailAddress}"
                                        maxLength="66" needDisabled="true"
                                        name="patOthersEmailAddress"/>
                          </div>
                        </div>
                        <div class="form-group" id="patNo" hidden>
                          <label class="col-xs-12 col-md-6 control-label">Reason for
                            no patients' records transfer <span style="color: red">*</span></label>
                          <div class="col-xs-6 col-sm-4 col-md-3">
                            <iais:input needDisabled="true" type="text"
                                        name="patNoRemarks"
                                        value="${appCessHci.patNoRemarks}"/>
                          </div>
                        </div>
                        <div class="form-group" id="patNoConfirmID"
                             hidden>
                          <div class="col-xs-6 col-sm-4 col-md-6">
                            <div class="form-check">
                              <input disabled class="form-check-input" id="patNoConfirm"
                                     type="checkbox"
                                     name="patNoConfirm"
                                     <c:if test="${appCessHci.patNoConfirm != null}">checked</c:if>>
                              <label class="form-check-label" for="patNoConfirm"><span
                                      class="check-square"></span><iais:message key="CESS_DEC001"/><span style="color: red">*</span></label>
                            </div>
                          </div>
                        </div>
                        <c:if test="${isGrpLic}">
                          <div>
                            <div class="form-group">
                              <label class="col-xs-12 col-md-6 control-label">To
                                Cease <span style="color: red">*</span></label>
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
                          <div hidden>
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
                  <c:forEach items="${specLicInfo}" var="map">
                    <c:set var="licNo" value="${map.key}"></c:set>
                    <c:if test="${appCess.licenceNo==licNo}">
                      <div><h4>The following specified healthcare services will also be ceased as
                        their
                        underlying licensable healthcare service(s) is/are listed above.</h4>
                      </div>
                      <table class="table-gp tablebox">
                        <tr style="text-align:center">
                          <th style="text-align:center;width: 0%">S/N</th>
                          <th style="text-align:center;width: 25%">Special Licensable Service
                            Licence No.
                          </th>
                          <th style="text-align:center;width: 25%">Special Licensable Service
                            Name
                          </th>
                          <th style="text-align:center;width: 25%">Base Service Licence No.
                          </th>
                          <th style="text-align:center;width: 25%">Base Service Name</th>
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
                </c:if>
              </div>
              </c:forEach>

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
  .col-md-5 {
    width: 31%;
  }

  /*.col-md-6 {*/
  /*    width: 35%;*/
  /*}*/

  #effectiveDate {
    margin-bottom: 0px;
  }

  input[type='text'] {
    margin-bottom: 0px;
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

    function changePatientCessFe() {
        if ($("#patientSelectId").val() == "CES004") {
            $("#patOthers").show();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthersMobileNo").show();
            $("#patOthersEmailAddress").show();
            $("#patRegNo").hide();
        } else if ($("#patientSelectId").val() == "CES005") {
            $("#patHciName").show();
            $("#hciName").show();
            $("#patOthers").hide();
            $("#patOthersMobileNo").hide();
            $("#patOthersEmailAddress").hide();
            $("#patRegNo").hide();
        } else if ($("#patientSelectId").val() == "CES006") {
            $("#patRegNo").show();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthers").hide();
            $("#patOthersMobileNo").hide();
            $("#patOthersEmailAddress").hide();

        }
    }

    function changePatSelectCessFe() {
        changePatientCessFe();
        if ($('#radioYes').is(':checked')) {
            $("#patYes").show();
            $("#patNo").hide();
            $("#patNoConfirmID").hide();
        } else if ($('#radioNo').is(':checked')) {
            $("#patNo").show();
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthers").hide();
            $("#patRegNo").hide();
            $("#patNoConfirmID").show();
        }
    }

    $(document).ready(function () {
        changeReasonCessFe();
        changePatientCessFe();
        changePatSelectCessFe();
    });

    $(document).ready(function () {

        if ($('#radioNo').is(':checked')) {
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthers").hide();
            $("#patRegNo").hide();
            $("#div").hide();
        }
    });

    $(document).ready(function () {
        $('input[type="text"]').css('border-color', '#ededed');
        $('input[type="text"]').css('color', '#999');
        if ($('#radioNo').is(':checked')) {
            $("#patYes").hide();
            $("#patHciName").hide();
            $("#hciName").hide();
            $("#patOthers").hide();
            $("#patRegNo").hide();
            $("#div").hide();
        }
    });

</script>