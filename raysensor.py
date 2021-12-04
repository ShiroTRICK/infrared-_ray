import board
import adafruit_mlx90614
import inspect

i2c = board.I2C()

sensor = adafruit_mlx90614.MLX90614(i2c)

print(sensor.ambient_temperature) #주변 온도
print(sensor.object_temperature) #물체 온도(사람)
