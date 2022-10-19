<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="isis" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<script type="text/javascript"
        src="<%=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT%>js/file-upload.js"></script>
<input type="hidden" id="isEditHiddenVal" class="personnel-content-edit" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>
<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<style>
    .addDpoDiv{
        margin-bottom: 20px;
    }
    .panel-main-content{
        margin-top: 20px;
    }
</style>
<div class="row form-horizontal special-person">
    <input type="hidden" id="curr" name="currentSvcCode" value="${currentSvcCode}"/>
    <c:choose>
        <c:when test="${'BLB' ==currentSvcCode}">
            <h4>The blood donation centre and/or mobile donation drive is/are under the supervision of</h4>
        </c:when>
        <c:when test="${'TSB' ==currentSvcCode}">
            <strong style="font-size: 20px;">Laboratory Director (Cord Blood Banking Service)</strong>
            <h4><iais:message key="NEW_ACK023"/></h4>
        </c:when>
        <c:when test="${'NMI' ==currentSvcCode}">
            <h4>Please appoint at least one person for each role listed under "Service Personnel".</h4>
        </c:when>
        <c:when test="${'NMA' ==currentSvcCode}">
            <h4>The Nuclear Medicine Assay Service have the following personnel that satisfy the minimum
                requirements at all times</h4>
        </c:when>
    </c:choose>
    <div class="personnel-edit">
        <c:if test="${AppSubmissionDto.needEditController }">
            <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && !isRfi}">
                <div class="text-right app-font-size-16">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span>&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
                </div>
            </c:if>
            <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
        </c:if>
    </div>
    <c:set var="arPractitionerCount" value="${svcPersonnelDto.arPractitionerCount}"/>
    <c:set var="nurseCount" value="${svcPersonnelDto.nurseCount}"/>
    <c:set var="embryologistMinCount" value="${svcPersonnelDto.embryologistMinCount}"/>
    <c:set var="specialCount" value="${svcPersonnelDto.specialCount}"/>
    <c:set var="normalCount" value="${svcPersonnelDto.normalCount}"/>


    <c:if test="${arPractitionerCount != 0}">
        <div class="panel-main-content" id="arContent">
            <iais:row>
                <div class="col-xs-12">
                    <p class="app-title" ><c:out value="Service Personnel"/></p>
                </div>
            </iais:row>
            <input type="hidden" class="maxCount" value="${arPersonnelMax}"/>
            <c:forEach begin="0" end="${arPractitionerCount - 1}" step="1" varStatus="status">
                <c:set value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER}" var="logo"/>
                <c:set var="index" value="${status.index}"/>
                <c:set var="appSvcPersonnelDto" value="${svcPersonnelDto.arPractitionerList[index]}"/>
                <%@include file="servicePersonnelArDetail.jsp" %>
            </c:forEach>

            <div class="form-group col-md-12 col-xs-12 addDpoDiv">
         <span class="addListBtn" style="color:deepskyblue;cursor:pointer;"> <span style="">+ Add Another AR Practitioner</span> </span>
            </div>
<%--             TODO --%>
            <iais:row>
                <iais:field width="5" value="Total Number of AR Practitioner"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="0"/>
<%--                    <span id="arNumber">0<span>--%>
                </iais:value>
            </iais:row>
        </div>
    </c:if>


    <c:if test="${nurseCount != 0}">
        <div class="panel-main-content">
            <input type="hidden" class="maxCount" value="${nuPersonnelMax}"/>
            <c:forEach begin="0" end="${nurseCount - 1}" step="1" varStatus="status">
                <c:set value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES}" var="logo"/>
                <c:set var="index" value="${status.index}"/>
                <c:set var="appSvcPersonnelDto" value="${svcPersonnelDto.nurseList[index]}"/>
                <%@include file="servicePersonnelNurse.jsp" %>
            </c:forEach>
         <div class="col-md-12 col-xs-12 addDpoDiv">
             <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">
                 <span style="">+ Add Another Nurse</span>
             </span>
         </div>
            <div>
                <div class="file-upload-gp nonHcsaRowDiv">
                <span><a href="${pageContext.request.contextPath}/co-non-hcsa-template2"
                         style="text-decoration: none;color:deepskyblue;cursor:pointer;">
                    List of Nurses Template</a>
                  </span>
                    <div class="uploadFileShowDiv" id="uploadFileShowId">
                    </div>
                    <div class="col-xs-12 uploadFileErrorDiv">
                        <span id="error_uploadFileError" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                    <br/>
                    <a class="btn btn-file-upload file-upload btn-secondary">Upload List of Nurses</a>
                </div>
            </div>
            <div id="selectFileDiv"></div>
        </div>
    </c:if>


    <c:if test="${embryologistMinCount != 0}">
        <div class="panel-main-content">
            <input type="hidden" class="maxCount" value="${emPersonnelMax}"/>
            <c:forEach begin="0" end="${embryologistMinCount - 1}" step="1" varStatus="status">
                <c:set value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST}" var="logo"/>
                <c:set var="index" value="${status.index}"/>
                <c:set var="appSvcPersonnelDto" value="${svcPersonnelDto.embryologistList[index]}"/>
                <%@include file="servicePersonnelEmbryologist.jsp" %>
            </c:forEach>
            <div class="col-md-12 col-xs-12 addDpoDiv">
         <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">
         <span style="">+ Add Another Embryologist </span>
         </span>
            </div>
        </div>
    </c:if>



    <c:if test="${normalCount != 0}">
        <div class="panel-main-content">
            <input type="hidden" class="maxCount" value="${othersPersonnelMax}"/>
            <c:forEach begin="0" end="${normalCount - 1}" step="1" varStatus="status">
                <c:set value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS}" var="logo"/>
                <c:set var="index" value="${status.index}"/>
                <c:set var="appSvcPersonnelDto" value="${svcPersonnelDto.normalList[index]}"/>
                <%@include file="servicePersonnelBlood.jsp" %>
            </c:forEach>
            <div class="form-group col-md-12 col-xs-12 addDpoDiv">
         <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">
         <span style="">+ Add Another Service Personnel</span>
         </span>
            </div>
        </div>
    </c:if>



    <c:if test="${specialCount != 0}">
        <div class="panel-main-content">
            <input type="hidden" class="maxCount" value="${spePersonnelMax}"/>
            <c:forEach begin="0" end="${specialCount - 1}" step="1" varStatus="status">
                <c:set var="index" value="${status.index}"/>
                <c:set value="${ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS}" var="logo"/>
                <c:set var="appSvcPersonnelDto" value="${svcPersonnelDto.specialList[index]}"/>
                <%@include file="servicePersonnelDetail.jsp" %>
            </c:forEach>
            <div class="form-group col-md-12 col-xs-12 addDpoDiv">
         <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">
         <span style="">+ Add Another Service Personnel</span>
         </span>
            </div>
        </div>
    </c:if>


    <c:if test="${empty psnContent}">
        <c:set var="psnContent" value="personnel-content"/>
    </c:if>

</div>
<%@include file="servicePersonnelOthers.jsp"%>
<%@include file="/WEB-INF/jsp/iais/application/common/prsLoad.jsp" %>
<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<%@include file="servicePersonnelFun.jsp" %>









