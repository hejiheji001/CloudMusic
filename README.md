#Download Music From Cloud Music!

#Simply put two arguments and everything will on the GO!

#Example:
##if you want the music of http://music.163.com//song?id=31273246 
##java -jar CloudMusic.jar 31273246 /Users/$whoami/Desktop/

##Also, you can use proxy and gui(do not support proxy yet) :)

#Useage:

##java -jar CloudMusic.jar <id> <path> [options]

##-h Show help

##-gui Open GUI

#Options

-proxy <host>: Use <host> as proxy, default = 210.26.85.241

(If you are outside China, you must define a Chinese proxy. Default value maybe not working)

-port <port>: Use <port> for proxy, default = 80

-proxytype <type>: Support http, https, ftp, socks4 and socks5, default = http

-playlist: Download all songs in a list!

-path <path>: Set save path, if you use -playlist then you must set path
