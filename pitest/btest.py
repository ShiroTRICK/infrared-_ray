from bluetooth import *
c_sok = BluetoothSocket(RFCOMM)
c_sok.connect(("98:D3:31:FD:4A:30",1))

while True:
    s = input("name:")  #안드로이드에서 신호 주는 식으로
    c_sok.send("q")
    m = c_sok.recv(1024)
    print("recv:",m)
c_sok.close()
