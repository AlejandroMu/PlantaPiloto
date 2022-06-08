# enable TLS client.tls_set(tls_version=mqtt.client.ssl.PROTOCOL_TLS)

import ssl
import paho.mqtt.client as paho
from paho import mqtt

# setting callbacks for different events to see if it works, print the message etc.
def on_connect(client, userdata, flags, rc, properties=None) :
    print("CONNACK received with code %s." % rc)

# with this callback you can see if your publish was successful
def on_publish(client, userdata, mid, properties=None) :
    print("mid: " + str(mid))

# print which topic was subscribed to
def on_subscribe(client, userdata, mid, granted_qos, properties=None) :
    print("Subscribed: " + str(mid) + " " + str(granted_qos))

# print message, useful for checking if it was successful
def on_message(client, userdata, msg) :
    print(msg.topic + " " + str(msg.qos) + " " + str(msg.payload))

# creates a new client with the specified username, password, host and port
def create_client(user, password, host, port) :
    client = paho.Client(client_id="", userdata=None, protocol=paho.MQTTv5)
    client.username_pw_set(user, password)
    client.tls_set(tls_version = ssl.PROTOCOL_TLSv1_2)
    client.on_connect = on_connect
    client.on_subscribe = on_subscribe
    client.on_message = on_message
    client.on_publish = on_publish
    client.connect(host, port)
    return client

