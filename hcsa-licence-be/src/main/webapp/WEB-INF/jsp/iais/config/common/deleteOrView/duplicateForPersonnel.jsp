<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<div class="col-xs-12 col-md-3 form-check" style="margin-top: 1%">
  <select disabled>
    <option value="">To duplicate for the personnel?</option>
    <option <c:if test="${doc.dupForPerson=='PO'}">selected</c:if> value="PO">Principal Officer (PO)</option>
    <option <c:if test="${doc.dupForPerson=='DPO'}">selected</c:if> value="DPO">Nominee</option>
    <option <c:if test="${doc.dupForPerson=='CGO'}">selected</c:if> value="CGO">Clinical Governance Officer (CGO)</option>
    <option <c:if test="${doc.dupForPerson=='SVCPSN'}">selected</c:if> value="SVCPSN">Service Personnel</option>
    <option <c:if test="${doc.dupForPerson=='MAP'}">selected</c:if> value="MAP">MedAlert Person </option>
  </select>
</div>