<%@tag description="process rfi page fragment" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@attribute name="processingDecision" required="true" type="java.lang.String" %>
<%@attribute name="pageAppEditSelectDto" required="false" type="sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto" %>
<%@attribute name="remarksToApplicant" required="true" type="java.lang.String" %>

<iais-bsb:single-constant constantName="MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION" classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION"/>
<%--@elvariable id="MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION" type="java.lang.String"--%>
<%--@elvariable id="rfiAppSelectDto" type="sg.gov.moh.iais.egp.bsb.dto.rfi.RfiAppSelectDto"--%>
<div id="rfiSubContent" <c:if test="${MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION ne processingDecision}">style="display: none" </c:if>>
    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">Request for Information <span style="color: red">*</span></label>
        <div class="col-sm-7 col-md-5 col-xs-10">
            <p>
                <input type="checkbox" name="rfiSelectCheckbox" id="applicationSelect" value="application" <c:if test="${rfiAppSelectDto.applicationSelect}">checked="checked"</c:if>/>
                <span style="font-size: 16px">Application</span>
            </p>
            <p>
                <input type="checkbox" name="rfiSelectCheckbox" id="preInspectionChecklistSelect" value="preInspectionChecklist" <c:if test="${rfiAppSelectDto.preInspectionChecklistSelect}">checked="checked"</c:if>/>
                <span style="font-size: 16px">Pre-Inspection Checklist</span>
            </p>
            <span id="rfiErrorMsg" class="error-msg" style="display: none">Please select one</span>
        </div>
    </div>
    <div class="form-group">
        <label for="remarksToApplicant" class="col-xs-12 col-md-4 control-label">Remarks to Applicant <span style="color: red">*</span></label>
        <div class="col-sm-7 col-md-5 col-xs-10">
            <div class="input-group">
                <textarea id="remarksToApplicant" name="remarksToApplicant" cols="70" rows="7" maxlength="1500"><c:out value="${remarksToApplicant}"/></textarea>
                <span data-err-ind="remarksToApplicant" class="error-msg"></span>
            </div>
        </div>
        <div class="clear"></div>
    </div>
    <div id="rfiSelect">
        <iais:row>
            <iais:field value="Sections where changes are allowed" required="false"/>
            <iais:value width="10">
                <div id="rfiSelect">
                    <div id="selectDetail" style="margin-top: 13px">
                        <c:if test="${pageAppEditSelectDto ne null &&  pageAppEditSelectDto.selectedList ne null && not empty pageAppEditSelectDto.selectedList}">
                            <c:set var="selectedList" value="${pageAppEditSelectDto.selectedList}"/>
                            <ul>
                                <c:forEach var="item" items="${selectedList}">
                                    <li style="padding-left: 0;">${item}</li>
                                </c:forEach>
                            </ul></c:if>
                    </div>
                    <span data-err-ind="allowForChange" class="error-msg"></span>
                </div>
            </iais:value>
        </iais:row>
    </div>
</div>


