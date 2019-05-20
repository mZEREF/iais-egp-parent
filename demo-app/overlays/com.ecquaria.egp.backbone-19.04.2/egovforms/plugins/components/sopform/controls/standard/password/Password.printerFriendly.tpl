{var id = control.getElementId(index)}
<div id="${id}" class="control ${control.properties.developerclass}">
	{if instructions}
		<div class="pf_instructions">
			{for instruction in instructions}
				<span class="control-input control-set-font control-font-normal">${instruction}</span><br/>
			{/for}
		</div>
	{/if}
	<div class="control-label-span control-set-alignment">
		<label id="${id}**label"
				for="${id}**text"
				class="control-label control-set-font control-font-label">${control.properties.caption|escape}</label>

		<div id="${id}**errorMsg_right" style="display: none;" class="error_placements"></div>
	</div>
	<div class="control-input-span control-set-alignment">
		{if hasValue}
			<span class="control-input control-set-font control-font-normal">${text}</span>
		{/if}
		{if !hasValue}
			<input type="password" id="${id}**text"
				class="control-input control-set-font control-font-normal"
				value="${control.properties.text|escape}"
				{if control.properties.size} size="${control.properties.size}" {/if}
				{if control.properties.maxLength} maxlength="${control.properties.maxLength}" {/if} />
		{/if}
	</div>
</div>
