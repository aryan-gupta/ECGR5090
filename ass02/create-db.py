import mysql.connector
import sys

def cat(filename):
    f = open(filename,mode='r')
    s = f.read()
    f.close()
    return s #.strip('\n\t ')

db = mysql.connector.connect(
    host=cat('./tmp-db-ip'),
    user="root",
    password=cat('./db-root-passwd')
)

ATTRIBUTES = [
    [ "mountain_bike", "race_bike", "street_bike" ], # "Type" : 
    range(4, 11), # "NumGears" : 
    range(36, 61, 6), # "WheelBase" : 
    range(1, 5), # "Height" : 
    [ "steel", "red", "blue", "black" ], # "Color" : 
    [ "carbon", "steel", "aluminium" ] # "ConstructionMaterial" : 
]

# options is a array that holds the index to which index of the
# sub-arrays we are creating. For example, [ 0, 0, 0, 0, 0, 0 ]
# would create an entry for mountain_bike with 4 gears, 36in wheel
# base, 1 height, etc
def recurse_options(cursor, layer, options):  
    if layer == len(ATTRIBUTES):
        sql = "INSERT INTO Attributes (type, num_gears, wheel_base, height, color, material) VALUES (%s, %s, %s, %s, %s, %s)"
        val = [ ]
        for i in range(len(ATTRIBUTES)):
            val.append(ATTRIBUTES[i][options[i]])
        cursor.execute(sql, val)
        return

    for i in range(len(ATTRIBUTES[layer])):
        tmpOptions = options.copy()
        tmpOptions[layer] = i
        recurse_options(cursor, layer + 1, tmpOptions)


cursor = db.cursor()
cursor.execute("CREATE DATABASE Bicycles")
cursor.execute("USE Bicycles")
cursor.execute("""CREATE TABLE Attributes (
    type ENUM("mountain_bike", "race_bike", "street_bike"),
    num_gears INTEGER,
    wheel_base INTEGER,
    height INTEGER,
    color ENUM("steel", "red", "blue", "black"),
    material ENUM("carbon", "steel", "aluminium")
)""")

recurse_options(cursor, 0, [0] * len(ATTRIBUTES))
db.commit()