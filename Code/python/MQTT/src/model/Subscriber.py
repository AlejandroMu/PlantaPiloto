import Client

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

my_client.loop_forever()
