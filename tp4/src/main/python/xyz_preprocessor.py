import numpy as np
from enum import Enum
from glob import glob

class DynamicInputState(Enum):
    START = 1
    COMMENT = 2
    PARTICLE = 3
    
prefix = ["analytic", "beeman", "gearpc", "verlet"]
train_folder = "tp4/output"

def algorithms_information():   
    # gets all positions for all deltaT in analytic
    analytic_pos, analytic_time = get_algorithm_information(prefix[0])
    
    # gets all positions for all deltaT in beeman
    beeman_pos, beeman_time = get_algorithm_information(prefix[1])
    
    # gets all positions for all deltaT in gearpc
    gearpc_pos, gearpc_time = get_algorithm_information(prefix[2])
    
    # gets all positions for all deltaT in verlet
    verlet_pos, verlet_time = get_algorithm_information(prefix[3])
    
    return analytic_pos, analytic_time, beeman_pos, beeman_time, gearpc_pos, gearpc_time, verlet_pos, verlet_time 

def get_algorithm_information(file_prefix):
    pos_arr = []
    times_arr = []
    filenames = np.sort(glob(f"{train_folder}/{file_prefix}*"))[::-1]
    for f in filenames:
        positions, times = get_positions_and_times(f)
        
        float_positions = list(map(float, positions))
        float_times = list(map(float, times))
        
        pos_arr.append(np.array(float_positions))
        times_arr.append(np.array(float_times))

    return np.array(pos_arr), np.array(times_arr)

def get_positions_and_times(dynamic_input_path):
    print(f"Analizing {dynamic_input_path}...")
    positions = []
    times = []
    state = DynamicInputState.START
    remaining = 0
    with open(dynamic_input_path) as in_f:
        for line in in_f:
            if state == DynamicInputState.START:
                remaining = int(line)
                state = DynamicInputState.COMMENT
            elif state == DynamicInputState.COMMENT:
                times.append(line)
                if remaining == 0:
                    state = DynamicInputState.START
                else:
                    state = DynamicInputState.PARTICLE
            elif state == DynamicInputState.PARTICLE:
                if remaining == 1:
                    elements = line.split("\t")
                    positions.append(elements[0])
                remaining -= 1
                if remaining == 0:
                    state = DynamicInputState.START
    return np.array(positions), np.array(times)
