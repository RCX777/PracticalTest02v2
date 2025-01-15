import socket
import datetime
import time
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

server_address = ('0.0.0.0', 5000)
server_socket.bind(server_address)

server_socket.listen(1)

while True:
    connection, client_address = server_socket.accept()
    while True:
        connection.sendall(datetime.datetime.now().strftime('%H:%M:%S').encode())
        connection.sendall('\n'.encode())
        time.sleep(1)


