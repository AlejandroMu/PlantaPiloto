from fileinput import close
import sys
from time import sleep
import Client
import optommp

if(len(sys.argv) != 3) :
    print('Please provide module # and channel # to suscribe.')
    print('Exiting script...')
    exit()

modN = int(sys.argv[1])
chN = int(sys.argv[2])

target = str(modN) + '/' + str(chN)

# raw_input() should be changed to input() when working with python 3.x or higher
user = raw_input('Username: ')
password = raw_input('Password: ')
host = raw_input('Broker url: ')
port = int(raw_input('port: '))
sleep_time = float(raw_input('sleep time (s): '))

my_client = Client.create_client(user, password, host, port)

grvEpic = optommp.O22MMP()

my_client.loop_start()

while True :
    value = grvEpic.GetAnalogPointValue(modN, chN)
    my_client.publish(topic = target, payload = value, qos = 1)
    print(target + ' : ' + str(value))
    sleep(sleep_time)

my_client.loop_stop()

grvEpic.close()