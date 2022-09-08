<%--<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>--%>
<%--<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>--%>
<%--<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>--%>
<%--<div class="amended-service-info-gp">--%>
<%--    <iais:row>--%>
<%--        <label class="app-title">${currStepName}</label>--%>
<%--    </iais:row>--%>
<%--    <div class="amend-preview-info form-horizontal min-row">--%>
<%--        <c:forEach items="${currentPreviewSvcInfo.appSvcSpecialServiceInfoList}" var="appSvcSpecialServiceInfo" varStatus="status">--%>
<%--            <iais:row>--%>
<%--                <div class="col-xs-12 app-title">--%>
<%--                    <p><c:out value="${appSvcSpecialServiceInfo.premName}"/></p>--%>
<%--                    <p><c:if test="${not empty appSvcSpecialServiceInfo.premAddress}"> Address: </c:if><c:out value="${appSvcSpecialServiceInfo.premAddress}"/></p>--%>
<%--                </div>--%>
<%--            </iais:row>--%>

<%--            <div class="panel-group" id="specialService" role="tablist" aria-multiselectable="true">--%>
<%--                <c:forEach var="specialServiceSectionDto" items="${appSvcSpecialServiceInfo.specialServiceSectionDtoList}" varStatus="subSvcRelStatus">--%>
<%--                    <div class="panel panel-default">--%>
<%--                        <div class="panel-heading " role="tab">--%>
<%--                            <iais:row>--%>
<%--                                <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">--%>
<%--                                    <p><strong><c:out value="${specialServiceSectionDto.svcName}"/></strong></p>--%>
<%--                                </div>--%>
<%--                            </iais:row>--%>
<%--                        </div>--%>
<%--                        <div id="${status.index}${subSvcRelStatus.index}SSI" class="panel-collapse collapse in">--%>
<%--                            <input type="hidden" class ="isPartEdit" name="isPartEdit${status.index}" value="0"/>--%>
<%--                            <div class="panel-body">--%>
<%--                                <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcNurseDtoList}" varStatus="nicStatus">--%>
<%--                                    <c:set var="index" value="${nicStatus.index}"/>--%>
<%--                                    <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcNurseDtoList.size()}"/>--%>
<%--                                    <c:set var="title" value="Nurse in Charge ${NurseDtoListLength > 1?index+1:''}"/>--%>
<%--                                    <%@include file="viewSpecialServicesFromDetail.jsp"%>--%>
<%--                                </c:forEach>--%>
<%--                                <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcDirectorDtoList}" varStatus="direStatus">--%>
<%--                                    <c:set var="index" value="${direStatus.index}"/>--%>
<%--                                    <c:set var="DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>--%>
<%--                                    <c:set var="title" value="Emergency Department Director ${DirectorDtoListLength > 1?index+1:''}"/>--%>
<%--                                    <%@include file="viewSpecialServicesFromDetail.jsp"%>--%>
<%--                                </c:forEach>--%>
<%--                                <c:forEach var="appSvcPersonnelDto" items="${specialServiceSectionDto.appSvcNurseDirectorDtoList}" varStatus="nurStatus">--%>
<%--                                    <c:set var="index" value="${nurStatus.index}"/>--%>
<%--                                    <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcNurseDirectorDtoList.size()}"/>--%>
<%--                                    <c:set var="title" value="Emergency Department Nurse Director ${NurseDtoListLength > 1?index+1:''}"/>--%>
<%--                                    <%@include file="viewSpecialServicesFromDetail.jsp"%>--%>
<%--                                </c:forEach>--%>
<%--                                <c:set var="appSvcSuplmFormDto" value="${specialServiceSectionDto.appSvcSuplmFormDto}"/>--%>
<%--                                <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">--%>
<%--                                    <c:set var="batchSize" value="${appSvcSuplmGroupDto.count}"/>--%>
<%--                                    <c:if test="${batchSize > 0}">--%>
<%--                                        <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>--%>
<%--                                        <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">--%>
<%--                                            <c:if test="${item.display}">--%>
<%--                                                <%@ include file="../supplementaryForm/viewItem.jsp" %>--%>
<%--                                            </c:if>--%>
<%--                                        </c:forEach>--%>
<%--                                    </c:if>--%>
<%--                                </c:forEach>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </c:forEach>--%>
<%--            </div>--%>
<%--        </c:forEach>--%>
<%--    </div>--%>
<%--</div>--%>
