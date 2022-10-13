<iais:row>
    <div class="col-md-12 col-xs-12 edit-content">
        <c:if test="${'true' == canEdit}">
            <input type="hidden" class="isPartEdit" name="${status.index}isPartEdit${index}" value="0"/>
            <div class="text-right app-font-size-16">
                <a class="edit" href="javascript:void(0);">
                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                </a>
            </div>
        </c:if>
    </div>
</iais:row>