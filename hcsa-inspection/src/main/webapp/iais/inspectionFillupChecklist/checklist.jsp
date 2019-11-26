<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form method="post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <h1>checklist</h1>
  <div class="main-content">
    <div class="container">
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
          <div class="row">
            <div class="col-xs-12">
              <div class="components">
                <h2 class="component-title">Search &amp; Result</h2>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="regulationClauseNo" value="Regulation Clause Number"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="regulationClause" value="Item"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="yes" value="Yes"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="no" value="No"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="na" value="Na"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="remark" value="Remark"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty checklistItemResult.rows}">
                        <tr>
                          <td colspan="6">
                            No Record!!
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var = "item" items = "${checklistItemResult.rows}" varStatus="status">
                          <tr>
                            <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>

                            <td>${item.regulationClauseNo}</td>
                            <td>${item.checklistItem}</td>
                            <td><input name="itemCheckboxYes" id="itemCheckboxYes" type="checkbox" value="${item.itemId}" /></td>
                            <td><input name="itemCheckboxNo" id="itemCheckboxNo" type="checkbox" value="${item.itemId}" /></td>
                            <td><input name="itemCheckboxNa" id="itemCheckboxNa" type="checkbox" value="${item.itemId}" /></td>
                            <td><input name="itemCheckboxRemark" id="itemCheckboxRemark" type="" value="" /></td>
                          </tr>
                        </c:forEach>
                      </c:otherwise>


                    </c:choose>
                    </tbody>
                  </table>
                  <div class="table-footnote">
                    <div class="row">
                      <div class="col-xs-6 col-md-8 text-right">
                        <div class="nav">
                          <ul class="pagination">
                            <li class="hidden"><a href="#" aria-label="Previous"><span aria-hidden="true"><i class="fa fa-chevron-left"></i></span></a></li>
                            <li class="active"><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#" aria-label="Next"><span aria-hidden="true"><i class="fa fa-chevron-right"></i></span></a></li>
                          </ul>
                          <br><br><br>
                          <div class="text-right text-center-mobile">
                            <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doSubmit();">Submit</a>
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

<script type="text/javascript">
  function doSubmit(){
    SOP.Crud.cfxSubmit("mainForm", "submitInspection");
  }

</script>