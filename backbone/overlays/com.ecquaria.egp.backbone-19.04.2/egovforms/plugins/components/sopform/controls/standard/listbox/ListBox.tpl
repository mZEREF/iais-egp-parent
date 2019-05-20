{var id = control.getElementId(index)}
<div id="${id}" class="form-horizontal formgap control ${control.properties.developerclass}">
	<div class="col-sm-4 control-label formtext">
		<label id="${id}--label"
				class="control-label control-set-font control-font-label">${control.properties.caption|escape}</label>
		<span class="upload_controls"></span>
		
	</div>
	<div class="col-sm-5">
		<div class="">
			<select id="${id}--select"
					class="form-control control-input control-set-font control-font-normal"
					multiple="true" size="${control.properties.size}">
			{for row in options}
				<option value="${row.code|escape}"
						{if control.properties.selected.indexOf(row.code) >= 0} selected {/if}>
					${row.description|escape}
				</option>
			{/for}
			</select>
		</div>
		<div id="${id}--errorMsg_right" style="display: none;" class="error_placements"></div>
	</div>
</div>
