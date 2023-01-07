<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="embryoTransferStageDto" value="${arSuperDataSubmissionDto.embryoTransferStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" href="#cycleDetails" data-toggle="collapse">
                Embryo Transfer
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">No. Transferred
                        <c:if test="${flagThree}">
                            <a class="btn-tooltip styleguide-tooltip flag3" href="javascript:void(0);"
                               data-toggle="tooltip"
                               data-html="true"
                               title="&lt;p&gt;<iais:message key="DS_ERR049"/>&lt;/p&gt;">!</a>
                        </c:if>
                    </label>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${embryoTransferStageDto.transferNum}"/>
                    </iais:value>
                </iais:row>
                <c:forEach var="embryoTransferDetailDto" items="${embryoTransferStageDto.embryoTransferDetailDtos}" varStatus="seq">
                    <div id="${seq.index+1}Embryo"
                         <c:if test="${embryoTransferStageDto.transferNum < seq.index+1}">style="display: none;"</c:if>>
                        <iais:row>
                                    <label class="col-xs-6 col-md-6 control-label">
                                        <c:choose>
                                            <c:when test="${seq.index eq '0'}">1st Embryo</c:when>
                                            <c:when test="${seq.index eq '1'}">2nd Embryo</c:when>
                                            <c:when test="${seq.index eq '2'}">3rd Embryo</c:when>
                                            <c:otherwise>${seq.index+1}th Embryo</c:otherwise>
                                        </c:choose>
                                        <c:if test="${flagTwo && (embryoTransferDetailDto.embryoAge == 'AOFET005' || embryoTransferDetailDto.embryoAge == 'AOFET006')}">
                                            <a class="btn-tooltip styleguide-tooltip flag2" href="javascript:void(0);"
                                               data-toggle="tooltip"
                                               data-html="true"
                                               title="&lt;p&gt;<iais:message key="DS_ERR047"/>&lt;/p&gt;">!</a>
                                        </c:if>
                                    </label>

                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <iais:code code="${embryoTransferDetailDto.embryoAge}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <c:choose>
                                <c:when test="${seq.index eq '0'}"><iais:field width="6" value="Was the 1st Embryo Transferred a fresh or thawed embryo?"
                                                                               mandatory="false" cssClass="col-md-6"/></c:when>
                                <c:when test="${seq.index eq '1'}"><iais:field width="6" value="Was the 2nd Embryo Transferred a fresh or thawed embryo?"
                                                                               mandatory="false" cssClass="col-md-6"/></c:when>
                                <c:when test="${seq.index eq '2'}"><iais:field width="6" value="Was the 3rd Embryo Transferred a fresh or thawed embryo?"
                                                                               mandatory="false" cssClass="col-md-6"/></c:when>
                                <c:otherwise><iais:field width="6" value="Was the ${seq.index+1}th Embryo Transferred a fresh or thawed embryo?"
                                                         mandatory="false" cssClass="col-md-6"/></c:otherwise>
                            </c:choose>

                            <iais:value width="3" cssClass="col-md-3" display="true">
                                <c:out value="${embryoTransferDetailDto.embryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </c:forEach>

                <iais:row>
                    <iais:field width="6" value="1st Date of Transfer" cssClass="col-md-6"/>
                    <iais:value width="6" display="true">
                        <fmt:formatDate value="${embryoTransferStageDto.firstTransferDate}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="2nd Date of Transfer (if applicable)" cssClass="col-md-6"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${secondTransferDate}"/>
                        <fmt:formatDate value="${embryoTransferStageDto.secondTransferDate}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                </iais:row>
                <span id="error_inventoryNoZero" name="iaisErrorMsg" class="error-msg col-md-12"
                      style="padding: 0px;"></span>
                <br><br>
                <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/common/patientInventoryTable.jsp"/>
            </div>
        </div>
    </div>
</div>