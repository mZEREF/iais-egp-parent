<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<webui:setLayout name="blank" />
<div class="alert-error">
	<p>
		<%=request.getAttribute("errMsg") %>
	</p>
</div>