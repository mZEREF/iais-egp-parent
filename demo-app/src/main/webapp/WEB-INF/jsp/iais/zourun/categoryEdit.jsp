<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <br><br>
        <div class="bg-title"><h2>Category Update</h2></div>
        <div class="form-horizontal">
            <div class="form-group">
                <iais:field value="Category Name" />
                <iais:value width="17">
                    <iais:select name="categoryName" id="categoryName" options="categoryNameSelect" firstOption="${sampleGameCategoryDto.categoryName}"></iais:select>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="Category NO" />
                <iais:value width="7">
                    <iais:input name="categoryNo" id="categoryNo" value="${sampleGameCategoryDto.categoryNo}"></iais:input>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:field value="Category Description" />
                <iais:value width="17">
                    <iais:input name="categoryDescription" id="categoryDescription" value="${sampleGameCategoryDto.categoryDescription}"></iais:input>
                </iais:value>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-4" style="float: left">
                <div class="button-group" >
                    <button type="button"  onclick="doAdd()"  class="btn btn-secondary" >ADD</button>
                </div>
            </div>
            <div class="col-xs-12 col-sm-4" style="float: right">
                <div class="button-group" >
                    <a class="btn btn-secondary" onclick="Utils.submit('mainForm','doBack')">back</a>
                    <a class="btn btn-primary next" onclick="doEdit()">Update</a>
                </div>
            </div>
        </div>

        <div class="components">
            <h3>
                <span>Games in this category</span>
            </h3>
            <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <iais:sortableHeader style="width:10%" needSort="false"  field="" value="S/N" ></iais:sortableHeader>
                        <iais:sortableHeader style="width:30%" needSort="false"  field="GameName" value="Game Name"></iais:sortableHeader>
                        <iais:sortableHeader style="width:30%" needSort="false"   field="Price" value="Price"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody style="text-align: left">
                    <%-- person entity--%>
                    <c:forEach var = "game" items = "${gameResult.rows}" varStatus="status">
                        <tr>
                            <td align="left" class="row_no" style="width: 5px">${(status.index + 1) + (categorySearchParam.pageNo - 1) * categorySearchParam.pageSize}</td>
                            <td align="left" ><iais:code code="${game.gameName}"></iais:code></td>
                            <td align="left" ><iais:code code="${game.price}"></iais:code></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
<%--        <div class="row">--%>
<%--            <div class="col-xs-12 col-sm-4" style="float: right">--%>
<%--                <div class="button-group" >--%>
<%--                    <button type="button"  onclick="doAdd()"  class="bbtn btn-primary" >ADD</button>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
    </form>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>


<script type="text/javascript">
    function doEdit(){
        if ($("#categoryName").val()==null||""==$("#categoryName").val()){
            $("#categoryName").val(${sampleGameCategoryDto.categoryName})
        }
        Utils.submit('mainForm','doEdit', '${sampleGameCategoryDto.id}')
    }

    function doAdd(){
        SOP.Crud.cfxSubmit("mainForm", "prepareAddGame");
    }
</script>
