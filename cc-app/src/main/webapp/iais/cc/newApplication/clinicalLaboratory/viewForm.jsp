<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="egp-blank"/>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div id="control--printerFriendly--1" class="section control " style="overflow: visible;">
  <div id="control--printerFriendly--1**errorMsg_section_top" class="error_placements">
  </div>
  <table class="control-grid">
    <tbody>
    <tr height="1" class="incomplete">
      <td style="width: 100%;" class="first last">
        <div class="control-set-font control-font-header section-header">
          <h2>Laboratory Disciplines</h2>
        </div>
        <div id="control--printerFriendly--2" class="control control-caption-horizontal" style="overflow: visible;">
          <div class="row">
          </div>
          <div class="form-group form-horizontal control-set-alignment formgap">
            <div class="col-sm-9 control-label formtext">
              <c:forEach var="appSvcLaboratoryDisciplinesDto" items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}">
                <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}">
                  <span>${checkList.chkName}</span>
                  <input type="checkbox" disabled="disabled" checked="checked" />
                </c:forEach>
              </c:forEach>
            </div>
          </div>
        </div>
      </td>
    </tr>
    <tr height="1" class="incomplete">
      <td style="width: 100%;" class="first last">
        <div class="control-set-font control-font-header section-header">
          <h2>GovernanceOfficers</h2>
        </div>
        <div id="control--printerFriendly--3" class="control control-caption-horizontal" style="overflow: visible;">
          <div class="row">
          </div>
          <div class="form-group form-horizontal control-set-alignment formgap">
            <div class="col-sm-9 control-label formtext">
              <c:forEach var="cgo" items="${currentPreviewSvcInfo.appSvcCgoDtoList}">
                <table>
                  <tr>
                    <td>${cgo.salutation}:</td><td>${cgo.name }</td>
                  </tr>
                  <tr>
                    <td>${cgo.idType }:</td><td>${cgo.idNo }</td>
                  </tr>
                  <tr>
                    <td>Designation:</td><td>${cgo.designation }</td>
                  </tr>
                  <tr>
                    <td>Mobile No:</td><td>${cgo.mobileNo}</td>
                  </tr>
                  <tr>
                    <td>EmailAddress:</td><td>${cgo.emailAddr}</td>
                  </tr>
                  <tr>
                    <td>ProfessionType:</td><td>${cgo.professionType }</td>
                  </tr>
                  <tr>
                    <td>ProfessionRegoNo:</td><td>${cgo.professionRegoNo}</td>
                  </tr>
                  <tr>
                    <td>Speciality:</td><td>${cgo.speciality}</td>
                  </tr>
                  <tr>
                    <td>professionRegoType:</td><td>${cgo.professionRegoType }</td>
                  </tr>
                </table>
              </c:forEach>
            </div>
          </div>
        </div>
      </td>
    </tr>



    <tr height="1" class="incomplete">
      <td style="width: 100%;" class="first last">
        <div class="control-set-font control-font-header section-header">
          <h2>DisciplineAllocation</h2>
        </div>
        <div id="" class="control control-caption-horizontal" style="overflow: visible;">
          <div class="form-group form-horizontal control-set-alignment formgap">
            <div class="col-sm-9 control-label formtext">
              <c:forEach var="allocation" items="${currentPreviewSvcInfo.appSvcDisciplineAllocationDtoList}">
                <table class="table discipline-table">
                  <thead>
                  <tr>
                    <th>Premises</th>
                    <th>Laboratory Disciplines</th>
                    <th>Clinical Governance Officers</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td rowspan="4">
                      <p>${allocation.premiseVal} </p>
                    </td>
                    <td>
                      <p>Laboratory Disciplines</p>
                    </td>
                    <td>
                      <p>${allocation.idNo}</p>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </c:forEach>
            </div>
          </div>
      </td>
    </tr>


    <tr height="1" class="incomplete">
      <td style="width: 100%;" class="first last">
        <div class="control-set-font control-font-header section-header">
          <h2>SvcPrincipalOfficers</h2>
        </div>
        <div id="" class="control control-caption-horizontal" style="overflow: visible;">
          <div class="form-group form-horizontal control-set-alignment formgap">
            <div class="col-sm-9 control-label formtext">
              <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po">
                <table>
                  <tr>
                    <td>${po.salutation}:</td><td>${po.name}</td>
                  </tr>
                  <tr>
                    <td>${po.idType}:</td><td>${po.idNo}</td>
                  </tr>
                  <tr>
                    <td>Designation:</td><td>${po.designation}</td>
                  </tr>
                  <tr>
                    <td>MobileNo:</td><td>${po.mobileNo}</td>
                  </tr>
                  <tr>
                    <td>EmailAddress:</td><td>${po.emailAddr}</td>
                  </tr>
                </table>
              </c:forEach>
            </div>
          </div>
        </div>
      </td>
    </tr>


    <tr height="1" class="incomplete">
      <td style="width: 100%;" class="first last">
        <div class="control-set-font control-font-header section-header">
          <h2>SvcPrincipalOfficers</h2>
        </div>
        <div id="" class="control control-caption-horizontal" style="overflow: visible;">
          <div class="form-group form-horizontal control-set-alignment formgap">
            <div class="col-sm-9 control-label formtext">
              <c:forEach items="${currentPreviewSvcInfo.appSvcDocDtoLit}" var="doc">
                <table>
                  <tr>
                    <td>***doc type***</td>
                    <td><a id="">${doc.fileName}</a></td>
                  </tr>
                </table>
              </c:forEach>
            </div>
          </div>
        </div>
      </td>
    </tr>

    </tbody>
  </table>
  <div id="control--printerFriendly--1**errorMsg_section_bottom" class="error_placements"></div>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        var svcId = "";

    });

</script>