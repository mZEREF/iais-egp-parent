<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <br><br>
    <div class="main-content">
        <div class="container">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-xs-4 col-md-2 control-label" >Domain Type</label>
                    <div class="col-xs-5 col-md-3">
                        <iais:select name="domainType" id="domainType" options="domainTypeSelect" firstOption="Please select" onchange="displaySection()"></iais:select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-xs-4 col-md-2 control-label" >Msg Type</label>
                    <div class="col-xs-5 col-md-3">
                        <iais:select name="msgType" options="msgTypeSelect" firstOption="Please select" ></iais:select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-xs-4 col-md-2 control-label" >Module</label>
                    <div class="col-xs-5 col-md-3">
                        <iais:select name="module"  options="moduleTypeSelect" firstOption="Please select" ></iais:select>
                    </div>
                </div>
            </div>


            <div class="components">
                <h2 class="component-title">Search &amp; Result</h2>
                <div class="table-gp">
                    <table class="table">
                        <thead>
                        <tr>
                            <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"  field="domain_type" value="Domain Type"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="msg_type" value="Message Type"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="description" value="Description"></iais:sortableHeader>
                            <iais:sortableHeader needSort="true"   field="message" value="Message"></iais:sortableHeader>
                            <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
                        </tr>
                        </thead>
                        <tbody style="text-align: center">
                        <c:choose>
                            <c:when test="${empty msgSearchResult.rows}">
                                <tr>
                                    <td colspan="6">
                                        No Record!!
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <%-- message entity--%>
                                <c:forEach var = "msgQuery" items = "${msgSearchResult.rows}" varStatus="status">
                                    <tr>
                                        <td class="row_no">${(status.index + 1) + (msgSearchParam.pageNo - 1) * msgSearchParam.pageSize}</td>
                                        <td>${msgQuery.domainType}</td>
                                        <td>${msgQuery.msgType}</td>
                                        <td>${msgQuery.module}</td>
                                        <td>${msgQuery.description}</td>
                                        <td>${msgQuery.message}</td>
                                        <td>
                                            <iais:link icon="form_edit" title="Edit" onclick="javascript:prepareEdit('${msgQuery.id}');"/>
                                            <iais:link icon="form_delete" title="Disable" onclick="javascript:disable('${msgQuery.id}');"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                    <div class="table-footnote">
                        <div class="row">
                            <div class="col-xs-6 col-md-4">
                                <p class="count">5 out of 25</p>
                            </div>
                            <div class="col-xs-6 col-md-8 text-right">
                                <div class="nav">
                                    <ul class="pagination">
                                        <li class="hidden"><a href="#" aria-label="Previous"><span aria-hidden="true"><i class="fa fa-chevron-left"></i></span></a></li>
                                        <li class="active"><a href="#">1</a></li>
                                        <li><a href="#">2</a></li>
                                        <li><a href="#">3</a></li>
                                        <li><a href="#" aria-label="Next"><span aria-hidden="true"><i class="fa fa-chevron-right"></i></span></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="application-tab-footer">
                    <td>
                        <div class="text-right text-center-mobile">
                            <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doSearch();">Search</a>

                        </div>

                    </td>
                </div>

            </div>
            </div>


    </div>
</form>

<script type="text/javascript">
    function doSearch(){
        SOP.Crud.cfxSubmit("mainForm", "doSearch");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm","changePage");
    }

    function prepareEdit(id){
        if(confirm('are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("mainForm", "prepareEdit", id);
        }
    }

    function disable(id){
        if(confirm('are sure you want to disable ? ')){
            SOP.Crud.cfxSubmit("mainForm", "disableStatus", id);
        }
    }

    function displaySection(){
       var val = document.getElementById("domainType").value;
       if(val == null){
           return;
       }

       var msgTypeRow = document.getElementById("msgTypeRow");
       var moduleTypeRow = document.getElementById("moduleTypeRow");
       if(msgTypeRow && moduleTypeRow){
           msgTypeRow.style = "block";
           moduleTypeRow.style = "block";
       }
    }

</script>
