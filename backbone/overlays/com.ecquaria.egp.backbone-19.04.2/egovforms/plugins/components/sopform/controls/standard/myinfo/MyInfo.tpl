{var id = control.getElementId()}
<div id="${id}" class="control ${control.properties.developerclass}">
	<div class="control-input-span control-set-font control-set-alignment control-button-wrapper">
		<input type="button" id="${id}--input"
			class="control-input control-set-font control-font-normal"
			value="${control.properties.caption|escape}" />
		<span class="upload_controls"></span>
	</div>
</div>
