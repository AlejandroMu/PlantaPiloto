import Client
import psycopg2

user = input("Username: ")
password = input("Password: ")
host = input("Host (url): ")
port = int(input("port: "))

my_client = Client.create_client(user, password, host, port)

while True :
    topic = input("topic (s - stop): ")
    if(topic == "s") :
        break
    my_client.subscribe(topic, qos = 1)

# WyksF8.Um58i9YH

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