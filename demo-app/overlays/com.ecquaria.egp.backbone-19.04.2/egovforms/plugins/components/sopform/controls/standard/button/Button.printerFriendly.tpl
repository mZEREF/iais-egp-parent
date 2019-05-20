{var id = control.getElementId()}
<div id="${id}" class="control ${control.properties.developerclass}">
	<div class="control-input-span control-set-font control-set-alignment">
		<input type="button" id="${id}**input"
			class="control-input control-set-font control-font-normal"
			value="${control.properties.caption|escape}"
			style="display: none;" />
		<span class="upload_controls"></span>
	</div>
</div>
