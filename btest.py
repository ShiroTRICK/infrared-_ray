from bluetooth import *
c_sok = BluetoothSocket(RFCOMM)
c_sok.connect(("98:D3:31:FD:4A:30",1))
while True:
    msg = input("send:")
    print(msg)
    c_sok.send(msg)
c_sok.close()
