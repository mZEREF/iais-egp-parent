<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 10/6/2019
  Time: 3:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style>
    .p{
        margin: 4px 0 10px
    }

    @media only screen and (min-width : 1201px) {
        td {
            max-width: 550px;
        }
    }

    @media only screen and (max-width : 1200px) {
        td {
            max-width: 400px;
        }
    }

    @media only screen and (max-width : 992px) {
        td {
            max-width: 400px;
        }
    }

    @media  only screen and (max-width: 767px) {
        td {
            max-width: 350px;
        }
    }

    @media only screen and (max-width: 370px) and (min-width: 320px) {
        td {
            max-width: 240px;
        }
    }

    @media only screen and (min-width : 768px) {
        td:nth-of-type(3), th:nth-of-type(3) {
            width: 25%;
        }
        td:nth-of-type(4), th:nth-of-type(4) {
            width: 40%;
        }
    }

    table > tbody > tr > td {
        vertical-align: top;
    }
</style>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input hidden="hidden" id="regulationId" name="regulationId" value="">

        <div class="center-content col-xs-12 col-md-12">
        <div class="bg-title"><h2>Regulation Management</h2></div>
        <span id="error_customValidation" name="iaisErrorMsg" class="error-msg"></span>

        <iais:section title="">

            <div class="form-horizontal">
                <div class="form-group">
                    <iais:field value="Regulation Clause Number" ></iais:field>
                    <div class="col-xs-5 col-md-3">
                        <input type="text" name="regulationClauseNo" maxlength="100" value="${param.regulationClauseNo}"/>
                        <span id="error_regulationClauseNo" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>

                <div class="form-group">
                    <iais:field value="Regulation" ></iais:field>
                    <div class="col-xs-5 col-md-3">
                        <input type="text" name="regulationClause" maxlength="8000" value="${param.regulationClause}"/>
                        <span id="error_regulationClause" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>
            </div>
        </iais:section>
        <div class="col-xs-12 col-md-12">
            <div class="text-right">
                <a class="btn btn-secondary" id="crud_clear_button"  href="#">Clear</a>
                <a class="btn btn-secondary"  id="exportButtonId" href="${pageContext.request.contextPath}/regulation-result-file?action=regulation">Export Regulation</a>
                <a class="btn btn-primary next" id="crud_search_button" value="doQuery" href="#">Search</a>
            </div>
        </div>

        <div class="tab-content">
            <div class="components">
                <h3>
                    <span>Search Results</span>
                </h3>
                <iais:pagination  param="regulationSearch" result="regulationResult"/>
                <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <iais:sortableHeader needSort="false" field="" value="No."/>
                        <th scope="col" ></th>
                        <iais:sortableHeader needSort="true" field="CLAUSE_NO" value="Regulation Clause Number"/>
                        <iais:sortableHeader needSort="true" field="CLAUSE" value="Regulations"/>
                        <iais:sortableHeader needSort="true" field="status" value="Status"/>
                        <iais:sortableHeader needSort="false" field="action" value="Action"/>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty regulationResult.rows}">
                            <tr>
                                <td colspan="6">
                                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="item" items="${regulationResult.rows}" varStatus="status">
                                <tr>
                                    <td class="row_no">
                                            ${(status.index + 1) + (regulationSearch.pageNo - 1) * regulationSearch.pageSize}
                                    </td>
                                    <td>
                                        <iais:checkbox name="regulation_itemCheckbox" checkboxId="regulation_itemCheckbox"
                                                       request="${pageContext.request}"  value="${item.id}"
                                                       forName="regulation_item_CheckboxReDisplay"/>
                                    </td>
                                    <td class="word-wrap">${item.clauseNo}</td>
                                    <td class="word-wrap">${item.clause}</td>
                                    <td>
                                        <iais:code code="${item.status}"/>
                                    </td>
                                    <td>
                                        <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" onclick="Utils.markSubmit('mainForm','preUpdate', 'regulationId', '<iais:mask name="regulationId" value="${item.id}"/>')" >Edit</button>
                                        <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" onclick="Utils.markSubmit('mainForm','doDelete', 'regulationId', '<iais:mask name="regulationId" value="${item.id}"/>')" >Delete</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                </div>
                <div class="table-footnote">
                    <div class="row">
                        <div class="col-xs-50 col-md-12 text-right">
                            <div class="text-center-mobile">
                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="Utils.submit('mainForm', 'preUpload')">Upload Regulation</a>

                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="Utils.submit('mainForm', 'preCreate')">Create</a>
                            </div>
                        </div>
                    </div>


                </div>
            </div>
        </div>

        </div>
    </form>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>