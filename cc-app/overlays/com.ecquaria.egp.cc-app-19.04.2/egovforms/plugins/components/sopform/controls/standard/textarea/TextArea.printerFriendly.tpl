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
	</div>
	<div class="control-input-span control-set-alignment">
		{if hasValue}
			<pre class="control-input control-set-font control-font-normal">${text}</pre>
		{/if}
		{if !hasValue}
			<textarea id="${id}**text"
				class="control-input control-set-font control-font-normal"
				{if control.properties.rows} rows="${control.properties.rows}" {/if}
				{if control.properties.cols} cols="${control.properties.cols}" {/if}
				>${control.properties.text|escape}</textarea>
		{/if}

		<div id="${id}**errorMsg_right" style="display: none;" class="error_placements"></div>
	</div>
</div>
