# heimdallr
Шлюз между охранными коммуникаторами и центральным сервером

Описание конфигурационного файла
<?xml version="1.0" encoding="UTF-8"?>
<Settings>
	<in>
		<addr ip="192.168.1.1" port="5555"/>
		<addr ip="192.168.1.2" port="5555"/>
		<addr ip="192.168.1.3" port="5555"/>
		<addr ip="192.168.1.4" port="5555"/>
	</in>
	<out>
		<addr ip="192.168.1.100" port="5555"/>
	</out>
	<filter-rules>
		<rule type="accept" deviceId="1" />
		<rule type="accept" deviceId="2" />
		<rule type="reject" deviceId="3" />
		<rule type="reject" deviceId="4" />
	</filter-rules>
</Settings>