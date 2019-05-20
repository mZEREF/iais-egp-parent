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
