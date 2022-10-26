<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<common:acknowledge task= "${currentTask}"  nextTask="${nextTask}" nextRole="${nextRole}" resultMsg="${resultMsg}"/>