<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>
<%@ include file="/include/formHidden.jsp" %>


<iais:body >

    <iais:section title="">
        <div><h2><strong>Section A (HCI Details)</strong></h2></div>
        <iais:row>
            <iais:field value="Licence No."/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.licenceNo}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Service Name"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.serviceName}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="HCI Code"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.hciCode}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="HCI Name"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.hciName}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="HCI Address"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.hciAddress}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Licensee Name"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.licenseeName}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Principal Officers"/>
            <iais:value width="18">
                <c:if test="${insRepDto.principalOfficers != null && not empty insRepDto.principalOfficers}">
                    <p><c:forEach items="${insRepDto.principalOfficers}" var="poName">
                        <c:out value="${poName}"/><br>
                    </c:forEach></p>
                </c:if>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Subsumed Services"/>
            <iais:value width="18">
                <c:if test="${insRepDto.subsumedServices != null && not empty insRepDto.subsumedServices}">
                    <c:forEach var="service" items="${insRepDto.subsumedServices}">
                        <p><c:out value="${service}"></c:out></p>
                    </c:forEach>
                </c:if>
            </iais:value>
        </iais:row>
    </iais:section>

    <iais:section title="">
        <div><h2><strong>Section B (Type of Inspection)</strong></h2></div>
        <iais:row>
            <iais:field value="Date of Inspection"/>
            <iais:value width="18">
                <p><fmt:formatDate value="${insRepDto.inspectionDate}" pattern="dd/MM/yyyy"></fmt:formatDate></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Time of Inspection"/>
            <iais:value width="18">
                <p><fmt:formatDate value="${insRepDto.inspectionStartTime}"
                                   pattern="dd/MM/yyyy"></fmt:formatDate>-
                    <fmt:formatDate value="${insRepDto.inspectionEndTime}"
                                    pattern="dd/MM/yyyy"></fmt:formatDate></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Reason for Visit"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.reasonForVisit}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Inspected By"/>
            <iais:value width="18">
                <c:if test="${insRepDto.inspectors != null && not empty insRepDto.inspectors}">
                <c:forEach items="${insRepDto.inspectors}" var="inspector" varStatus="status">
                <p><c:out value="${inspector}"></c:out></p>
            </c:forEach>
            </c:if>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Reported By"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.reportedBy}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Report Noted By"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.reportNoteBy}"/></p>
            </iais:value>
        </iais:row>
    </iais:section>

    <iais:section title="">
        <div><h2><strong>Section C (Inspection Findings)</strong></h2></div>
        <div><h3><strong>Part I: Inspection Checklist</strong></h3></div>
        <iais:row>
            <iais:field value="Checklist Used"/>
            <iais:value>
                <p><c:out value="${insRepDto.serviceName}"/></p>
                <c:if test="${insRepDto.commonCheckList != null}">
                <div class="tab-pane" id="ServiceInfo" role="tabpanel">
                    <c:forEach var ="cdto" items ="${insRepDto.subTypeCheckList.fdtoList}" varStatus="status">
                        <h3>${cdto.subType}</h3>
                        <div class="table-gp">
                            <c:forEach var ="section" items ="${cdto.sectionDtoList}">
                                <br/>
                                <h4><c:out value="${section.sectionName}"></c:out></h4>
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>No.</th>
                                        <th>Regulation Clause Number</th>
                                        <th>Item</th>
                                        <th>Rectified</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                        <tr>
                                            <td class="row_no">${(status.index + 1) }</td>
                                            <td>${item.incqDto.regClauseNo}</td>
                                            <td>${item.incqDto.checklistItem}</td>
                                            <c:set value = "${cdto.subName}${item.incqDto.sectionName}${item.incqDto.itemId}" var = "ckkId"/>
                                            <td>
                                                <div id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>ck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                    <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>rec" id="<c:out value="${cdto.subName}${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>rec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec" disabled/>
                                                </div>
                                                <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameSub}${item.incqDto.itemId}" var = "err"/>
                                                <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            </iais:value>
        </iais:row>
        <div><h3><strong>Part II: Findings</strong></h3></div>
        <iais:row>
            <iais:field value="Remarks"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.taskRemarks}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Marked for Audit"/>
            <iais:value width="18">
                <c:if test="${insRepDto.markedForAudit}">
                <p>Yes <fmt:formatDate value="${insRepDto.tcuDate}" pattern="dd/MM/yyyy"></fmt:formatDate></p>
            </c:if>
                <c:if test="${!insRepDto.markedForAudit}">
                    <p>No</p>
                </c:if>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Recommended Best Practices"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.bestPractice}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Non-Compliances"/>
            <iais:value width="18">
                <c:if test="${insRepDto.ncRegulation != null && not empty insRepDto.ncRegulation}">
                <table class="table">
                <thead>
                <tr>
                    <th>SN</th>
                    <th>Checklist Item</th>
                    <th>Regulation Clause</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${insRepDto.ncRegulation}" var="ncRegulations"
                           varStatus="status">
                    <tr>
                        <td>
                    <p><c:out value="${status.count}"></c:out></p>
                    </td>
                    <td>
                        <p><c:out value="${ncRegulations.nc}"></c:out></p>
                    </td>
                    <td>
                        <p><c:out value="${ncRegulations.regulation}"></c:out></p>
                    </td>
                    </tr>
                </c:forEach>
                </tbody>
                </table>
            </c:if>
                <c:if test="${insRepDto.ncRegulation == null}">
                    <p>0</p>
                </c:if>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Status"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.status}"/></p>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Risk Level" required="true"/>
            <iais:value width="18">
                <iais:select name="riskLevel" options="riskLevelOptions" firstOption="Please select" value="${appPremisesRecommendationDto.riskLevel}"/>
                <span id="error_riskLevel" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Subsumed Services"/>
            <iais:value width="18">
                <c:if test="${insRepDto.subsumedServices != null && not empty insRepDto.subsumedServices}">
                    <c:forEach var="service" items="${insRepDto.subsumedServices}">
                        <p><c:out value="${service}"></c:out></p>
                    </c:forEach>
                </c:if>
            </iais:value>
        </iais:row>
    </iais:section>

    <iais:section title="">
        <div><h2><strong>Section D (Rectification)</strong></h2></div>
        <iais:row>
            <iais:field value="Rectified"/>
            <iais:value width="18">
                <c:if test="${insRepDto.ncRectification != null}">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>SN</th>
                            <th>Checklist Item</th>
                            <th>Rectified?</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${insRepDto.ncRectification}" var="ncRectification" varStatus="status">
                            <tr>
                                <td>
                                    <p><c:out value="${status.count}"></c:out></p>
                                </td>
                                <td>
                                    <p><c:out value="${ncRectification.nc}"></c:out></p>
                                </td>
                                <td>
                                    <p><c:out value="${ncRectification.rectified}"></c:out></p>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${insRepDto.ncRectification == null}">
                    <p>NA</p>
                </c:if>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Remarks" required="true"/>
            <iais:value width="18">
                <textarea style="resize:none"  name="remarks" cols="50" rows="6" title="content" maxlength="8000"><c:out value="${appPremisesRecommendationDto.remarks}"/></textarea>
                <br/>
                <span id="error_remarks" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Rectified Within KPI?"/>
            <iais:value width="18">
                <p><c:out value="${insRepDto.rectifiedWithinKPI}"/></p>
            </iais:value>
        </iais:row>
    </iais:section>

    <iais:section title="">
        <div><h2><strong>Section E (Recommendations)</strong></h2></div>
        <iais:row>
            <iais:field value="Recommendation" required="true"/>
            <iais:value width="18">
                        <iais:select name="recommendation" options="recommendationOption" firstOption="Please select" value="${appPremisesRecommendationDto.recommendation}" onchange="javascirpt:changeRecommendation(this.value);"/>
                <br/>
                <span id="error_remarks" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
        </iais:row>
        <div id = "period" hidden>
        <iais:row>
            <iais:field value="Period" required="true"/>
            <iais:value width="18">
                        <iais:select name="periods" options="riskOption" firstOption="Please select" onchange="javascirpt:changePeriod(this.value);" value="${appPremisesRecommendationDto.period}"/>
                        <span id="error_period" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
        </iais:row>
        </div>
        <div id="selfPeriod" hidden>
        <iais:row>
            <iais:field value="Other Period" required="true"/>
            <iais:value width="18">
                <input id=recomInNumber type="text" name="number" value="${appPremisesRecommendationDto.recomInNumber}">
                <span id="error_recomInNumber" name="iaisErrorMsg" class="error-msg"></span>
                <iais:select id="chronoUnit" name="chrono" options="chronoOption" firstOption="Please select" value="${appPremisesRecommendationDto.chronoUnit}"/>
                <span id="error_chronoUnit" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
        </iais:row>
        </div>
    </iais:section>

    <iais:section title="">
        <div><h2><strong>Section F (After Action)</strong></h2></div>
        <iais:row>
            <iais:field value="Follow up Action"/>
            <iais:value width="18">
                <textarea name="followUpAction" cols="50" rows="6" title="content" maxlength="8000"><c:if test="${appPremisesRecommendationDto.followUpAction == null}"><c:out value="${followRemarks}"/></c:if><c:if test="${appPremisesRecommendationDto.followUpAction != null}"><c:out value="${appPremisesRecommendationDto.followUpAction}"/></c:if></textarea>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field value="To Engage Enforcement?"/>
            <iais:value width="18">
                <input type="checkbox" id="enforcement" name="engageEnforcement" onchange="javascirpt:changeEngage();" <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">checked</c:if> >
            </iais:value>
        </iais:row>
        <div id="engageRemarks" hidden>
        <iais:row>
            <iais:field value="Enforcement Remarks" required="true"/>
            <iais:value width="18">
                <textarea name="enforcementRemarks" cols="50" rows="6" title="content" MAXLENGTH="4000"><c:out value="${appPremisesRecommendationDto.engageEnforcementRemarks}"/></textarea>
                <br/>
                <span id="error_enforcementRemarks" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
        </iais:row>
        </div>
    </iais:section>

</iais:body>

<%@include file="/include/validation.jsp" %>

<script type="text/javascript">

    function insRepsubmit() {
        $("#mainForm").submit();
    }

    function changePeriod(obj) {
        if (obj == "Others") {
            document.getElementById("selfPeriod").style.display = "";
            $("#selfPeriod").show();
        } else {
            document.getElementById("selfPeriod").style.display = "none";
        }
    }

    function changeRecommendation(obj) {
        if (obj == "Approved") {
            document.getElementById("period").style.display = "";
            $("#period").show();
        } else {
            document.getElementById("period").style.display = "none";
        }
    }


    function changeEngage() {
        if ($('#enforcement').is(':checked')) {
            document.getElementById("engageRemarks").style.display = "";
            $("#engageRemarks").show();
        } else {
            document.getElementById("engageRemarks").style.display = "none";
        }
    }


    $(document).ready(function () {
        if ($("#recommendation").val() == "Approved") {
            changeRecommendation("Approved");
        }
        if ($("#periods").val() == "Others") {
            changePeriod("Others");
        }
        if ($('#tcuNeeded').is(':checked')) {
            $("#tcuDate").show();
        }
        if ($('#enforcement').is(':checked')) {
            $("#engageRemarks").show();
        }
    });

</script>

