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
