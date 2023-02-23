<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="embryoTransferStageDto" value="${arSuperDataSubmissionDto.embryoTransferStageDto}"/>

<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse">
                Embryo Transfer
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <%@include file="comPart.jsp" %>
                <iais:row>
                    <iais:field width="5" value="No. Transferred" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${embryoTransferStageDto.transferNum}"/>
                    </iais:value>
                </iais:row>

                <c:forEach var="embryoTransferDetailDto" items="${embryoTransferStageDto.embryoTransferDetailDtos}" varStatus="seq">
                <div id="${seq.index+1}Embryo">
                    <iais:row>
                        <c:choose>
                            <c:when test="${seq.index eq '0'}"><iais:field width="5" value="1st Embryo" mandatory="false" /></c:when>
                            <c:when test="${seq.index eq '1'}"><iais:field width="5" value="2nd Embryo" mandatory="false" /></c:when>
                            <c:when test="${seq.index eq '2'}"><iais:field width="5" value="3rd Embryo" mandatory="false" /></c:when>
                            <c:otherwise><iais:field width="5" value="${seq.index+1}th Embryo" mandatory="false" /></c:otherwise>
                        </c:choose>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <iais:code code="${embryoTransferDetailDto.embryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <c:choose>
                            <c:when test="${seq.index eq '0'}"><iais:field width="5" value="Was the 1st Embryo Transferred a fresh or thawed embryo?"
                                                                           mandatory="false" /></c:when>
                            <c:when test="${seq.index eq '1'}"><iais:field width="5" value="Was the 2nd Embryo Transferred a fresh or thawed embryo?"
                                                                           mandatory="false" /></c:when>
                            <c:when test="${seq.index eq '2'}"><iais:field width="5" value="Was the 3rd Embryo Transferred a fresh or thawed embryo?"
                                                                           mandatory="false" /></c:when>
                            <c:otherwise><iais:field width="5" value="Was the ${seq.index+1}th Embryo Transferred a fresh or thawed embryo?"
                                                     mandatory="false" /></c:otherwise>
                        </c:choose>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${embryoTransferDetailDto.embryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                        </iais:value>
                    </iais:row>
                </div>
                </c:forEach>


                <iais:row>
                    <iais:field width="5" value="1st Date of Transfer" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <fmt:formatDate value="${embryoTransferStageDto.firstTransferDate}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="2nd Date of Transfer (if applicable)" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${secondTransferDate}"/>
                        <fmt:formatDate value="${embryoTransferStageDto.secondTransferDate}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                </iais:row>
                <span id="error_inventoryNoZero" name="iaisErrorMsg" class="error-msg col-md-12"
                      style="padding: 0px;"></span>
                <br><br>
            </div>
        </div>
    </div>
</div>