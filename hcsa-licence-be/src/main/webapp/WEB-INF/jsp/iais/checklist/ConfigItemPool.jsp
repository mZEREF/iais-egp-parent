<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 10/6/2019
  Time: 3:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>


<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
  .form-check-gp{
    width: 50%;
    float:left;
  }

</style>



<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <input type="hidden" name="currentValidateId" value="">


  <span id="error_configCustomValidation" name="iaisErrorMsg" class="error-msg"></span>
  <div class="main-content">
    <div class="container">
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="form-horizontal">
          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Regulation Clause Number</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="regulationClauseNo" value="" />
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Regulation</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="regulationClause" value="" />
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Checklist Item</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="checklistItem" value="" />
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Risk Level</label>
            <div class="col-xs-12 col-md-8 col-lg-9">
              <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL" firstOption="Please Select"></iais:select>
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Status</label>
            <div class="col-xs-12 col-md-8 col-lg-9">
              <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                           firstOption="Select Status" filterValue="CMSTAT002,CMSTAT004,DRAFT001"/>
            </div>
          </div>
        </div>



        <div class="tab-content">
          <div class="row">
            <div class="col-xs-12">
              <div class="components">
                <div class="table-gp">
                  <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                      <th scope="col"></th>
                      <iais:sortableHeader needSort="true"   field="regulationClauseNo" value="Regulation Clause Number"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="regulationClause" value="Regulations"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="checklistItem" value="Checklist Item"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="riskLevel" value="Risk Level"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="status" value="Status"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>

                    <c:if test = "${not empty errorMap}">
                      <div class="error">
                        <c:forEach items="${errorMap}" var="map">
                          ${map.key}  ${map.value} <br/>
                        </c:forEach>
                      </div>
                    </c:if>

                    <c:choose>
                      <c:when test="${empty checklistItemResult.rows}">
                        <tr>
                          <td colspan="6">
                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var = "item" items = "${checklistItemResult.rows}" varStatus="status">
                          <tr>
                            <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                            <td><input name="itemCheckbox" id="itemCheckbox" type="checkbox" value="${item.itemId}" /></td>
                            <td>${item.regulationClauseNo}</td>
                            <td>${item.regulationClause}</td>
                            <td>${item.checklistItem}</td>
                            <td>${item.riskLevel}</td>
                            <td>${item.status}</td>
                          </tr>
                        </c:forEach>
                      </c:otherwise>


                    </c:choose>
                    </tbody>
                  </table>
                  <div class="table-footnote">
                    <div class="row">
                      <div class="col-xs-6 col-md-4">
                        <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                      </div>
                      <div class="col-xs-6 col-md-8 text-right">
                        <div class="nav">
                          <ul class="pagination">
                            <li class="hidden"><a href="#" aria-label="Previous"><span aria-hidden="true"><em class="fa fa-chevron-left"></em></span></a></li>
                            <li class="active"><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#" aria-label="Next"><span aria-hidden="true"><em class="fa fa-chevron-right"></em></span></a></li>
                          </ul>

                          <br><br><br>
                          <div class="text-right text-center-mobile">
                            <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: configToChecklist('${requestScope.currentValidateId}');">Add to Config</a>
                            <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doSearch();">Search</a>
                          </div>
                        </div>



                      </div>
                    </div>



                  </div>


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
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script type="text/javascript">
    function doSearch(){
        SOP.Crud.cfxSubmit("mainForm", "doSearch");
    }

    function configToChecklist(id){
        var inputs = $('form').find("input");
        if(inputs.length != 0){
            inputs.each(function(index, obj){
                if('currentValidateId' == obj.name){
                    obj.value = id;
                }
            });
        }

        SOP.Crud.cfxSubmit("mainForm", "configToChecklist");
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm","doCancel");
    }
</script>