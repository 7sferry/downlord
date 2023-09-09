# downlord
Simple download apps to list files from server and download to your device using Spring Boot and Thymeleaf.

I used to transfer files between devices through Wi-Fi with this without flashdisk or harddisk.

change the 'root-path' at application.yml to directory path you wanted, for example: 'C:\' for windows or '/home/' for 
linux or any directory to make it as root path. Then run apps and open 'localhost:8765/' to see all files in the directory and click 
file to download.

if you use docker you don't need to change the application.yml, just run the command `docker build --tag downlord .` to build and run `docker run -dp 8765:8765 -v /home/username:/usr/share/downlord --name downlord downlord`. it will run the app in port 8765 and share "home/username" directory to be downloaded as server. you can change the directory if you want in the command.
