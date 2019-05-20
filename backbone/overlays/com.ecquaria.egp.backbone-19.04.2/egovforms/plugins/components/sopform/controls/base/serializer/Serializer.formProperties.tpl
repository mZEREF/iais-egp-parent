{if control.properties}
<Properties>
	{if control.properties.font}
	<Entry name="font">
		<Map>
			{if control.properties.font.inherited != undefined}
			<Entry name="inherited"><Boolean>${control.properties.font.inherited}</Boolean></Entry>
			{/if}
			{if control.properties.font.inherited === undefined || control.properties.font.inherited === false}
			{if control.properties.font.type}
			<Entry name="type"><String>${control.properties.font.type|escape}</String></Entry>
			{/if}
			{if control.properties.font.color}
			<Entry name="color"><String>${control.properties.font.color|escape}</String></Entry>
			{/if}
			{if control.properties.font.bgcolor}
			<Entry name="bgcolor"><String>${control.properties.font.bgcolor|escape}</String></Entry>
			{/if}
			{if control.properties.font.size}
			<Entry name="size"><String>${control.properties.font.size|escape}</String></Entry>
			{/if}
			{if control.properties.font.bold}
			<Entry name="bold"><Boolean>${control.properties.font.bold}</Boolean></Entry>
			{/if}
			{if control.properties.font.italic}
			<Entry name="italic"><Boolean>${control.properties.font.italic}</Boolean></Entry>
			{/if}
			{/if}
		</Map>
	</Entry>
	{/if}
	<Entry name="refNumber">
		<Map>
			<Entry name="format"><String>${control.properties.refNumber.format|escape}</String></Entry>
			<Entry name="digits"><Integer>${control.properties.refNumber.digits}</Integer></Entry>
			<Entry name="startNum"><Integer>${control.properties.refNumber.startNum}</Integer></Entry>
		</Map>
	</Entry>
	<Entry name="requiresLogin"><String>${control.properties.requiresLogin|escape}</String></Entry>
	<Entry name="publishFormFor"><String>${control.properties.publishFormFor|escape}</String></Entry>
	<Entry name="themeName"><String>${control.properties.themeName|escape}</String></Entry>
	<Entry name="submissionMode"><String>${control.properties.submissionMode|escape}</String></Entry>
	<Entry name="processingMode"><String>${control.properties.processingMode|escape}</String></Entry>
	<Entry name="initValueString"><String>${initValueString|escape}</String></Entry>
</Properties>
{/if}
