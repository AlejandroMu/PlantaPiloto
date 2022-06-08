<<<<<<< HEAD
from getpass import getpass
import sys
import Client
import psycopg2
import datetime

if(len(sys.argv) != 3) :
    print('Please provide module # and channel # to suscribe.')
    print('Exiting script...')
    exit()

modN = str(sys.argv[1])
chN = str(sys.argv[2])

topic = modN + '/' + chN

print('---- BROKER INFORMATION ----')
user = input("Username: ")
password = getpass()
host = input("Broker url: ")
port = int(input("port: "))
print('----------------------------\n')

print('--- DATABASE INFORMATION ---')
db_name = input("DataBase: ")
db_user = input("Username: ")
db_password = getpass()
print('----------------------------\n')

my_client = Client.create_client(user, password, host, port)

my_client.subscribe(topic, qos = 1)

def establish_db_connection(db, u, p) :
    return psycopg2.connect(database = db, user = u, password = p)
    
connection = establish_db_connection(db_name, db_user, db_password)
cursor1 = connection.cursor()

count = 2

def on_message(client, userdata, msg) :
    print(msg.topic + " " + str(msg.qos) + " " + str(msg.payload))
    sql = 'INSERT INTO public."Value" values(%s)'
    e = datetime.datetime.now()
    time_s = e.strftime("%Y-%m-%d %H:%M:%S")
    value = float(msg.payload.decode('UTF-8'))
    channel = int(str(msg.topic).split("/")[1])
    data = (count, time_s, value, channel,)
    cursor1.execute(sql, data)
    connection.commit()
    count = count + 1

my_client.on_message = on_message

my_client.loop_forever()
=======
from getpass import getpass
import sys
import Client
import psycopg2
import datetime

if(len(sys.argv) != 3) :
    print('Please provide module # and channel # to suscribe.')
    print('Exiting script...')
    exit()

modN = str(sys.argv[1])
chN = str(sys.argv[2])

topic = modN + '/' + chN

print('---- BROKER INFORMATION ----')
user = input("Username: ")
password = getpass()
host = input("Broker url: ")
port = int(input("port: "))
print('----------------------------\n')

print('--- DATABASE INFORMATION ---')
db_name = input("DataBase: ")
db_user = input("Username: ")
db_password = getpass()
print('----------------------------\n')

my_client = Client.create_client(user, password, host, port)

my_client.subscribe(topic, qos = 1)

params = {
  'dbname': 'icesiepicdb',
  'user': 'icesiepicpg',
  'password': 'IcesiEpicPgDb',
  'host': 'xepic1',
  'port': 5432
}

def establish_db_connection(params) :
    return psycopg2.connect(**params)
    
connection = establish_db_connection(params)
cursor1 = connection.cursor()

count = 2

def on_message(client, userdata, msg) :
    global count
    print(msg.topic + " " + str(msg.qos) + " " + str(msg.payload))
    sql = 'INSERT INTO public."Value" values(%s,%s,%s,%s)'
    e = datetime.datetime.now()
    time_s = e.strftime("%Y-%m-%d %H:%M:%S")
    value = float(msg.payload.decode('UTF-8'))
    channel = int(str(msg.topic).split("/")[1])
    data = (count, time_s, value, channel+1)
    cursor1.execute(sql, data)
    connection.commit()
    count = count + 1

my_client.on_message = on_message

my_client.loop_forever()
>>>>>>> 8e99a192c1add2ad9faee3d085a97ce75d86e668
