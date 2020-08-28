from enum import Enum
INPUT_PATH = "../../../sample/"
STATIC_CONFIG_PATH = INPUT_PATH + "staticConfigOutput.txt"
DINAMIC_INPUT_PATH = INPUT_PATH + "2020-08-28 11:35:32.899.xyz"
OUTPUT_PATH = INPUT_PATH + "test.xyz"



class Particle():
    def __init__(self, stringified_properties):
        parsed_properties = stringified_properties.split("\t")
        self.name = parsed_properties[0]
        self.x = int(parsed_properties[1])
        self.y = int(parsed_properties[2])
        self.z = int(parsed_properties[3])
        self.colored = False
        self.r = 0
        self.g = 0
        self.b = 0

    def __str__(self):
        if self.colored:
            return "{}\t{}\t{}\t{}\t{}\t{}\t{}\n".format(self.name, self.x, self.y, self.z, self.r,self.g,self.b)
        else:
            return "{}\t{}\t{}\t{}\n".format(self.name, self.x, self.y, self.z)

class StaticConfig:
    def load(self):
        with open(STATIC_CONFIG_PATH) as file:
            self.dimensions = int(file.readline())
            self.sideLength = int(file.readline())
            self.epochs = int(file.readline())
            self.startingAlivePercentage = float(file.readline())
            self.center = int(file.readline())

class StaticInputState(Enum):
    START = 1
    COMMENT = 2
    PARTICLE = 3

class PostProcessor():

    def __init__(self, addTimeAsZDimension = True, addColor = True):
        self.addTimeAsZDimension = addTimeAsZDimension
        self.addColor = addColor
        

    def run(self):
        state = StaticInputState.START
        remaining = 0
        time = 9
        with open(DINAMIC_INPUT_PATH) as in_f:
            with open(OUTPUT_PATH, "w+") as out_f:
                for line in in_f:
                    if state == StaticInputState.START:
                        remaining = int(line)
                        out_f.write(line)
                        state = StaticInputState.COMMENT
                    elif state == StaticInputState.COMMENT:
                        out_f.write(line)
                        state = StaticInputState.PARTICLE
                    elif state == StaticInputState.PARTICLE:
                        particle = Particle(line)
                        
                        if self.addTimeAsZDimension:
                            particle.z = time

                        out_f.write(particle.__str__())
                        remaining -= 1
                        if remaining == 0:
                            state = StaticInputState.START
                            time += 1

    

if __name__ == "__main__":
    staticConfig = StaticConfig()
    staticConfig.load()

    pp = PostProcessor(addTimeAsZDimension=True, addColor=False)
    pp.run()    

