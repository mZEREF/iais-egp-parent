<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/6/18
  Time: 15:42
  To change this template use File | Settings | File Templates.
--%>
<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>

<div class="row businessForm">
    <div class="businessContent">

    </div>
</div>
<c:set var="premBusinessMap" value="${premAlignBusinessMap}"/>
<c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
    <c:set var="businessDto" value="${premBusinessMap[appGrpPremisesDto.premisesIndexNo]}"/>
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
        <div class="panel panel-default">
            <div class="panel-heading " id="business-heading"  role="tab">
                <h4 class="panel-title">
                    <strong >
                        <c:choose>
                            <c:when test="${'ONSITE' == appGrpPremisesDto.premisesType}">
                                <c:out value="Premises"/>: <c:out value="${appGrpPremisesDto.address}"/>
                            </c:when>
                            <c:when test="${'CONVEYANCE' == appGrpPremisesDto.premisesType}">
                                <c:out value="Conveyance"/>: <c:out value="${appGrpPremisesDto.address}"/>
                            </c:when>
                            <c:when test="${'OFFSITE'  == appGrpPremisesDto.premisesType}">
                                <c:out value="Off-site"/>: <c:out value="${appGrpPremisesDto.address}"/>
                            </c:when>
                            <c:when test="${'EASMTS'  == appGrpPremisesDto.premisesType}">
                                <c:out value="Conveyance"/>: <c:out value="${appGrpPremisesDto.address}"/>
                            </c:when>
                        </c:choose>
                    </strong>
                </h4>
            </div>
            <div class="panel-collapse collapse in" id="generate-charges-content" role="tabpanel" aria-labelledby="business-heading">
                <div class="row panel-body" style="padding-left: 6%">
                    <div class="panel-main-content">
                        <div class="col-md-12 col-xs-12">
                            <div class="row control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                    <div class="control-label formtext col-md-5 col-xs-5">
                                        <label  class="control-label control-set-font control-font-label">Business Name</label>
                                        <span class="mandatory">*</span>
                                    </div>
                                    <div class="col-md-7 col-xs-12">
                                        <iais:input cssClass="businessName" maxLength="25" type="text" name="businessName${status.index}" value="${businessDto.businessName}"></iais:input>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:forEach>