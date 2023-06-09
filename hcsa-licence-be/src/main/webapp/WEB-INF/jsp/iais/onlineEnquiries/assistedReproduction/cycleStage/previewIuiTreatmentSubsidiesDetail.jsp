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
                <iais:row>
                    <iais:field width="5" value="Is the IUI treatment co-funded"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${iuiTreatmentSubsidiesDto.artCoFunding}"/>
                    </iais:value>
                </iais:row>
            <c:if test="${isDisplayAppeal}">
                <iais:row>
                    <iais:field width="5" value="Is there an Approved Appeal?" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${iuiTreatmentSubsidiesDto.thereAppeal}"/>
                    </iais:value>
                </iais:row>
            </c:if>
                <iais:row>
                    <iais:field width="5" value="Please indicate appeal reference number (if applicable)" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${iuiTreatmentSubsidiesDto.appealNumber}"/>
                    </iais:value>
                </iais:row>
            </div>

        </div>
    </div>
</div>
