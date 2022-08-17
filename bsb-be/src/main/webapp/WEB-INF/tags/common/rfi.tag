<%@tag description="process rfi page fragment" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="processingDecision" required="true" type="java.lang.String" %>
<%@attribute name="rfiProcessDto" required="true" type="sg.gov.moh.iais.egp.bsb.dto.rfi.RfiProcessDto" %>
<%@attribute name="commentsToApplicant" required="true" type="java.lang.String" %>

<iais-bsb:single-constant constantName="MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION" classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION"/>
<%--@elvariable id="MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION" type="java.lang.String"--%>
<div id="rfiSubContent" <c:if test="${MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION eq processingDecision}">style="display: none" </c:if>>
    <div class="form-group">
        <label class="col-xs-12 col-md-4 control-label">Sections allowed to change</label>
        <div class="col-sm-7 col-md-5 col-xs-10">
            <%--this is not implement--%>
            <p>Facility Information</p>
        </div>
        <div class="clear"></div>
    </div>
    <div class="form-group">
        <label for="commentsToApplicant" class="col-xs-12 col-md-4 control-label">Comments to Applicant <span style="color: red">*</span></label>
        <div class="col-sm-7 col-md-5 col-xs-10">
            <div class="input-group">
                <textarea id="commentsToApplicant" name="commentsToApplicant" cols="70" rows="7" maxlength="1000"><c:out value="${commentsToApplicant}"/></textarea>
                <span data-err-ind="commentsToApplicant" class="error-msg"></span>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>


