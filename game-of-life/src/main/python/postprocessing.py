from enum import Enum
INPUT_PATH = "../../../sample/"
STATIC_CONFIG_PATH = INPUT_PATH + "staticConfigOutput.txt"
DINAMIC_INPUT_PATH = INPUT_PATH + "dynamic_output2020-08-30 11:54:37.155.xyz"
OUTPUT_PATH = INPUT_PATH + "dynamic_output_with_time.xyz"

COLORED = True

class StaticConfig:
    def load(self):
        with open(STATIC_CONFIG_PATH) as file:
            self.dimensions = int(file.readline())
            self.sideLength = int(file.readline())
            self.epochs = int(file.readline())
            self.startingAlivePercentage = float(file.readline())
            self.center = int(file.readline())

staticConfig = StaticConfig()
staticConfig.load()

class Particle():
    def __init__(self, stringified_properties):
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
            center = staticConfig.center
            side = staticConfig.sideLength


            # 200x200
            # 100
            # 0 < x < 200
            # 0 < r < 1

            # 0 < dist to center < (sideLength / 2)

            s2 = side / 2
            distX = abs(self.x - center)
            distY = abs(self.y - center)
            distZ = abs(self.z - center)
            self.r = 1 - distX / s2
            self.g = distY / s2
            self.b = 0
            return "{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\n".format(self.name, self.x, self.y, self.z, self.radius, self.r,self.g,self.b)
        else:
            return "{}\t{}\t{}\t{}\t{}\n".format(self.name, self.x, self.y, self.z, self.radius)



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
        time = 0
        with open(DINAMIC_INPUT_PATH) as in_f:
            with open(OUTPUT_PATH, "w") as out_f:
                for line in in_f:
                    if state == StaticInputState.START:
                        remaining = int(line)
                        out_f.write(line)
                        state = StaticInputState.COMMENT
                    elif state == StaticInputState.COMMENT:
                        out_f.write(line)
                        if remaining == 0:
                            state = StaticInputState.START
                        else:
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
    

    pp = PostProcessor(addTimeAsZDimension=True, addColor=COLORED)
    pp.run()    

