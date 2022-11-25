
["java:package:icesi.plantapiloto.icestorm"]
module publisher{


	["java:serializable:icesi.plantapiloto.controlLayer.common.Message"]
	sequence<byte> Message;

	interface Measures{
		void putMeasure(Message value);
	}  
}
