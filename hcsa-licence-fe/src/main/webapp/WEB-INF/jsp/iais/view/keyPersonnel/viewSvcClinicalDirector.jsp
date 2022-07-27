<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="app-title">${currStepName}</label>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach items="${currentPreviewSvcInfo.appSvcClinicalDirectorDtoList}" var="cdDto" varStatus="status">
            <iais:row>
                <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                    <p><strong><%=HcsaConsts.CLINICAL_DIRECTOR%><c:if test="${currentPreviewSvcInfo.appSvcClinicalDirectorDtoList.size() > 1}"> ${status.index+1}</c:if>:</strong></p>
                </div>
                <div class="clinicalDirectorContent">
                    <iais:row>
                        <iais:field width="5" cssClass="col-md-5" value="Professional Board"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <iais:code code="${cdDto.professionBoard}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" cssClass="col-md-5" value="Professional Regn. No."/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.profRegNo}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" cssClass="col-md-5" value="Salutation"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <iais:code code="${cdDto.salutation}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" cssClass="col-md-5" value="Name"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.name}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" cssClass="col-md-5" value="ID Type"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <iais:code code="${cdDto.idType}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="ID No."/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.idNo}" />
                        </iais:value>
                    </iais:row>

                    <c:if test="${cdDto.idType == 'IDTYPE003'}">
                        <iais:row>
                            <iais:field width="5" value="Country of issuance"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <iais:code code="${cdDto.nationality}" />
                            </iais:value>
                        </iais:row>
                    </c:if>

                    <iais:row>
                        <iais:field width="5" cssClass="col-md-5" value="Designation"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:code code="${cdDto.designation}" />
                        </iais:value>
                    </iais:row>

                    <c:if test="${'DES999' == cdDto.designation}">
                        <iais:row>
                            <iais:field width="5" cssClass="col-md-5" value=""/>
                            <iais:value width="3" cssClass="col-md-7" display="true">
                                <c:out value="${cdDto.otherDesignation}" />
                            </iais:value>
                        </iais:row>
                    </c:if>

                    <iais:row>
                        <iais:field width="5" value="Specialty"/>
                        <iais:value width="7" cssClass="col-md-7 speciality" display="true">
                            <c:out value="${cdDto.speciality}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Date when specialty was obtained"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.specialtyGetDateStr}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Type of Registration Date"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.typeOfCurrRegi}}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Current Registration Date"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.currRegiDateStr}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Practicing Certificate End Date "/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.praCerEndDateStr}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Type of Register"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.typeOfRegister}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Relevant Experience"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.relevantExperience}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Clinical Governance Officer (CGO) holds a valid certification issued by an Emergency Medical Services ('EMS') Medical Directors workshop"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:choose>
                                <c:when test="${'1' == cdDto.holdCerByEMS}">
                                    Yes
                                </c:when>
                                <c:when test="${'0' == cdDto.holdCerByEMS}">
                                    No
                                </c:when>
                            </c:choose>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Expiry Date (ACLS)"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.aclsExpiryDateStr}"/>
                        </iais:value>
                    </iais:row>

                    <c:if test="${'MTS' == currentPreviewSvcInfo.serviceCode}">
                        <iais:row>
                            <iais:field width="5" value="Expiry Date (BCLS and AED)"/>
                            <iais:value width="3" cssClass="col-md-7" display="true">
                                <c:out value="${cdDto.bclsExpiryDateStr}"/>
                            </iais:value>
                        </iais:row>
                    </c:if>

                    <iais:row>
                        <iais:field width="5" value="Mobile No."/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.mobileNo}" />
                        </iais:value>
                    </iais:row>
<%----%>
                    <iais:row>
                        <iais:field width="5" value="Email Address"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${cdDto.emailAddr}" />
                        </iais:value>
                    </iais:row>
                </div>
            </iais:row>
        </c:forEach>
    </div>
</div>
