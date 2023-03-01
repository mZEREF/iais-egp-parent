<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div style="margin-top: 99px;
        width: 90%;
        padding-left: 10px;
        padding-right: 10px;">
    <div class="container">
        <div class="row">
            <div class="row">
                <div class="col-lg-12 col-xs-12 cesform-box">
                    <div class="license-info-box">
                        <div class="col-lg-6 col-xs-12">
                            <div class="license-info">
                                <p class="lic-no">Licence Number</p>
                                <h4><c:out value="${confirmDto.licenceNo}"/></h4>
                            </div>
                        </div>
                        <div class="col-lg-6 col-xs-12">
                            <div class="license-info">
                                <p class="serv-name">Service Name</p>
                                <h4><c:out value="${confirmDto.svcName}"/></h4>
                            </div>
                        </div>
                    </div>
                    <c:forEach items="${confirmDto.appCessHciDtos}" var="appCessHci">
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

                                <iais:section title="" id="potentialAuditableHCIs">
                                    <iais:row>
                                        <iais:field value="Effective Date"/>
                                        <iais:value width="7">
                                            <fmt:formatDate value="${appCessHci.effectiveDate}"
                                                            pattern="dd/MM/yyyy"/>
                                        </iais:value>
                                        <iais:value>
                                            <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip"
                                               data-html="true"
                                               data-original-title="${cess_ack002}">i</a>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Cessation Reasons"/>
                                        <iais:value width="7">
                                            <iais:select disabled="true"
                                                         id="reasonId"
                                                         name="reason"
                                                         options="reasonOption"
                                                         value="${appCessHci.reason}"/>
                                        </iais:value>
                                    </iais:row>
                                    <div id="reason" style="display: none;">
                                        <iais:row>
                                            <iais:field value="Others"/>
                                            <iais:value width="7">
                                                <iais:input needDisabled="true" type="text" needReadonly="true"
                                                            name="otherReason"
                                                            value="${appCessHci.otherReason}"></iais:input>
                                            </iais:value>
                                        </iais:row>
                                    </div>
                                    <iais:row>
                                        <iais:field value="Patients' Record will be transferred"/>
                                        <iais:value>
                                            <div class="col-xs-12 col-sm-4 col-md-3">
                                                <div class="form-check-gp">
                                                    <div class="row">
                                                        <div class="col-xs-12 col-md-4">
                                                            <div class="form-check">
                                                                <input class="form-check-input"
                                                                       type="radio"
                                                                       name="patRadio"
                                                                       value="yes"
                                                                       id="radioYes"
                                                                       <c:if test="${appCessHci.patNeedTrans ==true}">checked</c:if>
                                                                       aria-invalid="false"
                                                                       disabled>
                                                                <label class="form-check-label"
                                                                       for="radioYes"><span
                                                                        class="check-circle"></span>Yes</label>
                                                            </div>
                                                        </div>
                                                        <div class="col-xs-12 col-md-4">
                                                            <div class="form-check">
                                                                <input class="form-check-input" type="radio"
                                                                       name="patRadio"
                                                                       value="no"
                                                                       id="radioNo"
                                                                       <c:if test="${appCessHci.patNeedTrans == false}">checked</c:if>
                                                                       aria-invalid="false"
                                                                       disabled>
                                                                <label class="form-check-label"
                                                                       for="radioNo"><span
                                                                        class="check-circle"></span>No</label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </iais:value>
                                    </iais:row>

                                    <div class="form-group" id="transferDetail" style="display: none;">
                                        <label class="col-xs-12 col-md-4">Please provide details of why the transfer could not be done and the reasonable measures that the licensee has taken to ensure continuity of care for the affected patients. </label>
                                        <div class="col-xs-6 col-sm-4 col-md-3">
                                            <textarea name="transferDetail"  cols="30" rows="2" maxLength="1000" readonly="readonly">${appCessHci.transferDetail}</textarea>
                                        </div>
                                    </div>
                                    <div class="form-group" id="transferredWhere" style="display: none;">
                                        <label class="col-xs-12 col-md-4">Please state where the patient's records will be transferred to and where the licensee will store the patients' health records after cessation. </label>
                                        <div class="col-xs-6 col-sm-4 col-md-3">
                                            <textarea name="transferredWhere"  cols="30" rows="2" maxLength="1000" readonly="readonly">${appCessHci.transferredWhere}</textarea>
                                        </div>
                                    </div>
                                </iais:section>
                            </div>
                        </div>
                    </c:forEach>

                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    function changeReason() {
        if ($("#reasonId").val() == "CES001") {
            $("#reason").show();
        } else {
            $("#reason").hide();
        }
    }


    function changePatSelect() {
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
        // changePatient();
        changeReason();
        changePatSelect();
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
</script>