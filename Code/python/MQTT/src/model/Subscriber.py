import sys
import Client
import psycopg2
import maskpass

if(len(sys.argv) != 3) :
    print('Please provide module # and channel # to suscribe.')
    print('Exiting script...')
    exit()

modN = str(sys.argv[1])
chN = str(sys.argv[2])

topic = modN + '/' + chN

user = input("Username: ")
password = maskpass.askpass(prompt="Password: ", mask="#")
host = input("Broker url: ")
port = int(input("port: "))

my_client = Client.create_client(user, password, host, port)

my_client.subscribe(topic, qos = 1)

def establish_db_connection(db, u, p) :
    return psycopg2.connect(database = db, user = u, password = p)
    
connection = establish_db_connection("Test", "postgres", "Diego-SQL")
cursor1 = connection.cursor()

def on_message(client, userdata, msg) :
    print(msg.topic + " " + str(msg.qos) + " " + str(msg.payload))
    sql = "INSERT INTO Test_Values(val) values(%s)"
    data = (msg.payload.decode('UTF-8'),)
    cursor1.execute(sql, data)
    connection.commit()

my_client.on_message = on_message

my_client.loop_forever()