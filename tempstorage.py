from bluetooth import *
import pymysql

username = ''
blank = 0
try:
    sok = BluetoothSocket(RFCOMM)
    sok.connect(("98:D3:31:FD:4A:30",1))
    conn = pymysql.connect(host="localhost", user="root", password="pi1234", db="Mtemp", charset="utf8")
    curs = conn.cursor()
    while True:
        if blank == 0:
            chk = input("name:") #안드로이드 값으로 대체
            sql = "select username from users where username = %s"
            curs.execute(sql, chk)
            rows = curs.fetchall()
            if len(rows) == 0: # 없는 경우
                continue
            for row in rows:
                username = row[0]
        sok.send("a") #아무 단어
        r = sok.recv(1024)
        temp = str(r)[-6:-1]
        try:
            temp = float(temp)
            if temp >60 or temp <30:#사람 사용 아닌경우
                blank = 1
                continue
            blank = 0
        except ValueError:#잘못된 값이면 되돌리기
            blank = 1   
            continue
        sql = "insert into usertemp(username, temp) values(%s,%s)"
        print(float(temp))
        curs.execute(sql, (username,float(temp)))
        
except KeyboardInterrupt:
    print("\nterminate connection")
finally:
    sok.close()
    conn.commit()
    conn.close()

