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
                        <iais:value width="10"><p><span style="font-size: 16px"><iais:code code="${applicationViewDto.applicationDto.status}"/></iais:value></span></p>
                    </iais:row>
                    <c:if test="${ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION == applicationViewDto.applicationDto.applicationType}">
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
                     </c:if>
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

    <%@include file="/WEB-INF/jsp/iais/inspectionncList/processHistory.jsp"%>
</iais:section>