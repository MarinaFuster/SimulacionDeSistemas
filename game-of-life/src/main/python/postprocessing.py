from enum import Enum
from math import sqrt
from os import listdir
from os.path import isfile, join


INPUT_PATH = "../../../output/"
DINAMIC_INPUT_PATH = INPUT_PATH + "dynamic_output2020-08-30 17:58:03.634.xyz"
STATISTICS_PATH = INPUT_PATH + "statistics.csv"

COLORED = True


def getStaticConfigPath(name):
    return "{}{}_static.txt".format(INPUT_PATH, name)

class StaticConfig:
    def __init__(self, path):
        self.path = path

    def load(self):
        with open(self.path) as file:
            self.dimensions = int(file.readline())
            self.sideLength = int(file.readline())
            self.epochs = int(file.readline())
            self.startingAlivePercentage = float(file.readline())
            self.center = int(file.readline())

class Particle():
    def __init__(self, stringified_properties, staticConfig):
        self.staticConfig = staticConfig
        parsed_properties = stringified_properties.split("\t")
        self.name = parsed_properties[0]
        self.x = int(parsed_properties[1])
        self.y = int(parsed_properties[2])
        self.z = int(parsed_properties[3])
        self.colored = COLORED
        self.r = 0
        self.g = 0
        self.b = 0
        self.radius = 0.3
        

    def __str__(self):
        if self.colored:
            center = self.staticConfig.center
            side = self.staticConfig.sideLength
            s2 = side / 2
            distX = abs(self.x - center)
            distY = abs(self.y - center)
            distZ = abs(self.z - center)
            self.r = 1 - distX / s2
            self.g = distY / s2
            self.b = 1 - distZ / s2
            return "{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\n".format(self.name, self.x, self.y, self.z, self.radius, self.r,self.g,self.b)
        else:
            return "{}\t{}\t{}\t{}\t{}\n".format(self.name, self.x, self.y, self.z, self.radius)



class DynamicInputState(Enum):
    START = 1
    COMMENT = 2
    PARTICLE = 3

class PostProcessor():

    def __init__(self, run_name, addTimeAsZDimension = True, addColor = True):
        self.run_name = run_name
        self.addTimeAsZDimension = addTimeAsZDimension
        self.addColor = addColor
        self.staticConfig = StaticConfig(getStaticConfigPath(run_name))
        self.staticConfig.load()
        self.output_path = "{}{}_visualization.xyz".format(INPUT_PATH,run_name)
        self.dynamic_input_path = "{}{}_dynamic.xyz".format(INPUT_PATH,run_name)

    def run(self):
        state = DynamicInputState.START
        remaining = 0
        time = 0
        with open(self.dynamic_input_path) as in_f:
            with open(self.output_path, "w") as out_f:
                for line in in_f:
                    if state == DynamicInputState.START:
                        remaining = int(line)
                        out_f.write(line)
                        state = DynamicInputState.COMMENT
                    elif state == DynamicInputState.COMMENT:
                        out_f.write(line)
                        if remaining == 0:
                            state = DynamicInputState.START
                        else:
                            state = DynamicInputState.PARTICLE
                    elif state == DynamicInputState.PARTICLE:
                        particle = Particle(line, self.staticConfig)
                        
                        if self.addTimeAsZDimension:
                            particle.z = time

                        out_f.write(particle.__str__())
                        remaining -= 1
                        if remaining == 0:
                            state = DynamicInputState.START
                            time += 1

class StatisticsExtractor:

    def __init__(self):
        pass


    def distanceToCenter(self, particle):

        x = particle.x
        y = particle.y
        z = particle.z
        c = staticConfig.center
        if staticConfig.dimensions == 2:
            return sqrt((x - c)**2+(y - c)**2)
        else:
            return sqrt( (x - c) ** 2 + (y - c) ** 2 + (z - c) ** 2 )

    def run(self):
        totalCells = staticConfig.sideLength ** 2
        maxDistance = 0
        state = DynamicInputState.START
        remaining = 0
        time = 0
        aliveCells = 0

        with open(DINAMIC_INPUT_PATH) as in_f:
            with open(STATISTICS_PATH, "w") as out_f:
                for line in in_f:
                    if state == DynamicInputState.START:
                        remaining = int(line)
                        aliveCells = remaining
                        state = DynamicInputState.COMMENT
                    elif state == DynamicInputState.COMMENT:
                        if remaining == 0:
                            state = DynamicInputState.START
                        else:
                            state = DynamicInputState.PARTICLE
                    elif state == DynamicInputState.PARTICLE:
                        particle = Particle(line)
                        maxDistance = max(maxDistance, self.distanceToCenter(particle))
                        
                        remaining -= 1
                        if remaining == 0:
                            state = DynamicInputState.START

                            alivePercentage = aliveCells / totalCells
                            out_f.write("{}\t{}\t{}\n".format(time, maxDistance, alivePercentage))
                            maxDistance = 0
                            time += 1



def getRunNames():
    files = [f for f in listdir(INPUT_PATH) if isfile(join(INPUT_PATH, f))]
    runs = []
    for f in files:
        filename = f.split(".")[0]
        split = filename.split("_")
        if split[-1] == "static":
            run_name = '_'.join(split[0:-1])
            runs.append(run_name)
    return runs

if __name__ == "__main__":
    names = getRunNames()
    

    operation = int(input("What do we run?\n1) PostProcessor for visualization\n2) Statistic Extractor\n"))

    print("Select which run to use:")
    i = 1
    for name in names:
        print("{}) {}".format(i, name))
        i+=1

    
    run = int(input())
    run_name = names[run-1]
    if operation == 1:
        pp = PostProcessor(run_name, addTimeAsZDimension=False, addColor=COLORED)
        pp.run()
    elif operation == 2:
        se = StatisticsExtractor(run_name)
        se.run()

    print("Done!")
    