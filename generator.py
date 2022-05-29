from datetime import timedelta, datetime
import json, random

SIZE = 50

START_TIME = datetime.strptime('01/05/22 01:00:00', '%d/%m/%y %H:%M:%S')
END_TIME = datetime.strptime('01/05/22 06:00:00', '%d/%m/%y %H:%M:%S')

SENSOR = "sensor"
PRES = "_pres"
HUM = "_hum"
TEMP = "_temp"

name_list = [PRES, HUM, TEMP]

def random_date(start, end):
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
    random_second = random.randrange(int_delta)
    random_date = start + timedelta(seconds=random_second)
    return random_date.strftime("%Y-%m-%d %H:%M:%S")


if __name__ == '__main__':

    dataset = []

    for i in range(SIZE):

        numberArea = random.randint(0, 3)
        numberSensor = random.randint(100, 999)
        time = random_date(START_TIME, END_TIME)
        sensor = SENSOR + str(numberSensor) + name_list[random.randint(0,2)]

        value = 0

        if PRES in sensor:
            value = random.randint(650, 800)
        if HUM in sensor:
            value = random.randint(20, 100)
        if TEMP in sensor:
            value = random.randint(0, 60)


        record = {'area':numberArea,'sensor':sensor, 'time':time, 'value':value}
        dataset.append(record)

    with open('weather_data.json', 'w') as outfile:
        outfile.write(json.dumps(dataset))

