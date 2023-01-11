<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#">
                IUI Treatment Co-funding
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="comPart.jsp" %>
                <c:set var="iuiTreatmentSubsidiesDto" value="${arSuperDataSubmissionDto.iuiTreatmentSubsidiesDto}" />
                <c:set var="iuiTreatmentSubsidiesDtoVersion" value="${arSuperDataSubmissionDtoVersion.iuiTreatmentSubsidiesDto}" />
                <iais:row>
                    <iais:field width="5" value="Is the IUI treatment co-funded"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${iuiTreatmentSubsidiesDto.artCoFunding}"/>
                    </iais:value>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${iuiTreatmentSubsidiesDtoVersion.artCoFunding}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Is there an Approved Appeal?" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal == true }">Yes</c:if>
                        <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal == false }">No</c:if>
                    </iais:value>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${iuiTreatmentSubsidiesDtoVersion.thereAppeal == true }">Yes</c:if>
                        <c:if test="${iuiTreatmentSubsidiesDtoVersion.thereAppeal == false }">No</c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Please indicate appeal reference number (if applicable)"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${iuiTreatmentSubsidiesDto.appealNumber}"/>
                    </iais:value>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${iuiTreatmentSubsidiesDtoVersion.appealNumber}"/>
                    </iais:value>
                </iais:row>
            </div>

        </div>
    </div>
</div>
