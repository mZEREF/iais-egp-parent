<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<c:set var="isRfi" value="${requestInformationConfig != null}"/>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="${isRFi ? '1' : '0'}"/>
<input id="isEditHiddenVal" type="hidden" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<div class="row">
    <div class="form-group">
        <div class="row control control-caption-horizontal">
            <div class="control-label col-md-5 col-xs-5">
                <label  class="control-label control-set-font control-font-label">
                    <div class="app-title">
                        <c:out value="${currStepName}"/>
                    </div>
                </label>
            </div>
            <div class="col-md-7 col-xs-7 text-right">
                <c:if test="${AppSubmissionDto.needEditController }">
                    <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                        <div class="app-font-size-16">
                            <a class="back" id="RfcSkip" href="javascript:void(0);">
                                Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                            </a>
                        </div>
                    </c:if>
                    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
                </c:if>
            </div>
        </div>
        <span class="error-msg" name="iaisErrorMsg" id="error_errorSECLDR"></span>
    </div>
    <div class="form-group">
        <h4><iais:message key="NEW_ACK030"/></h4>
    </div>
</div>

<div class="row normal-label form-horizontal">
    <c:choose>
    <c:when test="${empty sectionLeaderList && sectionLeaderConfig.mandatoryCount > 1}">
        <c:set var="pageLength" value="${sectionLeaderConfig.mandatoryCount}"/>
    </c:when>
    <c:when test="${empty sectionLeaderList}">
        <c:set var="pageLength" value="1"/>
    </c:when>
    <c:when test="${sectionLeaderConfig.mandatoryCount > sectionLeaderList.size() }">
        <c:set var="pageLength" value="${sectionLeaderConfig.mandatoryCount}"/>
    </c:when>
    <c:otherwise>
        <c:set var="pageLength" value="${sectionLeaderList.size()}"/>
    </c:otherwise>
    </c:choose>
    <input type="hidden" name="slLength" value="${pageLength}" />
    <input type="hidden" name="minCoutConfig" value="status" />
    <c:forEach begin="0" end="${pageLength - 1}" step="1" varStatus="slStat">
        <c:set var="index" value="${slStat.index}" />
        <c:set var="sectionLeader" value="${sectionLeaderList[index]}"/>
        <%@include file="sectionLeaderDetail.jsp" %>
    </c:forEach>

    <c:if test="${!isRfi}">
        <div class="col-md-12 col-xs-12 addSectionLeaderDiv">
            <span class="addSectionLeaderBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Another ${singleName}</span>
            </span>
        </div>
    </c:if>
</div>
<%@include file="sectionLeaderFun.jsp" %>

