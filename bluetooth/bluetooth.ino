#include <Wire.h> //i2c

#include <Adafruit_MLX90614.h>//sensor library

#include <SoftwareSerial.h>

SoftwareSerial hc06(2,3);

Adafruit_MLX90614 mlx = Adafruit_MLX90614();

float temp = 0;

void setup(){
  Serial.begin(9600);
  hc06.begin(9600);
  mlx.begin();
}

void loop(){
  temp = mlx.readObjectTempC();
  if(hc06.available()>0){
    Serial.write(hc06.read()); //q
    Serial.println(temp);
    hc06.print(temp); 
  }
}
