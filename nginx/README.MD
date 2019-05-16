## Https证书生成：
  * 创建服务器私钥，命令会让你输入一个口令：
    ```
    openssl genrsa -des3 -out server.key 1024
    ```
    ![image](image/1.jpg)
  * 创建签名请求的证书（CSR）：<br/>
    ```
    openssl req -new -key server.key -out server.csr
    ```
    ![image](image/2.jpg)
  * 在加载SSL支持的Nginx并使用上述私钥时除去必须的口令：
    ```
    cp server.key server.key.org
    
    openssl rsa -in server.key.org -out server.key
    ```
    ![image](image/3.jpg)
    
    ![image](image/4.jpg)
  * 最后标记证书使用上述私钥和CSR：
    ```
    openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
    ```
    ![image](image/5.jpg)
  * Copy the files server.crt, server.key into your nginx config folder