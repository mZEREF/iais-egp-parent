<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/8/2019
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
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

.form-inline .form-group {
  width: 30%;
  margin-bottom: 25px;
  display: inline-block;
  vertical-align: middle;
}

</style>
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <div class="main-content">
    <div class="container">
      <div class="form-horizontal">
        <div class="form-group">
          <div class="col-xs-12">
            <td>
              <label>
                Common  &nbsp; <input class="form-check-input" id="commmon" type="radio" name="common" aria-invalid="false" value="General Regulation"> General Regulation
              </label>
            </td>
          </div>
        </div>

      <div>
          <td>
            <iais:checkbox checkboxId="moduleCheckBox" codeCategory = "CATE_ID_APP_TYPE" name= "moduleCheckBox" labelName = "Module"></iais:checkbox>
          </td>


          <td>
            <iais:checkbox checkboxId="typeCheckBox" codeCategory = "CATE_ID_CHECKLIST_TYPE" forName="typeCheckBoxFor" name= "typeCheckBox" labelName = "Type"></iais:checkbox>
          </td>
       </div>
      </div>


      <div class="form-horizontal">
        <div class="form-group">
          <div class="col-xs-12">
            <td>
              Service Name &nbsp;<iais:select name="svcName" id="svcName" options = "svcNameSelect" firstOption="Select Service Name"></iais:select>
            </td>
          </div>
        </div>

      </div>

      <div class="form-horizontal">
        <div class="form-group">
          <div class="col-xs-12">
            <td>
              Service Sub Type &nbsp;<iais:select name="svcSubType" id="svcSubType"   options = "subtypeSelect" firstOption="Select Sub Type Name"></iais:select>
            </td>
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
              <td></td>
              <iais:sortableHeader needSort="true"   field="checklistItem" value="Checklist Item"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="isCommon" value="Common"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="type" value="Type"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="service" value="Service"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="svcSubType" value="Service Sub-type"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="order" value="Order No"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="checklistSection" value="Checklist Section"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="hciCode" value="HCI Code"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="eftStartDate" value="Effective Start Date"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="eftEndDate" value="Effective End Date"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="parentId" value="Parent ID"></iais:sortableHeader>
            </tr>
            </thead>
            <tbody>
            <tr>
              <c:forEach var = "configItem" items = "${checklistConfigResult.rows}" varStatus="status">
            <tr>
              <td class="row_no">${(status.index + 1) + (checklistConfigSearch.pageNo - 1) * checklistConfigSearch.pageSize}</td>
              <td></td>
              <td>${configItem.checklistItem}</td>
              <td>${configItem.module}</td>
              <td>${configItem.type}</td>
              <td>${configItem.common}</td>
              <td>${configItem.svcName}</td>
              <td>${configItem.svcSubType}</td>
              <td>${configItem.order}</td>
              <td>${configItem.checklistSection}</td>
              <td>${configItem.hciCode}</td>
              <td>${configItem.eftStartDate}</td>
              <td>${configItem.eftEndDate}</td>
              <td>${configItem.parentId}</td>
              <td>
                <iais:link icon="form_edit" title="Edit" onclick="javascript:prepareEditItem('${configItem.itemId}');"/>
                <iais:link icon="form_delete" title="Disable" onclick="javascript:disable('${configItem.itemId}');"/>
              </td>
            </tr>
            </c:forEach>
            </tr>
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
                  <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: prepareAddConfig();">Add Checklist</a>

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

  function prepareAddConfig(){
    SOP.Crud.cfxSubmit("mainForm", "prepareAddConfig");
  }

  function doCancel(){
    SOP.Crud.cfxSubmit("mainForm","doCancel");
  }
</script>