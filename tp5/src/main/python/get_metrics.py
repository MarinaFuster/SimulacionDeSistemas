import math
import numpy as np
import matplotlib.pyplot as plt

from enum import Enum
from glob import glob

class DynamicInputState(Enum):
    START = 1
    COMMENT = 2
    PARTICLE = 3

def distance(x1, y1, x2, y2):
    return math.sqrt((x2-x1) ** 2 + (y2-y1) ** 2)

def module(x, y):
    return math.sqrt(x*x + y*y)

def get_metrics(dynamic_input_path):
    state = DynamicInputState.START # initial state

    total_time = 0 # we need to know how long the simulation was
    first_iteration = True
    first_particle = True

    prev_x = 0 # for distance calculation
    prev_y = 0 # for distance calculation

    total_distance = 0

    v_modules = [] # for mean velocity calculation

    xs = []
    ys = []

    with open(dynamic_input_path) as in_f:
        for line in in_f:
            if state == DynamicInputState.START:
                remaining = int(line)
                state = DynamicInputState.COMMENT
            elif state == DynamicInputState.COMMENT:
                total_time = float(line)
                if remaining == 0:
                    state = DynamicInputState.START
                else:
                    state = DynamicInputState.PARTICLE
            elif state == DynamicInputState.PARTICLE:
                if first_particle:
                    first_particle = False
                    elements = line.split('\t')
                    if not first_iteration: # I want to calculate distance from previous point
                        curr_x = float(elements[0])
                        curr_y = float(elements[1])
                        total_distance += distance(prev_x, prev_y, curr_x, curr_y) # adds this distance to total distance
                    prev_x = float(elements[0])
                    prev_y = float(elements[1])
                    xs.append(prev_x)
                    ys.append(prev_y)
                    v_modules.append(module(float(elements[2]), float(elements[3]))) # velocity module
                remaining -= 1
                if remaining == 0:
                    state = DynamicInputState.START
                    first_iteration = False
                    first_particle = True

    # returns total simulation time, total distance and mean velocity
    return total_time, total_distance, np.array(v_modules).sum()/total_time, xs, ys

def plot_several_runs(input_fodler):
    filenames = np.sort(glob(f"{input_fodler}/*"))
    print(filenames)

def run_file_metrics():
    total_time, total_distance, mean_velocity, xs, ys = get_metrics("/home/marina/SimulacionDeSistemas/tp5/output/unnamed_dynamic.xyz")

    print("Total simulation time " + str(total_time))
    print("Total distance " + str(total_distance))
    print("Mean velocity " + str(mean_velocity))

    # plots trajectory
    plt.plot(xs, ys, color='cyan')
    plt.xlabel("x [m]")
    plt.ylabel("y [m]")
    plt.savefig("/home/marina/SimulacionDeSistemas/tp5/results/trajectory.png")



plot_several_runs("/home/marina/SimulacionDeSistemas/tp5/output")
