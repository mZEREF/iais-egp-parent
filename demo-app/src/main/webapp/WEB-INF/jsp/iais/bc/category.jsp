<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8"  %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" id="categoryId" name="categoryId" value="">
        <br>
        <br>
        <div class="bg-title"><h2>Categories</h2></div>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Category Name" required="true"/>
                <iais:value width="7">
                    <iais:select name="categoryName" id="categoryName" options="categoryNameSelect"
                                 firstOption="Please Select" value="${categoryName}"/>
                </iais:value>
            </div>
            <div class="row">
                <div class="col-xs-12 col-md-12">
                    <div class="text-right">
                        <a class="btn btn-secondary" id="crud_clear_button"  href="#">Clear</a>
                        <a class="btn btn-primary" id="crud_search_button" value="doSearch" href="#">Search</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="components">
            <h3>
                <span>Search Results</span>
            </h3>
            <iais:pagination  param="categorySearchParam" result="categorySearchResult" needRowNum="10"/>
            <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <iais:sortableHeader style="width:1%" needSort="false"  field="" value="S/N" ></iais:sortableHeader>
                        <iais:sortableHeader style="width:20%" needSort="true"  field="categoryNo" value="categoryNo"></iais:sortableHeader>
                        <iais:sortableHeader style="width:20%" needSort="true"   field="categoryName" value="categoryName"></iais:sortableHeader>
                        <iais:sortableHeader style="width:30%" needSort="true"  field="categoryDescription" value="categoryDescription"></iais:sortableHeader>
                        <iais:sortableHeader style="width:10%" needSort="false"  field="" value=""></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody style="text-align: left">
                        <%-- rooms entity--%>
                        <c:forEach var = "category" items = "${categorySearchResult.rows}" varStatus="status">
                            <tr>
                                <td align="left" class="row_no" style="width: 5px">${(status.index + 1) + (categorySearchParam.pageNo - 1) * categorySearchParam.pageSize}</td>
                                <td align="left" style="width: 20%"><iais:code code="${category.categoryNo}"></iais:code></td>
                                <td align="left" style="width: 20%"><iais:code code="${category.categoryName}"></iais:code></td>
                                <td align="left" style="width: 20%"><iais:code code="${category.categoryDescription}"></iais:code></td>
                                <td align="left" style="width: 10%">
                                <button type="button" value="${category.id}" onclick="prepareEdit('<iais:mask name="categoryId" value="${category.id}"/>')"
                                        class="btn btn-default btn-sm" >Edit</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-4" style="float: right">
                <div class="button-group" >
                    <button type="button"  onclick="doAdd()"  class="bbtn btn-primary" >ADD</button>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">
    function doSearch(){
        SOP.Crud.cfxSubmit("mainForm", "doSearch");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }

    function prepareEdit(id){
        $("#categoryId").val(id);
        SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
    }

    function doAdd(){
        SOP.Crud.cfxSubmit("mainForm", "prepareAddCategory");
    }
</script>
