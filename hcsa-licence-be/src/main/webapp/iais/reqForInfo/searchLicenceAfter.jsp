<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div id="base" class="">

            <!-- Unnamed (Rectangle) -->
            <div id="u1738" class="ax_default label">
                <div id="u1738_div" class=""></div>
                <div id="u1738_text" class="text ">
                    <p><span>&nbsp; Advanced Search Criteria For Licence</span></p>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1739" class="ax_default label">
                <div id="u1739_div" class=""></div>
                <div id="u1739_text" class="text ">
                    <p><span>Service Licence Type:</span></p>
                </div>
            </div>

            <!-- Unnamed (Droplist) -->
            <div id="u1740" class="ax_default droplist">
                <select id="u1740_input">
                </select>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1741" class="ax_default label">
                <div id="u1741_div" class=""></div>
                <div id="u1741_text" class="text ">
                    <p><span>Licence No:</span></p>
                </div>
            </div>

            <!-- Unnamed (Text Field) -->
            <div id="u1742" class="ax_default text_field">
                <input id="u1742_input" type="text" value=""/>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1743" class="ax_default label">
                <div id="u1743_div" class=""></div>
                <div id="u1743_text" class="text ">
                    <p><span>Licence Period:</span></p>
                </div>
            </div>

            <!-- Unnamed (Group) -->
            <div id="u1744" class="ax_default" data-left="593" data-top="123" data-width="122" data-height="30">

                <!-- date field (Text Field) -->
                <div id="u1745" class="ax_default text_field" data-label="date field">
                    <input id="u1745_input" type="text" value=""/>
                </div>

                <!-- calendar grid (Group) -->
                <div id="u1746" class="ax_default ax_default_hidden" data-label="calendar grid" style="display:none; visibility: hidden" data-left="0" data-top="0" data-width="0" data-height="0">

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1747" class="ax_default box_1">
                        <div id="u1747_div" class=""></div>
                    </div>

                    <!-- header (Rectangle) -->
                    <div id="u1748" class="ax_default box_1" data-label="header">
                        <div id="u1748_div" class=""></div>
                    </div>

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1749" class="ax_default box_1">
                        <div id="u1749_div" class=""></div>
                    </div>

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1750" class="ax_default box_1">
                        <div id="u1750_div" class=""></div>
                    </div>

                    <!-- grid (Repeater) -->
                    <div id="u1751" class="ax_default" data-label="grid">
                        <script id="u1751_script" type="axure-repeater-template" data-label="grid">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752" class="ax_default box_1 u1752" data-label="grid square">
                                <div id="u1752_div" class="u1752_div"></div>
                            </div>
                        </script>
                        <div id="u1751-1" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-1" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-1_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-2" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-2" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-2_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-3" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-3" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-3_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-4" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-4" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-4_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-5" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-5" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-5_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-6" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-6" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-6_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-7" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-7" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-7_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-8" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-8" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-8_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-9" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-9" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-9_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-10" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-10" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-10_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-11" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-11" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-11_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-12" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-12" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-12_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-13" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-13" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-13_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-14" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-14" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-14_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-15" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-15" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-15_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-16" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-16" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-16_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-17" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-17" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-17_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-18" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-18" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-18_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-19" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-19" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-19_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-20" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-20" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-20_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-21" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-21" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-21_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-22" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-22" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-22_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-23" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-23" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-23_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-24" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-24" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-24_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-25" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-25" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-25_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-26" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-26" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-26_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-27" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-27" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-27_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-28" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-28" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-28_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-29" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-29" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-29_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-30" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-30" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-30_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-31" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-31" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-31_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-32" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-32" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-32_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-33" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-33" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-33_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-34" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-34" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-34_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-35" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-35" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-35_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-36" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-36" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-36_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1751-37" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1752-37" class="ax_default box_1 u1752" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1752-37_div" class="u1752_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                    </div>

                    <!-- Unnamed (Triangle) -->
                    <div id="u1753" class="ax_default box_3">
                        <img id="u1753_img" class="img " src="images/cease_1st_page/u167.png"/>
                    </div>

                    <!-- Unnamed (Triangle) -->
                    <div id="u1754" class="ax_default box_3">
                        <img id="u1754_img" class="img " src="images/cease_1st_page/u167.png"/>
                    </div>
                </div>

                <!-- Unnamed (Group) -->
                <div id="u1755" class="ax_default" data-left="689" data-top="123" data-width="26" data-height="28">

                    <!-- calendar icon (Shape) -->
                    <div id="u1756" class="ax_default icon" data-label="calendar icon">
                        <img id="u1756_img" class="img " src="images/cease_1st_page/calendar_icon_u170.png"/>
                    </div>

                    <!-- current date (Text Field) -->
                    <div id="u1757" class="ax_default text_field ax_default_hidden" data-label="current date" style="display:none; visibility: hidden">
                        <input id="u1757_input" type="text" value=""/>
                    </div>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1758" class="ax_default label">
                <div id="u1758_div" class=""></div>
                <div id="u1758_text" class="text ">
                    <p><span>To</span></p>
                </div>
            </div>

            <!-- Unnamed (Group) -->
            <div id="u1759" class="ax_default" data-left="751" data-top="121" data-width="122" data-height="30">

                <!-- date field (Text Field) -->
                <div id="u1760" class="ax_default text_field" data-label="date field">
                    <input id="u1760_input" type="text" value=""/>
                </div>

                <!-- calendar grid (Group) -->
                <div id="u1761" class="ax_default ax_default_hidden" data-label="calendar grid" style="display:none; visibility: hidden" data-left="0" data-top="0" data-width="0" data-height="0">

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1762" class="ax_default box_1">
                        <div id="u1762_div" class=""></div>
                    </div>

                    <!-- header (Rectangle) -->
                    <div id="u1763" class="ax_default box_1" data-label="header">
                        <div id="u1763_div" class=""></div>
                    </div>

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1764" class="ax_default box_1">
                        <div id="u1764_div" class=""></div>
                    </div>

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1765" class="ax_default box_1">
                        <div id="u1765_div" class=""></div>
                    </div>

                    <!-- grid (Repeater) -->
                    <div id="u1766" class="ax_default" data-label="grid">
                        <script id="u1766_script" type="axure-repeater-template" data-label="grid">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767" class="ax_default box_1 u1767" data-label="grid square">
                                <div id="u1767_div" class="u1767_div"></div>
                            </div>
                        </script>
                        <div id="u1766-1" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-1" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-1_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-2" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-2" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-2_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-3" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-3" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-3_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-4" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-4" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-4_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-5" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-5" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-5_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-6" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-6" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-6_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-7" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-7" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-7_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-8" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-8" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-8_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-9" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-9" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-9_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-10" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-10" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-10_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-11" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-11" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-11_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-12" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-12" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-12_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-13" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-13" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-13_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-14" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-14" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-14_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-15" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-15" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-15_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-16" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-16" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-16_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-17" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-17" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-17_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-18" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-18" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-18_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-19" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-19" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-19_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-20" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-20" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-20_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-21" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-21" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-21_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-22" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-22" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-22_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-23" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-23" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-23_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-24" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-24" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-24_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-25" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-25" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-25_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-26" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-26" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-26_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-27" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-27" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-27_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-28" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-28" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-28_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-29" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-29" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-29_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-30" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-30" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-30_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-31" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-31" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-31_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-32" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-32" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-32_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-33" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-33" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-33_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-34" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-34" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-34_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-35" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-35" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-35_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-36" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-36" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-36_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1766-37" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1767-37" class="ax_default box_1 u1767" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1767-37_div" class="u1767_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                    </div>

                    <!-- Unnamed (Triangle) -->
                    <div id="u1768" class="ax_default box_3">
                        <img id="u1768_img" class="img " src="images/cease_1st_page/u167.png"/>
                    </div>

                    <!-- Unnamed (Triangle) -->
                    <div id="u1769" class="ax_default box_3">
                        <img id="u1769_img" class="img " src="images/cease_1st_page/u167.png"/>
                    </div>
                </div>

                <!-- Unnamed (Group) -->
                <div id="u1770" class="ax_default" data-left="847" data-top="121" data-width="26" data-height="28">

                    <!-- calendar icon (Shape) -->
                    <div id="u1771" class="ax_default icon" data-label="calendar icon">
                        <img id="u1771_img" class="img " src="images/cease_1st_page/calendar_icon_u170.png"/>
                    </div>

                    <!-- current date (Text Field) -->
                    <div id="u1772" class="ax_default text_field ax_default_hidden" data-label="current date" style="display:none; visibility: hidden">
                        <input id="u1772_input" type="text" value=""/>
                    </div>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1773" class="ax_default label">
                <div id="u1773_div" class=""></div>
                <div id="u1773_text" class="text ">
                    <p><span>Licence Status:</span></p>
                </div>
            </div>

            <!-- Unnamed (Droplist) -->
            <div id="u1774" class="ax_default droplist">
                <select id="u1774_input">
                </select>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1775" class="ax_default primary_button">
                <div id="u1775_div" class=""></div>
                <div id="u1775_text" class="text ">
                    <p><span>Search</span></p>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1776" class="ax_default primary_button">
                <div id="u1776_div" class=""></div>
                <div id="u1776_text" class="text ">
                    <p><span>Clear</span></p>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1777" class="ax_default primary_button">
                <div id="u1777_div" class=""></div>
                <div id="u1777_text" class="text ">
                    <p><span>Back</span></p>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1778" class="ax_default label">
                <div id="u1778_div" class=""></div>
                <div id="u1778_text" class="text ">
                    <p><span>Search Results</span></p>
                </div>
            </div>

            <!-- Unnamed (Table) -->
            <div id="u1779" class="ax_default">

                <!-- Unnamed (Table Cell) -->
                <div id="u1780" class="ax_default table_cell">
                    <img id="u1780_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1345.png"/>
                    <div id="u1780_text" class="text ">
                        <p><span>S/N</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1781" class="ax_default table_cell">
                    <img id="u1781_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1346.png"/>
                    <div id="u1781_text" class="text ">
                        <p><span>Application No</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1782" class="ax_default table_cell">
                    <img id="u1782_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1346.png"/>
                    <div id="u1782_text" class="text ">
                        <p><span>Application Type</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1783" class="ax_default table_cell">
                    <img id="u1783_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1348.png"/>
                    <div id="u1783_text" class="text ">
                        <p><span>Licence No</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1784" class="ax_default table_cell">
                    <img id="u1784_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1349.png"/>
                    <div id="u1784_text" class="text ">
                        <p><span>&nbsp;&nbsp;&nbsp; HCI Code</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1785" class="ax_default table_cell">
                    <img id="u1785_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1350.png"/>
                    <div id="u1785_text" class="text ">
                        <p><span>HCI Name</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1786" class="ax_default table_cell">
                    <img id="u1786_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1351.png"/>
                    <div id="u1786_text" class="text ">
                        <p><span>Address</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1787" class="ax_default table_cell">
                    <img id="u1787_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1352.png"/>
                    <div id="u1787_text" class="text ">
                        <p><span>Licensee Name</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1788" class="ax_default table_cell">
                    <img id="u1788_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1353.png"/>
                    <div id="u1788_text" class="text ">
                        <p><span>&nbsp;&nbsp; &nbsp; &nbsp; Service Name</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1789" class="ax_default table_cell">
                    <img id="u1789_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1354.png"/>
                    <div id="u1789_text" class="text ">
                        <p><span>Licence Period</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1790" class="ax_default table_cell">
                    <img id="u1790_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1355.png"/>
                    <div id="u1790_text" class="text ">
                        <p><span>Licence Status</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1791" class="ax_default table_cell">
                    <img id="u1791_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1356.png"/>
                    <div id="u1791_text" class="text ">
                        <p><span>Past Compliance History</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1792" class="ax_default table_cell">
                    <img id="u1792_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1357.png"/>
                    <div id="u1792_text" class="text ">
                        <p><span>Current Risk Tagging</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1793" class="ax_default table_cell">
                    <img id="u1793_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1358.png"/>
                    <div id="u1793_text" class="text ">
                        <p><span>1</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1794" class="ax_default table_cell">
                    <img id="u1794_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1359.png"/>
                    <div id="u1794_text" class="text ">
                        <p><span style="color:#000000;"><br></span></p><p><span style="text-decoration:underline;color:#169BD5;">NW-20181224-00004</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1795" class="ax_default table_cell">
                    <img id="u1795_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1359.png"/>
                    <div id="u1795_text" class="text ">
                        <p><span>New</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1796" class="ax_default table_cell">
                    <img id="u1796_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1361.png"/>
                    <div id="u1796_text" class="text ">
                        <p><span style="text-decoration:underline;">18L0001/01/181</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1797" class="ax_default table_cell">
                    <img id="u1797_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1362.png"/>
                    <div id="u1797_text" class="text ">
                        <p><span>18L0001</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1798" class="ax_default table_cell">
                    <img id="u1798_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1363.png"/>
                    <div id="u1798_text" class="text ">
                        <p><span>SAM LABORATORY </span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1799" class="ax_default table_cell">
                    <img id="u1799_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1364.png"/>
                    <div id="u1799_text" class="text ">
                        <p><span><br></span></p><p><span>37 Jalan Pemimpin</span></p><p><span>Mapex #04-13, Singapore 577177</span></p><p><span>Tel: +65 6737 3867</span></p><p><span>Fax: +65 6733 3527</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1800" class="ax_default table_cell">
                    <img id="u1800_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1365.png"/>
                    <div id="u1800_text" class="text ">
                        <p><span>Sam Group</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1801" class="ax_default table_cell">
                    <img id="u1801_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1366.png"/>
                    <div id="u1801_text" class="text ">
                        <p style="font-size:13px;"><span>&nbsp; Clinical Laboratory</span></p><p style="font-size:12px;"><span>&#149; Human Immunodeficiency Virus</span></p><p style="font-size:12px;"><span>&#149; Pre-implantation Genetics Diagnosis</span></p><p style="font-size:12px;"><span>&#149; Pre-implantation Genetics Screening</span></p><p style="font-size:12px;"><span><br></span></p><p style="font-size:13px;"><span><br></span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1802" class="ax_default table_cell">
                    <img id="u1802_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1367.png"/>
                    <div id="u1802_text" class="text ">
                        <p><span>01/12/2018&nbsp; -&nbsp;&nbsp; 30/11/2020&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; </span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1803" class="ax_default table_cell">
                    <img id="u1803_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1368.png"/>
                    <div id="u1803_text" class="text ">
                        <p><span>Active</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1804" class="ax_default table_cell">
                    <img id="u1804_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1369.png"/>
                    <div id="u1804_text" class="text ">
                        <p><span>Full</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1805" class="ax_default table_cell">
                    <img id="u1805_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1370.png"/>
                    <div id="u1805_text" class="text ">
                        <p><span>Low</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1806" class="ax_default table_cell">
                    <img id="u1806_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1371.png"/>
                    <div id="u1806_text" class="text ">
                        <p><span>2</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1807" class="ax_default table_cell">
                    <img id="u1807_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1372.png"/>
                    <div id="u1807_text" class="text ">
                        <p><span style="color:#000000;"><br></span></p><p><span style="text-decoration:underline;color:#169BD5;">NW-20181224-00004</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1808" class="ax_default table_cell">
                    <img id="u1808_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1372.png"/>
                    <div id="u1808_text" class="text ">
                        <p><span>New</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1809" class="ax_default table_cell">
                    <img id="u1809_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1374.png"/>
                    <div id="u1809_text" class="text ">
                        <p><span style="text-decoration:underline;">18L0001/01/182</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1810" class="ax_default table_cell">
                    <img id="u1810_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1375.png"/>
                    <div id="u1810_text" class="text ">
                        <p><span>18L0001</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1811" class="ax_default table_cell">
                    <img id="u1811_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1376.png"/>
                    <div id="u1811_text" class="text ">
                        <p><span>SAM LABORATORY </span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1812" class="ax_default table_cell">
                    <img id="u1812_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1377.png"/>
                    <div id="u1812_text" class="text ">
                        <p><span><br></span></p><p><span>37 Jalan Pemimpin</span></p><p><span>Mapex #04-13, Singapore 577177</span></p><p><span>Tel: +65 6737 3867</span></p><p><span>Fax: +65 6733 3527</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1813" class="ax_default table_cell">
                    <img id="u1813_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1378.png"/>
                    <div id="u1813_text" class="text ">
                        <p><span>Sam Group</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1814" class="ax_default table_cell">
                    <img id="u1814_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1379.png"/>
                    <div id="u1814_text" class="text ">
                        <p><span>Blood Banking</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1815" class="ax_default table_cell">
                    <img id="u1815_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1380.png"/>
                    <div id="u1815_text" class="text ">
                        <p><span>01/12/2018&nbsp; -&nbsp;&nbsp; 30/11/2020</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1816" class="ax_default table_cell">
                    <img id="u1816_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1381.png"/>
                    <div id="u1816_text" class="text ">
                        <p><span>Active</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1817" class="ax_default table_cell">
                    <img id="u1817_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1382.png"/>
                    <div id="u1817_text" class="text ">
                        <p><span>Partial</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1818" class="ax_default table_cell">
                    <img id="u1818_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1383.png"/>
                    <div id="u1818_text" class="text ">
                        <p><span>Moderate</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1819" class="ax_default table_cell">
                    <img id="u1819_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1371.png"/>
                    <div id="u1819_text" class="text ">
                        <p><span>3</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1820" class="ax_default table_cell">
                    <img id="u1820_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1372.png"/>
                    <div id="u1820_text" class="text ">
                        <p><span style="color:#000000;"><br></span></p><p><span style="text-decoration:underline;color:#169BD5;">NW-20181224-00004</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1821" class="ax_default table_cell">
                    <img id="u1821_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1372.png"/>
                    <div id="u1821_text" class="text ">
                        <p><span>New</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1822" class="ax_default table_cell">
                    <img id="u1822_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1374.png"/>
                    <div id="u1822_text" class="text ">
                        <p><span style="text-decoration:underline;">18L0001/01/183</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1823" class="ax_default table_cell">
                    <img id="u1823_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1375.png"/>
                    <div id="u1823_text" class="text ">
                        <p><span>18L0001</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1824" class="ax_default table_cell">
                    <img id="u1824_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1376.png"/>
                    <div id="u1824_text" class="text ">
                        <p><span>SAM LABORATORY </span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1825" class="ax_default table_cell">
                    <img id="u1825_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1377.png"/>
                    <div id="u1825_text" class="text ">
                        <p><span><br></span></p><p><span>37 Jalan Pemimpin</span></p><p><span>Mapex #04-13, Singapore 577177</span></p><p><span>Tel: +65 6737 3867</span></p><p><span>Fax: +65 6733 3527</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1826" class="ax_default table_cell">
                    <img id="u1826_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1378.png"/>
                    <div id="u1826_text" class="text ">
                        <p><span>Sam Group</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1827" class="ax_default table_cell">
                    <img id="u1827_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1379.png"/>
                    <div id="u1827_text" class="text ">
                        <p><span>Radiological Service</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1828" class="ax_default table_cell">
                    <img id="u1828_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1380.png"/>
                    <div id="u1828_text" class="text ">
                        <p><span>01/12/2018&nbsp; -&nbsp;&nbsp; 30/11/2020</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1829" class="ax_default table_cell">
                    <img id="u1829_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1381.png"/>
                    <div id="u1829_text" class="text ">
                        <p><span>Active</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1830" class="ax_default table_cell">
                    <img id="u1830_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1382.png"/>
                    <div id="u1830_text" class="text ">
                        <p><span>Full</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1831" class="ax_default table_cell">
                    <img id="u1831_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1383.png"/>
                    <div id="u1831_text" class="text ">
                        <p><span>Low</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1832" class="ax_default table_cell">
                    <img id="u1832_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1371.png"/>
                    <div id="u1832_text" class="text ">
                        <p><span>4</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1833" class="ax_default table_cell">
                    <img id="u1833_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1372.png"/>
                    <div id="u1833_text" class="text ">
                        <p><span style="color:#000000;"><br></span></p><p><span style="text-decoration:underline;color:#169BD5;">NW-20181224-00005</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1834" class="ax_default table_cell">
                    <img id="u1834_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1372.png"/>
                    <div id="u1834_text" class="text ">
                        <p><span>New</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1835" class="ax_default table_cell">
                    <img id="u1835_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1374.png"/>
                    <div id="u1835_text" class="text ">
                        <p><span style="text-decoration:underline;">17L0001/01/101</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1836" class="ax_default table_cell">
                    <img id="u1836_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1375.png"/>
                    <div id="u1836_text" class="text ">
                        <p><span>17L0001</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1837" class="ax_default table_cell">
                    <img id="u1837_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1376.png"/>
                    <div id="u1837_text" class="text ">
                        <p><span>TPY Laboratory</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1838" class="ax_default table_cell">
                    <img id="u1838_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1377.png"/>
                    <div id="u1838_text" class="text ">
                        <p><span><br></span></p><p><span>23, Jackson Square,</span></p><p><span>Lor 2, Singapore</span></p><p><span>319001</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1839" class="ax_default table_cell">
                    <img id="u1839_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1378.png"/>
                    <div id="u1839_text" class="text ">
                        <p><span>ECQ Corp</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1840" class="ax_default table_cell">
                    <img id="u1840_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1379.png"/>
                    <div id="u1840_text" class="text ">
                        <p><span>Clinical Laboratory</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1841" class="ax_default table_cell">
                    <img id="u1841_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1380.png"/>
                    <div id="u1841_text" class="text ">
                        <p><span>01/12/2019&nbsp; -&nbsp;&nbsp; 30/11/2021</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1842" class="ax_default table_cell">
                    <img id="u1842_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1381.png"/>
                    <div id="u1842_text" class="text ">
                        <p><span>Active</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1843" class="ax_default table_cell">
                    <img id="u1843_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1382.png"/>
                    <div id="u1843_text" class="text ">
                        <p><span>Full</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1844" class="ax_default table_cell">
                    <img id="u1844_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1383.png"/>
                    <div id="u1844_text" class="text ">
                        <p><span>Low</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1845" class="ax_default table_cell">
                    <img id="u1845_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1410.png"/>
                    <div id="u1845_text" class="text ">
                        <p><span>5</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1846" class="ax_default table_cell">
                    <img id="u1846_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1411.png"/>
                    <div id="u1846_text" class="text ">
                        <p><span style="color:#000000;"><br></span></p><p><span style="text-decoration:underline;color:#169BD5;">NW-20181224-00005</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1847" class="ax_default table_cell">
                    <img id="u1847_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1411.png"/>
                    <div id="u1847_text" class="text ">
                        <p><span>New</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1848" class="ax_default table_cell">
                    <img id="u1848_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1413.png"/>
                    <div id="u1848_text" class="text ">
                        <p><span style="text-decoration:underline;">17L0002/01/102</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1849" class="ax_default table_cell">
                    <img id="u1849_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1414.png"/>
                    <div id="u1849_text" class="text ">
                        <p><span>17L0002</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1850" class="ax_default table_cell">
                    <img id="u1850_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1415.png"/>
                    <div id="u1850_text" class="text ">
                        <p><span>AMK Laboratory</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1851" class="ax_default table_cell">
                    <img id="u1851_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1416.png"/>
                    <div id="u1851_text" class="text ">
                        <p><span><br></span></p><p><span>#02-23, AMK HUB,</span></p><p><span>Singapore</span></p><p><span>239001</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1852" class="ax_default table_cell">
                    <img id="u1852_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1417.png"/>
                    <div id="u1852_text" class="text ">
                        <p><span>ECQ Corp</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1853" class="ax_default table_cell">
                    <img id="u1853_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1418.png"/>
                    <div id="u1853_text" class="text ">
                        <p><span>Clinical Laboratory</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1854" class="ax_default table_cell">
                    <img id="u1854_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1419.png"/>
                    <div id="u1854_text" class="text ">
                        <p><span>01/12/2019&nbsp; -&nbsp;&nbsp; 30/11/2021</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1855" class="ax_default table_cell">
                    <img id="u1855_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1420.png"/>
                    <div id="u1855_text" class="text ">
                        <p><span>Active</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1856" class="ax_default table_cell">
                    <img id="u1856_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1421.png"/>
                    <div id="u1856_text" class="text ">
                        <p><span>Full</span></p>
                    </div>
                </div>

                <!-- Unnamed (Table Cell) -->
                <div id="u1857" class="ax_default table_cell">
                    <img id="u1857_img" class="img " src="images/licence_search_advanced_service_personnel_after/u1422.png"/>
                    <div id="u1857_text" class="text ">
                        <p><span>Low</span></p>
                    </div>
                </div>
            </div>

            <!-- Unnamed (Group) -->
            <div id="u1858" class="ax_default" data-left="7" data-top="301" data-width="140" data-height="22">

                <!-- Unnamed (Droplist) -->
                <div id="u1859" class="ax_default droplist">
                    <select id="u1859_input">
                        <option value="10">10</option>
                    </select>
                </div>

                <!-- Unnamed (Rectangle) -->
                <div id="u1860" class="ax_default label">
                    <div id="u1860_div" class=""></div>
                    <div id="u1860_text" class="text ">
                        <p><span>1-5 of 5 items</span></p>
                    </div>
                </div>
            </div>

            <!-- Unnamed (HTML Button) -->
            <div id="u1861" class="ax_default html_button">
                <input id="u1861_input" type="submit" value="Download to Excel"/>
            </div>
        </div>
    </div>
</form>