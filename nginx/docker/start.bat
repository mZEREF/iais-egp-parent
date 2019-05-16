c:
cd %NGINX_HOME%
echo nginx start
start nginx
echo "" > %NGINX_HOME%\logs\error.log
tail -f %NGINX_HOME%\logs\error.log