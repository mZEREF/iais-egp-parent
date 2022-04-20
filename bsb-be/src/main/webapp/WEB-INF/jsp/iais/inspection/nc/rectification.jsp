<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row">
  <div class="col-xs-12">
    <div class="panel-group" id="accordion2" role="tablist" aria-multiselectable="true">
      <div class="alert alert-info" role="alert">
        <strong>
          <h4>Verify Submitted Rectification</h4>
        </strong>
      </div>
      <iais:section title="" id = "retificationView">
        <div class="table-gp" id = "processRec">
          <table aria-describedby="" class="table">
            <thead>
            <tr>
              <th scope="col" >S/N</th>
              <th scope="col" >Item Description</th>
              <th scope="col" >Findings/Non-Compliance</th>
              <th scope="col" >Action Required</th>
              <th scope="col" >Remarks</th>
              <th scope="col" >Documents</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
              <c:when test="${empty insRectificationList}">
                <tr>
                  <td colspan="7">
                    <iais:message key="GENERAL_ACK018" escape="true"/>
                  </td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="item" items="${insRectificationList}" varStatus="status">
                  <tr>
                    <td><c:out value="${status.index+1}"/></td>
                    <td><c:out value="${item.itemText}"/></td>
                    <td><c:out value="${item.finding}"/></td>
                    <td><c:out value="${item.actionRequired}"/></td>
                    <td><iais:code code="${item.remark}"/></td>
                    <td>
                      <c:set var="inspectionDoc" value="${insRecordInfoSubTypeMap.get(item.itemValue)}"/>
                      <c:if test="${inspectionDoc != null}">
                        <c:forEach var="file" items="${inspectionDoc}" varStatus="status">
                          <div class="fileList ">
                            <c:set var="tmpId"><iais:mask name="file" value="${file.repoId}"/></c:set>
                            <span class="filename server-site" id="140">
                              <u><a href="javascript:void(0)" onclick="downloadFile('saved', '${tmpId}')">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</u>
                            </span>
                          </div>
                        </c:forEach>
                      </c:if>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
            </tbody>
          </table>
          <a style="float:left;padding-top: 1.1%;" class="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
        </div>
      </iais:section>
    </div>
  </div>
</div>
