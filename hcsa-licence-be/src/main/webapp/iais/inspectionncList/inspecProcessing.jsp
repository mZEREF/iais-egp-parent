<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="alert alert-info" role="alert">
    <strong>
        <h4>Processing Status Update</h4>
    </strong>
</div>
<iais:section title="" id = "process_Rectification">
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <iais:section title="">
                    <iais:row>
                        <iais:field value="Current Status" required="false"/>
                        <iais:value width="10"><p><iais:code code="${applicationViewDto.applicationDto.status}"/></p></iais:value>
                    </iais:row>

                        <iais:row>
                            <iais:field value="Licence Start Date" required="false"/>
                            <iais:value width="10">
                                <c:if test="${not empty applicationViewDto.recomLiceStartDate}">
                                    <p><fmt:formatDate value='${applicationViewDto.recomLiceStartDate}' pattern='dd/MM/yyyy' /></p>
                                </c:if>
                                <c:if test="${empty applicationViewDto.recomLiceStartDate}">
                                  <p>-</p>
                                </c:if>
                            </iais:value>
                        </iais:row>

                    <div class="fastTrack">
                        <iais:row>
                            <iais:field value="Fast Tracking?" required="false"/>
                            <iais:value width="10">
                                <p>
                                    <input   id="fastTracking" name="fastTracking" disabled type="checkbox" <c:if test="${applicationViewDto.applicationDto.fastTracking}">checked="checked"</c:if>/>
                                    <label class="form-check-label" for="fastTracking" ><span class="check-square"></span></label>
                                </p>
                            </iais:value>
                        </iais:row>
                    </div>

                </iais:section>
                <div align="right">
                    <button type="button" class="btn btn-primary" onclick="javascript: doSubmit();">
                        Submit
                    </button>
                </div>
                <div>&nbsp</div>
            </div>
        </div>
    </div>

    <div class="alert alert-info" role="alert">
        <strong>
            <h4>Processing History</h4>
        </strong>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Username</th>
                        <th>Working Group</th>
                        <th>Status Update</th>
                        <th>Remarks</th>
                        <th>Last Updated</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach
                            items="${applicationViewDto.appPremisesRoutingHistoryDtoList}"
                            var="appPremisesRoutingHistoryDto">
                        <tr>
                            <td>
                                <p><c:out
                                        value="${appPremisesRoutingHistoryDto.actionby}"></c:out></p>
                            </td>
                            <td>
                                <p><c:out
                                        value="${appPremisesRoutingHistoryDto.workingGroup}"></c:out></p>
                            </td>
                            <td>
                                <p><c:out
                                        value="${appPremisesRoutingHistoryDto.processDecision}"></c:out></p>
                            </td>
                            <td>
                                <p><c:out
                                        value="${appPremisesRoutingHistoryDto.internalRemarks}"></c:out></p>
                            </td>
                            <td>
                                <p><c:out
                                        value="${appPremisesRoutingHistoryDto.updatedDt}"></c:out></p>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</iais:section>