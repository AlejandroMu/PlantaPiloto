from time import sleep
from getpass import getpass
import sys
import Client
import optommp
import struct

if(len(sys.argv) != 3) :
    print('Please provide module # and channel # to suscribe.')
    print('Exiting script...')
    exit()

ANALOG_TYPE = 0
DIGITAL_TYPE = 1

modN = int(sys.argv[1])
chN = int(sys.argv[2])

target = str(modN) + '/' + str(chN)

# raw_input() must be changed to input() when working with python 3.x or higher
user = raw_input('Username: ')
password = getpass()
host = raw_input('Broker url: ')
port = int(raw_input('port: '))
type = int(raw_input('0-Analog, 1-Digital: '))
sleep_time = float(raw_input('sleep time (s): '))

my_client = Client.create_client(user, password, host, port)

grvEpic = optommp.O22MMP()

def read_analog() :
    return grvEpic.GetAnalogPointValue(modN, chN)

def read_digital() :
    dest = 0xF01E0000 + (modN * 0x1000) + (chN * 0x40)
    data = grvEpic.ReadBlock(dest, size = 4) #Min size is 4 bytes
    data_block = data[16:20] # data_block is in bytes 16-19 for Read Response, stop at 20.
    # decode bytearray in big-endian order (>) for float value (f)
    output = str(struct.unpack_from('>f', bytearray(data_block)))
    return float(output[1:-2])

read_value = lambda : read_analog() if type == ANALOG_TYPE else read_digital()

my_client.loop_start()

while True :
    value = read_value()
    my_client.publish(topic = target, payload = value, qos = 1)
    print(target + ' : ' + str(value))
    sleep(sleep_time)
