<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Add a GIRO Payee</h2>
                        </div>
                        <iais:row>
                            Note: This function is to add a GIRO Payee who submitted a manual application.
                        </iais:row>
                        <iais:row>
                            The GIRO arrangement must be approved by the bank, otherwise GIRO deductions for that payee will fail.
                        </iais:row>
                        <iais:row>&nbsp;</iais:row>
                        <iais:row>
                            Search Organisation
                        </iais:row>
                        <iais:row>
                            <iais:field value="UEN :"/>
                            <iais:value width="18">
                                <label>
                                    <input type="text"
                                           style="width:180%; font-weight:normal;"
                                           name="hciCode" maxlength="100"
                                           value="${uen}"/>
                                </label>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="HCI Name :"/>
                            <iais:value width="18">
                                <label>
                                    <input type="text"
                                           style="width:180%; font-weight:normal;"
                                           name="hciName" maxlength="100"
                                           value="${hciName}"/>
                                </label>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="HCI Code :"/>
                            <iais:value width="18">
                                <label>
                                    <input type="text"
                                           style="width:180%; font-weight:normal;"
                                           name="hciCode" maxlength="100"
                                           value="${hciCode}"/>
                                </label>
                            </iais:value>
                        </iais:row>
                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-primary" type="button"
                                        onclick="javascript:doSearch();">Search
                                </button>
                            </iais:action>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="tab-pane active" id="tabInbox" role="tabpanel">
                            <div class="tab-content">
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="components">
                                            <h3>
                                                <span>Search Results</span>
                                            </h3>

                                            <iais:pagination param="SearchParam" result="SearchResult"/>
                                            <div class="table-responsive">
                                                <div class="table-gp">
                                                    <table class="table">
                                                        <thead>
                                                        <tr align="center">
                                                            <th >&nbsp;</th>
                                                            <iais:sortableHeader needSort="false"
                                                                                 field="UEN"
                                                                                 value="UEN"/>
                                                            <iais:sortableHeader needSort="false"
                                                                                 field="HCI_NAME"
                                                                                 value="HCI Name"/>
                                                            <iais:sortableHeader needSort="false" field="HCI_CODE"
                                                                                 value="HCI Code"/>
                                                        </tr>
                                                        </thead>
                                                        <tbody class="form-horizontal">
                                                        <c:choose>
                                                            <c:when test="${empty SearchResult.rows}">
                                                                <tr>
                                                                    <td colspan="15">
                                                                        <iais:message key="GENERAL_ACK018"
                                                                                      escape="true"/>
                                                                    </td>
                                                                </tr>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:forEach var="pool"
                                                                           items="${SearchResult.rows}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td class="form-check"
                                                                            onclick="javascript:controlCease('${isASO}')">
                                                                            <input class="form-check-input licenceCheck"
                                                                                   id="licence${status.index + 1}"
                                                                                   type="checkbox"
                                                                                   name="appIds"
                                                                                   value="${pool.appId}|${pool.isCessation}|${pool.licenceId}|${pool.licenceStatus}">
                                                                            <label class="form-check-label"
                                                                                   for="licence${status.index + 1}"><span
                                                                                    class="check-square"></span>
                                                                            </label>
                                                                        </td>
                                                                        <td class="row_no">
                                                                            <c:out value="${status.index + 1+ (SearchParam.pageNo - 1) * SearchParam.pageSize}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:if test="${pool.appCorrId==null}">${pool.applicationNo}</c:if>
                                                                            <c:if test="${pool.appCorrId!=null}"><a
                                                                                    onclick="javascript:doAppInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.appCorrId)}')">${pool.applicationNo}</a></c:if>
                                                                        </td>
                                                                        <td><c:out
                                                                                value="${pool.applicationType}"/></td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        </tbody>
                                                    </table>
                                                </div>

                                            </div>
                                            <div class="row">&nbsp;</div>
                                            <div class="row" height="1"
                                                 style="display: none ;font-size: 1.6rem; color: #D22727; padding-left: 20px"
                                                 id="selectDecisionMsg">
                                                <iais:message key="CESS_ERR006" escape="flase"></iais:message>
                                            </div>
                                            <div class="row" height="1"
                                                 style="display: none ;font-size: 1.6rem; color: #D22727;padding-left: 20px"
                                                 id="selectDecisionMsgActive">
                                                <iais:message key="CESS_ERR005" escape="flase"></iais:message>
                                            </div>
                                            <iais:action style="text-align:right;">
                                                <button type="button" class="btn btn-primary CeaseBtn"
                                                        disabled
                                                        onclick="javascript:doCessation();">Select
                                                </button>
                                            </iais:action>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">
</script>
