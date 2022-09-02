<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="isis" uri="http://www.ecq.com/iais" %>
<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script type="text/javascript"
        src="<%=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT%>js/file-upload.js"></script>

<c:set var="isRfi" value="${requestInformationConfig != null}"/>
<%--<input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>--%>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit"
       value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>
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
    <iais:row>
        <div class="col-xs-12">
            <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
        </div>
    </iais:row>
    <c:set var="editControl"
           value="${(!empty AppSvcPersonnelDtoList && AppSubmissionDto.needEditController) || !AppSubmissionDto.needEditController}"/>
    <div class="personnel-edit">
        <c:if test="${AppSubmissionDto.needEditController }">
            <c:set var="isClickEdit" value="false"/>
            <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && !isRfi}">
                <div class="text-right app-font-size-16">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span>&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
                </div>
            </c:if>
            <c:if test="${'true' != isClickEdit}">
                <c:set var="locking" value="true"/>
                <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
            </c:if>
        </c:if>
    </div>
    <input type="hidden" name="prsFlag" value="${prsFlag}"/>
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
            <c:forEach begin="0" end="${arPractitionerCount - 1}" step="1" varStatus="status">
                <c:set value="SP002" var="logo"/>
                <c:set var="index" value="${status.index}"/>
                <c:set var="appSvcPersonnelDto" value="${svcPersonnelDto.arPractitionerList[index]}"/>
                <%@include file="servicePersonnelArDetail.jsp" %>
            </c:forEach>

            <div class="form-group col-md-12 col-xs-12 addDpoDiv">
         <span class="addListBtn" style="color:deepskyblue;cursor:pointer;"> <span style="">+ Add Another AR Practitioner</span> </span>
            </div>
<%--            <div class="form-group col-md-12 col-xs-12">--%>
<%--                <span style="">Total Number of AR Practitioner</span>--%>
<%--            </div>--%>

            <iais:row>
                <iais:field width="5" value="Total Number of AR Practitioner"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <span id="arNumber">0<span>
                </iais:value>
            </iais:row>

        </div>
    </c:if>

    <c:if test="${nurseCount != 0}">
        <div class="panel-main-content">
            <c:forEach begin="0" end="${nurseCount - 1}" step="1" varStatus="status">
                <c:set value="SP003" var="logo"/>
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
            <c:forEach begin="0" end="${embryologistMinCount - 1}" step="1" varStatus="status">
                <c:set value="SP001" var="logo"/>
                <c:set var="index" value="${status.index}"/>
                <c:set var="appSvcPersonnelDto" value="${svcPersonnelDto.embryologistList[index]}"/>
                <%@include file="servicePersonnelEmbryologist.jsp" %>
            </c:forEach>
            <div class="form-group col-md-12 col-xs-12 addDpoDiv">
         <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">
         <span style="">+ Add Another Embryologist </span>
         </span>
            </div>
        </div>
    </c:if>


    <c:if test="${specialCount != 0}">
        <c:forEach begin="0" end="${specialCount - 1}" step="1" varStatus="status">
            <c:set var="index" value="${status.index}"/>
            <c:set value="SP000" var="logo"/>
            <c:set var="appSvcPersonnelDto" value="${svcPersonnelDto.specialList[index]}"/>
            <%@include file="servicePersonnelDetail.jsp" %>
        </c:forEach>
        <div class="form-group col-md-12 col-xs-12" id="addPsnDiv">
         <span class="addSpecialListBtn" style="color:deepskyblue;cursor:pointer;">
         <span style="">+ Add Another Service Personnel</span>
         </span>
        </div>
    </c:if>
    <c:if test="${normalCount != 0}">
        <div class="panel-main-content">
            <c:forEach begin="0" end="${normalCount - 1}" step="1" varStatus="status">
                <c:set value="SP999" var="logo"/>
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
</div>
<%@include file="servicePersonnelOthers.jsp" %>
<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<%@include file="servicePersonnelFun.jsp" %>









