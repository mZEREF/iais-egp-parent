<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="embryoTransferStageDto" value="${arSuperDataSubmissionDto.embryoTransferStageDto}"/>
<c:set var="headingSign" value="${isPrint or !isNewDataSubmission ?'':'completed'}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse">
                Embryo Transfer
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
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
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">Age of 1st Embryo Transferred
                        <c:if test="${flagTwo && (embryoTransferStageDto.firstEmbryoAge == 'AOFET005' || embryoTransferStageDto.firstEmbryoAge == 'AOFET006')}">
                            <a class="btn-tooltip styleguide-tooltip flag2" href="javascript:void(0);"
                               data-toggle="tooltip"
                               data-html="true"
                               title="&lt;p&gt;<iais:message key="DS_ERR047"/>&lt;/p&gt;">!</a>
                        </c:if>
                    </label>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${embryoTransferStageDto.firstEmbryoAge}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Was the 1st Embryo Transferred a fresh or thawed embryo?"
                                cssClass="col-md-6"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${embryoTransferStageDto.firstEmbryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                    </iais:value>
                </iais:row>
                <div id="section2nd"
                     <c:if test="${embryoTransferStageDto.transferNum < 2}">style="display: none;"</c:if>>
                    <iais:row>
                        <label class="col-xs-6 col-md-6 control-label">Age of 2nd Embryo Transferred
                            <c:if test="${flagTwo && (embryoTransferStageDto.secondEmbryoAge == 'AOFET005' || embryoTransferStageDto.secondEmbryoAge == 'AOFET006')}">
                                <a class="btn-tooltip styleguide-tooltip flag2" href="javascript:void(0);"
                                   data-toggle="tooltip"
                                   data-html="true"
                                   title="&lt;p&gt;<iais:message key="DS_ERR047"/>&lt;/p&gt;">!</a>
                            </c:if>
                        </label>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <iais:code code="${embryoTransferStageDto.secondEmbryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Was the 2nd Embryo Transferred a fresh or thawed embryo?"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <c:out value="${embryoTransferStageDto.secondEmbryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="section2nd"
                     <c:if test="${embryoTransferStageDto.transferNum < 3}">style="display: none;"</c:if>>
                    <iais:row>
                        <label class="col-xs-6 col-md-6 control-label">Age of 3rd Embryo Transferred
                            <c:if test="${flagTwo && (embryoTransferStageDto.thirdEmbryoAge == 'AOFET005' || embryoTransferStageDto.thirdEmbryoAge == 'AOFET006')}">
                                <a class="btn-tooltip styleguide-tooltip flag2" href="javascript:void(0);"
                                   data-toggle="tooltip"
                                   data-html="true"
                                   title="&lt;p&gt;<iais:message key="DS_ERR047"/>&lt;/p&gt;">!</a>
                            </c:if>
                        </label>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <iais:code code="${embryoTransferStageDto.thirdEmbryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Was the 3rd Embryo Transferred a fresh or thawed embryo?"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <c:out value="${embryoTransferStageDto.thirdEmbryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                        </iais:value>
                    </iais:row>
                </div>
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
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>