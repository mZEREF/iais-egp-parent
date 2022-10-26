<%@tag description="process rfi page fragment" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@attribute name="processingDecision" required="true" type="java.lang.String" %>
<%@attribute name="pageAppEditSelectDto" required="false" type="sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto" %>
<%@attribute name="commentsToApplicant" required="true" type="java.lang.String" %>

<iais-bsb:single-constant constantName="MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION" classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION"/>
<%--@elvariable id="MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION" type="java.lang.String"--%>
<div id="rfiSubContent" <c:if test="${MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION ne processingDecision}">style="display: none" </c:if>>
    <div id="rfiSelect">
        <iais:row>
            <iais:field value="Sections allowed to change" required="false"/>
            <iais:value width="10">
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
            </iais:value>
        </iais:row>
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


