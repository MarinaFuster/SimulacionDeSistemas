import numpy as np
from matplotlib import pyplot as plt 
from enum import Enum
from math import sqrt
from os import listdir
from os.path import isfile, join



BASE_INPUT_PATH = "../../../output/"
INPUT_PATH = BASE_INPUT_PATH

COLORED = True

class RunTypes(Enum):
    UNKNOWN = "UNKNOWN TYPE"
    SINGLE = "Single run"
    MULTIPLE = "Multiple runs"

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

def getRunType(run_name):
    files = [f for f in listdir(INPUT_PATH) if isfile(join(INPUT_PATH, f))]
    for f in files:
        filename = f.split(".")[0]
        split_filename = filename.split("_")
        split_run_name = run_name.split("_")
        match = True
        for x in range(0, min(len(split_filename), len(split_run_name))):
            if split_filename[x] != split_run_name[x]:
                match = False
        
        if not match:
            continue
    
        run_type = split_filename[-1]
        if (run_type == "dynamic"):
            return RunTypes.SINGLE
        elif (run_type == "multiple"):
            return RunTypes.MULTIPLE
    
    return RunTypes.UNKNOWN

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
        

    def inWall(self):
        maxValue = self.staticConfig.sideLength - 1
        if self.staticConfig.dimensions == 2:
            return self.x == 0 or self.x == maxValue or self.y == 0 or self.y == maxValue
        else:
            return self.x == 0 or self.x == maxValue or self.y == 0 or self.y == maxValue or self.z == 0 or self.z == maxValue

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

        if (getRunType(run_name) != RunTypes.SINGLE):
            raise "Post processing for images only allowed for single runs"

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

class TimeStatistics:
    def __init__(self, time, radius, alive_percentage):
        self.time = time
        self.radius = radius
        self.alive_percentage = alive_percentage

class StatisticsExtractor:

    def __init__(self, run_name, statistic="radius", showBorderTouch=False  ):
        self.staticConfig = StaticConfig(getStaticConfigPath(run_name))
        self.staticConfig.load()
        self.run_name = run_name
        self.statistic = statistic
        self.showBorderTouch = showBorderTouch
    
    def run(self):
        if (getRunType(self.run_name) == RunTypes.SINGLE):
            self.extractForSingleRun()


    def distanceToCenter(self, particle):

        x = particle.x
        y = particle.y
        z = particle.z
        c = self.staticConfig.center
        if self.staticConfig.dimensions == 2:
            return sqrt((x - c)**2+(y - c)**2)
        else:
            return sqrt( (x - c) ** 2 + (y - c) ** 2 + (z - c) ** 2 )

    def extractForSingleRun(self):
        totalCells = self.staticConfig.sideLength ** self.staticConfig.dimensions
        maxDistance = 0
        state = DynamicInputState.START
        remaining = 0
        time = 0
        aliveCells = 0
        run_name = self.run_name
        stats = []
        graph_suffix = "stat"
        if self.statistic == "radius":
            graph_suffix = "radius"
        elif self.statistic == "mass":
            graph_suffix = "alive_cells"


        dynamic_input_path = "{}{}_dynamic.xyz".format(INPUT_PATH,run_name)
        statistics_output_path = "{}{}_statistics.tsv".format(INPUT_PATH,run_name)
        graph_output_path = "{}{}_{}.png".format(INPUT_PATH,run_name, graph_suffix)

        self.touchedWall = False
        self.touchedTime = 0
        self.repeatAcum =0
        self.lastMaxRadius = 0
        self.findingStagnation = True

        with open(dynamic_input_path) as in_f:
            with open(statistics_output_path, "w") as out_f:
                for line in in_f:
                    if state == DynamicInputState.START:
                        remaining = int(line)
                        aliveCells = remaining
                        state = DynamicInputState.COMMENT
                    elif state == DynamicInputState.COMMENT:
                        if remaining == 0:
                            state = DynamicInputState.START
                            out_f.write("{}\t{}\t{}\n".format(time, 0, 0))
                            stats.append([time,0,0])
                            time += 1
                            maxDistance = 0
                        else:
                            state = DynamicInputState.PARTICLE
                    elif state == DynamicInputState.PARTICLE:
                        particle = Particle(line, self.staticConfig)
                        maxDistance = max(maxDistance, self.distanceToCenter(particle))
                        remaining -= 1
                        if not self.touchedWall and particle.inWall():
                            self.touchedWall = True
                            self.touchedTime = time

                        if remaining == 0:
                            state = DynamicInputState.START
                            alivePercentage = (aliveCells / totalCells) * 100
                            out_f.write("{}\t{}\t{}\n".format(time, maxDistance, alivePercentage))
                            stats.append([time,maxDistance,alivePercentage])
                            self.lastRadius = maxDistance
                            if self.findingStagnation:
                                if self.lastMaxRadius == maxDistance:
                                    self.repeatAcum += 1
                                else:
                                    self.lastMaxRadius = maxDistance
                                    self.repeatAcum = 0

                                if self.repeatAcum == 5:
                                    self.findingStagnation = False
                                    self.stagnationTime = time - 5
                            maxDistance = 0
                            time += 1
        

        np_stats = np.array(stats)
        

        # Plot de radio"
        if self.statistic == "radius":
            plt.plot(np_stats[:,0], np_stats[:,1])
            plt.xlabel("Numero de iteraciones")
            plt.ylabel("Radio de crecimiento")
        elif self.statistic == "mass":
            # Plot de masa
            plt.plot(np_stats[:,0], np_stats[:,2])
            plt.xlabel("Numero de iteraciones")
            plt.ylabel("Porcentaje de celulas vivas")
        elif self.statistic == None:
            pass
        else:
            raise "invalid statistic"
        

        # gradiente hasta tocar la pared
        if self.touchedWall:
            st = np.array(stats)
            slope,intercept = np.polyfit(st[0:self.touchedTime, 0], st[0:self.touchedTime, 1], 1)
            self.slope = slope
        if self.showBorderTouch and self.touchedWall:
            plt.axvline(x=self.touchedTime, color='r', linestyle='--')

        
        # Prender estos cuando grafiquemos solo 1 cosa, no para multiples curvas
        plt.savefig(graph_output_path)
        plt.clf()





if __name__ == "__main__":

    names = getRunNames()
    names.sort()

    operation = int(input("What do we run?\n1) PostProcessor for visualization\n2) Statistic Extractor\n3) Generate graph and visualization for every run\n"))

    print("Select which run to use:")
    i = 1
    for name in names:
        if operation != 3:
            print("{}) {} ({}) ".format(i, name, getRunType(name).value))
            i+=1
        else:
            print("Running for name: " + name)
            se = StatisticsExtractor(name)
            se.run()
            pp = PostProcessor(name, addTimeAsZDimension=False, addColor=COLORED)
            pp.run()

    if operation == 3:
        plt.savefig(INPUT_PATH + "alive_cells.png")
        print("done!")
        exit()

    
    run = int(input())
    run_name = names[run-1]

    if operation == 1:
        pp = PostProcessor(run_name, addTimeAsZDimension=False, addColor=COLORED)
        pp.run()
    elif operation == 2:
        statistic = int(input("What graph do we generate?\n1) Radius vs Iterations\n2) Mass vs Iterations\n"))
        if statistic == 1:
            statistic_val = "radius"
        elif statistic == 2:
            statistic_val = "mass"
        else:
            raise "invalid statistic number"
        se = StatisticsExtractor(run_name, statistic_val)
        se.run()




    exit() # Comment this for BULK operations

    rule_name = "numbers2d"
    iterations = 10
    rule_base_path = BASE_INPUT_PATH + rule_name + "/"
    
    sp = [0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,0.9,1]
    x = list(map(lambda v: int(100*v), sp))
    touched_percent = []
    stagnation_mean = []
    stagnation_std = []
    stagtimes_mean = []
    stagtimes_std = []

    gradients_mean = []
    gradients_std = []
    for starting_percentage in range(1, 11):

        percentage_name = "{}_0{}".format(rule_name, starting_percentage)
        percentage_path = "{}{}/".format(rule_base_path, percentage_name)


        #  Este codigo es para dibujar Tiempo vs Radio

        # for iteration_number in range(1, iterations + 1):
        #     iteration_name = "{}_{}".format(percentage_name, iteration_number)
        #     INPUT_PATH = percentage_path
        #     se = StatisticsExtractor(iteration_name, "radius")
        #     se.run()
        # plt.xlabel("Numero de iteraciones")
        # plt.ylabel("Radio de crecimiento")
        # plt.savefig("{}{}_radius.png".format(rule_base_path, percentage_name))
        # plt.clf()

        #  Este codigo es para dibujar Tiempo vs Porcentaje de celulas vivas 

        # for iteration_number in range(1, iterations + 1):
        #     iteration_name = "{}_{}".format(percentage_name, iteration_number)
        #     INPUT_PATH = percentage_path
        #     se = StatisticsExtractor(iteration_name, "mass")
        #     se.run()
        # plt.xlabel("Numero de iteraciones")
        # plt.ylabel("Porcentaje de celulas vivas")
        # plt.savefig("{}{}_alive_cells.png".format(rule_base_path, percentage_name))
        # plt.clf()


        # Extraccion para Conway: Calcular cuantos tocaron la pared
        # Para los que no tocaron la pared, medir en que valor se estancaron        
        # touchedWall = 0
        # stagnated = 0
        # stagnated_values = []
        # for iteration_number in range(1, iterations + 1):
        #     iteration_name = "{}_{}".format(percentage_name, iteration_number)
        #     INPUT_PATH = percentage_path
        #     se = StatisticsExtractor(iteration_name, None)
        #     se.run()
        #     if se.touchedWall:
        #         touchedWall += 1
        #     else:
        #         stagnated += 1
        #         stagnated_values.append(se.lastRadius)
        # touched_percent.append((1 - (touchedWall / iterations)))
        # stagnated_vals = np.array(stagnated_values)
        # stagnation_mean.append(stagnated_vals.mean())
        # stagnation_std.append(stagnated_vals.std())



        # Pendiente de recta cuando toca la pared:
        # gradients = []
        # for iteration_number in range(1, iterations + 1):
        #     iteration_name = "{}_{}".format(percentage_name, iteration_number)
        #     INPUT_PATH = percentage_path
        #     se = StatisticsExtractor(iteration_name, None)
        #     se.run()
        #     if se.touchedWall:
        #         gradients.append(se.slope)

        # np_gradients = np.array(gradients)
        # gradients_mean.append(np_gradients.mean())
        # gradients_std.append(np_gradients.std())


        # Calculate stagnation time as observable    
        stagnation_times = []
        for iteration_number in range(1, iterations + 1):
            iteration_name = "{}_{}".format(percentage_name, iteration_number)
            INPUT_PATH = percentage_path
            se = StatisticsExtractor(iteration_name, None)
            se.run()
            if not se.findingStagnation:
                stagnation_times.append(se.stagnationTime)

        np_stagtimes = np.array(stagnation_times)
        stagtimes_mean.append(np_stagtimes.mean())
        stagtimes_std.append(np_stagtimes.std())
            
        

        
        # touched_percent.append((1 - (touchedWall / iterations)))
        # stagnated_vals = np.array(stagnated_values)
        # stagnation_mean.append(stagnated_vals.mean())
        # stagnation_std.append(stagnated_vals.std())

    
    
    

    # CONWAY
    # plt.ylim(0,1)
    # plt.scatter(x, touched_percent)
    # plt.plot(x, touched_percent)
    # plt.xlabel("Porcentaje Inicial de células vivas [%]")
    # plt.ylabel("Probabilidad de que el sistema se estanque")
    # plt.savefig(rule_base_path + "conway_stagnation_percent.png")
    # plt.clf()

    # plt.scatter(x, stagnation_mean)
    # # plt.plot(x, stagnation_mean)
    # plt.errorbar(x, stagnation_mean, stagnation_std, solid_capstyle='projecting', capsize=5)
    # plt.xlabel("Porcentaje Inicial de células vivas [%]")
    # plt.ylabel("Convergencia de radio")
    # plt.savefig(rule_base_path + "conway_stagnation.png")
    
    
    # GRADIENT ON WALL
    # plt.scatter(x, gradients_mean)
    # plt.errorbar(x, gradients_mean, gradients_std, solid_capstyle='projecting', capsize=5)
    # plt.xlabel("Porcentaje inicial de células vivas [%]")
    # plt.ylabel("Velocidad de crecimiento del radio")
    # plt.savefig(rule_base_path + "{}_growth.png".format(rule_name))


    # STAGNATION TIMES
    plt.scatter(x, stagtimes_mean)
    plt.errorbar(x, stagtimes_mean, stagtimes_std, solid_capstyle='projecting', capsize=5)
    plt.xlabel("Porcentaje inicial de células vivas [%]")
    plt.ylabel("Tiempo hasta el estancado")
    plt.savefig(rule_base_path + "{}_stagtime.png".format(rule_name))


   
    print("Done!")
    